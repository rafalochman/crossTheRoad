package game;

public class GameParameters {
    private String roads;
    private String level;
    private String login;
    private int catX;
    private int catY;
    private long stoppedTime;
    private boolean loadGame;

    public void setRoads(String roads) {
        this.roads = roads;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setCatX(int catX) {
        this.catX = catX;
    }

    public void setCatY(int catY) {
        this.catY = catY;
    }

    public void setStoppedTime(long stoppedTime) {
        this.stoppedTime = stoppedTime;
    }

    public void setLoadGame(boolean loadGame) {
        this.loadGame = loadGame;
    }

    public String getRoads() {
        return roads;
    }

    public String getLevel() {
        return level;
    }

    public String getLogin() {
        return login;
    }

    public int getCatX() {
        return catX;
    }

    public int getCatY() {
        return catY;
    }

    public long getStoppedTime() {
        return stoppedTime;
    }

    public boolean getLoadGame() {
        return loadGame;
    }

}
