package BusinessAnalysis.system.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {
    private String insight;
    private String risk;
    private String recommendation;
    private String explanation;
}
