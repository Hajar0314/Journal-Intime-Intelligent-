package com.journalintime.service;

import com.journalintime.dto.NoteDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PdfExportService {

    public void exportNoteToPdf(NoteDTO note, String filePath) throws IOException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.addTitle(note.getTitle());
            document.addAuthor("Journal Intime User");

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Font.BOLD);
            Paragraph title = new Paragraph(note.getTitle(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC);
            String dateStr = note.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm"));
            Paragraph date = new Paragraph("Date: " + dateStr, dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(30);
            document.add(date);

            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Paragraph content = new Paragraph(note.getContent(), contentFont);
            content.setSpacingAfter(40);
            document.add(content);

            if (note.getMoodAnalysis() != null) {
                document.add(new Paragraph(" ")); // Spacer

                Font analysisTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph analysisHeader = new Paragraph("AI Mood Analysis", analysisTitleFont);
                analysisHeader.setSpacingAfter(10);
                document.add(analysisHeader);

                Font analysisFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                document.add(new Paragraph("Mood: " + note.getMoodAnalysis().getOverallMood(), analysisFont));
                document.add(new Paragraph("Summary: " + note.getMoodAnalysis().getSummary(), analysisFont));

                if (note.getMoodAnalysis().getSuggestedExercises() != null
                        && !note.getMoodAnalysis().getSuggestedExercises().isEmpty()) {
                    document.add(new Paragraph("\nSuggested Exercises:",
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                    com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED);
                    note.getMoodAnalysis().getSuggestedExercises().forEach(
                            ex -> list.add(new ListItem(ex.getTitle() + " (" + ex.getDurationMinutes() + " min)")));
                    document.add(list);
                }
            }

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        } finally {
            document.close();
        }
    }
}
