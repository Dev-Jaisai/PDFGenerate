package com.example.demo.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
@Service
public class PDFGenerationService {

    public static final String LOGO_PATH = "static/images.png";

    public ByteArrayOutputStream generateBankLetterPDF(Map<String, String> bankDetails, Map<String, String> companyDetails, Map<String, String> facilityDetails) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        // Header with Bank Details and Address on right
        document.add(createHeader(bankDetails.get("bankName")));

        // Bank Details
        document.add(createParagraph("Bank Name: " + bankDetails.get("bankName")));
        document.add(createParagraph("Date: " + facilityDetails.get("date")));

        // Company Details
        document.add(createParagraph("Name of the Company: " + companyDetails.get("companyName")));
        document.add(createParagraph("Address: " + companyDetails.get("companyAddress")));
        document.add(createParagraph("Contact Details: " + companyDetails.get("contactDetails")));

        // Add the heading "Sanction Letter for Credit Facilities"
        Paragraph heading = new Paragraph("Sanction Letter for Credit Facilities", FontFactory.getFont(FontFactory.TIMES_BOLD, 16, Font.UNDERLINE));
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingAfter(20);
        document.add(heading);

        // Add Dear Sir/Madam text
        Paragraph dearSirMadam = new Paragraph("Dear Sir/Madam,", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10));
        dearSirMadam.setSpacingAfter(10);
        document.add(dearSirMadam);

        // Add the text about the sanctioned working capital facility
        String facilityText = "We are pleased to inform you that the bank has approved the following working capital facility for a duration of one year, specifically tailored to meet the financial needs of m/s Ibibi Group Pvt Ltd. This facility includes provisions for timely payments, ensuring smooth operations and sustained growth. We look forward to supporting your business endeavors and fostering a successful partnership.";
        Paragraph facilityParagraph = new Paragraph(facilityText, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10));
        facilityParagraph.setSpacingAfter(20);
        document.add(facilityParagraph);

        // Add the facility table
        document.add(createFacilityTable());

        String additionalParagraphs = " HDFC Bank limited. Nothing contained in this sanction letter should be deemed to create any right or obligation or interest whatsoever in favor of or against any party and the borrower (you/company) ";
        document.add(createParagraph(additionalParagraphs));

        // Add the footer
        createFooter(document, writer);

        // Close the document
        document.close();

        return out;
    }

    private PdfPTable createFacilityTable() throws DocumentException {
        PdfPTable facilityTable = new PdfPTable(4);
        facilityTable.setWidthPercentage(100);

        // Table Headers
        facilityTable.addCell(createHeaderCell("Facility"));
        facilityTable.addCell(createHeaderCell("Existing Limit"));
        facilityTable.addCell(createHeaderCell("Increase/Decrease of Total Limit"));
        facilityTable.addCell(createHeaderCell("Sanction Limit"));

        // Table Data
        facilityTable.addCell(createCell("OverDraft against Fixed Deposit", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("00", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("2500", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("2500", Element.ALIGN_CENTER));

        facilityTable.addCell(createCell("Bank Guarantee", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("00", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("(1500)", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("(1500)", Element.ALIGN_CENTER));

        facilityTable.addCell(createCell("Total", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("00", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("2500", Element.ALIGN_CENTER));
        facilityTable.addCell(createCell("2500", Element.ALIGN_CENTER));

        return facilityTable;
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell headerCell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK)));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return headerCell;
    }

    private PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK)));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private Paragraph createParagraph(String text) {
        return new Paragraph(text);
    }

    private Paragraph createParagraph(String text, int fontSize) {
        return new Paragraph(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize, BaseColor.BLACK));
    }

    private Paragraph createParagraph(Map<String, String> data) {
        Paragraph paragraph = new Paragraph();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            paragraph.add(new Phrase(entry.getKey() + ": " + entry.getValue(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK)));
        }
        return paragraph;
    }private PdfPTable createHeader(String bankName) throws DocumentException, IOException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        // Add logo image
        // Load logo image using classloader
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(LOGO_PATH);
        byte[] imageData = inputStream.readAllBytes(); // Read bytes from the InputStream
        Image logo = Image.getInstance(imageData);
        logo.scaleAbsolute(100f, 30f);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER); // Remove cell border if desired
        headerTable.addCell(logoCell);

        // Bank Name and Address cell
        PdfPCell bankNameAddressCell = new PdfPCell();
        bankNameAddressCell.setBorder(Rectangle.NO_BORDER);
        Paragraph bankNameParagraph = new Paragraph("", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.BLACK));
        bankNameAddressCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align cell content to the right
        bankNameAddressCell.setPaddingRight(20f); // Add some padding from the right edge
        bankNameAddressCell.addElement(bankNameParagraph);
        // Add bank address lines
        bankNameAddressCell.addElement(new Phrase("HDFC Bank Limited", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK)));
        bankNameAddressCell.addElement(new Phrase("HDFC Bank House", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK)));
        bankNameAddressCell.addElement(new Phrase("Mumbai", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK)));
        headerTable.addCell(bankNameAddressCell);

        return headerTable;
    }
    private void createFooter(Document document, PdfWriter writer) throws DocumentException {
        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidthPercentage(100); // Set the width percentage of the table
        footerTable.setTotalWidth(500f); // Or, set an absolute width

        // Left section of the footer
        PdfPCell leftCell = new PdfPCell(new Phrase("Yours Faithfully,\n\nAll Terms & Conditions Accepted\nFOR HDFC BANK LIMITED\nRelationship Manager\nInfrastructure Finance Group"));
        leftCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(leftCell);

        // Right section of the footer
        PdfPCell rightCell = new PdfPCell(new Phrase("www.hdfcbank.com\nCorporate Identity No.: L65920MH1994PLC080618\nRegd. Office: HDFC Bank Ltd., HDFC Bank House, Senapati Bapat Marg, Lower Parel (West), Mumbai - 400 013"));
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(rightCell);

        // Add the footer table to the document
        footerTable.writeSelectedRows(0, -1, 36, 50, writer.getDirectContent());
    }

}
