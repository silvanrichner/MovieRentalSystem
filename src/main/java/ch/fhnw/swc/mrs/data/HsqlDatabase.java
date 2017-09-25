package ch.fhnw.swc.mrs.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


/**
 * Responsible to initialize database.
 */
public final class HsqlDatabase implements Database {
    public static final String DB_DRIVER = "org.hsqldb.jdbcDriver";
    private String dbCONNECTION;

    @Override
    public void initDB(String dbconnection) throws Exception {
        dbCONNECTION = dbconnection;
        Connection connection = initDataSource();

        // create database tables only if they do not yet exist.
        createDatabaseModel(connection);

        connection.close();
    }

    private Connection initDataSource() throws Exception {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(dbCONNECTION, "SA", "");
    }

    /**
     * Create the database tables.
     */
    private void createDatabaseModel(Connection conn) {
        try {
            InputStream stream = getClass().getResourceAsStream("/data/DBSetup.script");
            List<String> commands = readAllLines(stream);
            
            for (String line: commands) {
                command(line, conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * use for SQL commands CREATE, DROP, INSERT and UPDATE.
     * 
     * @param expression SQL command
     * @throws SQLException when something went wrong
     */
    private synchronized void command(String expression, Connection connection) throws SQLException {
        Statement st = null;
        st = connection.createStatement(); // statements
        int i = st.executeUpdate(expression); // run the query
        if (i == -1) {
            System.out.println("db error : " + expression);
        }
        st.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbCONNECTION, "SA", "");
    }
    
    private List<String> readAllLines(InputStream is) throws IOException {
        List<String> result = new LinkedList<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = b.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

}
