import controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;
import sun.applet.Main;

public class ManagerApplication extends Application {
    private MainController controller;
    private StageController stageController;
    private Stage primaryStage;
    // new Version
    private CustomerController customerController;
    private DriverController driverController;
    private PartnerController partnerController;
    private PriceController priceController;
    private ReservationController reservationController;
    private VanController vanController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new MainController();
        this.stageController = new StageController();
        this.stageController.setController(controller);
        // new Version -----------
        this.stageController.setCustomerController(customerController);
        this.stageController.setDriverController(driverController);
        this.stageController.setPartnerController(partnerController);
        this.stageController.setPriceController(priceController);
        this.stageController.setReservationController(reservationController);
        this.stageController.setVanController(vanController);
        // -------------
        this.stageController.setPrimaryStage(this.primaryStage);
        this.stageController.showMainView();
        this.primaryStage.show();
    }
}
