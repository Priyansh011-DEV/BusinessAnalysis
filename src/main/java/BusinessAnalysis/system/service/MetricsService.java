package BusinessAnalysis.system.service;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    // Revenue per employee
    public double productivityPerEmployee(KPIData data) {
        if (data.getTeamSize() == 0) return 0;

        return (double) data.getAchievedRevenue() / data.getTeamSize();
    }

    // Required team to hit target
    public int requiredTeam(KPIData data) {

        double productivity = productivityPerEmployee(data);

        if (productivity == 0) return data.getTeamSize();

        return (int) Math.ceil(data.getRevenueTarget() / productivity);
    }

    // Performance ratio (achieved vs target)
    public double performanceRatio(KPIData data) {
        if (data.getRevenueTarget() == 0) return 1;

        return (double) data.getAchievedRevenue() / data.getRevenueTarget();
    }

    // Team gap
    public int teamGap(KPIData data) {
        return requiredTeam(data) - data.getTeamSize();
    }
}