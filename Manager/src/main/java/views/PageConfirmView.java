package views;

import controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import managers.ManagerDatabaseManager;
import models.Driver;
import models.Reservation;
import models.Van;
import org.controlsfx.control.CheckComboBox;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PageConfirmView implements Initializable {
    @FXML private Label lbDate, lbReservationId, lbAmtVip, lbAmtNm, lbAmtDriver;
    @FXML private ComboBox cbbIsDeposit;
    @FXML private Spinner spnHrs, spnMin;
    @FXML private TextArea taPlace;
    @FXML private Button btnConfirm;
    @FXML private AnchorPane confirmLayout;
    @FXML private DatePicker dpkDeposit;
    private MainController controller;
    private CheckComboBox ccbSelectVanVip, ccbSelectVanNormal, ccbSelectDriver;
    private Reservation reservation;

    public void setController(MainController controller) {
        this.controller = controller;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onClickConfirm();

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

        ccbSelectVanVip.setMaxWidth(178);
        ccbSelectVanNormal.setMaxWidth(178);
        ccbSelectDriver.setMaxWidth(262);


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
        ccbSelectDriver.getItems().addAll(controller.getDriverAvailable(startDate, endDate));
        ccbSelectDriver.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                Driver driver = (Driver) object;
                return driver.getFirstname() + " " + driver.getLastname() + " " + driver.getPhone();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

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
        lbReservationId.setText(this.reservation.getReserveId());
        lbDate.setText(ReservationDateFormatter.getInstance().getUiDateFormatter().format(this.reservation.getStartDate()));
        lbAmtVip.setText(this.reservation.getAmtVip()+ "");
        lbAmtNm.setText(this.reservation.getAmtNormal() + "");
        lbAmtDriver.setText((this.reservation.getAmtNormal() + this.reservation.getAmtVip()) + "");
        initCheckComboBox();
        setIsDepositComboBox();
    }

    public void onClickConfirm(){
        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbbIsDeposit.getValue().toString().equals("ยังไม่ชำระค่ามัดจำ")
                        || (reservation.getAmtVip() != ccbSelectVanVip.getCheckModel().getCheckedItems().size())
                        || (reservation.getAmtNormal() != ccbSelectVanNormal.getCheckModel().getCheckedItems().size())
                        || ((reservation.getAmtNormal() + reservation.getAmtVip()) != ccbSelectDriver.getCheckModel().getCheckedItems().size())){


                }else{
                    LocalDate localDate = dpkDeposit.getValue();
                    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                    System.out.println(taPlace.getText());
                    controller.addMeeting(taPlace.getText(), reservation.getStartDate(), reservation);
                    controller.confirmDeposit(reservation.getReserveId(), Date.from(instant));

                    List<Van> vans = new ArrayList<>();
                    for (Object i : ccbSelectVanVip.getCheckModel().getCheckedItems()) {
                        vans.add((Van) i);
                    }
                    for (Object j : ccbSelectVanNormal.getCheckModel().getCheckedItems()){
                        vans.add((Van) j);
                    }

                    controller.assignVan(vans, reservation);

                    List<Driver> drivers = new ArrayList<>();
                    for(Object d : ccbSelectDriver.getCheckModel().getCheckedItems()){
                        drivers.add((Driver) d);
                    }

                    controller.assignDriver(drivers, reservation);
                    System.out.println(ccbSelectVanVip.getCheckModel().getCheckedItems());
                    System.out.println(ccbSelectVanNormal.getCheckModel().getCheckedIndices());
                    System.out.println(ccbSelectDriver.getCheckModel().getCheckedIndices());

                }

            }
        });

    }
}
