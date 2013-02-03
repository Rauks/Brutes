package brutes;

import brutes.server.ServerThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class Brutes extends Application {

    private static ServerThread SERVER = new ServerThread();

    public static void exit() {
        Brutes.SERVER.interrupt();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Les brutes - Server");
        stage.setResizable(false);
        StackPane root = new StackPane();
        root.getChildren().add(new Text("Server running..."));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event t) {
                Brutes.exit();
            }
        });

        Brutes.SERVER.start();

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
