package ch.fhnw.swc.mrs.model;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a movie.
 * 
 */
public class Movie {

  private final IntegerProperty id = new SimpleIntegerProperty(0);
  private final BooleanProperty rented = new SimpleBooleanProperty(false);

  private final StringProperty title = new SimpleStringProperty("Untitled") {
    @Override public void set(String aTitle) {
      log.trace("entering title property setter.");
      if (aTitle == null || aTitle.trim().isEmpty()) {
        log.trace("Illegal argument. Throwing exception");
        throw new IllegalArgumentException("Title must not be null nor emtpy");
      }
      super.set(aTitle);
      log.trace("exiting title property setter.");
    }
  };
  
  private final ObjectProperty<LocalDate> releaseDate = new SimpleObjectProperty<LocalDate>() {
    @Override public void set(LocalDate aReleaseDate) {
      log.trace("entering releaseDate property setter");
      if (aReleaseDate == null) {
        log.trace("exiting abruptly releaseDate property setter");
        throw new IllegalArgumentException("Release date must not be null");
      }
      super.set(aReleaseDate);
      log.trace("exiting releaseDate property setter"); 
    }
  };
  
  private final IntegerProperty ageRating = new SimpleIntegerProperty(0) {
    @Override public void set(int anAgeRating) {
      log.trace("entering ageRating property setter");
      if (anAgeRating < 0 || anAgeRating > 18) {
        log.trace("exiting abruptly ageRating property setter");
        throw new IllegalArgumentException("age rating must be in range [0, 18]");
      }
      super.set(anAgeRating);      
      log.trace("exiting ageRating property setter"); 
    }
  };

  /** the rental cost of the movie. */
  private ObjectProperty<PriceCategory> priceCategory = new SimpleObjectProperty<PriceCategory>() {
    @Override public void set(PriceCategory aCategory) {
      log.trace("entering priceCategory property setter");
      if (aCategory == null) {
        log.trace("exiting abruptly ageRating property setter");
        throw new IllegalArgumentException("price category must not be null");
      }
      super.set(aCategory);
      log.trace("exiting ageRating property setter");
    }
  };

  /** Logger used to produce logs. */
  private static Logger log = LogManager.getLogger();

  /** Ctor only for testing needed. */
  protected Movie() {
    this("Untitled", LocalDate.now(), RegularPriceCategory.getInstance(), 0);
    log.warn("Protected constructor of Movie class should not be called in production mode");
  }

  /**
   * @param aTitle none.
   * @param aReleaseDate none.
   * @param aPriceCategory none.
   * @param anAgeRating none.
   */
  public Movie(String aTitle, LocalDate aReleaseDate, PriceCategory aPriceCategory, int anAgeRating) {
    log.trace("entering Movie(String, Date, PriceCategory, int)");
    title.set(aTitle);
    releaseDate.set(aReleaseDate);
    priceCategory.set(aPriceCategory);
    ageRating.set(anAgeRating);
    log.trace("exiting Movie(String, Date, PriceCategory, int)");
  }

  /**
   * @return unique identification number of this Movie.
   */
  public Integer getId() {
    log.trace("in getId");
    return id.get();
  }

  /**
   * @param anId set an unique identification number for this Movie.
   */
  public void setId(Integer anId) {
    log.trace("entering setId");
    this.id.set(anId);
    log.trace("exiting setId");
  }

  /**  @return Java FX property for unique identification number. */
  public IntegerProperty idProperty() {
    return id;
  }

  /**
   * @return The title of this Movie.
   */
  public String getTitle() {
    log.trace("in getTitle");
    return title.get();
  }

  /**
   * @param aTitle set the title of this Movie.
   */
  public void setTitle(String aTitle) {
    log.trace("entering setTitle");
    title.set(aTitle);
    log.trace("exiting setTitle");
  }

  /** @return Java FX property for title. */
  public StringProperty titleProperty() {
    log.trace("in titleProperty");
    return title;
  }

  /**
   * @return whether this Movie is rented to a User.
   */
  public boolean isRented() {
    log.trace("in isRented");
    return rented.get();
  }

  /**
   * @param isRented set the rented status.
   */
  public void setRented(boolean isRented) {
    log.trace("entering setRented");
    rented.set(isRented);
    log.trace("exiting setRented");
  }

