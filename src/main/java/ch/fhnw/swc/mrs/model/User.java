package ch.fhnw.swc.mrs.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the client of a movie store.
 * 
 */
public class User {

  /**
   * Flag indicating whether object has been initialized.
   */
  private boolean initialized = false;

  /** An identification number unique to each user. */
  private final IntegerProperty id = new SimpleIntegerProperty(0) {
    @Override public void set(int anId) {
      if (initialized) {
        throw new MovieRentalException("illegal change of user's id");
      } else {
        initialized = true;
        super.set(anId);
      }
    }
  };

  /** The user's family name. */
  private final StringProperty name = new SimpleStringProperty("Unnamed") {
    @Override public void set(String aName) {
      checkName(aName);
      super.set(aName);
    }
  };

  /** The user's first name. */
  private final StringProperty firstName = new SimpleStringProperty("Unnamed") {
    @Override public void set(String aName) {
      checkName(aName);
      super.set(aName);
    }
  };

  /** The user's birthdate is used to check age ratings. */
  private final ObjectProperty<LocalDate> birthdate = new SimpleObjectProperty<LocalDate>() {
    @Override public void set(LocalDate aBirthdate) {
      checkBirthdate(aBirthdate);
      super.set(aBirthdate);
    }
  };

  /**
   * A list of rentals of the user.
   */
  private List<Rental> rentals = new LinkedList<Rental>();

  /**
   * Create a new user with the given name information.
   * 
   * @param aName the user's family name.
   * @param aFirstName the user's first name.
   * @param aBirthdate the users's birthdate. Must not be in the future.
   * @throws IllegalArgumentException The name must neither be <code>null</code>.
   * @throws MovieRentalException If the name is empty ("") or longer than 40 characters.
   */
  public User(String aName, String aFirstName, LocalDate aBirthdate) {
    name.set(aName);
    firstName.set(aFirstName);
    birthdate.set(aBirthdate);
  }

  /**
   * Checks if birthdate is valid.
   * 
   * @param aBirthdate must not be null or in the future.
   */
  private void checkBirthdate(LocalDate aBirthdate) {
    LocalDate now = LocalDate.now();
    if (now.isBefore(aBirthdate)) {
      throw new IllegalArgumentException("illegal birthdate");
    }
  }

  /**
   * Checks if name is valid.
   * 
   * @param aName the name of the user.
   */
  private void checkName(String aName) {
    if (aName != null) {
      if ((aName.length() == 0) || (aName.length() > 40)) {
        throw new MovieRentalException("invalid name value");
      }
    } else {
      throw new IllegalArgumentException("non-existing name");
    }

  }

  /**
   * @return The user's unique identification number.
   */
  public int getId() {
    return id.get();
  }

  /**
   * @param anID set the user's unique identification number.
   */
  public void setId(int anID) {
    id.set(anID);
  }

  /** @return Java FX property for unique identification. */
  public IntegerProperty idProperty() {
    return id;
  }

  /**
   * @return get a list of the user's rentals.
   */
  public List<Rental> getRentals() {
    return rentals;
  }

  /**
   * @param someRentals set the user's rentals.
   */
  public void setRentals(List<Rental> someRentals) {
    this.rentals = someRentals;
  }

  /** {@inheritDoc} */
  public String getName() {
    return name.get();
  }

  /**
   * @param aName set the user's family name.
   * @throws NullPointerException The name must neither be <code>null</code>.
   * @throws MovieRentalException If the name is emtpy ("") or longer than 40 characters.
   */
  public void setName(String aName) {
    name.set(aName);
  }

  /** @return Java FX property for surname. */
  public StringProperty nameProperty() {
    return name;
  }

  /**
   * @return get the user's first name.
   */
  public String getFirstName() {
    return firstName.get();
  }

  /**
   * @param aFirstName set the user's family name.
   * @throws NullPointerException The first name must not be <code>null</code>.
   * @throws MovieRentalException If the name is emtpy ("") or longer than 40 characters.
   */
  public void setFirstName(String aFirstName) {
    firstName.set(aFirstName);
  }

  /** @return Java FX property for first name. */
  public StringProperty firstNameProperty() {
    return firstName;
  }

  /**
   * @return user's birth date.
   */
  public LocalDate getBirthdate() {
    return birthdate.get();
  }

  /**
   * Set a users birthdate.
   * 
   * @param aBirthdate must not be in the future.
   */
  public void setBirthdate(LocalDate aBirthdate) {
    birthdate.set(aBirthdate);
  }

  /** @return Java FX property for birthdate. */
  public ObjectProperty<LocalDate> birthdateProperty() {
    return birthdate;
  }

  /**
   * Calculate the total charge the user has to pay for all his/her rentals.
   * 
   * @return the total charge.
   */
  public double getCharge() {
    double result = 0.0d;
    for (Rental rental : rentals) {
      result += rental.getMovie().getPriceCategory().getCharge(rental.getRentalDays());
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    boolean result = this == o;
    if (!result) {
      if (o instanceof User) {
        User other = (User) o;
        result = getId() == other.getId();
        result &= getName().equals(other.getName());
        result &= getFirstName().equals(other.getFirstName());
        result &= getBirthdate().equals(other.getBirthdate());
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    int result = (initialized) ? getId() : 0;
    result = 19 * result + getName().hashCode();
    result = 19 * result + getFirstName().hashCode();
    return result;
  }

  /**
   * check if user has rentals.
   * 
   * @return true if found
   */
  public boolean hasRentals() {
    return !rentals.isEmpty();
  }

  /**
   * add a new rental to the user.
   * 
   * @param rental the rental
   * @return number of rentals of the user
   */
  public int addRental(Rental rental) {
    rentals.add(rental);
    return rentals.size();
  }
}
