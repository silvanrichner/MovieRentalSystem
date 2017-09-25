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
import ch.fhnw.swc.mrs.model.Rental;
import ch.fhnw.swc.mrs.model.User;

/**
 * SQL access to data.
 */
public class SQLRentalDAO extends AbstractDAO implements RentalDAO {

    /** SQL statement to delete movie. */
    private static final String DELETE_SQL = "DELETE FROM rentals WHERE id = ?";
    /** SQL statement to create movie. */
    private static final String INSERT_SQL = "INSERT INTO rentals ( movieid, clientid, rentaldate )"
            + "  VALUES ( ?, ?, ? )";
    /** select clause of queries. */
    private static final String SELECT_CLAUSE = "SELECT id, movieid, clientid, rentaldate FROM rentals ";
    /** SQL statement to get movie by id. */
    private static final String GET_BY_ID_SQL = SELECT_CLAUSE + " WHERE id = ?";

    /** SQL statement to get all movies. */
    private static final String GET_ALL_SQL = SELECT_CLAUSE;

    /**
     * Create a new DAO which uses the given connection.
     * 
     * @param c connection.
     */
    public SQLRentalDAO(Connection c) {
        super(c);
    }

    @Override
    public void delete(Rental rental) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL);
            int id = rental.getId();
            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read single Rental object.
     * 
     * @param r cursor into result set
     * @return Rental object
     * @throws SQLException whenever there is a problem.
     */
    private Rental readRental(ResultSet r) throws SQLException {
        int id = r.getInt("id");
        int mid = r.getInt("movieid");
        int cid = r.getInt("clientid");
        LocalDate rentaldate = r.getDate("rentaldate").toLocalDate();

        SQLMovieDAO mDao = new SQLMovieDAO(getConnection());
        Movie m = mDao.getById(mid);

        SQLUserDAO uDao = new SQLUserDAO(getConnection());
        User u = uDao.getById(cid);

        Rental rental = Rental.materializeRentalFromDB(id, u, m, rentaldate);
        return rental;
    }

    @Override
    public List<Rental> getAll() {
        try {
            List<Rental> result = new LinkedList<Rental>();
            PreparedStatement ps = getConnection().prepareStatement(GET_ALL_SQL);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                Rental rtl = readRental(r);
                result.add(rtl);
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Rental getById(int id) {
        try {
            Rental result = null;
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                result = readRental(r);
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Rental> getRentalsByUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(Rental rental) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, rental.getId());
            ResultSet r = ps.executeQuery();
            PreparedStatement writeStmt;
            int paramcount = 1;
            writeStmt = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            writeStmt.setInt(paramcount++, rental.getMovie().getId());
            writeStmt.setInt(paramcount++, rental.getUser().getId());
            writeStmt.setDate(paramcount++, Date.valueOf(rental.getRentalDate()));
            writeStmt.execute();
            getConnection().commit();
            r = writeStmt.getGeneratedKeys();
            if (r.next()) {
                rental.setId(r.getInt(1));
            }
            writeStmt.close();
            r.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
