package views;

import controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Van;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class VanDetailView implements Initializable{
    @FXML private Label lb_name, lb_regisNum, lb_type;
    @FXML private TableView table_vanDetail;
    @FXML private TableColumn col_startDate, col_endDate, col_jobStatus;
    @FXML private Button btn_editVan;

    private MainController controller;
    private Van van;
    private List<Van> jobs;
    private VanMenuView vanMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickEditVan();

    }

    public void showDetail(){
        String name = van.getName();
        String regis_Number = van.getRegisNumber();
        String type = (van.getType().equals("VIP"))?"VIP (9 ที่นั่ง)":"ธรรมดา (15 ที่นั่ง)";
        lb_name.setText(name);
        lb_regisNum.setText(regis_Number);
        lb_type.setText(type);

    }
    public void onClickEditVan(){
        btn_editVan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/van_detail_edit.fxml"));
                    AnchorPane detail = loader.load();
                    VanDetailEditView vanDetailEditView = loader.getController();
                    vanDetailEditView.setController(controller);
                    vanDetailEditView.setVan(van);
                    vanDetailEditView.setVanDetailView(VanDetailView.this);
                    vanDetailEditView.setVanMenuView(vanMenuView);

                    Scene scene = new Scene(detail);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("แก้ไขข้อมูลรถตู้");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void initCol(){
    }
    public void initData(){
        ObservableList<Van> data = FXCollections.observableList(jobs);
        table_vanDetail.setItems(data);
    }

    public void refreshVanTable(){
//        this.jobs = controller.getVans();
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setVan(Van van) {
        this.van = van;
        showDetail();
    }
    public void setVanMenuView(VanMenuView vanMenuView){
        this.vanMenuView = vanMenuView;
    }

    public void setLb_name(String name) {
        this.lb_name.setText(name);
    }

    public void setLb_type(String type) {
        this.lb_type.setText(type);
    }
}
