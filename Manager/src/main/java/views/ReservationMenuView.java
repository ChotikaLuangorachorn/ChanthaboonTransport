package views;

import controllers.MainController;
import controllers.StageController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import models.Reservation;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationMenuView implements Initializable{
    @FXML private Label lbCometoMain;
    @FXML private TableView table_reserves;
    @FXML private TableColumn col_reserveId, col_reserveDate, col_province, col_district, col_fee, col_statusReservation;
    private List<Reservation> reservations;
    private MainController controller;
    private StageController stageController;

    public void setController(MainController controller) {
        this.controller = controller;
        refreshReservationTable();
    }

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
        lbCometoMain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageController.showMainView();
            }
        });
    }

    public void initColumn(){
        col_reserveId.setCellValueFactory(new PropertyValueFactory<Reservation, String>("reserveId"));
        col_reserveDate.setCellValueFactory(new PropertyValueFactory<Reservation, Date>("reserveDate"));
        col_district.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> param) {
                String district = param.getValue().getDestination().getDistrict();
                return new SimpleStringProperty(district);
            }
        });

        col_province.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                String province = param.getValue().getDestination().getProvince();
                return new SimpleStringProperty(province);
            }
        });

        col_fee.setCellValueFactory(new PropertyValueFactory<Reservation, Double>("price"));
        col_statusReservation.setCellValueFactory(new PropertyValueFactory<Reservation, String>("isDeposited"));

    }

    public void initData(){
        ObservableList<Reservation> reservationObservableList = FXCollections.observableList(reservations);
        table_reserves.setItems(reservationObservableList);

    }

    public void refreshReservationTable(){
        this.reservations = controller.getReservation();
        initData();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
    }
}
