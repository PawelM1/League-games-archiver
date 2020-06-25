package app.DAO;

import app.Entity.Season;
import app.Other.DialogBox;
import app.Other.Loaders;
import org.hibernate.query.Query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeasonDao extends HibernateUtil {

    public void insert(Season entity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTSEASON(:season_start,:season_end)");
            query.setParameter("season_start", entity.getSeason_start());
            query.setParameter("season_end", entity.getSeason_end());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public Season id(int id) {
        Season entity = new Season();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Season_by_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, id);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setSeason_id(rs.getInt("season_id"));
                entity.setSeason_start(rs.getDate("season_start").toLocalDate());
                entity.setSeason_end(rs.getDate("season_end").toLocalDate());
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public List<Season> all() {
        openSessionWithTrans();
        List<Season> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_ALL_SEASON(?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                Season entity = new Season();
                entity.setSeason_id(rs.getInt("season_id"));
                entity.setSeason_start(rs.getDate("season_start").toLocalDate());
                entity.setSeason_end(rs.getDate("season_end").toLocalDate());
                list.add(entity);
            }
        });
        closeSessionWithTrans();
        return list;

    }

    public void update(Season k) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call UPDATESeason(:obj_id,:season_start,:season_end)");
            query.setParameter("obj_id", k.getSeason_id());
            query.setParameter("season_start", k.getSeason_start());
            query.setParameter("season_end", k.getSeason_start());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public void delete(Season k) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call DELETESeason(:obj_id)");
        query.setParameter("obj_id", k.getSeason_id());
        query.executeUpdate();
        closeSessionWithTrans();
    }
}
