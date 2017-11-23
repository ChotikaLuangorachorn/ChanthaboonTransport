package views;

import controllers.MainController;
import controllers.StageController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class FeeMenuView implements Initializable{
    @FXML private TableView table_fee;
    @FXML private TableColumn col_vanType, col_reserveType, col_rate, col_base;
    @FXML private Button btn_editFee;
    @FXML private Label lbCometoMain;
    private MainController controller;
    private StageController stageController;


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

}
