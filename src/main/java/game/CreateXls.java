package game;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CreateXls {
    private Logger logger = Logger.getLogger(CreateXls.class);
    private FilesOperations filesOperations = new FilesOperations();

    public void saveXls() {
        HSSFWorkbook scoresWorkbook = new HSSFWorkbook();
        HSSFSheet firstSheet = scoresWorkbook.createSheet("SCORES");
        addHeader(firstSheet);
        addScores(firstSheet);

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File("scores.xls"));
            scoresWorkbook.write(fileOutputStream);
            scoresWorkbook.close();
            fileOutputStream.close();
            logger.info("Scores saved to scores.xls");
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

    }

    private void addHeader(HSSFSheet firstSheet){
        HSSFRow row = firstSheet.createRow(0);
        row.createCell(0).setCellValue("Login");
        row.createCell(1).setCellValue("Time");
        row.createCell(2).setCellValue("Level");
        row.createCell(3).setCellValue("Roads");
        row.createCell(4).setCellValue("Date");
    }

    private void addScores(HSSFSheet firstSheet){
        List<String> data = filesOperations.loadScores();
        int rowCounter = 1;
        for (int i = data.size(); i > 0; i--) {
            String[] dataArray = data.get(i - 1).split(" ");
            if (dataArray.length > 1) {
                HSSFRow scoresRow = firstSheet.createRow(rowCounter);
                scoresRow.createCell(0).setCellValue(dataArray[0]);
                scoresRow.createCell(1).setCellValue(dataArray[1]);
                scoresRow.createCell(2).setCellValue(dataArray[2]);
                scoresRow.createCell(3).setCellValue(dataArray[3]);
                scoresRow.createCell(4).setCellValue(dataArray[4]);
                rowCounter++;
            }
        }
    }

}
