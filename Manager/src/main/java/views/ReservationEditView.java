package views;

import controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Reservation;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ReservationEditView implements Initializable {
    @FXML private Label lbReservationId, lbPayDate, lbStatus, lbDate;
    @FXML private Spinner spnHrs, spnMin;
    @FXML private TextArea taPlace;
    @FXML private Button btnConfirm;

    private Reservation reservation;
    private MainController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onClickBtnConfirm();
    }

    public void setDataReservation(){
        lbReservationId.setText(String.format("%05d",Integer.parseInt(reservation.getReserveId())));
        lbDate.setText(ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getStartDate()));
        lbPayDate.setText(ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getDepositDate()));
        String status = (reservation.getIsDeposited().equals("true"))?"ชำระแล้ว":"ยังไม่ชำระ";
        lbStatus.setText(status);
        spnHrs.getValueFactory().setValue(reservation.getStartDate().getHours());
        spnMin.getValueFactory().setValue(reservation.getStartDate().getMinutes());
        taPlace.setText(reservation.getMeetingPlace());

    }
    public void onClickBtnConfirm(){
        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int hr = (int) spnHrs.getValueFactory().getValue();
                int mi = (int) spnMin.getValueFactory().getValue();
                reservation.getStartDate().setHours(hr);
                reservation.getStartDate().setMinutes(mi);
                controller.addMeeting(taPlace.getText(), reservation.getStartDate(), reservation);

                Stage stage = (Stage) taPlace.getScene().getWindow();
                stage.close();

            }
        });
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        setDataReservation();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
