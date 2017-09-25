package ch.fhnw.swc.mrs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract price category class for movie rentals.
 * 
 */
public abstract class PriceCategory {

  private static List<PriceCategory> registeredCategories = new ArrayList<>(3);
  
  /**
   * @param daysRented none.
   * @return none.
   */
  public abstract double getCharge(long daysRented);

  /**
   * @param daysRented none.
   * @return none.
   */
  public int getFrequentRenterPoints(int daysRented) {
    return daysRented > 0 ? 1 : 0;
  }

  /**
   * Get the concrete PriceCategory object.
   * @param name the name of the price category to retrieve.
   * @return the price category with the given id or null if not found.
   */
  public static PriceCategory getPriceCategoryFromId(String name) {
    for (PriceCategory cat: registeredCategories) {
      if (cat.toString().equals(name)) {
        return cat;
      }
    }
    return null;
  }
  
  /**
   * Register a concrete price category for retrieval through getPriceCategoryFromId.
   * @param pc a price category to register.
   */
  private static void registerPriceCategory(PriceCategory pc) {
    if (!registeredCategories.contains(pc)) {
      registeredCategories.add(pc);
    }   
  }
  
  /** Initialize a list of available price categories. */
  public static void init() {
    registerPriceCategory(RegularPriceCategory.getInstance());
    registerPriceCategory(ChildrenPriceCategory.getInstance());
    registerPriceCategory(NewReleasePriceCategory.getInstance());    
  }
}
