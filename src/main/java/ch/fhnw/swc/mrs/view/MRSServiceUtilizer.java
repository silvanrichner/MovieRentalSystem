package ch.fhnw.swc.mrs.view;

import ch.fhnw.swc.mrs.model.MRSServices;

/**
 * Implementing class utilizes services from MRSServices.
 */
public interface MRSServiceUtilizer {
  /**
   * Set the MRSServices instance in order to utilize its services.
   * @param provider of MRS services.
   */
  void setServiceProvider(MRSServices provider);
  
  /**
   * Reload data used/processed/displayed in this ServiceUtilizer.
   */
  void reload();
}
