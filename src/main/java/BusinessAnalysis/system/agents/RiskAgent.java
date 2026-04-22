package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class RiskAgent {
    public String evaluateRisk(KPIData data, boolean marketHot, int gap, double performance) {

        if (performance < 0.6) {
            return "High execution risk due to major performance gap";
        }

        if (gap > 0 && marketHot) {
            return "Risk of under-hiring in high-demand market";
        }

        if (gap > 0) {
            return "Capacity risk due to insufficient team size";
        }

        return "Low operational risk";
    }
}