  /** @return Java FX property for rented status. */
  public BooleanProperty rentedProperty() {
    return rented;
  }

  /**
   * @return the date this Movie was released.
   */
  public LocalDate getReleaseDate() {
    log.trace("in getReleaseDate");
    return releaseDate.get();
  }

  /**
   * @param aReleaseDate set the date this Movie was released.
   */
  public void setReleaseDate(LocalDate aReleaseDate) {
    log.trace("entering setReleaseDate");
    releaseDate.set(aReleaseDate);
    log.trace("exiting setReleaseDate");
  }

  /** @return Java FX property for release date. */
  public ObjectProperty<LocalDate> releaseDateProperty() {
    log.trace("in releaseDateProperty");
    return releaseDate;
  }

  /**
   * @return the minimum age to rent this movie.
   */
  public int getAgeRating() {
    log.trace("in getAgeRating");
    return ageRating.get();
  }
  
  /** Set the minimum age to rent this movie.
   *  @param anAgeRating must be in range [0, 18]. */
  public void setAgeRating(int anAgeRating) {
    log.trace("entering setAgeRating");
    ageRating.set(anAgeRating);
    log.trace("exiting setAgeRating");
  }
  
  /** @return Java FX property for age rating. */
  public IntegerProperty ageRatingProperty() {
    return ageRating;
  }
  

  /**
   * @return PriceCategory of this Movie.
   */
  public PriceCategory getPriceCategory() {
    log.trace("in getPriceCategory");
    return priceCategory.get();
  }

  /**
   * @param aPriceCategory set PriceCategory for this Movie.
   */
  public void setPriceCategory(PriceCategory aPriceCategory) {
    log.trace("entering setPriceCategory");
    this.priceCategory.set(aPriceCategory);
    log.trace("exiting setPriceCategory");
  }

  /** @return Java FX property for PriceCategory. */
  public ObjectProperty<PriceCategory> priceCategoryProperty() {
    return priceCategory;
  }

  /**
   * @see java.lang.Object#hashCode()
   * @return none.
   */
  @Override
  public int hashCode() {
    log.trace("entering hashCode");
    final int prime = 31;
    int result = prime + getId();
    result = prime * result + ((getReleaseDate() == null) ? 0 : getReleaseDate().hashCode());
    result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
    log.trace("exiting hashCode");
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   * @param obj none.
   * @return none.
   */
  @Override
  public boolean equals(Object obj) {
    log.trace("entering equals");
    // first: cheap test on identity
    if (this == obj) {
      log.trace("exiting equals (objects are the same)");
      return true;
    }
    // second: check if its worth looking into the object
    if (!isOfSameType(obj)) {
      log.trace("exiting equals (objects are of different type)");
      return false;
    }
    // third: check equality on each attribute of the object
    Movie m = (Movie) obj;
    log.trace("exiting equals");
    return hasSameID(m) && hasEqualReleaseDates(m) && hasEqualTitles(m);
  }
  
  /**
   * Checks if parameter is either null or of the same type as this.
   * 
   * @param obj the other object to check.
   * @return true if obj is null or of type Movie.
   */
  private boolean isOfSameType(Object obj) {
    log.trace("in isOfSameType");
    return (obj != null) && (obj instanceof Movie);
  }

  /**
   * Checks if the other movie has the same id as this.
   * 
   * @param other Movie to check.
   * @return true if obj is null or of type Movie.
   */
  private boolean hasSameID(final Movie other) {
    log.trace("in hasSameID");
    return this.getId() == other.getId();
  }

  /**
   * @param other the other Movie we are comparing with.
   * @return checks whether other Movie has the same releaseDate as this.
   */
  private boolean hasEqualReleaseDates(final Movie other) {
    log.trace("in hasEqualReleaseDates");
    return getReleaseDate() == null ? other.getReleaseDate() == null : getReleaseDate().equals(other.getReleaseDate());
  }

  /**
   * @param other the other Movie we are comparing with.
   * @return whether the Movies titles are equal.
   */
  private boolean hasEqualTitles(final Movie other) {
    log.trace("in hasEqualTitles");
    return getTitle() == null ? other.getTitle() == null : getTitle().equals(other.getTitle());
  }
}
