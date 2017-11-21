package views;

import controllers.MainController;
import controllers.StageController;

public class MainView {
    private MainController controller;
    private StageController stageController;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}
