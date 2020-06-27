package game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

public class Main extends Application {
    private static final String ICON_PATH = "icon.png";
    private Logger logger = Logger.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        Scenes scenes = new Scenes();
        scenes.setPrimaryStage(primaryStage);
        scenes.setMenuScene();
        primaryStage.setTitle("Cross The Road");
        primaryStage.getIcons().add(new Image(ICON_PATH));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                logger.info("application closed");
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
