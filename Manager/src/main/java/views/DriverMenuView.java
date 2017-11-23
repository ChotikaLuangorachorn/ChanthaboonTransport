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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Driver;
import models.Reservation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
        onClickDeleteDriver();
        onDoubleClickDriver();


    }
    public void onClickDeleteDriver(){
        btn_deleteDriver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Driver driver = (Driver) table_driver.getSelectionModel().getSelectedItem();
                if(driver!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ยืนยันการลบข้อมูลคนขับ");
                    alert.setHeaderText("ยืนยันการลบข้อมูลคนขับ");
                    String s = "ชื่อ:\t\t" + driver.getFirstname() + "\n"
                            +"นามสกุล:\t" + driver.getLastname();
                    alert.setContentText(s);

                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        controller.deleteDriver(driver);
                        table_driver.getSelectionModel().clearSelection();
                        refreshDriverTable();
                    }
                }
            }
        });
    }
    public void onDoubleClickDriver(){
        table_driver.setOnMouseClicked(even->{
            Driver driver = (Driver) table_driver.getSelectionModel().getSelectedItem();

            if(even.getClickCount() == 2 && (driver!=null)){
                try {
                    System.out.println("Double Click");
                    Stage secondStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/driver_detail.fxml"));
                    Pane detailLayout = loader.load();
                    DriverDetailView driverDetailView = loader.getController();
                    driverDetailView.setController(controller);
                    driverDetailView.setDriver(driver);
                    driverDetailView.setDriverMenuView(DriverMenuView.this);

                    Scene scene = new Scene(detailLayout);
                    secondStage.setScene(scene);
                    secondStage.setResizable(false);
                    secondStage.setTitle("รายละเอียดของคนขับรถ");
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
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

