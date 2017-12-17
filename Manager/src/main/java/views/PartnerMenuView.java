package views;

import controllers.MainController;
import controllers.StageController;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.Partner;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PartnerMenuView  implements Initializable{
    @FXML private TableView table_partner;
    @FXML private TableColumn col_id, col_name, col_company, col_companyPhone;
    @FXML private Button btn_deletePartner, btn_editPartner;
    @FXML private Label lbCometoMain;

    private MainController controller;
    private StageController stageController;
    private List<Partner> partners;
    final HBox hBox = new HBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickDeletePartner();
        onClickEditPartner();
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

    public void onClickDeletePartner(){
        btn_deletePartner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Partner partner = (Partner) table_partner.getSelectionModel().getSelectedItem();
                if(partner!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ยืนยันการลบข้อมูลพันธมิตร");
                    alert.setHeaderText("ยืนยันการลบข้อมูลพันธมิตร");
                    // TODO partner last name
                    String s = "ชื่อ:\t\t" + partner.getFirstName() + "\n"
                            +"บริษัท:\t" + partner.getCompany();
                    alert.setContentText(s);

                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        controller.deletePartner(partner);
                        table_partner.getSelectionModel().clearSelection();
                        refreshPartnerTable();
                }
                }
            }
        });
    }
    public void onClickEditPartner(){
        btn_editPartner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Partner partner = (Partner) table_partner.getSelectionModel().getSelectedItem();
                    if (partner!=null){
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/partner_edit.fxml"));
                        Pane detail = loader.load();
                        PartnerEditView partnerEditView = loader.getController();
                        partnerEditView.setController(controller);
                        partnerEditView.setPartner(partner);
                        partnerEditView.setPartnerMenuView(PartnerMenuView.this);

                        Scene scene = new Scene(detail);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setTitle("แก้ไขข้อมูลพันธมิตร");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void initCol(){
        col_id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Partner,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Partner,String> partner) {
                String id = String.format("%05d",partner.getValue().getId());
                return new SimpleStringProperty(id);
            }
        });
        col_name.setCellValueFactory(new PropertyValueFactory<Partner,String>("name"));
        col_company.setCellValueFactory(new PropertyValueFactory<Partner,String>("company"));
        col_companyPhone.setCellValueFactory(new PropertyValueFactory<Partner,String>("companyPhone"));


    }
    public void initData(){
        ObservableList<Partner> data = FXCollections.observableList(partners);
        table_partner.setItems(data);

    }

    public void refreshPartnerTable(){
        this.partners = controller.getPartners();
        initData();
    }
    public void setController(MainController controller) {
        this.controller = controller;
        refreshPartnerTable();
    }


}
