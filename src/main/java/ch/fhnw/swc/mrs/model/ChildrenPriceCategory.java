package ch.fhnw.swc.mrs.model;

/**
 * @author wolfgang.schwaiger
 * 
 */
public final class ChildrenPriceCategory extends PriceCategory {

  /**
   * The first three days cost only 1.5, then each days costs an extra 1.5.
   * 
   * @see ch.fhnw.swc.mrs.model.PriceCategory#getCharge(long)
   * @param daysRented no of days that a movie is rented.
   * @return rental price for movie.
   */
  @Override
  public double getCharge(long daysRented) {
    double result = 0;
    if (daysRented > 0) {
      result = 1.5;
      if (daysRented > 3) {
        result += (daysRented - 3) * 1.5;
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return "Children";
  }

  /** singleton instance. */
  private static ChildrenPriceCategory singleton = new ChildrenPriceCategory();

  /**
   * prevent instantiation from outside.
   */
  private ChildrenPriceCategory() {
  };

  /**
   * Access singleton instance.
   * 
   * @return singleton
   */
  public static ChildrenPriceCategory getInstance() {
    return singleton;
  }

}
