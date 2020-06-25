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

public class LeagueTeamDao extends HibernateUtil {
    public void insert(League lentity, Team tentity, Season sentity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTLEAGUETEAM(:team_id,:league_id,:season_id)");
            query.setParameter("team_id", tentity.getTeam_id());
            query.setParameter("league_id", lentity.getLeague_id());
            query.setParameter("season_id", sentity.getSeason_id());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public List<League> getLeagueListByTeam(int teamId) {
        openSessionWithTrans();
        LeagueDao leagueDao = new LeagueDao();
        List<League> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_League_by_Team_id(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, teamId);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                list.add(leagueDao.id(rs.getInt("league_id")));
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public void deleteLeagueTeam(int teamId, int leagueId) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call deleteLeagueTeam(:team_id,:league_id)");
        query.setParameter("team_id", teamId);
        query.setParameter("league_id", leagueId);
        query.executeUpdate();
        closeSessionWithTrans();
    }
}
