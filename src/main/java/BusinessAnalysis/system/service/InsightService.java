package BusinessAnalysis.system.service;

import BusinessAnalysis.system.ai.AIService;
import BusinessAnalysis.system.dto.AnalysisResponse;
import BusinessAnalysis.system.dto.UploadRequest;
import BusinessAnalysis.system.engine.DecisionEngine;
import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsightService {
    @Autowired
    private DecisionEngine decisionEngine;

    @Autowired
    private AIService aiService;

    public AnalysisResponse generateInsights(UploadRequest request) {

        // 🔹 Build KPIData manually (since no parser here)
        KPIData kpi = KPIData.builder()
                .revenueTarget(request.getRevenueTarget())
                .achievedRevenue(request.getRevenueAchieved())
                .teamSize(request.getTeamSize())
                .build();

        // 🔥 NO TEXT → pass empty string
        Result result = decisionEngine.run(kpi, "");

        String explanation;

        try {
            explanation = aiService.generateExplanation(
                    result.getInsight(),
                    result.getRisk(),
                    result.getRecommendation()
            );

            if (explanation == null || explanation.isBlank()) {
                explanation = fallback(result);
            }

        } catch (Exception e) {
            explanation = fallback(result);
        }

        return new AnalysisResponse(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation(),
                explanation
        );
    }

    private String fallback(Result result) {
        return "Analysis Summary: " +
                result.getInsight() +
                ". Risk identified: " +
                result.getRisk() +
                ". Recommended action: " +
                result.getRecommendation() + ".";
    }
}
