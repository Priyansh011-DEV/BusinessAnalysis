package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.nlp.BusinessLexiconService;
import org.springframework.stereotype.Service;

@Service
public class RiskAgent {
    private final BusinessLexiconService lexicon;

    public RiskAgent(BusinessLexiconService lexicon) {
        this.lexicon = lexicon;
    }

    public String evaluateRisk(KPIData data, int gap, double performance, String text) {

        String base;

        if (performance < 0.6) {
            base = "High execution risk due to major performance gap";
        } else if (gap > 0) {
            base = "Capacity risk due to insufficient team size";
        } else {
            base = "Low operational risk";
        }

        String riskContext = lexicon.getBestSentence(text, "risk");

        return base + ". " + riskContext;
    }
}
