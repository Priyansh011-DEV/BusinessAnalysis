package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.nlp.BusinessLexiconService;
import org.springframework.stereotype.Service;

@Service
public class PlannerAgent {

    private final BusinessLexiconService lexicon;

    public PlannerAgent(BusinessLexiconService lexicon) {
        this.lexicon = lexicon;
    }

    public String plan(KPIData data, double performance, String text) {

        String base;

        if (performance < 0.5) {
            base = "Severe underperformance vs targets";
        } else if (performance < 0.8) {
            base = "Underperforming against growth targets";
        } else if (performance <= 1.1) {
            base = "On track with planned growth";
        } else {
            base = "Exceeding growth expectations";
        }

        String insight = lexicon.getBestSentence(text, "insight");

        return base + ". " + insight;
    }
}