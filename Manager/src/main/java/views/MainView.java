package views;

import controllers.StageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public class MainView implements Initializable{
    @FXML private Button btn_van, btn_driver, btn_reserve, btn_fee, btn_confirmReserve, btn_partner;
    private StageController stageController;

    public void onClickButton(){
        btn_van.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showVanMenuView();
            }
        });

        btn_driver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showDriverMenu();
            }
        });

        btn_reserve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showReservationView();
            }
        });

        btn_fee.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showFeeMenuView();
            }
        });

        btn_partner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showPartnerMenuView();
            }
        });

        btn_confirmReserve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageController.showConfirmReservationMenu();
            }
        });
    }


    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onClickButton();
    }
}
