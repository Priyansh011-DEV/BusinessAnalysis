package BusinessAnalysis.system.engine;

import BusinessAnalysis.system.agents.MarketAgent;
import BusinessAnalysis.system.agents.PlannerAgent;
import BusinessAnalysis.system.agents.RecommendationAgent;
import BusinessAnalysis.system.agents.RiskAgent;
import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.model.Result;
import BusinessAnalysis.system.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecisionEngine {

    @Autowired
    private PlannerAgent plannerAgent;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private RiskAgent riskAgent;

    @Autowired
    private RecommendationAgent recommendationAgent;

    public Result run(KPIData data, String extractedText) {

        // 🔹 Core metrics (KEEP — this is your real engine)
        double productivity = metricsService.productivityPerEmployee(data);
        int requiredTeam = metricsService.requiredTeam(data);
        int gap = metricsService.teamGap(data);
        double performance = metricsService.performanceRatio(data);

        // 🔹 Agent decisions (now lexicon-enhanced)
        String plan = plannerAgent.plan(data, performance, extractedText);
        String risk = riskAgent.evaluateRisk(data, gap, performance, extractedText);
        String recommendation = recommendationAgent.recommend(plan, gap, performance, extractedText);

        // 🔹 Quantitative insight (this is VERY strong — keep it)
        String quantifiedInsight =
                plan +
                        " | Performance: " + String.format("%.2f", performance * 100) + "%" +
                        " | Productivity per employee: " + (long) productivity +
                        " | Required team: " + requiredTeam +
                        " | Current team: " + data.getTeamSize() +
                        " | Gap: " + gap;

        return Result.builder()
                .insight(quantifiedInsight)
                .risk(risk)
                .recommendation(recommendation)
                .build();
    }
}