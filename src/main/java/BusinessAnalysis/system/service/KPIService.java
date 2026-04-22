package BusinessAnalysis.system.service;

import BusinessAnalysis.system.dto.UploadRequest;
import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

@Service
public class KPIService {

    public KPIData extractKPI(UploadRequest request) {
        return KPIData.builder()
                .revenueTarget(request.getRevenueTarget())
                .teamSize(request.getTeamSize())
                .build();
    }
}
