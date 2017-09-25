package ch.fhnw.swc.mrs.data;

import java.sql.Connection;

public class AbstractDAO implements DAO {
    
    /** java.sql.Connection to use for db access. */
    private Connection connection;

    /**
     * Create a new DAO which uses the given connection.
     * @param c connection.
     */
    protected AbstractDAO(Connection c) {
        connection = c;
    }
    
    protected Connection getConnection() {
        return connection;
    }

    @Override
    public void dispose() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
