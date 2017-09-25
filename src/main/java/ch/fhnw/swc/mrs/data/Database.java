package ch.fhnw.swc.mrs.data;

import java.sql.Connection;

/**
 * Interface to create a database.
 */
public interface Database {
    /**
     * Initialize database.
     * @param dbconnection database connection string
     * @throws Exception whenever something goes wrong.
     */
    void initDB(String dbconnection) throws Exception;
    /**
     * @return a connection to this database.
     * @throws Exception whenever something goes wrong.
     */
    Connection getConnection() throws Exception;
}
