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


}
