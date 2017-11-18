package view;

import controller.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Destination;
import models.Reservation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MyReservationView implements Initializable{
    @FXML private TableView table_reserve;
    @FXML private TableColumn col_reserveId,col_reserveDate,col_province,col_district,col_startDate,col_endDate,col_statusReservation;
    private MainController controller;

    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        initData();
    }

    public void initCol(){
        col_reserveId.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveId"));
        col_reserveDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                return new SimpleStringProperty(reservation.getValue().getReserveDate().toString());
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
                return new SimpleStringProperty(reservation.getValue().getStartDate().toString());
            }
        });
        col_endDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                return new SimpleStringProperty(reservation.getValue().getEndDate().toString());
            }
        });
        col_statusReservation.setCellValueFactory(new PropertyValueFactory<Reservation,String>("status"));

    }
    public void initData(){
        //Test TableView by simple data
        ArrayList<Reservation> reserves = new ArrayList<Reservation>();
        reserves.add(new Reservation("01","1","meet1",1,2,new Destination("Testจังหวัด","Testอำเภอ","Testสภานที่"),new Date(),new Date(),new Date(),new Date(),111,"รอยืนยัน"));
        reserves.add(new Reservation("02","1","meet2",1,2,new Destination("Testจังหวัด","Testอำเภอ","Testสภานที่"),new Date(),new Date(),new Date(),new Date(),111,"รอยืนยัน"));
        //Add data to table
        ObservableList<Reservation> data = FXCollections.observableList(reserves);
        table_reserve.setItems(data);


    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
