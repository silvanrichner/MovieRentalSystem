package ch.fhnw.swc.mrs.model;

import java.util.List;

/**
 * The service interface for all services that the MRS offers.
 */
public interface MRSServices {
  /**
   * add a new Movie.
   * 
   * @param m a Movie that is used as a source for the newly created object.
   * @return a new and valid Movie object. Use this object and discard the object given in the
   *         parameter.
   */
  Movie createMovie(Movie m);

  /**
   * Retrieve all Movies.
   * 
   * @return all Movies.
   */
  List<Movie> getAllMovies();

  /**
   * get all rented or available Movies.
   * 
   * @param rented whether the available or the rented Movies shall be retrieved.
   * @return all Movies that are either rented or not (depending on parameter rented)
   */
  List<Movie> getAllMovies(boolean rented);

  /**
   * @param id the identification of the Movie to retrieve.
   * @return get Movie by its ID.
   */
  Movie getMovieById(int id);

  /**
   * Update Movie with new data.
   * 
   * @param movie contains the new data.
   * @return whether the update operation was successful.
   */
  boolean updateMovie(Movie movie);

  /**
   * Delete Movie.
   * 
   * @param movie Movie to delete.
   * @return whether the delete operation was successful.
   */
  boolean deleteMovie(Movie movie);

  /**
   * Retrieve all Users.
   * @return all Users.
   */
  List<User> getAllUsers();

  /**
   * @param id the identification of the User to retrieve.
   * @return get User by its ID.
   */
  User getUserById(int id);
  
  /**
   * @param name retrieve first user found with given name.
   * @return User or null if not found.
   */
  User getUserByName(String name);

  /**
   * Add a new User.
   * 
   * @param u a User that is used as a source for the newly created object.
   * @return a new and valid User object. Use this object and discard the object given in the
   *         parameter.
   */
  User createUser(User u);

  /**
   * Update User with new data.
   * 
   * @param u contains the new data.
   * @return whether the update operation was successful.
   */
  boolean updateUser(User u);

  /**
   * Delete User.
   * 
   * @param u User to delete.
   * @return whether the delete operation was successful.
   */
  boolean deleteUser(User u);
  
  /**
   * Retrieve all Rentals.
   * @return all Rentals.
   */
  List<Rental> getAllRentals();
  
  /**
   * Create a new Rental.
   * @param u the User that is renting a Movie.
   * @param m the Movie that is rented.
   * @return a new Rental object.
   */
  boolean createRental(User u, Movie m);
  
  /**
   * Return a rented Movie.
   * @param r the rental to terminate.
   * @return whether the return was successful.
   */
  boolean returnRental(Rental r);
  
  /**
   * Initialize the backend component.
   */
  void init();
}
