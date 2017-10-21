package ch.fhnw.swc.mrs.data;

import org.dbunit.IDatabaseTester;

import java.sql.Connection;

public class ITMovieDAO {

    /** Class under test: MovieDAO. */
    private MovieDAO dao;
    private IDatabaseTester tester;
    private Connection connection;

    private static final String COUNT_SQL = "SELECT COUNT(*) FROM clients";
    private static final String DB_CONNECTION = "jdbc:hsqldb:mem:mrs";
}
