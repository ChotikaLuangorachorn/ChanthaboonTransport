package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Reservation;
import org.controlsfx.control.CheckComboBox;
import java.net.URL;
import java.util.ResourceBundle;

public class PageConfirmView implements Initializable {
    @FXML private Label lbDate;
    @FXML private ComboBox cbbIsDeposit;
    @FXML private Spinner spnHrs, spnMin;
    @FXML private TextArea taPlace;
    @FXML private Button btnConfirm;
    private MainController controller;
    private CheckComboBox ccbSelectVanVip, ccbSelectVanNormal, ccbSelectDriver;
    private Reservation reservation;

    public void setController(MainController controller) {
        this.controller = controller;
        initCheckComboBox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void initCheckComboBox(){
        ccbSelectVanVip = new CheckComboBox();
        ccbSelectVanVip.setLayoutX(282);
        ccbSelectVanVip.setLayoutY(298);
        ccbSelectVanVip.prefWidth(178);
        ccbSelectVanVip.prefHeight(31);

        ccbSelectVanNormal = new CheckComboBox();
        ccbSelectVanNormal.setLayoutX(282);
        ccbSelectVanNormal.setLayoutY(334);
        ccbSelectVanNormal.prefWidth(178);
        ccbSelectVanNormal.prefHeight(31);

        ccbSelectDriver = new CheckComboBox();
        ccbSelectDriver.setLayoutX(203);
        ccbSelectDriver.setLayoutY(373);
        ccbSelectDriver.prefWidth(262);
        ccbSelectDriver.prefHeight(31);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
