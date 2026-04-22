package BusinessAnalysis.system.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private String insight;
    private String risk;
    private String recommendation;
}
