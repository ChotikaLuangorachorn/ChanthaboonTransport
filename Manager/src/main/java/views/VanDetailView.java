package views;

import controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import models.Van;
import utils.ReservationDateFormatter;

import java.awt.*;
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("แก้ไขข้อมูลรถตู้");
                Label text1 = new Label("หมายเลขรถตู้:");
                Label text2 = new Label("ประเภทรถ:");
                Label errorText = new Label("");
                ComboBox<String> cb_typeName = new ComboBox<>();
                ObservableList<String> typesName = FXCollections.observableArrayList("N","V");
                cb_typeName.setItems(typesName);
                cb_typeName.setValue(van.getName().substring(0,1));

                TextField tf_name = new TextField(van.getName().substring(1));
                tf_name.setMaxWidth(50);

                String type = (van.getType().equals("VIP"))?"VIP(9 ที่นั่ง)":"ธรรมดา(15 ที่นั่ง)";
                Label lb_type = new Label(type);


                GridPane grid = new GridPane();
                GridPane subgrid = new GridPane();

                grid.add(text1, 1, 1);
                subgrid.add(cb_typeName, 1, 1);
                subgrid.add(tf_name, 2, 1);
                grid.add(subgrid,2,1);
                grid.add(text2, 1, 2);
                grid.add(lb_type, 2, 2);
                grid.add(errorText, 2, 3);
                grid.setVgap(10);
                grid.setHgap(20);
                alert.getDialogPane().setContent(grid);

                cb_typeName.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (cb_typeName.getValue().equals("N")){
                            lb_type.setText("ธรรมดา(15 ที่นั่ง)");
                        }else {
                            lb_type.setText("VIP(9 ที่นั่ง)");
                        }
                    }
                });
                tf_name.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        List<List<String>> nums = checkNumberVan();
                        List<String> normal = nums.get(0);
                        List<String> vip = nums.get(1);

                        Boolean checkType = false;
                        try {
                            if (tf_name.getText().equals(van.getName().substring(1))&& cb_typeName.getValue().equals(van.getName().substring(0,1))){
                                checkType=true;
                            }
                            else if ((cb_typeName.getValue().equals("N") && normal.indexOf(tf_name.getText())==-1) || (cb_typeName.getValue().equals("V") &&vip.indexOf(tf_name.getText())==-1)){
                                if (tf_name.getText().length()==2){
                                    int number = Integer.parseInt(tf_name.getText());
                                    checkType = true;
                                }
                            }
                        } catch (Exception e) {
                            checkType = false;
                        }
                        if(!checkType){
                            errorText.setTextFill(Color.RED);
                            errorText.setText("หมายเลขรถผิดพลาด หรือ ซ้ำ");
                        }
                        else {
                            errorText.setText("");
                        }
                    }
                });
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)){
//                        System.out.println("oldPwd = " + oldPwd.getText());
//                        System.out.println("newPwd = " + newPwd.getText());
//                        controller.changeCustomerPassword(CustomerInfoManager.getInstance().getCustomer().getCitizenId(), oldPwd.getText(), confirmPwd.getText());
                }
            }
        });
    }
    public List<List<String>> checkNumberVan(){
        List<Van> vans= controller.getVans();
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
}
