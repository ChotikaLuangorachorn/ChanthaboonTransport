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
import models.Partner;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PartnerMenuView  implements Initializable{
    @FXML private TableView table_partner;
    @FXML private TableColumn col_id, col_name, col_company, col_companyPhone;
    @FXML private Button btn_deletePartner, btn_editPartner;

    private MainController controller;
    private List<Partner> partners;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickDeletePartner();
        onClickEditPartner();
    }
    public void onClickEditPartner(){
        btn_editPartner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

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
                    String s = "ชื่อ:\t\t" + partner.getName() + "\n"
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

    public void initCol(){
        col_id.setCellValueFactory(new PropertyValueFactory<Partner,String>("id"));
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
