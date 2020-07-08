package game;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FilesOperations {
    private final Logger logger = Logger.getLogger(FilesOperations.class);
    private static final String SCORES_PATH = "scores.txt";
    private static final String SAVE_GAME_PATH = "save.json";

    public void saveScore(String login, String time, String roads, String level) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            FileWriter file = new FileWriter(SCORES_PATH, true);
            file.write(login + " " + time + " " + level + " " + roads + " " + dateFormat.format(date) + System.getProperty("line.separator"));
            file.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public List<String> loadScores() {
        List<String> data = new ArrayList<String>();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(SCORES_PATH)));
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return data;
    }

    public void saveGame(GameParameters gameParameters) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("LOGIN", gameParameters.getLogin());
        jsonData.put("TIME", gameParameters.getStoppedTime());
        jsonData.put("ROADS", gameParameters.getRoads());
        jsonData.put("LEVEL", gameParameters.getLevel());
        jsonData.put("CATX", gameParameters.getCatX());
        jsonData.put("CATY", gameParameters.getCatY());

        try {
            FileWriter file = new FileWriter(SAVE_GAME_PATH);
            file.write(jsonData.toString());
            file.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void deleteGameSave() {
        try {
            FileWriter file = new FileWriter(SAVE_GAME_PATH);
            file.write("empty");
            file.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public List<String> loadGame() {
        String read;
        List<String> data = new ArrayList<String>();
        data.add("empty");
        JSONObject jsonData = new JSONObject();
        try {
            FileReader file = new FileReader(SAVE_GAME_PATH);
            Scanner reader = new Scanner(file);
            read = reader.nextLine();
            reader.close();
            file.close();

            if (!read.equals("empty")) {
                jsonData = new JSONObject(read);
            }

            if (!read.equals("empty")) {
                data = new ArrayList<String>(Arrays.asList(jsonData.get("ROADS").toString(), jsonData.get("LEVEL").toString(), jsonData.get("LOGIN").toString(), jsonData.get("CATX").toString(),
                        jsonData.get("CATY").toString(), jsonData.get("TIME").toString()));
            } else {
                data.add(read);
            }
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        return data;
    }

}
