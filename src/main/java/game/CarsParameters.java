package game;

public class CarsParameters {
    private int redCarsSpeed;
    private int purpleCarsSpeed;
    private boolean purpleCarsDirection;

    public CarsParameters(String level) {
        setRedCarsSpeed(level);
        setPurpleCarsSpeed(level);
        setPurpleCarsDirection(level);
    }

    private void setPurpleCarsDirection(String level) {
        purpleCarsDirection = level.equals("easy");
    }

    private void setRedCarsSpeed(String level) {
        if (level.equals("easy")) {
            redCarsSpeed = 3;
        } else if (level.equals("medium")) {
            redCarsSpeed = 3;
        } else if (level.equals("hard")) {
            redCarsSpeed = 4;
        }
    }

    private void setPurpleCarsSpeed(String level) {
        if (level.equals("easy")) {
            purpleCarsSpeed = 3;
        }
        if (level.equals("medium")) {
            purpleCarsSpeed = -3;
        }
        if (level.equals("hard")) {
            purpleCarsSpeed = -4;
        }
    }

    public boolean getPurpleCarsDirection() {
        return purpleCarsDirection;
    }

    public int getPurpleCarsSpeed() {
        return purpleCarsSpeed;
    }

    public int getRedCarsSpeed() {
        return redCarsSpeed;
    }
}
