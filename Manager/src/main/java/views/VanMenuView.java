package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Van;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VanMenuView implements Initializable{
    @FXML private TableView table_van;
    @FXML private TableColumn col_regisNum, col_type, col_jobStatus;
    @FXML private Button btn_deleteVan, btn_editVan;

    private List<Van> vans;
    private MainController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickEditVan();
        onClickDeleteVan();


    }
    public void onClickEditVan(){
        btn_editVan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

    }
    public void onClickDeleteVan(){
        btn_deleteVan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Van van = (Van) table_van.getSelectionModel().getSelectedItem();
                if(van!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ยืนยันการลบข้อมูลรถตู้");
                    alert.setHeaderText("ยืนยันการลบข้อมูลรถตู้");
                    String s = "ทะเบียนรถ:\t\t" + van.getRegisNumber()+ "\n"
                            +"ประเภทรถ:\t" + van.getType();
                    alert.setContentText(s);

                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        controller.deleteVan(van);
                        table_van.getSelectionModel().clearSelection();
                        refreshVanTable();
                    }
                }
            }
        });
    }

    public void initCol(){
        col_regisNum.setCellValueFactory(new PropertyValueFactory<Van,String>("regisNumber"));
        col_type.setCellValueFactory(new PropertyValueFactory<Van,String>("type"));
//        col_jobStatus.setCellValueFactory(new PropertyValueFactory<Van,String>("nickname"));
    }
    public void initData(){
        ObservableList<Van> data = FXCollections.observableList(vans);
        table_van.setItems(data);

    }

    public void refreshVanTable(){
        this.vans = controller.getVans();
        initData();
    }
    public void setController(MainController controller) {
        this.controller = controller;
        refreshVanTable();
    }
}
