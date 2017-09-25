package ch.fhnw.swc.mrs.data;

import java.util.List;

import ch.fhnw.swc.mrs.model.Movie;

/**
 * @author wolfgang.schwaiger
 * 
 */
public interface MovieDAO extends DAO {
    /**
     * @param id none.
     * @return none.
     */
    Movie getById(int id);

    /**
     * @return none.
     */
    List<Movie> getAll();
    
    /**
     * Get movies according to their rented status.
     * @param rented if the movies shall be rented or not.
     * @return movies that fulfill the rented status.
     */
    List<Movie> getAll(boolean rented);

    /**
     * @param title none.
     * @return none.
     */
    List<Movie> getByTitle(String title);

    /**
     * @param movie none.
     */
    void saveOrUpdate(Movie movie);

    /**
     * @param movie none.
     */
    void delete(Movie movie);
}
