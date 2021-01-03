package game;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Cat extends ImageView {
    private boolean isLeft;
    private boolean isRight;
    private boolean isUp;
    private boolean isDown;
    private final Scene gameScene;

    public Cat(String path, Scene gameScene) {
        super(path);
        this.gameScene = gameScene;
        createKeyListeners();
    }

    public void createKeyListeners() {
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

    public void catMovement() {
        if (isLeft && this.getLayoutX() > 0 && this.getLayoutY() > 30) {
            this.setLayoutX(this.getLayoutX() - 3);
        }
        if (isRight && this.getLayoutX() < 590 && this.getLayoutY() > 30) {
            this.setLayoutX(this.getLayoutX() + 3);
        }
        if (isUp && this.getLayoutY() > 35) {
            this.setLayoutY(this.getLayoutY() - 3);
        }
        if (isDown && this.getLayoutY() < 750) {
            this.setLayoutY(this.getLayoutY() + 3);
        }
        if (isUp && this.getLayoutY() > 0 && this.getLayoutX() < 380 && this.getLayoutX() > 203) {
            this.setLayoutY(this.getLayoutY() - 3);
        }
        if (isLeft && this.getLayoutX() > 206 && this.getLayoutX() < 380 && this.getLayoutY() < 30) {
            this.setLayoutX(this.getLayoutX() - 3);
        }
        if (isRight && this.getLayoutX() > 203 && this.getLayoutX() < 377 && this.getLayoutY() < 30) {
            this.setLayoutX(this.getLayoutX() + 3);
        }
    }
}
