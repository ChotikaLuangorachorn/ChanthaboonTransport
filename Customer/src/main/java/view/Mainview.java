package view;

import controller.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class Mainview {
    @FXML private Tab tab_information;
    @FXML private Tab tab_myReserve;
    @FXML private Tab tab_reserve;
    private InformationView tabInformation;
    private MyReservationView myReservationView;
    private ReservationView reservationView;

    private SceneManager sceneManager;

    //New Vaersion
    private ReservationController reservationController;
    private CustomerController customerController;
    private PriceController priceController;
    private VanController vanController;

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
            tabInformation.setController(customerController);

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
            reservationView.setPriceController(priceController);
            reservationView.setReservationController(reservationController);
            reservationView.setVanController(vanController);
            reservationView.setMyReservationView(myReservationView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTabMyReservation(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/myreservation_tab.fxml"));
            Pane tabMyReserve = loader.load();
            tab_myReserve.setContent(tabMyReserve);
            myReservationView = loader.getController();
            myReservationView.setReservationController(reservationController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        initTabReservation();
        initTabMyReservation();
        initTabInformation();
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setPriceController(PriceController priceController) {
        this.priceController = priceController;
    }

    public void setVanController(VanController vanController) {
        this.vanController = vanController;
    }
}
