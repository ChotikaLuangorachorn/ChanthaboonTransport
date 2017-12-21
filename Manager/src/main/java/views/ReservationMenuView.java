package views;

import controllers.ReservationController;
import controllers.StageController;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Reservation;
import utils.ReservationDateFormatter;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReservationMenuView implements Initializable{
    @FXML private Label lbCometoMain;
    @FXML private TableView table_reserves;
    @FXML private TableColumn col_reserveId, col_reserveDate, col_province, col_district, col_fee, col_statusReservation;
    @FXML private Button btn_deleteReserve;
    private List<Reservation> reservations;
    private ReservationController reservationController;
    private StageController stageController;

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
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

    public void onDoubleClickReservation(){
        table_reserves.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Reservation reservation = (Reservation) table_reserves.getSelectionModel().getSelectedItem();
                if(event.getClickCount() == 2 && (reservation!=null)){
                    try {
                        System.out.println("on double click in ReservationView");
                        Stage secondStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/reservation_edit.fxml"));
                        Pane detailLayout = loader.load();
                        ReservationEditView reservationEditView = loader.getController();
                        reservationEditView.setReservationController(reservationController);
                        reservationEditView.setReservation(reservation);

                        Scene scene = new Scene(detailLayout);
                        secondStage.setScene(scene);
                        secondStage.setResizable(false);
                        secondStage.setTitle("แก้ไขการจอง");
                        secondStage.initModality(Modality.APPLICATION_MODAL);
                        secondStage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ไม่สามารถแก้ไขการจองได้");
                        alert.setHeaderText("ไม่สามารถแก้ไขการจองได้");
                        alert.setContentText("เนื่องจากหมายเลขการจองที่ "+String.format("%05d",Integer.parseInt(reservation.getReserveId()))+" ยังไม่มีการชำระเงิน");
                        alert.showAndWait();
                    }

                }
            }
        });
    }

    public void initColumn(){
        col_reserveId.setCellValueFactory(new PropertyValueFactory<Reservation, String>("reserveId"));
        col_reserveId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                String reserveId = reservation.getValue().getReserveId();
                return new SimpleStringProperty(String.format("%05d", Integer.parseInt(reserveId)));

            }
        });
        col_reserveDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation,String> reservation) {
                Date reserveDate = reservation.getValue().getStartDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(reserveDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(reserveDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
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

        col_fee.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservation) {
                return new SimpleStringProperty(String.format("%,.2f",reservation.getValue().getPrice()));
            }
        });

        col_statusReservation.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservation) {
                String isDeposited = ("true".equals(reservation.getValue().getIsDeposited()))?"ชำระแล้ว":"ยังไม่ชำระ";
                return new SimpleStringProperty(isDeposited);
            }
        });
    }

    public void initData(){
        ObservableList<Reservation> reservationObservableList = FXCollections.observableList(reservations);
        table_reserves.setItems(reservationObservableList);
        onDoubleClickReservation();

    }

    public void refreshReservationTable(){
        this.reservations = reservationController.getReservations();
        initData();

    }

    public void onClickDelete(){
        btn_deleteReserve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Reservation reservation = (Reservation) table_reserves.getSelectionModel().getSelectedItem();
                LocalDate meeting = reservation.getMeetingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(!meeting.equals(LocalDate.now())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ไม่สามารถลบการจองได้");
                    alert.setHeaderText("ไม่สามารถลบการจองได้");
                    alert.setContentText("เนื่องจากหมายเลขการจองที่ "+String.format("%05d",Integer.parseInt(reservation.getReserveId()))+" ได้ดำเนินการผ่านมาแล้ว");
                    alert.showAndWait();
                    return;
                }
                if(reservation!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ยืนยันการลบการจองของลูกค้า");
                    alert.setHeaderText("ยืนยันการลบการจองของลูกค้า");
                    String s = "หมายเลขการจอง:\t\t" + String.format("%05d",Integer.parseInt(reservation.getReserveId()))+ "\n";
                    alert.setContentText(s);

                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        table_reserves.getSelectionModel().clearSelection();
                        reservationController.deleteReservation(reservation);
                        refreshReservationTable();
                    }
                }
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
        onClickDelete();
    }
}
