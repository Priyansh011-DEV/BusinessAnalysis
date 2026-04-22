package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class PlannerAgent {

    public String plan(KPIData data) {

        if (data.getEffectiveRevenue() > 200000000 && data.getTeamSize() < 30) {
            return "Aggressive growth plan required with team expansion";
        }
        if (data.getEffectiveRevenue() > 200000000) {
            return "Aggressive growth plan";
        }

        return "Moderate growth plan";
    }
}