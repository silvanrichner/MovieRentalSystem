package ch.fhnw.swc.mrs.view;

import java.time.LocalDate;
import java.util.List;

import ch.fhnw.swc.mrs.model.ChildrenPriceCategory;
import ch.fhnw.swc.mrs.model.MRSServices;
import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.NewReleasePriceCategory;
import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.model.RegularPriceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Java FX controller class for Movies.
 */
public class MovieController implements MRSServiceUtilizer {
  @FXML
  private TableView<Movie> movieTable;
  @FXML
  private TableColumn<Movie, Number> idColumn;
  @FXML
  private TableColumn<Movie, String> titleColumn;
  @FXML
  private TableColumn<Movie, LocalDate> releaseDateColumn;
  @FXML
  private TableColumn<Movie, Number> ageRatingColumn;
  @FXML
  private TableColumn<Movie, PriceCategory> priceCategoryColumn;
  @FXML
  private GridPane grid;
  @FXML
  private TextField titleField;
  @FXML
  private DatePicker releaseDatePicker;
  @FXML
  private ComboBox<PriceCategory> priceCategoryChooser;
  @FXML
  private ComboBox<Integer> ageRatingChooser;
  @FXML
  private Button cancelButton;
  @FXML
  private Button newButton;
  @FXML
  private Button editButton;
  @FXML
  private Button deleteButton;
  @FXML
  private Button saveButton;
  
  private Movie editing = null;  // currently no movie is being edited.
  private MRSServices backend;
  private ObservableList<Movie> movieList = FXCollections.observableArrayList();

  /**
   * Initializes the controller class. This method is automatically called after
   * the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    // Initialize the movie table.
    idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
    ageRatingColumn.setCellValueFactory(cellData -> cellData.getValue().ageRatingProperty());
    priceCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().priceCategoryProperty());
    
    priceCategoryChooser.getItems().addAll(
         RegularPriceCategory.getInstance(),
         ChildrenPriceCategory.getInstance(),
         NewReleasePriceCategory.getInstance());
    
    ageRatingChooser.getItems().addAll(0, 6, 12, 14, 16, 18);
    
    movieTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> handleSelectionChange(oldValue, newValue));
    
    grid.setDisable(true);
    showMovieDetails(null);
  }
  
  private Object handleSelectionChange(Movie oldValue, Movie newValue) {
    handleCancel();
    if (newValue != null) {
      editButton.setDisable(false);
      deleteButton.setDisable(false);
    }
    return null;
  }

  private void showMovieDetails(Movie movie) {
    if (movie != null) {
      // fill the labels with info from the Movie object
      titleField.setText(movie.getTitle());
      releaseDatePicker.setValue(movie.getReleaseDate());
      priceCategoryChooser.setValue(movie.getPriceCategory());
      ageRatingChooser.setValue(movie.getAgeRating());
    } else {
      // clear the content and set default values
      titleField.setText("");
      releaseDatePicker.setValue(null);
      priceCategoryChooser.setValue(null);
      ageRatingChooser.setValue(null);
    }
  }
  
  @FXML
  private void handleCancel() {
    cancelButton.setDisable(true);
    newButton.setDisable(false);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(true);
    showMovieDetails(null);
    titleField.setEditable(false);
    releaseDatePicker.setEditable(false);
    grid.setDisable(true);
    editing = null;
  }
  
  @FXML
  private void handleNew() {
    cancelButton.setDisable(false);
    newButton.setDisable(true);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(false);
    showMovieDetails(null);
    titleField.setEditable(true);
    releaseDatePicker.setEditable(true);
    grid.setDisable(false);
    titleField.requestFocus();
    editing = null;
  }
  
  @FXML
  private void handleSave() {
    if (editing == null) {
      Movie m = new Movie(titleField.getText(),
                          releaseDatePicker.getValue(),
                          priceCategoryChooser.getValue(),
                          ageRatingChooser.getValue());
      m = backend.createMovie(m);
      movieTable.getItems().add(m);
    } else {
      editing.setTitle(titleField.getText());
      editing.setReleaseDate(releaseDatePicker.getValue());
      editing.setPriceCategory(priceCategoryChooser.getValue());
      editing.setAgeRating(ageRatingChooser.getValue());
      backend.updateMovie(editing);
    }
    handleCancel();
  }
  
  @FXML
  private void handleEdit() {
    cancelButton.setDisable(false);
    newButton.setDisable(true);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(false);
    Movie m = movieTable.getSelectionModel().getSelectedItem();
    showMovieDetails(m);
    titleField.setEditable(true);
    releaseDatePicker.setEditable(true);
    grid.setDisable(false); 
    titleField.requestFocus();
    editing = m;
  }
  
  @FXML
  private void handleDelete() {
    cancelButton.setDisable(true);
    newButton.setDisable(false);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(true);
    Movie m = movieTable.getSelectionModel().getSelectedItem();
    if (backend.deleteMovie(m)) {
      movieTable.getItems().remove(m);
      movieTable.getSelectionModel().clearSelection();
    }
    showMovieDetails(null);
    titleField.setEditable(false);
    releaseDatePicker.setEditable(false);
    grid.setDisable(true);
    editing = null;
  }
  
  @Override
  public void setServiceProvider(MRSServices provider) {
    backend = provider;
  }

  @Override
  public void reload() {
    movieList.clear();
    List<Movie> movies = backend.getAllMovies();
    for (Movie m : movies) {
      movieList.add(m);
    }
    movieTable.setItems(movieList);
  }

}
