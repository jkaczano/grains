import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Main extends Application {

    Stage primaryStage;
    AnchorPane rootLayout;


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("test");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ziarna");
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            System.out.println("a");
            loader.setLocation(Main.class.getResource("view/rootLayout.fxml"));
            System.out.println("b");
            rootLayout = loader.load();
            System.out.println("c");

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ziarna");
            System.out.println("d");
            primaryStage.show();
            System.out.println("e");

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
