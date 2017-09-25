package ch.fhnw.swc.mrs.data;

import java.util.List;

import ch.fhnw.swc.mrs.model.User;

/**
 * Data Access Object that provides access to the underlying database. Use this DAO to access User related data.
 */
public interface UserDAO extends DAO {
    /**
     * Retrieve a user by his/her identification.
     * @param id the unique identification of the user object to retrieve.
     * @return the user with the given identification or <code>null</code> if none found.
     */
    User getById(int id);

    /**
     * Retrieve all users stored in this system.
     * @return a list of all users.
     */
    List<User> getAll();

    /**
     * Persist a User object. Use this method either when storing a new User object or for updating an existing one.
     * @param user the object to persist.
     */
    void saveOrUpdate(User user);

    /**
     * Remove a user from the database. After this operation the user does not exist any more in the database.
     * Make sure to dispose the object too!
     * @param user the User to remove.
     */
    void delete(User user);

    /**
     * Retrieve a user by his/her name. Use the family name to retrieve a list of all users with that name.
     * Note this method does not support wildcards!
     * @param name the family name of the users to retrieve.
     * @return a list of users with the given name.
     */
    List<User> getByName(String name);
}
