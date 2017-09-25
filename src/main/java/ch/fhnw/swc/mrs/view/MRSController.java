package ch.fhnw.swc.mrs.view;

import java.io.IOException;

import ch.fhnw.swc.mrs.model.MRSServices;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

/**
 * FX controller responsible for overall application.
 */
public class MRSController {
  
  @FXML private TabPane tabPane;
  @FXML private Tab rentMovieTab;
  @FXML private Tab movieTab;
  @FXML private Tab userTab;
  @FXML private Tab rentalTab;
  private ObjectProperty<MRSServiceUtilizer> rentMovieController = new SimpleObjectProperty<MRSServiceUtilizer>();
  private ObjectProperty<MRSServiceUtilizer> movieController = new SimpleObjectProperty<MRSServiceUtilizer>();
  private ObjectProperty<MRSServiceUtilizer> userController = new SimpleObjectProperty<MRSServiceUtilizer>();
  private ObjectProperty<MRSServiceUtilizer> rentalController = new SimpleObjectProperty<MRSServiceUtilizer>();
  
  /**
   * The constructor. The constructor is called before the initialize() method.
   */
  public MRSController() {
  }

  /**
   * Initializes the controller class. This method is automatically called after
   * the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    tabPane.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldTab, newTab) -> handleTabChange(oldTab, newTab));
  }
  
  private Object handleTabChange(Tab oldTab, Tab newTab) {
    switch (newTab.getId()) {
      case "RentMovie": rentMovieController.get().reload(); break;
      case "Movies": movieController.get().reload(); break;
      case "Users": userController.get().reload(); break;
      case "Rentals": rentalController.get().reload(); break;
      default:
    }
    return null;
  }
  
  /**
   * Set the list of movies in the table.
   * @param provider the MRSServiceProvider to use in the tabs' controllers.
   */
  public void initTabs(MRSServices provider) {
    rentMovieTab.setContent(initTab("Rent Movies", "RentMovieTab.fxml", provider, rentMovieController));
    movieTab.setContent(initTab("Movies", "MovieTab.fxml", provider, movieController));
    userTab.setContent(initTab("Users", "UserTab.fxml", provider, userController));
    rentalTab.setContent(initTab("Rentals", "RentalTab.fxml", provider, rentalController));
    //preload first tab, as it will not receive a selection change event.
    rentMovieController.get().reload();
  }

  private Node initTab(String name, String ressource, 
                       MRSServices provider, 
                       ObjectProperty<MRSServiceUtilizer> ctrl) {
    Node content;
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(this.getClass().getResource(ressource));
      content = loader.load();
      MRSServiceUtilizer controller = loader.getController();
      controller.setServiceProvider(provider);
      ctrl.set(controller);
    } catch (IOException e) {
      content = new TextArea(e.getStackTrace().toString());
    }
    return content;
  }
  
}
