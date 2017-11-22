package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Driver;
import models.Reservation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DriverMenuView implements Initializable{
    @FXML private TableView table_driver;
    @FXML private TableColumn col_fName, col_lName, col_nName, col_phone, col_license;
    @FXML private Button btn_editDriver, btn_deleteDriver;

    private MainController controller;
    private List<Driver> drivers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();


    }
    public void onClickEditDriver(){
        btn_editDriver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

    }
    public void onClickDeleteDriver(){
        btn_deleteDriver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public void initCol(){
        col_fName.setCellValueFactory(new PropertyValueFactory<Driver,String>("firstname"));
        col_lName.setCellValueFactory(new PropertyValueFactory<Driver,String>("lastname"));
        col_nName.setCellValueFactory(new PropertyValueFactory<Driver,String>("nickname"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Driver,String>("phone"));
        col_license.setCellValueFactory(new PropertyValueFactory<Driver,String>("driverLicense"));
    }
    public void initData(){
        ObservableList<Driver> data = FXCollections.observableList(drivers);
        table_driver.setItems(data);

    }

    public void refreshDriverTable(){
        this.drivers = controller.getDrivers();
        initData();
    }
    public void setController(MainController controller) {
        this.controller = controller;
        refreshDriverTable();
    }


}

