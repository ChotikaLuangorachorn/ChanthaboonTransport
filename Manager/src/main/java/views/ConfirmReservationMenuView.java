package views;

import controllers.MainController;
import controllers.StageController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Customer;
import models.Reservation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmReservationMenuView implements Initializable{
    @FXML private TableView<Reservation> table_confirmReserve;
    @FXML private TableColumn<Reservation, String> col_fee, col_reserveId, col_firstName, col_lastName, col_isDeposited;
    @FXML private Label lbCometoMain;
    private List<Reservation> reservations;
    private MainController controller;
    private StageController stageController;

    public void setController(MainController controller) {
        this.controller = controller;
        reservations = this.controller.getReservation();
        initColumn();
        initData();
        onDoubleClickReservation();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
    public void onDoubleClickReservation(){
        table_confirmReserve.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Reservation reservation = table_confirmReserve.getSelectionModel().getSelectedItem();
                System.out.println("reservation.toString() = " + reservation.toString());

                if(event.getClickCount() == 2 && (reservation!=null)){
                    try {
                        System.out.println("Double Click");
                        Stage secondStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/reservation_confirm.fxml"));
                        Pane confirmLayout = loader.load();
                        PageConfirmView pageConfirmView = loader.getController();
                        pageConfirmView.setController(controller);
                        pageConfirmView.setReservation(reservation);
                        pageConfirmView.setConfirmReservationMenu(ConfirmReservationMenuView.this);

                        Scene scene = new Scene(confirmLayout);
                        secondStage.setScene(scene);
                        secondStage.setResizable(false);
                        secondStage.initModality(Modality.APPLICATION_MODAL);
                        secondStage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void initColumn(){
        table_confirmReserve.setEditable(false);
        col_reserveId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservation) {
                return new SimpleStringProperty(String.format("%05d",Integer.parseInt(reservation.getValue().getReserveId())));
            }
        });

        col_firstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                Customer customer = param.getValue().getCustomer();
                return new SimpleStringProperty(customer.getFirstName());
            }
        });

        col_lastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                Customer customer = param.getValue().getCustomer();
                return new SimpleStringProperty(customer.getLastName());
            }
        });

        col_fee.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                return new SimpleStringProperty(String.format("%,.2f",param.getValue().getPrice()));
            }
        });



        col_isDeposited.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                String isDeposited = ("true".equals(param.getValue().getIsDeposited()))?"ชำระแล้ว":"ยังไม่ชำระ";
                return new SimpleStringProperty(isDeposited);
            }
        });
    }

    public void initData(){
        System.out.println(reservations.toString());
        ObservableList<Reservation> reservationObservableList = FXCollections.observableList(reservations);
        FXCollections.reverse(reservationObservableList);
        table_confirmReserve.setItems(reservationObservableList);
    }
    public void refreshReservationTable(){
        this.reservations = controller.getReservation();
        this.initData();
    }
}
