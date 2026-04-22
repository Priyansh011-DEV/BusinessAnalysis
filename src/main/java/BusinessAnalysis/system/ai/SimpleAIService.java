package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!local & !prod")   // only loads if no other profile is active
@Qualifier("simpleAIService")
public class SimpleAIService implements AIService {
    @Override
    public String generateExplanation(String plan, String risk, String recommendation) {

        return "Based on your KPI targets, the system suggests: " + plan +
                ". Current market conditions indicate: " + risk +
                ". Therefore, recommended action is: " + recommendation + ".";
    }
}
