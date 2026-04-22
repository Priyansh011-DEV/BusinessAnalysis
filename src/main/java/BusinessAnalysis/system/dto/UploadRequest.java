package BusinessAnalysis.system.dto;

import lombok.*;

@Data
public class UploadRequest {
    private long revenueTarget;
    private int teamSize;
    private long revenueAchieved;
}
