package com.journalintime.ui.controller;

import com.journalintime.service.NoteService;
import com.journalintime.service.LocalizationService;
import com.journalintime.dto.NoteDTO;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsController {

        @FXML
        private VBox analysisContainer;
        @FXML
        private Label summaryPhrase;
        @FXML
        private PieChart moodPieChart;
        @FXML
        private Label headerLabel, summaryTitleLabel, todayLabel, avgLabel, totalLabel, moodDistLabel;
        @FXML
        private Label todayCount, avgMoodScore, totalNotes;
        @FXML
        private Label legendTitleLabel, legendVeryGood, legendGood, legendLow, legendBad, legendExplanation;
        @FXML
        private javafx.scene.control.Button backBtn;

        private NoteService noteService;

        @FXML
        public void initialize() {
                noteService = new NoteService();
                applyLocalization();
                loadData();
        }

        private void applyLocalization() {
                UserDTO user = SessionContext.getInstance().getCurrentUser();
                com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                                : com.journalintime.model.enums.AppLanguage.EN;

                headerLabel.setText(LocalizationService.getString("analytics.header", lang));
                summaryTitleLabel.setText(LocalizationService.getString("analytics.summaryTitle", lang));
                todayLabel.setText(LocalizationService.getString("analytics.today", lang));
                avgLabel.setText(LocalizationService.getString("analytics.avgMood", lang));
                totalLabel.setText(LocalizationService.getString("analytics.total", lang));
                moodDistLabel.setText(LocalizationService.getString("analytics.moodDist", lang));

                legendTitleLabel.setText(LocalizationService.getString("analytics.legend.title", lang));
                legendVeryGood.setText(LocalizationService.getString("analytics.legend.veryGood", lang));
                legendGood.setText(LocalizationService.getString("analytics.legend.good", lang));
                legendLow.setText(LocalizationService.getString("analytics.legend.low", lang));
                legendBad.setText(LocalizationService.getString("analytics.legend.bad", lang));
                legendExplanation.setText(LocalizationService.getString("analytics.legend.explanation", lang));

                backBtn.setText(lang == com.journalintime.model.enums.AppLanguage.FR ? "← Retour" : "← Back");
        }

        private void loadData() {
                Long userId = SessionContext.getInstance().getCurrentUser().getId();
                List<NoteDTO> notes = noteService.getUserNotes(userId);

                UserDTO user = SessionContext.getInstance().getCurrentUser();
                com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                                : com.journalintime.model.enums.AppLanguage.EN;

                if (notes.isEmpty()) {
                        summaryPhrase.setText(LocalizationService.getString("analytics.placeholder", lang));
                        return;
                }

                long countToday = notes.stream()
                                .filter(n -> n.getCreatedAt().toLocalDate().equals(java.time.LocalDate.now()))
                                .count();
                double avgScore = notes.stream()
                                .filter(n -> n.getMoodAnalysis() != null)
                                .mapToDouble(n -> n.getMoodAnalysis().getScore())
                                .average()
                                .orElse(0.0);

                if (todayCount != null)
                        todayCount.setText(String.valueOf(countToday));
                if (avgMoodScore != null)
                        avgMoodScore.setText(String.format("%.1f/10", avgScore));
                if (totalNotes != null)
                        totalNotes.setText(String.valueOf(notes.size()));

                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                Map<com.journalintime.model.enums.MoodLevel, Long> moodCounts = notes.stream()
                                .filter(n -> n.getMoodAnalysis() != null)
                                .collect(Collectors.groupingBy(n -> n.getMoodAnalysis().getOverallMood(),
                                                Collectors.counting()));

                moodCounts.forEach((mood, count) -> {
                        String label = switch (mood) {
                                case VERY_GOOD -> lang == com.journalintime.model.enums.AppLanguage.FR ? "Très Bien"
                                                : "Very Good";
                                case GOOD -> lang == com.journalintime.model.enums.AppLanguage.FR ? "Bien" : "Good";
                                case NEUTRAL ->
                                        lang == com.journalintime.model.enums.AppLanguage.FR ? "Neutre" : "Neutral";
                                case BAD -> lang == com.journalintime.model.enums.AppLanguage.FR ? "Mauvais" : "Bad";
                                case VERY_BAD -> lang == com.journalintime.model.enums.AppLanguage.FR ? "Très Mauvais"
                                                : "Very Bad";
                        };
                        pieChartData.add(new PieChart.Data(label, count));
                });
                moodPieChart.setData(pieChartData);

                generateSummary(notes, avgScore);
        }

        private void generateSummary(List<NoteDTO> notes, double avgScore) {
                UserDTO user = SessionContext.getInstance().getCurrentUser();
                com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                                : com.journalintime.model.enums.AppLanguage.EN;

                if (notes.size() < 3) {
                        String msg = lang == com.journalintime.model.enums.AppLanguage.FR
                                        ? "Continuez à écrire ! J'aurai besoin d'un peu plus d'entrées pour vous donner un résumé détaillé."
                                        : "Keep writing! I'll need a few more entries to give you a detailed summary.";
                        summaryPhrase.setText(msg);
                        return;
                }

                String tone;
                if (lang == com.journalintime.model.enums.AppLanguage.FR) {
                        tone = (avgScore >= 7) ? "généralement positive et encourageante"
                                        : (avgScore >= 4) ? "assez équilibrée avec quelques hauts et bas"
                                                        : "montrant quelques signes de stress ou de baisse de moral";
                } else {
                        tone = (avgScore >= 7) ? "generally positive and uplifting"
                                        : (avgScore >= 4) ? "quite balanced with some ups and downs"
                                                        : "showing some signs of stress or low mood";
                }

                String frequency;
                if (lang == com.journalintime.model.enums.AppLanguage.FR) {
                        frequency = (notes.size() > 10) ? "journaliste dévoué" : "habitude de réflexion croissante";
                } else {
                        frequency = (notes.size() > 10) ? "dedicated journalist" : "growing reflection habit";
                }

                if (lang == com.journalintime.model.enums.AppLanguage.FR) {
                        summaryPhrase.setText(String.format(
                                        "D'après vos %d dernières entrées, votre humeur est %s. Vous développez une %s. N'oubliez pas de prendre du temps pour vous !",
                                        notes.size(), tone, frequency));
                } else {
                        summaryPhrase.setText(String.format(
                                        "Based on your last %d entries, your mood is %s. You are building a %s. Remember to take time for yourself!",
                                        notes.size(), tone, frequency));
                }
        }

        @FXML
        private void handleBack() throws IOException {
                MainUI.setRoot("main_dashboard");
        }
}
