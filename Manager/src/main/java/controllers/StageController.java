package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import views.MainView;

import java.io.IOException;

public class StageController {
    private Stage primaryStage;
    private MainController controller;

    public void showMainView(){
        System.out.println("showLoginView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainviewmanage.fxml"));
            AnchorPane mainLayout = loader.load();
            MainView mainView = loader.getController();
            mainView.setController(controller);
            mainView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
