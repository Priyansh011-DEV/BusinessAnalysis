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
    public long getEffectiveRevenue() {
        if (revenueTarget > 0) return revenueTarget;
        return achievedRevenue;
    }
}
