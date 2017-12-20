import controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;
import sun.applet.Main;

public class ManagerApplication extends Application {
    private StageController stageController;
    private Stage primaryStage;
    // new Version

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
        this.stageController = new StageController();
        // new Version -----------
        this.driverController = new DriverController();
        this.partnerController = new PartnerController();
        this.priceController = new PriceController();
        this.reservationController = new ReservationController();
        this.vanController = new VanController();
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
