package ch.fhnw.swc.mrs;

import ch.fhnw.swc.mrs.data.DbMRSServices;
import ch.fhnw.swc.mrs.model.MRSServices;
import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.view.MRSController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the Movie Rental System App.
 */
public class MovieRentalSystem extends Application {

	/** The backend component used in this application. */
	private MRSServices backend = new DbMRSServices();

	@Override
	public void start(Stage primaryStage) {
		backend.init();
		primaryStage.setTitle("Software Construction Lab");

		try {
			// Load gui content
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/MRS.fxml"));

			// Show the scene containing the root layout.
			primaryStage.setScene(new Scene(loader.load()));
			primaryStage.show();

			MRSController controller = loader.getController();
			controller.initTabs(backend);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method to start the app.
	 * 
	 * @param args
	 *            currently ignored.
	 */
	public static void main(String[] args) {
	    PriceCategory.init();
		launch(args);
	}

}
