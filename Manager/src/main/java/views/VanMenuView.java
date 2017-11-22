package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Van;

import java.io.IOException;
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
        onClickDeleteVan();
        onDoubleClickVan();
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
    public void onDoubleClickVan(){
        table_van.setOnMouseClicked(even->{
            Van van = (Van) table_van.getSelectionModel().getSelectedItem();

            if(even.getClickCount() == 2 && (van!=null)){
                try {
                    System.out.println("Double Click");
                    Stage secondStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/van_detail.fxml"));
                    Pane detailLayout = loader.load();
                    VanDetailView vanDetailView = loader.getController();
                    vanDetailView.setController(controller);
                    vanDetailView.setVan(van);

                    Scene scene = new Scene(detailLayout);
                    secondStage.setScene(scene);
                    secondStage.setResizable(false);
                    secondStage.setTitle("Reservation detail");
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
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
