package onlinepaymentsimulator.dao;

import java.sql.SQLException;
import java.util.List;


public interface Dao<T> {
    T single(Integer key) throws SQLException;
    List<T> all() throws SQLException;
}
