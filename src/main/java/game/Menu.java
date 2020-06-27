package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

import java.util.List;

public class Menu extends AnchorPane {
    private Logger logger = Logger.getLogger(Menu.class);
    private Scenes scenes;
    private Button resumeButton;
    private FilesOperations filesOperations;

    public Menu(Scenes scenes) {
        this.scenes = scenes;
        createButtonStart();
        createButtonResume();
        createButtonScores();
        createButtonEnd();
        addTitle();

        filesOperations = new FilesOperations();
        logger.info("application started");
    }

    public void updateResumeButton(boolean ifResume) {
        resumeButton.setDisable(ifResume);
        if (!ifResume) {
            List<String> data = filesOperations.loadGame();
            resumeButton.setText("RESUME" + System.getProperty("line.separator") + "(" + data.get(2) + ")");
        } else {
            resumeButton.setText("RESUME");
        }
    }

    private void addTitle(){
        Label titleLabel = new Label("CROSS THE ROAD");
        titleLabel.setLayoutX(150);
        titleLabel.setLayoutY(30);
        titleLabel.setId("titleLabel");
        this.getChildren().add(titleLabel);
    }

    private void createButtonStart() {
        Button startButton = new Button();
        startButton.setLayoutX(280);
        startButton.setLayoutY(150);
        startButton.setText("START");
        this.getChildren().add(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                scenes.setStartScene();
            }
        });
    }

    private void createButtonResume() {
        resumeButton = new Button();
        resumeButton.setLayoutX(275);
        resumeButton.setLayoutY(250);
        resumeButton.setText("RESUME");
        this.getChildren().add(resumeButton);

        resumeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                List<String> data = filesOperations.loadGame();
                if (!data.get(0).equals("null")) {
                    scenes.hide();
                    GameParameters gameParameters = new GameParameters();
                    gameParameters.setRoads(data.get(0));
                    gameParameters.setLevel(data.get(1));
                    gameParameters.setLogin(data.get(2));
                    gameParameters.setCatX(new Integer(data.get(3)));
                    gameParameters.setCatY(new Integer(data.get(4)));
                    gameParameters.setStoppedTime(new Long(data.get(5)));
                    gameParameters.setLoadGame(true);
                    Game gameView = new Game(scenes);
                    gameView.createGame(gameParameters);
                    logger.info("game loaded");
                }
            }
        });
    }

    private void createButtonScores() {
        Button scoresButton;
        scoresButton = new Button();
        scoresButton.setLayoutX(280);
        scoresButton.setLayoutY(350);
        scoresButton.setText("SCORES");
        this.getChildren().add(scoresButton);

        scoresButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                scenes.setScoresScene();
            }
        });
    }

    private void createButtonEnd() {
        Button endButton = new Button();
        endButton.setLayoutX(290);
        endButton.setLayoutY(450);
        endButton.setText("END");
        this.getChildren().add(endButton);

        endButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logger.info("application closed");
                scenes.close();
            }
        });
    }

}
