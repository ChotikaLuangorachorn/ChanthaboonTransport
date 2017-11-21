import controllers.MainController;
import controllers.StageController;
import javafx.application.Application;
import javafx.stage.Stage;
import sun.applet.Main;

public class ManagerApplication extends Application {
    private MainController controller;
    private StageController stageController;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new MainController();
        this.stageController = new StageController();
        this.stageController.setController(controller);
        this.stageController.setPrimaryStage(this.primaryStage);
        this.stageController.showMainView();
        this.primaryStage.show();
    }
}
