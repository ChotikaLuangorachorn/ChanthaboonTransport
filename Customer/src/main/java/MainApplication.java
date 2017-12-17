import controller.*;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.CustomerDatabaseManager;

public class MainApplication extends Application {
    private SceneManager sceneManager;
    private MainController controller;
    // new Version
    private CustomerController customerController;
    private PriceController priceController;
    private ReservationController reservationController;
    private VanController vanController;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new MainController();
        // new Version ----------
        this.customerController = new CustomerController();
        this.priceController = new PriceController();
        this.reservationController = new ReservationController();
        this.vanController = new VanController();
        // ---------------

        this.sceneManager = new SceneManager();
        this.sceneManager.setStage(this.primaryStage);
        this.sceneManager.setController(this.controller);
        this.sceneManager.showLoginView();
        this.primaryStage.show();
    }


}
