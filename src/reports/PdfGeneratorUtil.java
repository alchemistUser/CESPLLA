/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

/**
 *
 * @author Pololoers
 */
import java.io.File;
import java.io.FileNotFoundException;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public final class PdfGeneratorUtil {

    private static final String REPORT_DIRECTORY
            = "generated_reports/";

    private PdfGeneratorUtil() {

    }

    // =====================================================
    // CREATE DOCUMENT
    // =====================================================
    public static Document createDocument(String fileName)
            throws FileNotFoundException {

        createReportDirectory();

        String fullPath = REPORT_DIRECTORY + fileName;

        PdfWriter writer = new PdfWriter(fullPath);

        PdfDocument pdfDocument = new PdfDocument(writer);

        return new Document(pdfDocument);
    }

    // =====================================================
    // CREATE REPORT TITLE
    // =====================================================
    public static Paragraph createTitle(String title) {

        return new Paragraph(title)
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
    }

    // =====================================================
    // CREATE SECTION HEADER
    // =====================================================
    public static Paragraph createSectionHeader(String text) {

        return new Paragraph(text)
                .setBold()
                .setFontSize(14)
                .setMarginTop(15)
                .setMarginBottom(10);
    }

    // =====================================================
    // CREATE NORMAL PARAGRAPH
    // =====================================================
    public static Paragraph createParagraph(String text) {

        return new Paragraph(text)
                .setFontSize(11)
                .setMarginBottom(5);
    }

    // =====================================================
    // CREATE TABLE
    // =====================================================
    public static Table createTable(float[] columnWidths) {

        Table table = new Table(
                UnitValue.createPercentArray(columnWidths));

        table.setWidth(UnitValue.createPercentValue(100));

        return table;
    }

    // =====================================================
    // CREATE HEADER CELL
    // =====================================================
    public static Cell createHeaderCell(String text) {

        return new Cell()
                .add(
                        new Paragraph(text)
                                .setBold()
                )
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    // =====================================================
    // CREATE NORMAL CELL
    // =====================================================
    public static Cell createCell(String text) {

        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(10);
    }

    // =====================================================
    // CREATE REPORT DIRECTORY
    // =====================================================
    private static void createReportDirectory() {

        File directory = new File(REPORT_DIRECTORY);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
