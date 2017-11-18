package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;
import view.LoginView;
import view.Mainview;

import java.io.IOException;

public class SceneManager {
    private Stage primaryStage;
    private MainController controller;

    public void setStage(Stage stage){
        this.primaryStage = stage;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void showLoginView(){
        System.out.println("showLoginView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/login.fxml"));
            AnchorPane loginLayout = loader.load();
            LoginView loginView = loader.getController();
            loginView.setController(controller);
            loginView.setSceneManager(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(loginLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView(CustomerInfoManager customerInfo){
        System.out.println("show MainView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainview.fxml"));
            AnchorPane mainViewLayout = loader.load();
            Mainview mainview = loader.getController();
            mainview.setController(controller);
            mainview.setSceneManager(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainViewLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationView(){

    }

    public void showMyReservationView(){

    }

    public void showInformationView(){

    }
}
