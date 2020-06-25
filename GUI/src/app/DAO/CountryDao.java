package app.DAO;

import app.Entity.Country;
import app.Other.DialogBox;
import app.Other.Loaders;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryDao extends HibernateUtil {

    public void insert(Country entity) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call INSERTCOUNTRY(:name)");
            query.setParameter("name", entity.getName());
            query.executeUpdate();
        } catch (PersistenceException e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public Country id(int id) {
        Country entity = new Country();
        openSessionWithTrans();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call Get_Country_by_ID(?,?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.setInt(2, id);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                entity.setCountry_id(rs.getInt("country_id"));
                entity.setName(rs.getString("name"));
            }
        });
        closeSessionWithTrans();
        return entity;
    }

    public List<Country> all() {
        openSessionWithTrans();
        List<Country> list = new ArrayList<>();
        getCurrentLocalSession().doWork((Connection connection) -> {
            CallableStatement statement = connection.prepareCall("{call GET_ALL_COUNTRY(?)}");
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();
            ResultSet rs = (ResultSet) statement.getObject(1);

            while (rs.next()) {
                Country entity = new Country();
                entity.setCountry_id(rs.getInt("country_id"));
                entity.setName(rs.getString("name"));
                list.add(entity);
            }
        });
        closeSessionWithTrans();
        return list;
    }

    public void update(Country k) {
        openSessionWithTrans();
        try {
            Query query = getCurrentLocalSession().createSQLQuery("call UPDATECountry(:obj_id,:name)");
            query.setParameter("obj_id", k.getCountry_id());
            query.setParameter("name", k.getName());
            query.executeUpdate();
        } catch (Exception e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        closeSessionWithTrans();
    }

    public void delete(Country k) {
        openSessionWithTrans();
        Query query = getCurrentLocalSession().createSQLQuery("call DELETECOUNTRY(:obj_id)");
        query.setParameter("obj_id", k.getCountry_id());
        query.executeUpdate();
        closeSessionWithTrans();
    }
}
