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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final Logger logger = Logger.getLogger(Game.class);

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private static final String RED_CAR_PATH = "redCar.png";
    private static final String PURPLE_CAR_PATH = "purpleCar.png";
    private static final String CAT_PATH = "redCat.png";
    private static final String COLLISION_PATH = "collision.png";
    private static final String BACKGROUND_3_PATH = "roads3Background.png";
    private static final String BACKGROUND_4_PATH = "roads4Background.png";
    private static final String BACKGROUND_5_PATH = "roads5Background.png";
    private static final String ICON_PATH = "icon.png";

    private final AnchorPane gamePane;
    private final Scene gameScene;
    private final Stage gameStage;

    private String roads;
    private String level;
    private String login;

    private AnimationTimer animationTimer;
    private Cat cat;
    private Label timeLabel;
    private Car[] redCars;
    private Car[] purpleCars;
    private CarsParameters carsParameters;
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

    private final Scenes scenes;
    private final FilesOperations filesOperations;

    public Game(Scenes scenes) {
        this.scenes = scenes;
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
        gameStage.setTitle("Cross The Road");
        gameStage.getIcons().add(new Image(ICON_PATH));
        gameScene.getStylesheets().add("gameStyles.css");
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
        carsParameters = new CarsParameters(level);
        createTimeLabel();
        createStopButton();
        startTime = System.currentTimeMillis();
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

    private void saveGame() {
        GameParameters gameParameters = new GameParameters();
        gameParameters.setRoads(roads);
        gameParameters.setLevel(level);
        gameParameters.setLogin(login);
        gameParameters.setCatX((int) cat.getLayoutX());
        gameParameters.setCatY((int) cat.getLayoutY());
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
            redCars = new Car[9];
            purpleCars = new Car[9];
            layoutYTemporary = layoutYFor3Roads;
        } else if (roads.equals("4")) {
            redCars = new Car[12];
            purpleCars = new Car[12];
            layoutYTemporary = layoutYFor4Roads;
        } else if (roads.equals("5")) {
            redCars = new Car[15];
            purpleCars = new Car[15];
            layoutYTemporary = layoutYFor5Roads;
        }

        for (int i = 0; i < 3; i++) {
            createCarsLane(layoutYTemporary, i, 1, 0);
        }

        for (int i = 3; i < 6; i++) {
            createCarsLane(layoutYTemporary, i, 3, 3);
        }

        for (int i = 6; i < 9; i++) {
            createCarsLane(layoutYTemporary, i, 5, 6);
        }

        if (roads.equals("4") || roads.equals("5")) {
            for (int i = 9; i < 12; i++) {
                createCarsLane(layoutYTemporary, i, 7, 9);
            }
        }

        if (roads.equals("5")) {
            for (int i = 12; i < 15; i++) {
                createCarsLane(layoutYTemporary, i, 9, 12);
            }
        }
    }

    private void createCarsLane(int[] layoutYTemporary, int i, int step, int point) {
        int[] parameters;
        parameters = new int[]{layoutYTemporary[step - 1], point, i};
        carsLane(redCars, parameters, RED_CAR_PATH);
        parameters[0] = layoutYTemporary[step];
        carsLane(purpleCars, parameters, PURPLE_CAR_PATH);
    }

    private void carsLane(Car[] cars, int[] parameters, String path) {
        randomPlace = new Random();
        cars[parameters[2]] = new Car(path);
        cars[parameters[2]].setLayoutX(randomPlace.nextInt(170) + (parameters[2] - parameters[1]) * 215);
        cars[parameters[2]].setLayoutY(parameters[0]);
        gamePane.getChildren().add(cars[parameters[2]]);
    }

    private void carsDriving(Car[] cars, int speed) {
        for (Car car : cars) {
            car.drive(speed);
        }
    }

    private void keepCarsInGame(Car[] cars, boolean direction) {
        for (Car car : cars) {
            car.keepInGame(direction);
        }
    }

    private void isCollision(Car[] cars) {
        for (Car car : cars) {
            if (cat.getLayoutX() < car.getLayoutX() + 30 && cat.getLayoutX() + 20 > car.getLayoutX() &&
                    cat.getLayoutY() < car.getLayoutY() + 20 && cat.getLayoutY() + 20 > car.getLayoutY()) {
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

    private void createAnimations() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cat.catMovement();
                carsDriving(redCars, carsParameters.getRedCarsSpeed());
                carsDriving(purpleCars, carsParameters.getPurpleCarsSpeed());
                keepCarsInGame(redCars, true);
                keepCarsInGame(purpleCars, carsParameters.getPurpleCarsDirection());
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
        cat = new Cat(CAT_PATH, gameScene);
        cat.createKeyListeners();
        if (!loadGame) {
            cat.setLayoutX(290);
            cat.setLayoutY(GAME_HEIGHT - 50);
        } else {
            cat.setLayoutX(catX);
            cat.setLayoutY(catY);
        }
        gamePane.getChildren().add(cat);
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
