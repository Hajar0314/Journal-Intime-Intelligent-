package com.journalintime.service;

import com.journalintime.model.enums.AppLanguage;
import java.util.List;
import java.util.Random;

public class PromptService {

    private static final List<String> enPrompts = List.of(
            "What made you smile today?",
            "What is one thing you learned today?",
            "Describe a challenge you faced and how you handled it.",
            "What are you most grateful for right now?",
            "If you could change one thing about your day, what would it be?",
            "How do you feel in your body at this moment?",
            "What is a small victory you achieved today?",
            "Who inspired you today and why?",
            "What is a goal you want to focus on tomorrow?",
            "Write about a beautiful thing you saw today.");

    private static final List<String> frPrompts = List.of(
            "Qu'est-ce qui vous a fait sourire aujourd'hui ?",
            "Quelle est la chose que vous avez apprise aujourd'hui ?",
            "Décrivez un défi que vous avez relevé et comment vous l'avez géré.",
            "De quoi êtes-vous le plus reconnaissant en ce moment ?",
            "Si vous pouviez changer une chose dans votre journée, ce serait quoi ?",
            "Comment vous sentez-vous dans votre corps en ce moment ?",
            "Quelle est la petite victoire que vous avez remportée aujourd'hui ?",
            "Qui vous a inspiré aujourd'hui et pourquoi ?",
            "Sur quel objectif voulez-vous vous concentrer demain ?",
            "Écrivez sur une belle chose que vous avez vue aujourd'hui.");

    public String getRandomPrompt(AppLanguage lang) {
        List<String> prompts = (lang == AppLanguage.FR) ? frPrompts : enPrompts;
        return prompts.get(new Random().nextInt(prompts.size()));
    }

    public String getTemplate(String type, AppLanguage lang) {
        if (lang == AppLanguage.FR) {
            return switch (type) {
                case "MORNING" ->
                    "### Intentions du Matin\n- Mon intention d'aujourd'hui : \n- Ce que je veux accomplir : \n- Affirmation positive : ";
                case "EVENING" ->
                    "### Bilan du Soir\n- 3 choses positives : \n- Ce que j'ai appris : \n- Mon humeur générale : ";
                default -> "";
            };
        } else {
            return switch (type) {
                case "MORNING" ->
                    "### Morning Intentions\n- My intention for today: \n- What I want to accomplish: \n- Positive affirmation: ";
                case "EVENING" ->
                    "### Evening Reflection\n- 3 positive things: \n- What I learned: \n- My overall mood: ";
                default -> "";
            };
        }
    }
}
