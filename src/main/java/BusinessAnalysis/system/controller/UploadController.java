package BusinessAnalysis.system.controller;

import BusinessAnalysis.system.dto.AnalysisResponse;
import BusinessAnalysis.system.service.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apiv2")
public class UploadController {
    @Autowired
    private IngestionService ingestionService;



    @PostMapping("/upload/pdf")
    public AnalysisResponse uploadPDF(@RequestParam("file") MultipartFile file) {
        return ingestionService.processPDF(file);
    }
    @PostMapping("/compare")
    public AnalysisResponse compare(
            @RequestParam("past") MultipartFile pastPdf,
            @RequestParam("target") MultipartFile targetPdf
    ) {
        return ingestionService.processComparison(pastPdf, targetPdf);
    }
}
