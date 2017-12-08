package views;

import controllers.MainController;
import controllers.StageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sun.font.TrueTypeFont;

import java.net.URL;
import java.util.ResourceBundle;

public class FeeMenuView implements Initializable{
    @FXML private TextField tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal;
    @FXML private TextField tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip;
    @FXML private Button btn_editFee, btn_cancel, btn_save;
    @FXML private Label lbCometoMain;
    private MainController controller;
    private StageController stageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                TextField[] textFields = new TextField[]{tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal,tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip};
                for(TextField tf : textFields) {
                    tf.setEditable(true);
                    tf.setStyle("{-fx-background-color: }");
                }
                btn_editFee.setVisible(false);
                btn_cancel.setVisible(true);
                btn_save.setVisible(true);
            }
        });
    }
    public void onClickCancel(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextField[] textFields = new TextField[]{tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal,tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip};
                for(TextField tf : textFields) {
                    tf.setEditable(false);
                    tf.setStyle("{-fx-background-color: #fcee86}");
                }
                btn_editFee.setVisible(true);
                btn_cancel.setVisible(false);
                btn_save.setVisible(false);
            }
        });
    }
    public void onClickSave(){
        btn_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextField[] textFields = new TextField[]{tf_dist_normal,tf_base_normal, tf_rateDst_normal, tf_rateDay_normal,tf_dist_vip,tf_base_vip, tf_rateDst_vip, tf_rateDay_vip};
                for(TextField tf : textFields) {
                    tf.setEditable(false);
                    tf.setStyle("{-fx-background-color: #fcee86}");
                }
                btn_editFee.setVisible(true);
                btn_cancel.setVisible(false);
                btn_save.setVisible(false);
            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

}
