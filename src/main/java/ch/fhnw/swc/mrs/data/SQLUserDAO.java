/**
 *
 */
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

import ch.fhnw.swc.mrs.model.User;

/**
 * SQL access to data.
 */
public class SQLUserDAO extends AbstractDAO implements UserDAO {

    /** SQL statement to delete user. */
    private static final String DELETE_SQL = "DELETE FROM clients WHERE id = ?";
    /** SQL statement to create user. */
    private static final String INSERT_SQL = "INSERT INTO clients ( firstname, name, birthdate ) VALUES ( ?, ?, ? )";
    /** SQL statement to update user. */
    private static final String UPDATE_SQL = "UPDATE clients SET firstname = ?, name = ?, birthdate = ? WHERE id = ?";
    /** SQL statement to get user by id. */
    private static final String GET_BY_ID_SQL = "SELECT id, firstname, name, birthdate FROM clients WHERE id = ?";
    /** SQL statement to get user by name. */
    private static final String GET_BY_NAME_SQL = "SELECT id, firstname, name, birthdate FROM clients WHERE name = ?";
    /** SQL statement to get all users. */
    private static final String GET_ALL_SQL = "SELECT id, firstname, name, birthdate FROM clients";

    /**
     * Create a new DAO which uses the given connection.
     * 
     * @param c connection.
     */
    public SQLUserDAO(Connection c) {
        super(c);
    }

    @Override
    public void delete(User user) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL);
            ps.setInt(1, user.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read single User object.
     * 
     * @param r cursor into result set.
     * @return newly read User object
     * @throws SQLException whenever there is a problem
     */
    private User readUser(ResultSet r) throws SQLException {
        String firstname = r.getString("FirstName");
        String lastname = r.getString("Name");
        int i = r.getInt("Id");
		LocalDate date = r.getDate("Birthdate").toLocalDate();
        User u = new User(lastname, firstname, date);
        u.setId(i);

        return u;
    }

    @Override
    public List<User> getAll() {
        try {
            List<User> result = new LinkedList<>();
            PreparedStatement ps = getConnection().prepareStatement(GET_ALL_SQL);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                result.add(readUser(r));
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getById(int id) {
        try {
            User result = null;
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                result = readUser(r);
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getByName(String name) {
        try {
            List<User> result = new LinkedList<>();
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_NAME_SQL);
            ps.setString(1, name);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                result.add(readUser(r));
            }
            r.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveOrUpdate(User user) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(GET_BY_ID_SQL);
            ps.setInt(1, user.getId());
            ResultSet r = ps.executeQuery();
            PreparedStatement writeStmt;
            boolean isUpdate = r.next();
            int paramcount = 1;
            if (isUpdate) { // there exists already a user with this id => UPDATE
                writeStmt = getConnection().prepareStatement(UPDATE_SQL);
            } else { // new user => INSERT
                writeStmt = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            }
            writeStmt.setString(paramcount++, user.getFirstName());
            writeStmt.setString(paramcount++, user.getName());
            Date d = Date.valueOf(user.getBirthdate());
            writeStmt.setDate(paramcount++, d);
			if (isUpdate) {
				writeStmt.setInt(paramcount, user.getId());
			}
			writeStmt.execute();
			getConnection().commit();
            if (!isUpdate) {
                r = writeStmt.getGeneratedKeys();
                if (r.next()) {
                    user.setId(r.getInt(1));
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
