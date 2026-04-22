package BusinessAnalysis.system.agents;

import org.springframework.stereotype.Service;

@Service
public class RecommendationAgent {
    public String recommend(String plan, int gap, double performance) {

        if (performance < 0.6) {
            return "Urgently improve execution and increase team capacity";
        }

        if (gap > 0) {
            return "Hire " + gap + " additional employees to meet target based on current productivity";
        }

        return "Maintain current strategy and optimize efficiency";
    }
}
