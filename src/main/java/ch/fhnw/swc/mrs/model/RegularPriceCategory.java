package ch.fhnw.swc.mrs.model;

/**
 * @author wolfgang.schwaiger
 * 
 */
public final class RegularPriceCategory extends PriceCategory {

  /**
   * A regular movie costs 2 in the first two days and then another 1.5 for each additional day.
   * 
   * @see ch.fhnw.swc.mrs.model.PriceCategory#getCharge(long)
   * @param daysRented no of days that a movie is rented.
   * @return rental price for movie.
   */
  @Override
  public double getCharge(long daysRented) {
    double result = 0;
    if (daysRented > 0) {
      result = 2;
    }
    if (daysRented > 2) {
      result = 2 + (daysRented - 2) * 1.5;
    }
    return result;
  }

  @Override
  public String toString() {
    return "Regular";
  }

  /** singleton instance. */
  private static RegularPriceCategory singleton = new RegularPriceCategory();

  /**
   * prevent instantiation from outside.
   */
  private RegularPriceCategory() {
  };

  /**
   * Access singleton instance.
   * 
   * @return singleton
   */
  public static RegularPriceCategory getInstance() {
    return singleton;
  }
}
