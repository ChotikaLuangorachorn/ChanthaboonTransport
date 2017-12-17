package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import views.*;

import java.io.IOException;

public class StageController {
    private Stage primaryStage;
    private MainController controller;
    // new Version
    private CustomerController customerController;
    private DriverController driverController;
    private PartnerController partnerController;
    private PriceController priceController;
    private ReservationController reservationController;
    private VanController vanController;

    public void showMainView(){
        System.out.println("Manager: showMainView");
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

    public void showVanMenuView(){
        System.out.println("Manager: showVanMenuView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/van_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            VanMenuView vanMenuView = loader.getController();
            vanMenuView.setController(controller);
            vanMenuView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationView(){
        System.out.println("Manager: showReservationView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/reservation_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            ReservationMenuView reservationMenuView = loader.getController();
            reservationMenuView.setController(controller);
            reservationMenuView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDriverMenu(){
        System.out.println("Manager: showDriverView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/driver_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            DriverMenuView driverMenuView = loader.getController();
            driverMenuView.setController(controller);
            driverMenuView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFeeMenuView(){
        System.out.println("Manager: showFeeMenuView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fee_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            FeeMenuView feeMenuView = loader.getController();
            feeMenuView.setController(controller);
            feeMenuView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPartnerMenuView(){
        System.out.println("Manager: showPartnerMenuView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/partner_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            PartnerMenuView partnerMenuView = loader.getController();
            partnerMenuView.setController(controller);
            partnerMenuView.setStageController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConfirmReservationMenu(){
        System.out.println("Manager: showConfirmReservationMenuView");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/confirmReservation_menu.fxml"));
            AnchorPane mainLayout = loader.load();
            ConfirmReservationMenuView confirmReservationMenu = loader.getController();
            confirmReservationMenu.setController(controller);
            confirmReservationMenu.setStageController(this);

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

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setDriverController(DriverController driverController) {
        this.driverController = driverController;
    }

    public void setPartnerController(PartnerController partnerController) {
        this.partnerController = partnerController;
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
