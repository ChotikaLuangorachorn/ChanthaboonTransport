package view;

import controller.MainController;
import controller.SceneManager;
import javafx.fxml.FXML;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;

public class Mainview {
    private MainController controller;
    private SceneManager sceneManager;

    @FXML
    public void initialize(){

    }

    public void setController(MainController controller) {
        this.controller = controller;
    }


    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
