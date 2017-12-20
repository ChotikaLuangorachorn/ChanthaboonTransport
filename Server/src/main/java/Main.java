import javafx.application.Application;
import javafx.stage.Stage;
import models.Customer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        ApplicationContext bf = new ClassPathXmlApplicationContext("server_config.xml");
        System.out.println("Server start...");
    }
}
