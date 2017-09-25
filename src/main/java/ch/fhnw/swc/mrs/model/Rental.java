package ch.fhnw.swc.mrs.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author wolfgang.schwaiger
 * 
 */
public class Rental {
    /** Flag indicating whether the object has been initialized. */
    private boolean initialized = false;
    private final IntegerProperty id = new SimpleIntegerProperty(0) {
        @Override
        public void set(int anId) {
            if (initialized) {
                throw new MovieRentalException("illegal change of rental's id");
            } else {
                initialized = true;
                super.set(anId);
            }
        }
    };
    private final ObjectProperty<Movie> movie = new SimpleObjectProperty<Movie>() {
        @Override
        public void set(Movie aMovie) {
            if (aMovie == null || (initialized && aMovie.isRented())) {
                throw new MovieRentalException("movie must not be null or is already rented.");
            }
            super.set(aMovie);
        }
    };
    private final ObjectProperty<User> user = new SimpleObjectProperty<User>() {
        @Override
        public void set(User anUser) {
            if (anUser == null) {
                throw new MovieRentalException("user must not be null.");
            }
            super.set(anUser);
        }
    };
    private final ObjectProperty<LocalDate> rentalDate = new SimpleObjectProperty<LocalDate>() {
        @Override
        public void set(LocalDate aRentalDate) {
            if (aRentalDate == null || aRentalDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Rental date must not be null or in the future.");
            }
            super.set(aRentalDate);
        }
    };
    private final ReadOnlyLongProperty rentalDays = new SimpleLongProperty(0) {
        @Override
        public long get() {
            return ChronoUnit.DAYS.between(rentalDate.get(), LocalDate.now());
        }
    };
    private final ReadOnlyDoubleProperty rentalFee = new SimpleDoubleProperty(0.0D) {
        @Override
        public double get() {
            return getMovie().getPriceCategory().getCharge(getRentalDays());
        }
    };

    /**
     * Unique identifier for a Rental object.
     * 
     * @return Java FX property for unique identification number.
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * The movie that was rented.
     * 
     * @return Java FX property for the movie.
     */
    public ObjectProperty<Movie> movieProperty() {
        return movie;
    }

    /**
     * The user who is renting.
     * 
     * @return Java FX property for the user.
     */
    public ObjectProperty<User> userProperty() {
        return user;
    }

    /**
     * The date when the move was rented.
     * 
     * @return Java FX property for the rental date.
     */
    public ObjectProperty<LocalDate> rentalDateProperty() {
        return rentalDate;
    }

    /**
     * The duration of this rental in days.
     * 
     * @return Java FX property for the user.
     */
    public ReadOnlyLongProperty rentalDaysProperty() {
        return rentalDays;
    };

    /**
     * The fee that is charged for this Rental.
     * 
     * @return Java FX property for rental fee.
     */
    public ReadOnlyDoubleProperty rentalFeeProperty() {
        return rentalFee;
    }

    /** The maximum number of movies a user may rent. */
    public static final int MAX_NUMBER_OF_RENTALS = 3;

    /**
     * Constructs a rental of a movie to a user for a certain number of days.
     * 
     * @param aUser User who is renting aMovie.
     * @param aMovie Movie that is rented.
     * @throws IllegalStateException if the movie is already rented.
     * @throws NullPointerException if not all input parameters where set.
     * @throws IllegalArgumentException if the user is too young to see the movie, or the user has
     *             already rented the maximum rentable movies
     * @throws MovieRentalException if the user is not old enough to rent the movie.
     */
    public Rental(User aUser, Movie aMovie) {
        initialized = true;
        user.set(aUser);
        movie.set(aMovie);
        initialized = false;
        
        if (maxNumberOfRentalsReached(aUser)) {
            throw new IllegalArgumentException("Max. " + MAX_NUMBER_OF_RENTALS + " Filme ausleihbar");
        }
        if (!isUserOfAge(aMovie, aUser)) {
            throw new MovieRentalException("user " + aUser.getId() + " not old enough to rent movie " + aMovie.getId());
        }

        aUser.getRentals().add(this);
        aMovie.setRented(true);
        rentalDate.set(LocalDate.now());
    }
    
