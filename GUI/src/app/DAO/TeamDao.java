package app.DAO;

import app.Entity.Team;
import app.Model.TeamDetailsForTableViewModel;
import app.Other.DialogBox;
import app.Other.Loaders;
import oracle.jdbc.OracleTypes;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TeamDao extends HibernateUtil {

    public void insert(Team entity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTTEAM(:name,:city)");
            query.setParameter("name", entity.getName());
            query.setParameter("city", entity.getCity());
            query.executeUpdate();
        } catch (PersistenceException e) {
            throw new PersistenceException();
        }
        closeSessionWithTrans();
    }

    public Team id(int id) {
        Team entity = new Team();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Team_by_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, id);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setTeam_id(rs.getInt("team_id"));
                entity.setName(rs.getString("name"));
                entity.setCity(rs.getString("city"));
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public Team getTeamIdByName(String name) {
        Team entity = new Team();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Team_by_name(?,?)}");
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, name);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setTeam_id(rs.getInt("team_id"));
                entity.setName(rs.getString("name"));
                entity.setCity(rs.getString("city"));
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public List<Team> all() {
        openSessionWithTrans();
        List<Team> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_ALL_TEAM(?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                Team entity = new Team();
                entity.setTeam_id(rs.getInt("team_id"));
                entity.setName(rs.getString("name"));
                entity.setCity(rs.getString("city"));
                list.add(entity);
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public void update(Team k) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call UPDATETeam(:obj_id,:name,:city)");
            query.setParameter("obj_id", k.getTeam_id());
            query.setParameter("name", k.getName());
            query.setParameter("city", k.getCity());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public void delete(Team k) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call DELETETEAM(:obj_id)");
        query.setParameter("obj_id", k.getTeam_id());
        query.executeUpdate();
        closeSessionWithTrans();
    }

    public List<TeamDetailsForTableViewModel> getTeamListWithDetails(int league_id, int season_id) {
        openSessionWithTrans();
        List<TeamDetailsForTableViewModel> teamList = new ArrayList<>(26);
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement1 = connection.prepareCall("{call GETTEAMBYSEASONANDLEAGUEID(?,?,?)}");
            statement1.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement1.setInt(2, league_id);
            statement1.setInt(3, season_id);
            statement1.execute();
            ResultSet rs = (ResultSet) statement1.getObject(1);
            while (rs.next()) {
                TeamDetailsForTableViewModel team = new TeamDetailsForTableViewModel();
                team.setTeam(this.id(rs.getInt("team_id")));
                teamList.add(team);
            }
            CallableStatement statement2 = connection.prepareCall("{call Get_DETAILS_OF_GAME(?,?,?,?)}");
            statement2.registerOutParameter(1, OracleTypes.CURSOR);
            statement2.setInt(3, league_id);
            statement2.setInt(4, season_id);
            for (TeamDetailsForTableViewModel team : teamList) {
                statement2.setInt(2, team.getTeam().getTeam_id());
                statement2.execute();
                rs = (ResultSet) statement2.getObject(1);
                while (rs.next()) {
                    team.setPoints(rs.getInt("points"));
                    team.setMatches(rs.getInt("playedmatches"));
                    team.setWon(rs.getInt("wonmatches"));
                    team.setDraw(rs.getInt("drawmatches"));
                    team.setLoose(rs.getInt("lostmatches"));
                    team.setScoreGoals(rs.getInt("scoregolas"));
                    team.setLostGoals(rs.getInt("lostgoals"));
                    team.setBalance(rs.getInt("bilansgoals"));
                }
            }
        });
        closeSessionWithTrans();
        return teamList;
    }

    public int biggestWinGameId(int team_id) {
        AtomicInteger hGame_id = new AtomicInteger(0);
        AtomicInteger gGame_id = new AtomicInteger(0);
        AtomicInteger biggestWin;
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{? = call BIGGESTWIN(?)}");
            statement.registerOutParameter(1, OracleTypes.INTEGER);
            statement.setInt(2, team_id);
            statement.execute();
            hGame_id.set(statement.getInt(1));
        });
        closeSessionWithTrans();
        biggestWin = hGame_id.get() > gGame_id.get() ? hGame_id : gGame_id;
        return biggestWin.get();
    }
}