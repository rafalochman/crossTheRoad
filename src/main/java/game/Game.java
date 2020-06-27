package game;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private static final String RED_CAR_PATH = "redCar.png";
    private static final String PURPLE_CAR_PATH = "purpleCar.png";
    private static final String CAT_PATH = "redCat.png";
    private static final String COLLISION_PATH = "collision.png";
    private static final String BACKGROUND_3_PATH = "roads3Background.png";
    private static final String BACKGROUND_4_PATH = "roads4Background.png";
    private static final String BACKGROUND_5_PATH = "roads5Background.png";

    private Logger logger = Logger.getLogger(Game.class);

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private boolean isLeft;
    private boolean isRight;
    private boolean isUp;
    private boolean isDown;

    private String roads;
    private String level;
    private String login;

    private AnimationTimer animationTimer;
    private ImageView cat;
    private Label timeLabel;
    private ImageView[] redCars;
    private ImageView[] purpleCars;
    private Random randomPlace;
    private ImageView collision;

    private long startTime;
    private long breakTime;
    private String gameTime;
    private long stoppedTime;
    private long actualGameTime;
    private long beforeBreakTime;

    private boolean loadGame;
    private Popup popup;

    private Scenes scenes;
    private FilesOperations filesOperations;

    public Game(Scenes scenes) {
        this.scenes = scenes;
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
        gameStage.setTitle("Cross The Road");
        gameScene.getStylesheets().add("gameStyles.css");
        createKeyListeners();
        randomPlace = new Random();
        filesOperations = new FilesOperations();
        gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                logger.info("application closed");
                System.exit(0);
            }
        });
    }

    public void createGame(GameParameters gameParameters) {
        this.roads = gameParameters.getRoads();
        this.level = gameParameters.getLevel();
        this.login = gameParameters.getLogin();
        this.loadGame = gameParameters.getLoadGame();
        this.stoppedTime = gameParameters.getStoppedTime();
        gameStage.show();
        createCat(gameParameters.getCatX(), gameParameters.getCatY());
        createAnimations();
        addBackground();
        createCars();
        createTimeLabel();
        createStopButton();
        startTime = System.currentTimeMillis();
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    isLeft = true;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    isRight = true;
                } else if (event.getCode() == KeyCode.UP) {
                    isUp = true;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDown = true;
                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    isLeft = false;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    isRight = false;
                } else if (event.getCode() == KeyCode.UP) {
                    isUp = false;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDown = false;
                }
            }
        });
    }

    private void addBackground() {
        Image image = new Image(BACKGROUND_3_PATH);
        if (roads.equals("4")) {
            image = new Image(BACKGROUND_4_PATH);
        } else if (roads.equals("5")) {
            image = new Image(BACKGROUND_5_PATH);
        }
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        gamePane.setBackground(background);
    }

    private void breakPopUP(final Button stopButton) {
        popup = new Popup();
        Label label = new Label();
        label.setMinHeight(150);
        label.setMinWidth(150);
        label.setId("popUpLabel");
        popup.getContent().add(label);

        beforeBreakTime = System.currentTimeMillis();

        Button resumeButton = new Button();
        resumeButton.setText("RESUME");
        resumeButton.setLayoutX(45);
        resumeButton.setLayoutY(30);
        resumeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                popup.hide();
                animationTimer.start();
                breakTime += System.currentTimeMillis() - beforeBreakTime;
                stopButton.setDisable(false);
                logger.info("game resumed");
            }
        });
        popup.getContent().add(resumeButton);

        Button saveButton = new Button();
        saveButton.setText("SAVE AND EXIT");
        Text icon = GlyphsDude.createIcon(FontAwesomeIcon.SAVE, "16px");
        icon.setId("icon");
        saveButton.setGraphic(icon);
        saveButton.setLayoutX(15);
        saveButton.setLayoutY(85);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                saveGame();
                gameStage.close();
                scenes.setMenuScene();
                scenes.show();
                animationTimer.stop();
                logger.info("game saved");
            }
        });
        popup.getContent().add(saveButton);

        popup.show(gameStage);
        animationTimer.stop();
    }

    private void saveGame(){
        GameParameters gameParameters = new GameParameters();
        gameParameters.setRoads(roads);
        gameParameters.setLevel(level);
        gameParameters.setLogin(login);
        gameParameters.setCatX((int)cat.getLayoutX());
        gameParameters.setCatY((int)cat.getLayoutY());
        gameParameters.setStoppedTime(actualGameTime);
        gameParameters.setLoadGame(false);
        filesOperations.saveGame(gameParameters);
    }

    private void createStopButton() {
        final Button stopButton = new Button();
        stopButton.setLayoutX(560);
        stopButton.setLayoutY(4);
        stopButton.setText("STOP");
        stopButton.setId("stopButton");
        gamePane.getChildren().add(stopButton);

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                breakPopUP(stopButton);
                stopButton.setDisable(true);
                logger.info("game stopped");
            }
        });
    }

    private void createTimeLabel() {
        timeLabel = new Label("Time: ");
        timeLabel.setLayoutX(10);
        timeLabel.setLayoutY(8);
        gamePane.getChildren().add(timeLabel);
    }

    private void createCars() {
        int[] layoutYFor3Roads = {130, 190, 360, 420, 585, 645};
        int[] layoutYFor4Roads = {97, 143, 277, 323, 455, 503, 635, 683};
        int[] layoutYFor5Roads = {75, 117, 220, 262, 365, 407, 510, 552, 655, 697};
        int[] layoutYTemporary = new int[10];

        if (roads.equals("3")) {
            redCars = new ImageView[9];
            purpleCars = new ImageView[9];
            layoutYTemporary = layoutYFor3Roads;
        } else if (roads.equals("4")) {
            redCars = new ImageView[12];
            purpleCars = new ImageView[12];
            layoutYTemporary = layoutYFor4Roads;
        } else if (roads.equals("5")) {
            redCars = new ImageView[15];
            purpleCars = new ImageView[15];
            layoutYTemporary = layoutYFor5Roads;
        }

        int[] parameters;

        for (int i = 0; i < 3; i++) {
            parameters = new int[]{layoutYTemporary[0], 0, i};
            carsLane(redCars, parameters, RED_CAR_PATH);
            parameters[0] = layoutYTemporary[1];
            carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
        }

        for (int i = 3; i < 6; i++) {
            parameters = new int[]{layoutYTemporary[2], 3, i};
            carsLane(redCars, parameters, RED_CAR_PATH);
            parameters[0] = layoutYTemporary[3];
            carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
        }

        for (int i = 6; i < 9; i++) {
            parameters = new int[]{layoutYTemporary[4], 6, i};
            carsLane(redCars, parameters, RED_CAR_PATH);
            parameters[0] = layoutYTemporary[5];
            carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
        }

        if (roads.equals("4") || roads.equals("5")) {
            for (int i = 9; i < 12; i++) {
                parameters = new int[]{layoutYTemporary[6], 9, i};
                carsLane(redCars, parameters, RED_CAR_PATH);
                parameters[0] = layoutYTemporary[7];
                carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
            }
        }

        if (roads.equals("5")) {
            for (int i = 12; i < 15; i++) {
                parameters = new int[]{layoutYTemporary[8], 12, i};
                carsLane(redCars, parameters, RED_CAR_PATH);
                parameters[0] = layoutYTemporary[9];
                carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
            }
        }

    }

    private void carsLane(ImageView[] cars, int[] parameters, String path) {
        randomPlace = new Random();
        cars[parameters[2]] = new ImageView(path);
        cars[parameters[2]].setLayoutX(randomPlace.nextInt(170) + (parameters[2] - parameters[1]) * 215);
        cars[parameters[2]].setLayoutY(parameters[0]);
        gamePane.getChildren().add(cars[parameters[2]]);
    }

    private void carsDriving(ImageView[] cars, int speed) {
        for (int i = 0; i < cars.length; i++) {
            cars[i].setLayoutX(cars[i].getLayoutX() + speed);
        }
    }

    private void keepCarsInGame(ImageView[] cars, boolean direction) {
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].getLayoutX() > 620 && direction) {
                cars[i].setLayoutX(-20);
            }
            if (cars[i].getLayoutX() < -20 && !direction) {
                cars[i].setLayoutX(620);
            }
        }
    }

    private void isCollision(ImageView[] cars) {
        for (int i = 0; i < cars.length; i++) {
            if (cat.getLayoutX() < cars[i].getLayoutX() + 30 && cat.getLayoutX() + 20 > cars[i].getLayoutX() &&
                    cat.getLayoutY() < cars[i].getLayoutY() + 20 && cat.getLayoutY() + 20 > cars[i].getLayoutY()) {
                collisionEffect();
            }
        }
    }

    private void collisionEffect() {
        collision = new ImageView(COLLISION_PATH);
        collision.setLayoutX(cat.getLayoutX());
        collision.setLayoutY(cat.getLayoutY());
        gamePane.getChildren().add(collision);

        animationTimer.stop();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                collision.setVisible(false);
                animationTimer.start();
                cat.setLayoutY(GAME_HEIGHT - 50);
                breakTime += 1000;
            }
        }, 1000);
    }

    private int redCarsSpeedOnLevel() {
        if (level.equals("easy")) {
            return 3;
        }
        if (level.equals("medium")) {
            return 3;
        }
        if (level.equals("hard")) {
            return 4;
        } else {
            return 0;
        }
    }

    private int purpleCarsSpeedOnLevel() {
        if (level.equals("easy")) {
            return 3;
        }
        if (level.equals("medium")) {
            return -3;
        }
        if (level.equals("hard")) {
            return -4;
        } else {
            return 0;
        }
    }

    private boolean keepPurpleCarsOnLevel() {
        return level.equals("easy");
    }

    private void createAnimations() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                catMovement();
                carsDriving(redCars, redCarsSpeedOnLevel());
                carsDriving(purpleCars, purpleCarsSpeedOnLevel());
                keepCarsInGame(redCars, true);
                keepCarsInGame(purpleCars, keepPurpleCarsOnLevel());
                isCollision(redCars);
                isCollision(purpleCars);
                updateTime();
                endGame();
            }
        };
        animationTimer.start();
    }

    private void updateTime() {
        actualGameTime = (System.currentTimeMillis() - startTime - breakTime);
        if (loadGame) {
            actualGameTime += stoppedTime;
        }
        long milliseconds = (actualGameTime % 1000) / 10;
        long seconds = (actualGameTime / 1000) % 60;
        long minutes = (actualGameTime / (1000 * 60)) % 60;
        gameTime = minutes + ":" + seconds + ":" + milliseconds;
        timeLabel.setText("Time: " + gameTime);
    }

    private void createCat(int catX, int catY) {
        cat = new ImageView(CAT_PATH);
        if (!loadGame) {
            cat.setLayoutX(290);
            cat.setLayoutY(GAME_HEIGHT - 50);
        } else {
            cat.setLayoutX(catX);
            cat.setLayoutY(catY);
        }
        gamePane.getChildren().add(cat);
    }

    private void catMovement() {
        if (isLeft && cat.getLayoutX() > 0 && cat.getLayoutY() > 30) {
            cat.setLayoutX(cat.getLayoutX() - 3);
        }
        if (isRight && cat.getLayoutX() < 590 && cat.getLayoutY() > 30) {
            cat.setLayoutX(cat.getLayoutX() + 3);
        }
        if (isUp && cat.getLayoutY() > 35) {
            cat.setLayoutY(cat.getLayoutY() - 3);
        }
        if (isDown && cat.getLayoutY() < 750) {
            cat.setLayoutY(cat.getLayoutY() + 3);
        }
        if (isUp && cat.getLayoutY() > 0 && cat.getLayoutX() < 380 && cat.getLayoutX() > 203) {
            cat.setLayoutY(cat.getLayoutY() - 3);
        }
        if (isLeft && cat.getLayoutX() > 206 && cat.getLayoutX() < 380 && cat.getLayoutY() < 30) {
            cat.setLayoutX(cat.getLayoutX() - 3);
        }
        if (isRight&& cat.getLayoutX() > 203 && cat.getLayoutX() < 377 && cat.getLayoutY() < 30) {
            cat.setLayoutX(cat.getLayoutX() + 3);
        }
    }

    private void endGame() {
        if (cat.getLayoutY() == 0) {
            String endGameTime = gameTime;
            gameStage.close();
            animationTimer.stop();
            filesOperations.saveScore(login, endGameTime, roads, level);
            filesOperations.deleteGameSave();
            scenes.setScoresScene();
            scenes.show();
            logger.info("game finished");
        }
    }
}
