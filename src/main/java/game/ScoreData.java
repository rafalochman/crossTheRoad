package game;

public class ScoreData {
    private String login;
    private String time;
    private String level;
    private String roads;
    private String date;

    public ScoreData(String login, String time, String roads, String level, String date) {
        this.login = login;
        this.time = time;
        this.roads = roads;
        this.level = level;
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoads() {
        return roads;
    }

    public void setRoads(String roads) {
        this.roads = roads;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
