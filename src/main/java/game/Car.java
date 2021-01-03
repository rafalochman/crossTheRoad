package game;

import javafx.scene.image.ImageView;

public class Car extends ImageView {
    public Car(String path) {
        super(path);
    }

    public void drive(int speed) {
        this.setLayoutX(this.getLayoutX() + speed);
    }

    public void keepInGame(boolean direction) {
        if (this.getLayoutX() > 620 && direction) {
            this.setLayoutX(-20);
        }
        if (this.getLayoutX() < -20 && !direction) {
            this.setLayoutX(620);
        }
    }
}
