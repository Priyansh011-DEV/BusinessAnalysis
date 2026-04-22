package BusinessAnalysis.system.ai;

import org.springframework.context.annotation.Bean;

public interface AIService {
    String generateExplanation(String plan, String risk, String recommendation);
}
