package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.CustomerInfoManager;
import view.LoginView;
import view.Mainview;

import java.io.IOException;

public class SceneManager {
    private Stage primaryStage;
//    private MainController controller;
    // new Version
    private CustomerController customerController;
    private PriceController priceController;
    private ReservationController reservationController;
    private VanController vanController;

    public void setStage(Stage stage){

        this.primaryStage = stage;
    }

    public void showLoginView(){
        System.out.println("showLoginView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/login.fxml"));
            AnchorPane loginLayout = loader.load();
            LoginView loginView = loader.getController();
            loginView.setController(customerController);
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
            mainview.setCustomerController(customerController);
            mainview.setPriceController(priceController);
            mainview.setReservationController(reservationController);
            mainview.setVanController(vanController);
            mainview.setSceneManager(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainViewLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setPriceController(PriceController priceController) {
        this.priceController = priceController;
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void setVanController(VanController vanController) {
        this.vanController = vanController;
    }
}
