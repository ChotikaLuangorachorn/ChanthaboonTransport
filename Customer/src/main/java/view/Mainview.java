package view;

import controller.MainController;
import controller.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;

import java.io.IOException;

public class Mainview {
    @FXML private Tab tab_information;
    @FXML private Tab tab_myReserve;
    @FXML private Tab tab_reserve;
    private InformationView tabInformation;
    private MyReservationView myReservationView;
    private ReservationView reservationView;
    private MainController controller;
    private SceneManager sceneManager;

    @FXML
    public void initialize(){
    }

    public void initTabInformation(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/information_tab.fxml"));
            Pane tabInfo = loader.load();
            tab_information.setContent(tabInfo);
            tabInformation = loader.getController();
            tabInformation.setController(controller);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void initTabReservation(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/vanreservation_tab.fxml"));
            Pane tabreserve = loader.load();
            tab_reserve.setContent(tabreserve);
            reservationView = loader.getController();
            reservationView.setController(controller);
            reservationView.setMyReservationView(myReservationView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTabMyReservation(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/myreservation_tab.fxml"));
            Pane tabMyreserve = loader.load();
            tab_myReserve.setContent(tabMyreserve);
            myReservationView = loader.getController();
            myReservationView.setController(controller);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
        initTabInformation();
        initTabMyReservation();
        initTabReservation();
    }


    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
