package BusinessAnalysis.system.agents;

import BusinessAnalysis.system.model.TrendData;
import BusinessAnalysis.system.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketAgent {
    @Autowired
    private TrendService trendService;

    public boolean isMarketHot() {

        List<TrendData> trends = trendService.getAllTrends();

        return trends.stream()
                .anyMatch(t -> t.getDemandScore() > 0.8);
    }
}