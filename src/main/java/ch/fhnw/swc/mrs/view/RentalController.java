package ch.fhnw.swc.mrs.view;

import java.time.LocalDate;
import java.util.List;

import ch.fhnw.swc.mrs.model.MRSServices;
import ch.fhnw.swc.mrs.model.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Java FX controller class for Rentals.
 */
public class RentalController implements MRSServiceUtilizer {
  @FXML
  private TableView<Rental> rentalTable;
  @FXML
  private TableColumn<Rental, Number> idColumn;
  @FXML
  private TableColumn<Rental, Number> rentalDaysColumn;
  @FXML
  private TableColumn<Rental, LocalDate> rentalDateColumn;
  @FXML
  private TableColumn<Rental, String> surnameColumn;
  @FXML
  private TableColumn<Rental, String> firstNameColumn;
  @FXML
  private TableColumn<Rental, String> titleColumn;
  @FXML
  private TableColumn<Rental, Number> rentalFeeColumn;
  @FXML
  private Button deleteButton;
  
  private MRSServices backend;
  private ObservableList<Rental> rentalList = FXCollections.observableArrayList();

  /**
   * Initializes the controller class. This method is automatically called after
   * the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    // Initialize the movie table.
    idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    rentalDaysColumn.setCellValueFactory(cellData -> cellData.getValue().rentalDaysProperty());
    rentalDateColumn.setCellValueFactory(cellData -> cellData.getValue().rentalDateProperty());
    surnameColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().nameProperty());
    firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().firstNameProperty());
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().getMovie().titleProperty());
    rentalFeeColumn.setCellValueFactory(cellData -> cellData.getValue().rentalFeeProperty());
    
    rentalTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> handleSelectionChange(oldValue, newValue));
    
    deleteButton.setDisable(true);
  }

  private Object handleSelectionChange(Rental oldValue, Rental newValue) {
    deleteButton.setDisable(newValue == null);
    return null;
  }

  @FXML
  private void handleDelete() {
    deleteButton.setDisable(true);
    Rental r = rentalTable.getSelectionModel().getSelectedItem();
    if (backend.returnRental(r)) {
      rentalTable.getItems().remove(r);
      rentalTable.getSelectionModel().clearSelection();
    }
  }

  @Override
  public void reload() {
    rentalList.clear();
    List<Rental> rentals = backend.getAllRentals();
    for (Rental r : rentals) {
      rentalList.add(r);
    }
    rentalTable.setItems(rentalList);
  }

  @Override
  public void setServiceProvider(MRSServices provider) {
    backend = provider;
  }
  
}
