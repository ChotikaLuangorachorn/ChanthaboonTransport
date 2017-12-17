package views;

import controllers.MainController;
import controllers.ReservationController;
import controllers.StageController;
import controllers.VanController;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Schedule;
import models.Van;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VanMenuView implements Initializable{
    @FXML private TableView table_van;
    @FXML private TableColumn col_regisNum, col_type, col_jobStatus, col_name;
    @FXML private Button btn_deleteVan, btn_editVan;
    @FXML private Label lbCometoMain;

    private List<Van> vans;
    private VanController vanController;
    private ReservationController reservationController;
    private StageController stageController;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
        lbCometoMain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageController.showMainView();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickDeleteVan();

    }
    public void onClickDeleteVan(){
        btn_deleteVan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Van van = (Van) table_van.getSelectionModel().getSelectedItem();
                List<Schedule> jobs = vanController.getVanSchedule(van.getRegisNumber());
                if(van!=null){
                    if (jobs.size()==0){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("ยืนยันการลบข้อมูลรถตู้");
                        alert.setHeaderText("ยืนยันการลบข้อมูลรถตู้");
                        String s = "ทะเบียนรถ:\t\t" + van.getRegisNumber()+ "\n"
                                +"ประเภทรถ:\t\t" + van.getType();
                        alert.setContentText(s);

                        Optional<ButtonType> result = alert.showAndWait();
                        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                            vanController.deleteVan(van);
                            table_van.getSelectionModel().clearSelection();
                            refreshVanTable();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ไม่สามารถทำการลบได้");
                        alert.setHeaderText("ไม่สามารถทำการลบได้");
                        String s = "เนื่องจากรถตู้\n"+
                                "ทะเบียนรถ:\t\t" + van.getRegisNumber()+ "\n"
                                +"ประเภทรถ:\t\t" + van.getType()+"\nยังมีตารางการทำงานอยู่";
                        alert.setContentText(s);
                        Optional<ButtonType> result = alert.showAndWait();
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
                    vanDetailView.setVanController(vanController);
                    vanDetailView.setReservationController(reservationController);
                    vanDetailView.setVan(van);
                    vanDetailView.setVanMenuView(VanMenuView.this);

                    Scene scene = new Scene(detailLayout);
                    secondStage.setScene(scene);
                    secondStage.setResizable(false);
                    secondStage.setTitle("รายละเอียดของรถตู้");
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void initCol(){
        col_name.setCellValueFactory(new PropertyValueFactory<Van,String>("name"));
        col_regisNum.setCellValueFactory(new PropertyValueFactory<Van,String>("regisNumber"));
        col_type.setCellValueFactory(new PropertyValueFactory<Van,String>("type"));
    }
    public void initData(){
        ObservableList<Van> data = FXCollections.observableList(vans);
        table_van.setItems(data);
        table_van.getSortOrder().add(col_name);
        onDoubleClickVan();
    }

    public void refreshVanTable(){
        this.vans = vanController.getVans();
        initData();
    }
    public void setVanController(VanController vanController) {
        this.vanController = vanController;
        refreshVanTable();
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }
}
