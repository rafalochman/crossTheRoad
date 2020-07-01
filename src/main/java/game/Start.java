package game;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

public class Start extends AnchorPane {
    private final Logger logger = Logger.getLogger(Start.class);
    private Scenes scenes;
    private FilesOperations filesOperations;
    private String login;
    private String roads;
    private String level;

    public Start(Scenes scenes) {
        this.scenes = scenes;
        filesOperations = new FilesOperations();

        addLabel("Choose number of roads:", 200, 10);
        createRoadsRadioButtons();
        addLabel("Choose level:", 230, 100);
        createLevelRadioButton();
        addLabel("Type your login:", 230, 200);

        TextField loginTextField = new TextField();
        loginTextField.setLayoutX(205);
        loginTextField.setLayoutY(240);
        this.getChildren().add(loginTextField);

        createStartGameButton(loginTextField);
        createBackToMenuButton();
    }

    private void addLabel(String text, int layoutX, int layoutY) {
        Label label = new Label(text);
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
        label.setId("startMenuLabel");
        this.getChildren().add(label);
    }

    private void createRoadsRadioButtons() {
        ToggleGroup roadsGroup = new ToggleGroup();
        roadsGroup.setUserData("roadsGroup");

        createRadioButton("3", roadsGroup, 150, 50);
        createRadioButton("4", roadsGroup, 250, 50);
        createRadioButton("5", roadsGroup, 350, 50);

        whichCheckedRadioButton(roadsGroup);
    }

    private void createLevelRadioButton() {
        ToggleGroup LevelGroup = new ToggleGroup();
        LevelGroup.setUserData("levelGroup");

        createRadioButton("easy", LevelGroup, 150, 140);
        createRadioButton("medium", LevelGroup, 250, 140);
        createRadioButton("hard", LevelGroup, 350, 140);

        whichCheckedRadioButton(LevelGroup);
    }

    private void createRadioButton(String text, ToggleGroup toggleGroup, int layoutX, int layoutY) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setLayoutX(layoutX);
        radioButton.setLayoutY(layoutY);
        this.getChildren().add(radioButton);
    }

    private void whichCheckedRadioButton(final ToggleGroup group) {
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton radioButton = (RadioButton) t1.getToggleGroup().getSelectedToggle();
                if (group.getUserData() == "roadsGroup") {
                    roads = radioButton.getText();
                } else if (group.getUserData() == "levelGroup") {
                    level = radioButton.getText();
                }
            }
        });
    }

    private void createStartGameButton(final TextField textField) {
        Button startGameButton = new Button();
        startGameButton.setLayoutX(240);
        startGameButton.setLayoutY(350);
        startGameButton.setText("START GAME");
        this.getChildren().add(startGameButton);

        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                login = textField.getText();
                if (login.equals("")) {
                    login = "Anonim";
                }

                if (roads != null && level != null) {
                    scenes.hide();
                    setGame();
                    filesOperations.deleteGameSave();
                }
            }
        });
    }

    private void createBackToMenuButton() {
        Button backToMenuButton = new Button();
        backToMenuButton.setLayoutX(235);
        backToMenuButton.setLayoutY(450);
        backToMenuButton.setText("BACK TO MENU");
        this.getChildren().add(backToMenuButton);

        backToMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                scenes.setMenuScene();
            }
        });
    }

    private void setGame() {
        GameParameters gameParameters = new GameParameters();
        gameParameters.setRoads(roads);
        gameParameters.setLevel(level);
        gameParameters.setLogin(login);
        gameParameters.setCatX(0);
        gameParameters.setCatY(0);
        gameParameters.setStoppedTime(0);
        gameParameters.setLoadGame(false);
        Game gameView = new Game(scenes);
        gameView.createGame(gameParameters);
        logger.info("game started");
    }

}
