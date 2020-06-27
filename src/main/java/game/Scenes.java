package game;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Scenes {
    private static final int SCENE_SIZE = 600;
    private Stage primaryStage;
    private Menu menuPane;
    private Scores scoresPane;
    private Scene menuScene;
    private Scene startScene;
    private Scene scoresScene;

    public Scenes() {
        Start startPane = new Start(this);
        menuPane = new Menu(this);
        scoresPane = new Scores(this);
        menuScene = new Scene(menuPane, SCENE_SIZE, SCENE_SIZE);
        startScene = new Scene(startPane, SCENE_SIZE, SCENE_SIZE);
        scoresScene = new Scene(scoresPane, SCENE_SIZE, SCENE_SIZE);
        menuScene.getStylesheets().add("menuStyles.css");
        startScene.getStylesheets().add("menuStyles.css");
        scoresScene.getStylesheets().add("menuStyles.css");
    }

    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public void setMenuScene() {
        primaryStage.setScene(menuScene);

        FilesOperations filesOperations = new FilesOperations();
        List<String> data = filesOperations.loadGame();
        if (data.get(0).equals("null")) {
            menuPane.updateResumeButton(true);
        } else {
            menuPane.updateResumeButton(false);
        }
    }

    public void setStartScene() {
        primaryStage.setScene(startScene);
    }

    public void setScoresScene() {
        primaryStage.setScene(scoresScene);
        scoresPane.fillTable();
    }

    public void hide() {
        primaryStage.hide();
    }

    public void show() {
        primaryStage.show();
    }

    public void close() {
        primaryStage.close();
        System.exit(0);
    }

}
