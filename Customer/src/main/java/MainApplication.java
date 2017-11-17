import controller.MainController;
import controller.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.CustomerDatabaseManager;

public class MainApplication extends Application {
    private SceneManager sceneManager;
    private CustomerDatabaseManager customerDatabaseManager;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.customerDatabaseManager = new MainController();
        this.sceneManager = new SceneManager();
        this.sceneManager.setStage(primaryStage);
        this.sceneManager.setCustomerDatabaseManager(customerDatabaseManager);
        this.sceneManager.showLoginView();
        primaryStage.show();
    }


}
