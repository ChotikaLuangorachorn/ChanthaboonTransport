import controller.MainController;
import controller.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.CustomerDatabaseManager;

public class MainApplication extends Application {
    private SceneManager sceneManager;
    private MainController controller;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new MainController();
        this.sceneManager = new SceneManager();
        this.sceneManager.setStage(this.primaryStage);
        this.sceneManager.setController(this.controller);
        this.sceneManager.showLoginView();
        this.primaryStage.show();
    }


}
