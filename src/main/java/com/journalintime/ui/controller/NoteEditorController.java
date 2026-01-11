package com.journalintime.ui.controller;

import com.journalintime.ui.MainUI;
import com.journalintime.ui.viewmodel.NoteEditorViewModel;
import com.journalintime.dto.MoodAnalysisDTO;
import com.journalintime.dto.ExerciseDTO;
import com.journalintime.model.enums.MoodLevel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class NoteEditorController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private VBox analysisContainer;
    @FXML
    private ToggleButton favoriteToggle;
    @FXML
    private ToggleButton importantToggle;
    @FXML
    private Button saveBtn, backBtn, deleteBtn, exportBtn;
    @FXML
    private Label analysisTitleLabel, promptTitleLabel, promptTextLabel;

    private NoteEditorViewModel viewModel;
    private final com.journalintime.service.PdfExportService pdfExportService = new com.journalintime.service.PdfExportService();
    private final com.journalintime.service.PromptService promptService = new com.journalintime.service.PromptService();

    @FXML
    public void initialize() {
        viewModel = new NoteEditorViewModel();

        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        contentArea.textProperty().bindBidirectional(viewModel.contentProperty());
        favoriteToggle.selectedProperty().bindBidirectional(viewModel.favoriteProperty());
        importantToggle.selectedProperty().bindBidirectional(viewModel.importantProperty());
        viewModel.analysisProperty().addListener((obs, oldVal, newVal) -> renderAnalysis(newVal));

        applyLocalization();
        handleNewPrompt();

        applyLocalization();
        handleNewPrompt();
        renderAnalysis(viewModel.analysisProperty().get());
    }

    private void applyLocalization() {
        com.journalintime.dto.UserDTO user = com.journalintime.ui.SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;

        titleField.setPromptText(
                com.journalintime.service.LocalizationService.getString("editor.titlePrompt", lang));
        contentArea.setPromptText(
                com.journalintime.service.LocalizationService.getString("editor.contentPrompt", lang));
        saveBtn.setText(com.journalintime.service.LocalizationService.getString("editor.save", lang));
        deleteBtn.setText(com.journalintime.service.LocalizationService.getString("editor.delete", lang));
        backBtn.setText(com.journalintime.service.LocalizationService.getString("editor.back", lang));
        exportBtn.setText(com.journalintime.service.LocalizationService.getString("editor.export", lang));
        analysisTitleLabel.setText(
                com.journalintime.service.LocalizationService.getString("editor.moodAnalysis", lang));
    }

    private void renderAnalysis(MoodAnalysisDTO analysis) {
        analysisContainer.getChildren().clear();

        com.journalintime.dto.UserDTO user = com.journalintime.ui.SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;

        if (analysis == null) {
            Label placeholder = new Label(
                    com.journalintime.service.LocalizationService.getString("editor.placeholder", lang));
            placeholder.setWrapText(true);
            placeholder.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            analysisContainer.getChildren().add(placeholder);
            return;
        }

        VBox moodCard = new VBox(5);
        moodCard.getStyleClass().add("mood-card");

        Label moodTitle = new Label(
                com.journalintime.service.LocalizationService.getString("editor.moodTitle", lang));
        moodTitle.getStyleClass().add("card-title");

        Label moodValue = new Label(analysis.getOverallMood().name());
        moodValue.getStyleClass().add("mood-value");
        applyMoodStyle(moodValue, analysis.getOverallMood());

        Label summaryLabel = new Label(analysis.getSummary());
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 12px;");

        moodCard.getChildren().addAll(moodTitle, moodValue, summaryLabel);
        analysisContainer.getChildren().add(moodCard);

        if (analysis.getSuggestedExercises() != null && !analysis.getSuggestedExercises().isEmpty()) {
            Label exHeader = new Label(
                    com.journalintime.service.LocalizationService.getString("editor.suggested", lang));
            exHeader.getStyleClass().add("section-header");
            analysisContainer.getChildren().add(exHeader);

            for (ExerciseDTO ex : analysis.getSuggestedExercises()) {
                VBox exCard = new VBox(5);
                exCard.getStyleClass().add("exercise-card");

                Label exTitle = new Label(ex.getTitle());
                exTitle.getStyleClass().add("exercise-title");

                Label exDesc = new Label(ex.getDescription());
                exDesc.setWrapText(true);

                Label exMeta = new Label(ex.getType() + " • " + ex.getDurationMinutes() + " min");
                exMeta.setStyle("-fx-font-size: 10px; -fx-text-fill: grey;");

                exCard.getChildren().addAll(exTitle, exMeta, exDesc);
                analysisContainer.getChildren().add(exCard);
            }
        }
    }

    @FXML
    private void handleNewPrompt() {
        com.journalintime.dto.UserDTO user = com.journalintime.ui.SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;
        promptTextLabel.setText(promptService.getRandomPrompt(lang));
    }

    @FXML
    private void handleMorningTemplate() {
        applyTemplate("MORNING");
    }

    @FXML
    private void handleEveningTemplate() {
        applyTemplate("EVENING");
    }

    private void applyTemplate(String type) {
        com.journalintime.dto.UserDTO user = com.journalintime.ui.SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;
        String template = promptService.getTemplate(type, lang);
        if (contentArea.getText() == null || contentArea.getText().trim().isEmpty()) {
            contentArea.setText(template);
        } else {
            contentArea.appendText("\n\n" + template);
        }
    }

    private void applyMoodStyle(Label label, MoodLevel mood) {
        String color = switch (mood) {
            case VERY_GOOD, GOOD -> "#388E3C"; // Green
            case BAD, VERY_BAD -> "#D32F2F"; // Red
            default -> "#F57C00"; // Orange (Neutral)
        };
        label.setStyle("-fx-text-fill: " + color + ";");
    }

    @FXML
    private void handleSave() throws IOException {
        viewModel.save();
    }

    @FXML
    private void handleBack() throws IOException {
        MainUI.setRoot("main_dashboard");
    }

    @FXML
    private void handleDelete() throws IOException {
        if (viewModel.delete()) {
            MainUI.setRoot("main_dashboard");
        }
    }

    @FXML
    private void handleExport() {
        if (viewModel.getCurrentNote() == null)
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(viewModel.getCurrentNote().getTitle().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf");

        File file = fileChooser.showSaveDialog(titleField.getScene().getWindow());
        if (file != null) {
            try {
                pdfExportService.exportNoteToPdf(viewModel.getCurrentNote(), file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
