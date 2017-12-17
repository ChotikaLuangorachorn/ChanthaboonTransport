package view;

import controller.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.CustomerInfoManager;
import models.Destination;
import models.Reservation;
import utils.ReservationDateFormatter;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MyReservationView implements Initializable{
    @FXML private TableView table_reserve;
    @FXML private TableColumn col_reserveId,col_reserveDate,col_province,col_district,col_startDate,col_endDate,col_isDeposited;

    private MainController controller;
    private List<Reservation> reserves;

    public void initialize(URL location, ResourceBundle resources) {
        onDoubleClickReservation();
    }

    public void onDoubleClickReservation(){
        table_reserve.setOnMouseClicked(even->{
            Reservation reservation = (Reservation) table_reserve.getSelectionModel().getSelectedItem();

            if(even.getClickCount() == 2 && (reservation!=null)){
                try {
                    System.out.println("Double Click");
                    Stage secondStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/reservation_detail.fxml"));
                    Pane detailLayout = loader.load();
                    MyReservationDetailView myReservationDetailView = loader.getController();
                    myReservationDetailView.setController(controller);
                    myReservationDetailView.setReservation(reservation);

                    Scene scene = new Scene(detailLayout);
                    secondStage.setScene(scene);
                    secondStage.setResizable(false);
                    secondStage.setTitle("Reservation detail");
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void initCol(){
        col_reserveId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                String id = String.format("%05d",Integer.parseInt(reservation.getValue().getReserveId()));
                return new SimpleStringProperty(id);
            }
        });
        col_reserveDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                Date reserveDate = reservation.getValue().getReserveDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(reserveDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFullFormatter().format(reserveDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_province.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                return new SimpleStringProperty(reservation.getValue().getDestination().getProvince());
            }
        });
        col_district.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                return new SimpleStringProperty(reservation.getValue().getDestination().getDistrict());
            }
        });
        col_startDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                Date startDate = reservation.getValue().getStartDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(startDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(startDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_endDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                Date endDate = reservation.getValue().getEndDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(endDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(endDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
//        col_isDeposited.setCellValueFactory(new PropertyValueFactory<Reservation,String>("isDeposited"));
        col_isDeposited.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                String isDeposited = (reservation.getValue().getIsDeposited().equals("true"))?"ชำระแล้ว":"ยังไม่ชำระ";
                return new SimpleStringProperty(isDeposited);
            }
        });

    }
    public void initData(){
        //Test TableView by simple data
//        ArrayList<Reservation> reserves = new ArrayList<Reservation>();
//        reserves.add(new Reservation("01","1","meet1",1,2,new Destination("Testจังหวัด","Testอำเภอ","Testสภานที่"),new Date(),new Date(),new Date(),new Date(),111,"รอยืนยัน"));
//        reserves.add(new Reservation("02","1","meet2",1,2,new Destination("Testจังหวัด","Testอำเภอ","Testสภานที่"),new Date(),new Date(),new Date(),new Date(),111,"รอยืนยัน"));
        //Add data to table
        ObservableList<Reservation> data = FXCollections.observableList(reserves);
        FXCollections.reverse(data);
        table_reserve.setItems(data);
    }

    public void setController(MainController controller) {
        this.controller = controller;
//        onDoubleClickReservation();
        initCol();
        this.refreshReservationTable();
    }
    public void refreshReservationTable(){
        this.reserves = this.controller.getHistoryReservation(CustomerInfoManager.getInstance().getCustomer().getId());
        this.initData();
    }

}
