package app.DAO;

import app.Entity.League;
import app.Other.DialogBox;
import app.Other.Loaders;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class LeagueDao extends HibernateUtil {
    private CountryDao countryDao = new CountryDao();

    public void insert(League entity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTLeague(:country_id,:league_name)");
            query.setParameter("country_id", entity.getCountry().getCountry_id());
            query.setParameter("league_name", entity.getLeague_name());
            query.executeUpdate();
        } catch (PersistenceException e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public League id(int id) {
        League entity = new League();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_League_by_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, id);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setLeague_id(rs.getInt("league_id"));
                entity.setCountry(countryDao.id(rs.getInt("country_id")));
                entity.setLeague_name(rs.getString("league_name"));
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public List<League> all() {
        openSessionWithTrans();
        List<League> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_ALL_LEAGUE(?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                League entity = new League();
                entity.setLeague_id(rs.getInt("league_id"));
                entity.setCountry(countryDao.id(rs.getInt("country_id")));
                entity.setLeague_name(rs.getString("league_name"));
                list.add(entity);
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public void update(League k) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call UPDATELeague(:obj_id,:country_id,:league_name)");
            query.setParameter("obj_id", k.getLeague_id());
            query.setParameter("country_id", k.getCountry().getCountry_id());
            query.setParameter("league_name", k.getLeague_name());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public void delete(League k) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call DELETELeague(:obj_id)");
        query.setParameter("obj_id", k.getLeague_id());
        query.executeUpdate();
        closeSessionWithTrans();
    }
}
