package ch.fhnw.swc.mrs.view;

import java.time.LocalDate;
import java.util.List;

import ch.fhnw.swc.mrs.model.MRSServices;
import ch.fhnw.swc.mrs.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Java FX controller class for Users.
 */
public class UserController implements MRSServiceUtilizer {
  @FXML
  private TableView<User> userTable;
  @FXML
  private TableColumn<User, Number> idColumn;
  @FXML
  private TableColumn<User, String> surnameColumn;
  @FXML
  private TableColumn<User, String> firstNameColumn;
  @FXML
  private TableColumn<User, LocalDate> birthdateColumn;
  @FXML
  private GridPane grid;
  @FXML
  private TextField surnameField;
  @FXML
  private TextField firstNameField;
  @FXML
  private DatePicker birthdatePicker;
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

  private User editing = null;  // currently no user is being edited.
  private MRSServices backend;
  private ObservableList<User> userList = FXCollections.observableArrayList();

  /**
   * Initializes the controller class. This method is automatically called after
   * the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    // Initialize the movie table.
    idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    surnameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
    birthdateColumn.setCellValueFactory(cellData -> cellData.getValue().birthdateProperty());

    userTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> handleSelectionChange(oldValue, newValue));

    grid.setDisable(true);
    showUserDetails(null);
  }

  private Object handleSelectionChange(User oldValue, User newValue) {
    handleCancel();
    if (newValue != null) {
      editButton.setDisable(false);
      deleteButton.setDisable(false);
    }
    return null;
  }

  private void showUserDetails(User user) {
    if (user != null) {
      // fill the labels with info from the Movie object
      surnameField.setText(user.getName());
      firstNameField.setText(user.getFirstName());
      birthdatePicker.setValue(user.getBirthdate());
    } else {
      // clear the content and set default values
      surnameField.setText("");
      firstNameField.setText("");
      birthdatePicker.setValue(null);
    }
  }

  @FXML
  private void handleCancel() {
    cancelButton.setDisable(true);
    newButton.setDisable(false);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(true);
    showUserDetails(null);
    surnameField.setEditable(false);
    firstNameField.setEditable(false);
    birthdatePicker.setEditable(false);
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
    showUserDetails(null);
    surnameField.setEditable(true);
    firstNameField.setEditable(true);
    birthdatePicker.setEditable(true);
    grid.setDisable(false);
    surnameField.requestFocus();
    editing = null;
  }

  @FXML
  private void handleSave() {
    if (editing == null) {
      User u = new User(surnameField.getText(),
                        firstNameField.getText(),
                        birthdatePicker.getValue());
      u = backend.createUser(u);
      userTable.getItems().add(u);
    } else {
      editing.setName(surnameField.getText());
      editing.setFirstName(firstNameField.getText());
      editing.setBirthdate(birthdatePicker.getValue());
      backend.updateUser(editing);
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
    User u = userTable.getSelectionModel().getSelectedItem();
    showUserDetails(u);
    surnameField.setEditable(true);
    firstNameField.setEditable(true);
    birthdatePicker.setEditable(true);
    grid.setDisable(false);
    surnameField.requestFocus();
    editing = u;
  }

  @FXML
  private void handleDelete() {
    cancelButton.setDisable(true);
    newButton.setDisable(false);
    editButton.setDisable(true);
    deleteButton.setDisable(true);
    saveButton.setDisable(true);
    User m = userTable.getSelectionModel().getSelectedItem();
    if (backend.deleteUser(m)) {
      userTable.getItems().remove(m);
      userTable.getSelectionModel().clearSelection();
    }
    showUserDetails(null);
    surnameField.setEditable(false);
    firstNameField.setEditable(false);
    birthdatePicker.setEditable(false);
    grid.setDisable(true);
    editing = null;
  }

  @Override
  public void setServiceProvider(MRSServices provider) {
    backend = provider;
  }

  @Override
  public void reload() {
    userList.clear();
    List<User> users = backend.getAllUsers();
    for (User u: users) {
      userList.add(u);
    }
    userTable.setItems(userList);
  }

}
