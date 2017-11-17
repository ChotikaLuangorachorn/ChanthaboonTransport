package view;

import controller.SceneManager;
import javafx.fxml.FXML;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;

public class Mainview {
    private CustomerDatabaseManager controller;
    private CustomerInfoManager customer;
    private SceneManager sceneManager;

    @FXML
    public void initialize(){

    }

    public void setController(CustomerDatabaseManager controller) {
        this.controller = controller;
    }

    public void setCustomer(CustomerInfoManager customer) {
        this.customer = customer;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
