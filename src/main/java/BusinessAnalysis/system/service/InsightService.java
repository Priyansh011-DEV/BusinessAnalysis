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
    private KPIService kpiService;

    @Autowired
    private DecisionEngine decisionEngine;

    @Autowired
    private AIService aiService;

    public AnalysisResponse generateInsights(UploadRequest request) {

        KPIData kpi = kpiService.extractKPI(request);
        Result result = decisionEngine.run(kpi);

        String explanation;

        try {
            explanation = aiService.generateExplanation(
                    result.getInsight(),
                    result.getRisk(),
                    result.getRecommendation()
            );
        } catch (Exception e) {
            // fallback
            explanation = "Based on current analysis: " + result.getRecommendation();
        }

        return new AnalysisResponse(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation(),
                explanation
        );
}   }
