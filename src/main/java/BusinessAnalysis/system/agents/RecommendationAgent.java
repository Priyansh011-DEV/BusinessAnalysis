package BusinessAnalysis.system.agents;

import org.springframework.stereotype.Service;

@Service
public class RecommendationAgent {
    public String recommend(String plan, boolean marketHot, String risk) {

        if (marketHot && plan.contains("Aggressive") && risk.contains("under-hiring")) {
            return "Increase hiring capacity immediately, especially in high-demand roles";
        }

        if (!marketHot && plan.contains("Aggressive")) {
            return "Proceed cautiously. Focus on cost control and selective hiring";
        }

        if (risk.contains("Low")) {
            return "Maintain steady growth with balanced hiring strategy";
        }

        return "Re-evaluate strategy based on current KPIs and market signals";
    }
}
