package BusinessAnalysis.system.model;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class KPIData {
    private long revenueTarget;
    private int teamSize;
    private long achievedRevenue;
    private int periodMonths = 3;
    public long getEffectiveRevenue() {
        if (revenueTarget > 0) return revenueTarget;
        return achievedRevenue;
    }
}
