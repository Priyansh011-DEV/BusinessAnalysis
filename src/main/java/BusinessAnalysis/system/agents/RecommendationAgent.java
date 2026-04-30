package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.nlp.BusinessLexiconService;
import org.springframework.stereotype.Service;

@Service
public class RecommendationAgent {
    private final BusinessLexiconService lexicon;

    public RecommendationAgent(BusinessLexiconService lexicon) {
        this.lexicon = lexicon;
    }

    public String recommend(String plan, int gap, double performance, String text) {

        String base;

        if (performance < 0.6) {
            base = "Urgently improve execution and increase team capacity";
        } else if (gap > 0) {
            base = "Hire " + gap + " additional employees to meet target based on current productivity";
        } else {
            base = "Maintain current strategy and optimize efficiency";
        }

        String recContext = lexicon.getBestSentence(text, "recommendation");

        return base + ". " + recContext;
    }
}
