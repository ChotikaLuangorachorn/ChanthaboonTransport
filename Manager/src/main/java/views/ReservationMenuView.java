package views;

import controllers.MainController;
import controllers.StageController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ReservationMenuView {
    @FXML private Label lbCometoMain;
    private MainController controller;
    private StageController stageController;

    public void setController(MainController controller) {
        this.controller = controller;
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
}
