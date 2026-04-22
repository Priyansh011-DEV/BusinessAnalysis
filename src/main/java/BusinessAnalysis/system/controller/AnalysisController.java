package BusinessAnalysis.system.controller;


import BusinessAnalysis.system.dto.AnalysisResponse;
import BusinessAnalysis.system.dto.UploadRequest;
import BusinessAnalysis.system.service.InsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    @Autowired
    private InsightService insightService;

    @PostMapping("/analyze")
    public AnalysisResponse analyze(@RequestBody UploadRequest request) {
        return insightService.generateInsights(request);
    }
}
