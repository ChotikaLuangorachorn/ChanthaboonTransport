package views;

import controllers.*;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Driver;
import models.Reservation;
import models.Schedule;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DriverMenuView implements Initializable{
    @FXML private TableView table_driver;
    @FXML private TableColumn col_fName, col_lName, col_nName, col_phone, col_license;
    @FXML private Button btn_deleteDriver;
    @FXML private Label lbCometoMain;

    private DriverController driverController;
    private ReservationController reservationController;
    private StageController stageController;
    private List<Driver> drivers;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
        lbCometoMain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageController.showMainView();
            }
        });
    }

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
                List<Schedule> schedules = driverController.getDriverSchedule(driver.getId());
                if(driver!=null){
                    if (schedules.size()==0){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("ยืนยันการลบข้อมูลคนขับ");
                        alert.setHeaderText("ยืนยันการลบข้อมูลคนขับ");
                        String s = "ชื่อ:\t\t" + driver.getFirstName() + "\n"
                                +"นามสกุล:\t" + driver.getLastName();
                        alert.setContentText(s);

                        Optional<ButtonType> result = alert.showAndWait();
                        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                            driverController.deleteDriver(driver);
                            table_driver.getSelectionModel().clearSelection();
                            refreshDriverTable();
                        }
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ไม่สามารถทำการลบได้");
                        alert.setHeaderText("ไม่สามารถทำการลบได้");
                        String s = "เนื่องจากคนขับ\n"+
                                "ชื่อ:\t\t" + driver.getFirstName() + "    "+driver.getLastName()+"\nยังมีตารางการทำงานอยู่";
                        alert.setContentText(s);
                        Optional<ButtonType> result = alert.showAndWait();
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
                    driverDetailView.setDriverController(driverController);
                    driverDetailView.setReservationController(reservationController);
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
        col_fName.setCellValueFactory(new PropertyValueFactory<Driver,String>("firstName"));
        col_lName.setCellValueFactory(new PropertyValueFactory<Driver,String>("lastName"));
        col_nName.setCellValueFactory(new PropertyValueFactory<Driver,String>("nickname"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Driver,String>("phone"));
        col_license.setCellValueFactory(new PropertyValueFactory<Driver,String>("driverLicense"));
    }
    public void initData(){
        ObservableList<Driver> data = FXCollections.observableList(drivers);
        table_driver.setItems(data);

    }

    public void refreshDriverTable(){
        this.drivers = driverController.getDrivers();
        initData();
    }
    public void setDriverController(DriverController driverController) {
        this.driverController = driverController;
        refreshDriverTable();
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }
}

