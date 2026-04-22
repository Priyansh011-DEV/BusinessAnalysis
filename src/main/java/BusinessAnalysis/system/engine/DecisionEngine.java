package BusinessAnalysis.system.engine;

import BusinessAnalysis.system.agents.MarketAgent;
import BusinessAnalysis.system.agents.PlannerAgent;
import BusinessAnalysis.system.agents.RecommendationAgent;
import BusinessAnalysis.system.agents.RiskAgent;
import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.model.Result;
import BusinessAnalysis.system.model.TrendData;
import BusinessAnalysis.system.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecisionEngine {

    @Autowired
    private PlannerAgent plannerAgent;

    @Autowired
    private MarketAgent marketAgent;

    @Autowired
    private RiskAgent riskAgent;

    @Autowired
    private RecommendationAgent recommendationAgent;

    public Result run(KPIData data) {

        String plan = plannerAgent.plan(data);
        boolean marketHot = marketAgent.isMarketHot();
        String risk = riskAgent.evaluateRisk(data, marketHot);
        String recommendation = recommendationAgent.recommend(plan, marketHot, risk);

        return Result.builder()
                .insight(plan)
                .risk(risk)
                .recommendation(recommendation)
                .build();
    }
}
