package views;

import controllers.MainController;
import controllers.VanController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Van;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VanDetailEditView implements Initializable{
    @FXML private Label lb_typeName, lb_error;
    @FXML private TextField tf_typeNum;
    @FXML private ComboBox<String> cb_type;
    @FXML private Button btn_confirm, btn_cancel;
    private VanController vanController;
    private Van van;
    private VanDetailView vanDetailView;
    private VanMenuView vanMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (van != null)
            showEditForm();
    }

    public void showEditForm(){
        lb_typeName.setText(van.getName().substring(0,1));
        tf_typeNum.setText(van.getName().substring(1));
        ObservableList<String> types = FXCollections.observableArrayList("ธรรมดา (15 ที่นั่ง)","VIP (9 ที่นั่ง)");
        cb_type.setItems(types);
        System.out.println(van);
        String type = (van.getType().equals("NORMAL"))?"ธรรมดา (15 ที่นั่ง)":"VIP (9 ที่นั่ง)";
        cb_type.setValue(type);
        cb_type.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("change type van");
                if (cb_type.getValue().equals("ธรรมดา (15 ที่นั่ง)")){
                    lb_typeName.setText("N");
                }else {
                    lb_typeName.setText("V");
                }
            }
        });

    }
    public void onClickConfirmEdit(){

        btn_confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Boolean checkType = false;
                String typeName = lb_typeName.getText();
                String typeNum = tf_typeNum.getText();
                String type = cb_type.getValue();
                if (type.equals("ธรรมดา (15 ที่นั่ง)")){
                    lb_typeName.setText("N");
                }else {
                    lb_typeName.setText("V");
                }

                List<List<String>> nums = checkNumberVan();
                List<String> normal = nums.get(0);
                List<String> vip = nums.get(1);
                try {
                    if (typeNum.equals(van.getName().substring(1)) && typeName.equals(van.getName().substring(0,1))){
                        checkType=true;
                    }
                    else if ((typeName.equals("N") && normal.indexOf(typeNum)==-1) || (typeName.equals("V") &&vip.indexOf(typeNum)==-1)){
                        if (typeNum.length()==2){
                            int number = Integer.parseInt(tf_typeNum.getText());
                            checkType = true;
                        }
                    }
                } catch (Exception e) {
                    checkType = false;
                }
                if(!checkType){
                    tf_typeNum.setStyle("-fx-border-color: Red");
                    if((typeName.equals("N") && normal.indexOf(typeNum)!=-1) || (typeName.equals("V") &&vip.indexOf(typeNum)!=-1)){
                        lb_error.setText("หมายเลขรถซ้ำ");
                    }
                    else {
                        lb_error.setText("หมายเลขผิดพลาด (ระบุตัวเลข2หลัก)");
                    }
                }
                else {
                    lb_error.setText("");
                    lb_error.setStyle("-fx-border-color: ");
                }

                if (typeNum.equals(van.getName().substring(1)) && typeName.equals(van.getName().substring(0,1))){
                    lb_error.setText("ไม่มีการเปลี่ยนแปลงข้อมูล");
                }else if ("".equals(lb_error.getText())){
                    System.out.println("confirm to edit");
                    String vanType= (lb_typeName.getText().equals("V"))?"VIP":"NORMAL";
                    String name = lb_typeName.getText() + tf_typeNum.getText();
                    Van newVan = new Van(van.getRegisNumber(),null,vanType,name);
                    vanController.editVan(newVan);

                    newVan = vanController.getVan(van.getRegisNumber());
                    vanDetailView.setLb_name(newVan.getName());
                    vanType = (newVan.getType().equals("VIP"))?"VIP (9 ที่นั่ง)":"ธรรมดา (15 ที่นั่ง)";
                    vanDetailView.setLb_type(vanType);

                    Stage stage = (Stage) btn_cancel.getScene().getWindow();
                    stage.close();
                    vanMenuView.refreshVanTable();}
            }
        });
    }
    public void onClickCancelEdit(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_cancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    public List<List<String>> checkNumberVan(){
        List<Van> vans= vanController.getVans();
        List<String> normal = new ArrayList<>();
        List<String> vip = new ArrayList<>();
        for(Van v : vans){
            if(v.getName().substring(0,1).equals("N")){
                normal.add(v.getName().substring(1));
            }
            else if(v.getName().substring(0,1).equals("V")){
                vip.add(v.getName().substring(1));
            }
        }
        List<List<String>> numbers = new ArrayList<>();
        numbers.add(normal);
        numbers.add(vip);
        return numbers;

    }
    public void setVanController(VanController vanController){
        this.vanController = vanController;

    }

    public void setVan(Van van) {
        this.van = van;
        if (tf_typeNum != null) {
            showEditForm();
            onClickConfirmEdit();
            onClickCancelEdit();
        }
    }
    public void setVanDetailView(VanDetailView vanDetailView){
        this.vanDetailView=vanDetailView;
    }

    public void setVanMenuView(VanMenuView vanMenuView){
        this.vanMenuView = vanMenuView;
    }
}
