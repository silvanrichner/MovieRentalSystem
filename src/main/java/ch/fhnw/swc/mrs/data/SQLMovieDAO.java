package ch.fhnw.swc.mrs.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.PriceCategory;

/**
 * SQL access to data.
 */
public class SQLMovieDAO extends AbstractDAO implements MovieDAO {
    /** SQL statement to delete movie. */
    private static final String DELETE_SQL = "DELETE FROM movies WHERE id = ?";
    /** SQL statement to create movie. */
    private static final String INSERT_SQL = 
            "INSERT INTO movies (title, isrented, releasedate, pricecategory, agerating)"
            + "  VALUES (?, ?, ?, ?, ?)";
    /** SQL statement to update movie. */
    private static final String UPDATE_SQL = "UPDATE movies "
            + "SET title = ?, isrented = ?, releasedate = ?, pricecategory = ? , agerating = ?" + "WHERE id = ?";
    /** select clause of queries. */
    private static final String SELECT_CLAUSE = "SELECT id, title, isrented, releasedate, pricecategory, agerating "
            + "  FROM movies ";
    /** SQL statement to get movie by id. */
    private static final String GET_BY_ID_SQL = SELECT_CLAUSE + " WHERE id = ?";
    /** SQL statement to get movie by name. */
    private static final String GET_BY_TITLE_SQL = SELECT_CLAUSE + " WHERE title = ?";
    /** SQL statement to get all movies. */
    private static final String GET_ALL_SQL = SELECT_CLAUSE;
    /** SQL statement to get all movies of a given rented status. */
    private static final String GET_ALL_RENTED_SQL = SELECT_CLAUSE + " WHERE isrented = ?";

    /**
     * Create a new DAO which uses the given connection.
     * 
     * @param c connection.
     */
    public SQLMovieDAO(Connection c) {
        super(c);
    }

    @Override
    public void delete(Movie movie) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL);
            ps.setInt(1, movie.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read a single movie object from database.
     * 
     * @param r Cursor into result set.
     * @return a movie object
     * @throws SQLException in case of any problem
     */
    private Movie readMovie(ResultSet r) throws SQLException {
        String title = r.getString("Title");
        boolean isrented = r.getBoolean("IsRented");
        LocalDate date = r.getDate("ReleaseDate").toLocalDate();
        int i = r.getInt("Id");
        String pc = r.getString("PriceCategory");
        PriceCategory cat = PriceCategory.getPriceCategoryFromId(pc);
        Movie m = new Movie(title, date, cat, 0);
        m.setId(i);
        m.setRented(isrented);
        m.setAgeRating(r.getInt("agerating"));
        return m;
    }

    @Override
    public List<Movie> getAll() {
        try {
            PreparedStatement ps = getConnection().prepareStatement(GET_ALL_SQL);
            return getAll(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getAll(boolean rented) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(GET_ALL_RENTED_SQL);
            ps.setBoolean(1, rented);
            return getAll(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> getAll(PreparedStatement ps) throws Exception {
        List<Movie> result = new LinkedList<Movie>();
        ResultSet r = ps.executeQuery();
        while (r.next()) {
            Movie m = readMovie(r);
            result.add(m);
        }
        r.close();
        ps.close();
        return result;
    }

    @Override
    public Movie getById(int id) {
        try {
            Movie result = null;
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                result = readMovie(r);
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getByTitle(String title) {
        try {
            List<Movie> result = new LinkedList<Movie>();
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_TITLE_SQL);
            ps.setString(1, title);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                result.add(readMovie(r));
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveOrUpdate(Movie movie) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, movie.getId());
            ResultSet r = ps.executeQuery();
            PreparedStatement writeStmt;
            boolean isUpdate = r.next();
            int paramcount = 1;
            if (isUpdate) { // there exists already a movie with this id => UPDATE
                writeStmt = getConnection().prepareStatement(UPDATE_SQL);
            } else {
                writeStmt = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            }
            writeStmt.setString(paramcount++, movie.getTitle());
            writeStmt.setBoolean(paramcount++, movie.isRented());
            Date d = Date.valueOf(movie.getReleaseDate());
            writeStmt.setDate(paramcount++, d);
            writeStmt.setString(paramcount++, movie.getPriceCategory().toString());
            writeStmt.setInt(paramcount++, movie.getAgeRating());
            if (isUpdate) {
                writeStmt.setInt(paramcount, movie.getId());
            }
            writeStmt.executeUpdate();
            getConnection().commit();
            if (!isUpdate) {
                r = writeStmt.getGeneratedKeys();
                if (r.next()) {
                    movie.setId(r.getInt(1));
                }
            }
            writeStmt.close();
            r.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
