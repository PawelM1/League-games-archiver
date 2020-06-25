package app.DAO;

import app.Entity.Games;
import app.Other.DialogBox;
import app.Other.Loaders;
import org.hibernate.query.Query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GameDao extends HibernateUtil {
    private TeamDao teamDao = new TeamDao();

    public void insert(Games entity, int leagueId, int seasonId) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTGAME(:host_team_id,:guest_team_id,:host_team_score,:guest_team_score,:game_date,:season_id,:league_id)");
            query.setParameter("host_team_id", entity.getHost_team().getTeam_id());
            query.setParameter("guest_team_id", entity.getGuest_team().getTeam_id());
            query.setParameter("host_team_score", entity.getHost_team_score());
            query.setParameter("guest_team_score", entity.getGuest_team_score());
            query.setParameter("game_date", entity.getGameDate());
            query.setParameter("season_id", seasonId);
            query.setParameter("league_id", leagueId);
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.games.create"));
        }
        closeSessionWithTrans();
    }

    public Games id(int id) {
        Games entity = new Games();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Games_by_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, id);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setGame_id(rs.getInt("game_id"));
                entity.setHost_team(teamDao.id(rs.getInt("host_team_id")));
                entity.setGuest_team(teamDao.id(rs.getInt("guest_team_id")));
                entity.setHost_team_score(rs.getInt("host_team_score"));
                entity.setGuest_team_score(rs.getInt("guest_team_score"));
                entity.setGameDate(rs.getDate("game_date").toLocalDate());
                entity.setSeason_id(rs.getInt("season_id"));
                entity.setLeague_id(rs.getInt(("league_id")));
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public List<Games> all() {
        openSessionWithTrans();
        List<Games> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_ALL_GAMES(?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                Games entity = new Games();
                entity.setGame_id(rs.getInt("game_id"));
                entity.setHost_team(teamDao.id(rs.getInt("host_team_id")));
                entity.setGuest_team(teamDao.id(rs.getInt("guest_team_id")));
                entity.setHost_team_score(rs.getInt("host_team_score"));
                entity.setGuest_team_score(rs.getInt("guest_team_score"));
                entity.setGameDate(rs.getDate("game_date").toLocalDate());
                entity.setSeason_id(rs.getInt("season_id"));
                entity.setLeague_id(rs.getInt("league_id"));
                list.add(entity);
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public void update(Games k) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call UPDATEGAMES(:obj_id,:host_team_id,:guest_team_id,:host_team_score,:guest_team_score,:game_date,:season_id,:league_id)");
            query.setParameter("obj_id", k.getGame_id());
            query.setParameter("host_team_id", k.getHost_team().getTeam_id());
            query.setParameter("guest_team_id", k.getGuest_team().getTeam_id());
            query.setParameter("host_team_score", k.getHost_team_score());
            query.setParameter("guest_team_score", k.getGuest_team_score());
            query.setParameter("game_date", k.getGameDate());
            query.setParameter("season_id", k.getSeason_id());
            query.setParameter("league_id", k.getLeague_id());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.games.create"));
        }
        closeSessionWithTrans();
    }

    public void delete(Games k) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call DELETEGAMES(:obj_id)");
        query.setParameter("obj_id", k.getGame_id());
        query.executeUpdate();
        closeSessionWithTrans();
    }

}