    /**
     * Used to instantiate Rental objects based on data from the database.
     */
    private Rental() { }

    /**
     * Constructs a rental of a movie to a user at a given date for a certain number of days.
     * 
     * @param id the unique identification of this rental
     * @param aUser User who is renting aMovie.
     * @param aMovie Movie that is rented.
     * @param aRentalDate date of start of this rental.
     * @throws NullPointerException if not all input parameters where set.
     * @throws MovieRentalException if the user is not old enough to rent the movie.
     * @return a newly initialized Rental object.
     */
    public static Rental materializeRentalFromDB(int id, User aUser, Movie aMovie, LocalDate aRentalDate) {
        Rental rental = new Rental();
        rental.user.set(aUser);
        rental.movie.set(aMovie);
        rental.rentalDate.set(aRentalDate);
        rental.setId(id);

        aUser.getRentals().add(rental);
        aMovie.setRented(true);
        return rental;
    }

    /**
     * Checks if the number of maximum rentals has been reached.
     * 
     * @param aUser the user to check the rentals.
     * @return true if maximum is reached, otherwise false.
     */
    private boolean maxNumberOfRentalsReached(User aUser) {
        return aUser.getRentals().size() >= MAX_NUMBER_OF_RENTALS;
    }

    /**
     * @return the unique rental identifier.
     */
    public int getId() {
        return id.get();
    }

    /**
     * The unique identifier can only be set once.
     * 
     * @param anId a unique identifier for rentals.
     * @throws MovieRentalException if the id has already been set.
     */
    public void setId(int anId) {
        id.set(anId);
    }

    /**
     * Calculate the duration of this rental.
     * 
     * @return the number of days this movie is rented to the user.
     */
    public long getRentalDays() {
        return rentalDays.get();
    }

    /**
     * @return The rental fee to pay for this rental.
     */
    public double getRentalFee() {
        return rentalFee.get();
    }

    /**
     * @return the rented movie.
     */
    public Movie getMovie() {
        return movie.get();
    }

    /**
     * @return the user who is renting.
     */
    public User getUser() {
        return user.get();
    }

    /**
     * @return the rental date.
     */
    public LocalDate getRentalDate() {
        return rentalDate.get();
    }

    /**
     * @param aMovie the movie that is rented.
     * @throws NullPointerException if aMovie is <code>null</code>.
     */
    protected void setMovie(Movie aMovie) {
        movie.set(aMovie);
    }

    /**
     * @param anUser the user that is renting a movie.
     * @throws NullPointerException if anUser is <code>null</code>.
     */
    protected void setUser(User anUser) {
        user.set(anUser);
    }

    /**
     * @param aRentalDate the date of the rental.
     */
    protected void setRentalDate(LocalDate aRentalDate) {
        rentalDate.set(aRentalDate);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = this == o;
        if (!result) {
            if (o instanceof Rental) {
                Rental other = (Rental) o;
                result = initialized ? getId() == other.getId() : initialized == other.initialized;
                result &= getMovie().equals(other.getMovie());
                result &= getUser().equals(other.getUser());
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = initialized ? getId() : 0;
        result = result * 19 + getMovie().hashCode();
        result = result * 19 + getUser().hashCode();
        return result;
    }

    /**
     * @param m the movie the user u wants to rent.
     * @param u the user who wants to rent movie m.
     * @return whether the user is old enough to see the movie.
     */
    private static boolean isUserOfAge(Movie m, User u) {
        LocalDate today = LocalDate.now();
        LocalDate birthdate = u.getBirthdate();
        int ageRating = m.getAgeRating();

        Period age = Period.between(birthdate, today);
        return age.getYears() >= ageRating;
    }

}
