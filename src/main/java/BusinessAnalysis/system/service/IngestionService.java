package BusinessAnalysis.system.service;

import BusinessAnalysis.system.ai.AIService;
import BusinessAnalysis.system.dto.AnalysisResponse;
import BusinessAnalysis.system.engine.DecisionEngine;
import BusinessAnalysis.system.model.KPIData;
import BusinessAnalysis.system.model.Result;
import BusinessAnalysis.system.util.KPIextractionService;
import BusinessAnalysis.system.util.PDFParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IngestionService {

    @Autowired
    private PDFParser pdfParser;

    @Autowired
    private KPIextractionService extractor;

    @Autowired
    private DecisionEngine decisionEngine;

    @Autowired
    private AIService aiService;

    // 🔹 Existing (single PDF)
    public AnalysisResponse processPDF(MultipartFile file) {

        String text = pdfParser.extractText(file);
        KPIData kpi = extractor.extract(text);

        Result result = decisionEngine.run(kpi);

        String explanation = aiService.generateExplanation(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation()
        );

        return new AnalysisResponse(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation(),
                explanation
        );
    }

    // 🔥 NEW: Comparison (Past vs Target)
    public AnalysisResponse processComparison(MultipartFile pastFile, MultipartFile targetFile) {

        // 🔹 Extract text
        String pastText = pdfParser.extractText(pastFile);
        String targetText = pdfParser.extractText(targetFile);

        // 🔹 Extract KPIs separately
        KPIData pastData = extractor.extract(pastText);
        KPIData targetData = extractor.extract(targetText);

        // 🔥 Merge into ONE dataset
        KPIData finalData = KPIData.builder()
                .achievedRevenue(pastData.getAchievedRevenue())
                .teamSize(pastData.getTeamSize())
                .revenueTarget(targetData.getRevenueTarget())
                .build();

        // Debug (optional but useful)
        System.out.println("=== COMPARISON DATA ===");
        System.out.println("Achieved: " + finalData.getAchievedRevenue());
        System.out.println("Target: " + finalData.getRevenueTarget());
        System.out.println("Team: " + finalData.getTeamSize());

        // 🔹 Run decision engine
        Result result = decisionEngine.run(finalData);

        // 🔹 AI explanation
        String explanation = aiService.generateExplanation(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation()
        );

        return new AnalysisResponse(
                result.getInsight(),
                result.getRisk(),
                result.getRecommendation(),
                explanation
        );
    }
}