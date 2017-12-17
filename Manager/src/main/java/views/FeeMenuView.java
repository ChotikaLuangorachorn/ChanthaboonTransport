package views;

import controllers.MainController;
import controllers.PriceController;
import controllers.StageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.PriceFactor;
import sun.font.TrueTypeFont;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FeeMenuView implements Initializable{
    @FXML private TextField tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal;
    @FXML private TextField tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip;
    @FXML private Button btn_editFee, btn_cancel, btn_save;
    @FXML private Label lbCometoMain, lb_error;
    private TextField[] textFields;
    private double[] values;
    private PriceController priceController;
    private StageController stageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new TextField[]{tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal,tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip};
        onClickEditFee();
        onClickCancel();
        onClickSave();
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
    public void onClickEditFee(){
        btn_editFee.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(TextField tf : textFields) {
                    tf.setEditable(true);
                    tf.setStyle("{-fx-background-color: }");
                }
                btn_editFee.setVisible(false);
                btn_cancel.setVisible(true);
                btn_save.setVisible(true);

                for(int i = 0 ; i<textFields.length;i++){
                    textFields[i].setText(values[i]+"");
                }
            }
        });
    }
    public void onClickCancel(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(TextField tf : textFields) {
                    tf.setEditable(false);
                    tf.setStyle("-fx-background-color: #fcee86");
                }
                btn_editFee.setVisible(true);
                btn_cancel.setVisible(false);
                btn_save.setVisible(false);
                initData();
            }
        });
    }
    public void onClickSave(){
        btn_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    lb_error.setText("");
                    double distFree_normal = 0.0;
                    double base_normal = 0.0;
                    double rateDst_normal = 0.0;
                    double rateDay_normal = 0.0;
                    double distFree_vip = 0.0;
                    double base_vip = 0.0;
                    double rateDst_vip = 0.0;
                    double rateDay_vip = 0.0;
                    tf_dist_normal.setStyle("{-fx-border-color: }");
                    tf_base_normal.setStyle("{-fx-border-color: }");
                    tf_rateDst_normal.setStyle("{-fx-border-color: }");
                    tf_rateDay_normal.setStyle("{-fx-border-color: }");
                    tf_dist_vip.setStyle("{-fx-border-color: }");
                    tf_base_vip.setStyle("{-fx-border-color: }");
                    tf_rateDst_vip.setStyle("{-fx-border-color: }");
                    tf_rateDay_vip.setStyle("{-fx-border-color: }");
                    try{
                        distFree_normal = Double.parseDouble(tf_dist_normal.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_dist_normal.setStyle("-fx-border-color: Red");

                    }
                    try{
                        base_normal = Double.parseDouble(tf_base_normal.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_base_normal.setStyle("-fx-border-color: Red");
                    }
                    try{
                        rateDst_normal = Double.parseDouble(tf_rateDst_normal.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_rateDst_normal.setStyle("-fx-border-color: Red");
                    }
                    try{
                        rateDay_normal = Double.parseDouble(tf_rateDay_normal.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_rateDay_normal.setStyle("-fx-border-color: Red");
                    }
                    try{
                        distFree_vip = Double.parseDouble(tf_dist_vip.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_dist_vip.setStyle("-fx-border-color: Red");
                    }
                    try{
                    base_vip = Double.parseDouble(tf_base_vip.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_base_vip.setStyle("-fx-border-color: Red");
                    }
                    try{
                        rateDst_vip = Double.parseDouble(tf_rateDst_vip.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_rateDst_vip.setStyle("-fx-border-color: Red");
                    }
                    try{
                        rateDay_vip = Double.parseDouble(tf_rateDay_vip.getText());
                    }catch (Exception e){
                        lb_error.setText("กรอกข้อมูลผิดพลาด โปรดกรอกข้อมูลเป็นจำนวนตัวเลข");
                        tf_rateDay_vip.setStyle("-fx-border-color: Red");
                    }
                    if (lb_error.getText().equals("")) {
                        for (TextField tf : textFields) {
                            tf.setEditable(false);
                            tf.setStyle("-fx-background-color: #fcee86");
                        }
                        btn_editFee.setVisible(true);
                        btn_cancel.setVisible(false);
                        btn_save.setVisible(false);
                        PriceFactor price = new PriceFactor(0.0, 0.0, rateDay_vip, rateDay_normal, 0.0, 0.0, base_vip, base_normal, rateDst_vip, rateDst_normal, distFree_vip, distFree_normal);
                        System.out.println(price + "+++");
                        priceController.updatePriceFactor(price);
                        initData();
                    }
            }
        });
    }

    public void initData(){
        PriceFactor priceFactor = priceController.getPriceFactor();
        double distFree_normal = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.FREE);
        double base_normal = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.BASE);
        double rateDst_normal = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.RATE);
        double rateDay_normal = priceFactor.getFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.RATE);

        double distFree_vip = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.FREE);
        double base_vip = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.BASE);
        double rateDst_vip = priceFactor.getFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.RATE);
        double rateDay_vip = priceFactor.getFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.RATE);
        values = new double[]{distFree_normal,base_normal, rateDst_normal, rateDay_normal,distFree_vip,base_vip, rateDst_vip, rateDay_vip};

        for(int i = 0 ; i<textFields.length;i++){
            textFields[i].setText(String.format("%,.2f",values[i]));
        }
    }

    public void setPriceController(PriceController priceController) {
        this.priceController = priceController;
        initData();
    }

}
