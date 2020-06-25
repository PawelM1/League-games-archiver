package app.DAO;

import app.Entity.League;
import app.Entity.Season;
import app.Entity.Team;
import app.Other.DialogBox;
import app.Other.Loaders;
import org.hibernate.query.Query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeasonLeagueDao extends HibernateUtil {
    public void insert(League lEntity, Season sEntity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTSEASONLEAGUE(:league_id,:season_id)");
            query.setParameter("league_id", lEntity.getLeague_id());
            query.setParameter("season_id", sEntity.getSeason_id());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public List<League> getLeagueBySeasonId(int season_id) {
        openSessionWithTrans();
        List<League> list = new ArrayList<>();
        LeagueDao leagueDao = new LeagueDao();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_LEAGUE_BY_SEASON_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, season_id);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                list.add(leagueDao.id(rs.getInt("league_id")));
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public List<Season> getSeasonByLeagueId(int league_id) {
        openSessionWithTrans();
        List<Season> list = new ArrayList<>();
        SeasonDao seasonDao = new SeasonDao();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Season_by_League_id(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, league_id);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                list.add(seasonDao.id(rs.getInt("season_id")));
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public List<Team> getTeamBySeasonAndLeagueId(int leagueId, int seasonId) {
        openSessionWithoutTrans();
        List<Team> teamList = new ArrayList<>();
        TeamDao teamDao = new TeamDao();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GETTEAMBYSEASONANDLEAGUEID(?,?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, leagueId);
            statement.setInt(3, seasonId);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                teamList.add(teamDao.id(rs.getInt("team_id")));
            }
        });
        closeSessionWithTrans();
        return teamList;
    }

    public void deleteSeasonLeague(int seasonId, int leagueId) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call deleteSeasonFromLeague(:season_id,:league_id)");
        query.setParameter("season_id", seasonId);
        query.setParameter("league_id", leagueId);
        query.executeUpdate();
        closeSessionWithTrans();
    }
}
