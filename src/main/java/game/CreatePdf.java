package game;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreatePdf {
    private Logger logger = Logger.getLogger(CreatePdf.class);
    private FilesOperations filesOperations=new FilesOperations();
    private PdfPTable pdfPTable;

    public void savePdf() {
        DateFormat dateFormat = new SimpleDateFormat("H:m dd/MM/yyyy");
        Date date = new Date();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("scores.pdf"));
            document.open();
            document.add(new Paragraph("Game scores - saved: " + dateFormat.format(date)));
            pdfPTable = new PdfPTable(5);
            pdfPTable.setSpacingBefore(15);
            addTableHeader();
            fillTable();
            document.add(pdfPTable);
            document.close();
            writer.close();

            logger.info("Scores saved to scores.pdf");
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (DocumentException e) {
            logger.error(e);
        }

    }

    private void addTableHeader() {
        addHeaderCell("Login");
        addHeaderCell("Time");
        addHeaderCell("Level");
        addHeaderCell("Roads");
        addHeaderCell("Date");
    }

    private void fillTable() {
        List<String> data = filesOperations.loadScores();
        for (int i = data.size(); i > 0; i--) {
            String[] dataArray = data.get(i - 1).split(" ");
            if (dataArray.length > 1) {
                addCell(dataArray[0]);
                addCell(dataArray[1]);
                addCell(dataArray[2]);
                addCell(dataArray[3]);
                addCell(dataArray[4]);
            }
        }
    }

    private void addHeaderCell(String text) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(text));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCell);
    }

    private void addCell(String text) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(text));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(pdfPCell);
    }
}
