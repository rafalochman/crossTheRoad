package game;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class Scores extends AnchorPane {
    private final Scenes scenes;
    private final FilesOperations filesOperations;
    private final CreatePdf createPdf;
    private final CreateXls createXls;
    private final TableView<ScoreData> tableView;

    public Scores(Scenes scenes) {
        this.scenes = scenes;
        tableView = new TableView<ScoreData>();
        filesOperations = new FilesOperations();
        createPdf = new CreatePdf();
        createXls = new CreateXls();
        addTitleLabel();
        createTable();
        createButtonBackToMenu();
        createButtonCreatePdf();
        createButtonCreateXls();
    }

    public void fillTable() {
        tableView.getItems().clear();
        List<String> data = filesOperations.loadScores();
        if (data.size() >= 1) {
            for (int i = data.size(); i > 0; i--) {
                String[] dataArray = data.get(i - 1).split(" ");
                tableView.getItems().add(new ScoreData(dataArray[0], dataArray[1], dataArray[3], dataArray[2], dataArray[4]));
            }
        }
    }

    private void addTitleLabel() {
        Label scoresLabel = new Label("Scores:");
        scoresLabel.setLayoutX(250);
        scoresLabel.setLayoutY(10);
        scoresLabel.setId("scoresLabel");
        this.getChildren().add(scoresLabel);
    }

    private void createTable() {
        TableColumn<ScoreData, String> columnLogin = new TableColumn<ScoreData, String>("Login");
        columnLogin.setCellValueFactory(new PropertyValueFactory<ScoreData, String>("login"));

        TableColumn<ScoreData, String> columnTime = new TableColumn<ScoreData, String>("Time");
        columnTime.setCellValueFactory(new PropertyValueFactory<ScoreData, String>("time"));

        TableColumn<ScoreData, String> columnRoads = new TableColumn<ScoreData, String>("Roads");
        columnRoads.setCellValueFactory(new PropertyValueFactory<ScoreData, String>("roads"));

        TableColumn<ScoreData, String> columnLevel = new TableColumn<ScoreData, String>("Level");
        columnLevel.setCellValueFactory(new PropertyValueFactory<ScoreData, String>("level"));

        TableColumn<ScoreData, String> columnDate = new TableColumn<ScoreData, String>("Date");
        columnDate.setCellValueFactory(new PropertyValueFactory<ScoreData, String>("date"));

        tableView.getColumns().add(columnLogin);
        tableView.getColumns().add(columnTime);
        tableView.getColumns().add(columnRoads);
        tableView.getColumns().add(columnLevel);
        tableView.getColumns().add(columnDate);

        columnLogin.setPrefWidth(100);
        columnTime.setPrefWidth(100);
        columnRoads.setPrefWidth(80);
        columnLevel.setPrefWidth(100);
        columnDate.setPrefWidth(102);

        columnLogin.setResizable(false);
        columnTime.setResizable(false);
        columnRoads.setResizable(false);
        columnLevel.setResizable(false);
        columnDate.setResizable(false);

        fillTable();

        tableView.setPrefWidth(500);
        tableView.setMaxHeight(300);
        tableView.setLayoutX(60);
        tableView.setLayoutY(60);
        Label noScoresLabel = new Label("No scores");
        noScoresLabel.setId("noScoresLabel");
        tableView.setPlaceholder(noScoresLabel);
        this.getChildren().add(tableView);
    }

    private void createButtonBackToMenu() {
        Button backToMenuButton = new Button();
        backToMenuButton.setLayoutX(250);
        backToMenuButton.setLayoutY(470);
        backToMenuButton.setText("BACK TO MENU");
        this.getChildren().add(backToMenuButton);

        backToMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                scenes.setMenuScene();
            }
        });
    }

    private void createButtonCreatePdf() {
        final Button saveToPdfButton = new Button();
        saveToPdfButton.setLayoutX(120);
        saveToPdfButton.setLayoutY(400);
        saveToPdfButton.setText("SAVE SCORES TO PDF");
        Text pdfIcon = GlyphsDude.createIcon(FontAwesomeIcon.FILE_PDF_ALT, "20px");
        pdfIcon.setId("pdfIcon");
        saveToPdfButton.setGraphic(pdfIcon);
        this.getChildren().add(saveToPdfButton);

        saveToPdfButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (createPdf.savePdf()) {
                    setButtonColor(saveToPdfButton, "-fx-background-color: lightgreen");
                } else {
                    setButtonColor(saveToPdfButton, "-fx-background-color: red");
                }
            }
        });
    }

    private void createButtonCreateXls() {
        final Button saveToXlsButton = new Button();
        saveToXlsButton.setLayoutX(320);
        saveToXlsButton.setLayoutY(400);
        saveToXlsButton.setText("SAVE SCORES TO XLS");
        Text xlsIcon = GlyphsDude.createIcon(FontAwesomeIcon.FILE_EXCEL_ALT, "20px");
        xlsIcon.setId("xlsIcon");
        saveToXlsButton.setGraphic(xlsIcon);
        this.getChildren().add(saveToXlsButton);

        saveToXlsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (createXls.saveXls()) {
                    setButtonColor(saveToXlsButton, "-fx-background-color: lightgreen");
                } else {
                    setButtonColor(saveToXlsButton, "-fx-background-color: red");
                }
            }
        });
    }

    private void setButtonColor(final Button button, String style) {
        button.setStyle(style);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                button.setStyle(null);
            }
        });
        pause.play();
    }

}
