package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import models.Reservation;

import java.net.URL;
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

    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
