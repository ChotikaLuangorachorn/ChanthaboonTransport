package view;

import controller.MainController;
import controller.SceneManager;
import javafx.fxml.FXML;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;

public class Mainview {
    @FXML private InformationView tabInformation;
    private MainController controller;
    private SceneManager sceneManager;

    @FXML
    public void initialize(){

    }

    public void initTab(){
        tabInformation.setController(controller);
    }

    public void setController(MainController controller) {
        this.controller = controller;
        initTab();
    }


    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
