package BusinessAnalysis.system.nlp;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service

public class BusinessLexiconService {
    private final KeywordConfig keywordConfig;

    public BusinessLexiconService(KeywordConfig keywordConfig) {
        this.keywordConfig = keywordConfig;
    }

    // 🔥 MAIN METHOD
    public String getBestSentence(String text, String category) {

        List<String> sentences = split(text);
        Map<String, Integer> keywords = keywordConfig.get(category);

        return sentences.stream()
                .max(Comparator.comparingInt(s -> score(s, keywords)))
                .orElse("No relevant data found");
    }

    // 🔍 SCORING ENGINE
    private int score(String sentence, Map<String, Integer> keywords) {

        int score = 0;
        String lower = sentence.toLowerCase();

        for (var entry : keywords.entrySet()) {
            if (lower.contains(entry.getKey())) {
                score += entry.getValue();
            }
        }

        // 🔥 smart boosts
        if (lower.matches(".*\\d+.*")) score += 2;
        if (sentence.length() > 100) score += 1;

        return score;
    }

    // ✂️ Sentence splitter
    private List<String> split(String text) {
        return List.of(text.split("\\."))
                .stream()
                .map(String::trim)
                .filter(s -> s.length() > 10)
                .toList();
    }
}
