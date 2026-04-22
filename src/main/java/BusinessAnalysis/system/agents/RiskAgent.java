package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class RiskAgent {
    public String evaluateRisk(KPIData data, boolean marketHot) {

        if (marketHot && data.getEffectiveRevenue() > 200000000 && data.getTeamSize() < 30) {
            return "High risk of missing market opportunity due to under-hiring";
        }

        if (!marketHot && data.getRevenueTarget() > 200000000) {
            return "Risk of over-expansion in a slow market";
        }

        return "Low operational risk";
    }
}
