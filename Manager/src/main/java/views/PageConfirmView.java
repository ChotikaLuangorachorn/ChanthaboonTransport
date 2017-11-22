package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import managers.ManagerDatabaseManager;
import models.Reservation;
import models.Van;
import org.controlsfx.control.CheckComboBox;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class PageConfirmView implements Initializable {
    @FXML private Label lbDate;
    @FXML private ComboBox cbbIsDeposit;
    @FXML private Spinner spnHrs, spnMin;
    @FXML private TextArea taPlace;
    @FXML private Button btnConfirm;
    @FXML private AnchorPane confirmLayout;
    private MainController controller;
    private CheckComboBox ccbSelectVanVip, ccbSelectVanNormal, ccbSelectDriver;
    private Reservation reservation;

    public void setController(MainController controller) {
        this.controller = controller;

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


        Date startDate = reservation.getStartDate();
        Date endDate = reservation.getEndDate();
        ccbSelectVanVip.getItems().addAll(controller.getVanAvailable(startDate, endDate).get(ManagerDatabaseManager.VIP));
        ccbSelectVanVip.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                Van van = (Van) object;
                String s =  van.getName()+" - "+ van.getRegisNumber();
                return s;
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        ccbSelectVanNormal.getItems().addAll(controller.getVanAvailable(startDate, endDate).get(ManagerDatabaseManager.NORMAL));
        ccbSelectVanNormal.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                Van van = (Van) object;
                return van.getName() + " - " + van.getRegisNumber();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
//        ccbSelectDriver.getItems().addAll()
        confirmLayout.getChildren().add(ccbSelectDriver);
        confirmLayout.getChildren().add(ccbSelectVanVip);
        confirmLayout.getChildren().add(ccbSelectVanNormal);
    }

    public void setIsDepositComboBox(){
        String[] s = new String[] {"ยังไม่ชำระค่ามัดจำ", "ชำระแล้ว"};
        cbbIsDeposit.getItems().addAll(s);
        cbbIsDeposit.getSelectionModel().selectFirst();

    }


    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        initCheckComboBox();
        setIsDepositComboBox();
    }
}
