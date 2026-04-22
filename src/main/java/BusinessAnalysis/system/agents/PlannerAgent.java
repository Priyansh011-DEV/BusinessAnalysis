package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class PlannerAgent {

    public String plan(KPIData data, double performance) {

        if (performance < 0.5) {
            return "Severe underperformance vs targets";
        }

        if (performance < 0.8) {
            return "Underperforming against growth targets";
        }

        if (performance <= 1.1) {
            return "On track with planned growth";
        }

        return "Exceeding growth expectations";
    }
}