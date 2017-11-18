package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Destination;
import models.Reservation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyReservationView implements Initializable{
    @FXML private TableView table_reserve;
    @FXML private TableColumn col_reserveId,col_reserveDate,col_province,col_district,col_startDate,col_endDate,col_statusReservation;

    public void initialize(URL location, ResourceBundle resources) {

    }
    public void initCol(){
        col_reserveId.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveId"));
        col_reserveDate.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));
        col_province.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));
        col_district.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));
        col_endDate.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));
        col_statusReservation.setCellValueFactory(new PropertyValueFactory<Reservation,String>("reserveDate"));

    }
    public void initData(){
        ArrayList<Reservation> reserves = new ArrayList<Reservation>();
        reserves.add(new Reservation("1","1","meet",1,2,new Destination("00","11","22"),null,null,null,null,111));

        ObservableList<Reservation> data = FXCollections.observableList(reserves);
        table_reserve.setItems(data);

    }
}
