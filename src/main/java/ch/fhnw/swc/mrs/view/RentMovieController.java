package ch.fhnw.swc.mrs.view;

import java.time.LocalDate;
import java.util.List;

import ch.fhnw.swc.mrs.model.MRSServices;
import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** Java FX controller class for rent movies tab. */
public class RentMovieController implements MRSServiceUtilizer {
  @FXML
  private TableView<Movie> availableMoviesTable;
  @FXML
  private TableColumn<Movie, Number> idColumn;
  @FXML
  private TableColumn<Movie, String> titleColumn;
  @FXML
  private TableColumn<Movie, LocalDate> releaseDateColumn;
  @FXML
  private TableColumn<Movie, PriceCategory> priceCategoryColumn;
  @FXML
  private TextField idField;
  @FXML
  private TextField surnameField;
  @FXML
  private TextField firstnameField;
  @FXML
  private DatePicker birthdatePicker;
  @FXML
  private DatePicker rentalDatePicker;
  @FXML
  private CheckBox newUser;
  @FXML
  private Button getUserButton;
  @FXML
  private Button clearAllButton;
  @FXML
  private Button saveButton;

  private MRSServices backend;
  private ObservableList<Movie> rentMovieList = FXCollections.observableArrayList();
  private User found = null;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @FXML
  private void initialize() {
    // Initialize the movie table.
    idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
    priceCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().priceCategoryProperty());
    availableMoviesTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> handleSelectionChange(oldValue, newValue));
  }

  @Override
  public void setServiceProvider(MRSServices provider) {
    backend = provider;
  }

  @Override
  public void reload() {
    rentMovieList.clear();
    List<Movie> movies = backend.getAllMovies(false);
    for (Movie m : movies) {
      rentMovieList.add(m);
    }
    availableMoviesTable.setItems(rentMovieList);
  }

  @FXML
  private void handleNewUser() {
    if (newUser.isSelected()) {
      setNewUserEnabling();
    } else {
      setReadyEnabling();
    }
    clearAllFields();
    surnameField.requestFocus();
  }

  private void setReadyEnabling() {
    newUser.setDisable(false);
    getUserButton.setDisable(false);
    saveButton.setDisable(true);
    idField.setDisable(false);
    surnameField.setDisable(false);
    firstnameField.setDisable(true);
    birthdatePicker.setDisable(true);
    rentalDatePicker.setDisable(true);
  }

  private void setNewUserEnabling() {
    newUser.setDisable(false);
    getUserButton.setDisable(true);
    saveButton.setDisable(false);
    idField.setDisable(true);
    surnameField.setDisable(false);
    firstnameField.setDisable(false);
    birthdatePicker.setDisable(false);
    rentalDatePicker.setDisable(false);
  }
  
  private void clearAllFields() {
    surnameField.clear();
    firstnameField.clear();
    idField.clear();
    rentalDatePicker.setValue(null);
    birthdatePicker.setValue(null);    
  }

  @FXML
  private void handleClearAll() {
    newUser.setSelected(false);
    setReadyEnabling();
    clearAllFields();
  }

  @FXML
  private void handleGetUser() {
    String username = surnameField.getText();
    String idstring = idField.getText();
    try {
      int id = Integer.parseInt(idstring);
      found = backend.getUserById(id);
    } catch (NumberFormatException e) {
      found = backend.getUserByName(username);
    }
    if (found != null) {
      idField.setText(Integer.toString(found.getId()));
      surnameField.setText(found.getName());
      firstnameField.setText(found.getFirstName());
      birthdatePicker.setValue(found.getBirthdate());
      rentalDatePicker.setValue(LocalDate.now());
      idField.setDisable(true);
      surnameField.setDisable(true);
    } else {
      idField.setText(null);
      surnameField.setText(null);
      firstnameField.setText(null);
      surnameField.requestFocus();
    }
  }

  @FXML
  private void handleSave() {
    Movie m = availableMoviesTable.getSelectionModel().getSelectedItem();
    backend.createRental(found, m);
    reload();
    handleClearAll();
  }
  
  @FXML
  private void enterPressed() {
    if (newUser.isSelected() && !saveButton.isDisabled()) { // enter means save
      handleSave();
    } else { // enter means get User
      handleGetUser();
    }
  }

  private void handleSelectionChange(Movie oldMovie, Movie newMovie) {
    saveButton.setDisable(newMovie == null);
  }

}
