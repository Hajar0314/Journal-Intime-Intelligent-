package com.journalintime.service;

import com.journalintime.model.enums.AppLanguage;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {
    private static final Map<AppLanguage, Map<String, String>> translations = new HashMap<>();

    static {
        Map<String, String> en = new HashMap<>();
        en.put("app.title", "My Journal");
        en.put("nav.analytics", "Analytics");
        en.put("nav.profile", "Profile");
        en.put("nav.logout", "Logout");
        en.put("dashboard.notes", "Your Notes");
        en.put("filter.favorites", "Favorites only");
        en.put("editor.save", "Save");
        en.put("editor.delete", "Delete");
        en.put("editor.back", "Back");
        en.put("profile.header", "Profile & Settings");
        en.put("profile.userInfo", "User Information");
        en.put("profile.username", "Username");
        en.put("profile.email", "Email");
        en.put("profile.update", "Update Profile");
        en.put("profile.personalization", "App Personalization");
        en.put("profile.appearance", "Appearance Mode");
        en.put("profile.language", "Interface Language");
        en.put("profile.font", "Preferred Font");
        en.put("profile.save", "Save All Preferences");

        en.put("analytics.header", "Personal Insights");
        en.put("analytics.summaryTitle", "Your Personal Summary");
        en.put("analytics.today", "Today's Entries");
        en.put("analytics.avgMood", "Average Mood Score");
        en.put("analytics.total", "Total Reflections");
        en.put("analytics.moodTrend", "Mood Trends");
        en.put("analytics.moodDist", "Mood Distribution");
        en.put("analytics.placeholder", "Welcome! Start writing to see your personal insights here.");
        en.put("analytics.legend.title", "Analysis Legend");
        en.put("analytics.legend.veryGood", "8-10: Very Good / Positive");
        en.put("analytics.legend.good", "5-7: Good / Neutral");
        en.put("analytics.legend.low", "3-4: Low / Stressed");
        en.put("analytics.legend.bad", "0-2: Bad / Very Bad");
        en.put("analytics.legend.explanation",
                "The score is determined by analyzing the sentiment and keywords in your journal entries.");

        en.put("editor.titlePrompt", "Note Title");
        en.put("editor.contentPrompt", "Write your thoughts...");
        en.put("editor.placeholder", "Write and save to see analysis...");
        en.put("editor.moodTitle", "Detected Mood");
        en.put("editor.moodAnalysis", "AI Mood Analysis");
        en.put("editor.suggested", "Suggested for You");
        en.put("editor.export", "Export PDF");

        en.put("daydetails.header", "Day Details");
        en.put("daydetails.dayscore", "Day Score");
        en.put("daydetails.entries", "Entries");
        en.put("daydetails.listtitle", "Journal Entries");

        en.put("login.title", "Login");
        en.put("login.username", "Username");
        en.put("login.password", "Password");
        en.put("login.remember", "Remember Me");
        en.put("login.btn", "Login");
        en.put("login.noaccount", "No account? Register");
        en.put("register.title", "Create Account");
        en.put("register.email", "Email");
        en.put("register.btn", "Register");
        en.put("register.back", "Back to Login");

        translations.put(AppLanguage.EN, en);

        Map<String, String> fr = new HashMap<>();
        fr.put("app.title", "Mon Journal");
        fr.put("nav.analytics", "Analytique");
        fr.put("nav.profile", "Profil");
        fr.put("nav.logout", "Déconnexion");
        fr.put("dashboard.notes", "Mes Notes");
        fr.put("filter.favorites", "Favoris uniquement");
        fr.put("editor.save", "Enregistrer");
        fr.put("editor.delete", "Supprimer");
        fr.put("editor.back", "Retour");
        fr.put("profile.header", "Profil et Paramètres");
        fr.put("profile.userInfo", "Informations Utilisateur");
        fr.put("profile.username", "Nom d'utilisateur");
        fr.put("profile.email", "Email");
        fr.put("profile.update", "Mettre à jour le profil");
        fr.put("profile.personalization", "Personnalisation");
        fr.put("profile.appearance", "Mode d'apparence");
        fr.put("profile.language", "Langue de l'interface");
        fr.put("profile.font", "Police préférée");
        fr.put("profile.save", "Enregistrer les préférences");

        fr.put("analytics.header", "Aperçus Personnels");
        fr.put("analytics.summaryTitle", "Votre Résumé Personnel");
        fr.put("analytics.today", "Entrées d'aujourd'hui");
        fr.put("analytics.avgMood", "Score d'humeur moyen");
        fr.put("analytics.total", "Total des réflexions");
        fr.put("analytics.moodTrend", "Tendances de l'humeur");
        fr.put("analytics.moodDist", "Distribution de l'humeur");
        fr.put("analytics.placeholder", "Bienvenue ! Commencez à écrire pour voir vos statistiques ici.");
        fr.put("analytics.legend.title", "Légende d'Analyse");
        fr.put("analytics.legend.veryGood", "8-10: Très Bien / Positif");
        fr.put("analytics.legend.good", "5-7: Bien / Neutre");
        fr.put("analytics.legend.low", "3-4: Bas / Stressé");
        fr.put("analytics.legend.bad", "0-2: Mauvais / Très Mauvais");
        fr.put("analytics.legend.explanation",
                "Le score est déterminé en analysant le sentiment et les mots-clés de votre journal.");

        fr.put("editor.titlePrompt", "Titre de la note");
        fr.put("editor.contentPrompt", "Écrivez vos pensées...");
        fr.put("editor.placeholder", "Écrivez et enregistrez pour voir l'analyse...");
        fr.put("editor.moodTitle", "Humeur Détectée");
        fr.put("editor.moodAnalysis", "Analyse d'humeur IA");
        fr.put("editor.suggested", "Suggéré pour vous");
        fr.put("editor.export", "Exporter en PDF");

        fr.put("daydetails.header", "Détails de la Journée");
        fr.put("daydetails.dayscore", "Score du Jour");
        fr.put("daydetails.entries", "Entrées");
        fr.put("daydetails.listtitle", "Notes du Journal");

        fr.put("login.title", "Connexion");
        fr.put("login.username", "Nom d'utilisateur");
        fr.put("login.password", "Mot de passe");
        fr.put("login.remember", "Se souvenir de moi");
        fr.put("login.btn", "Se connecter");
        fr.put("login.noaccount", "Pas de compte ? S'inscrire");
        fr.put("register.title", "Créer un compte");
        fr.put("register.email", "Email");
        fr.put("register.btn", "S'inscrire");
        fr.put("register.back", "Retour à la connexion");

        translations.put(AppLanguage.FR, fr);
    }

    public static String getString(String key, AppLanguage lang) {
        return translations.getOrDefault(lang, translations.get(AppLanguage.EN)).getOrDefault(key, key);
    }
}
