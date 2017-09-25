package ch.fhnw.swc.mrs.data;

import java.util.List;

import ch.fhnw.swc.mrs.model.Rental;
import ch.fhnw.swc.mrs.model.User;

/**
 * The Rental data object model class.
 * 
 */
public interface RentalDAO extends DAO {
    /**
     * @param id none.
     * @return none.
     */
    Rental getById(int id);

    /**
     * @param user none.
     * @return none.
     */
    List<Rental> getRentalsByUser(User user);

    /**
     * @return none.
     */
    List<Rental> getAll();

    /**
     * @param rental none.
     */
    void save(Rental rental);

    /**
     * @param rental none.
     */
    void delete(Rental rental);
}
