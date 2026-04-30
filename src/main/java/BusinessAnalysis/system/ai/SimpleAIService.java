package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!local & !prod")   // only loads if no other profile is active
public class SimpleAIService implements AIService {
    @Override
    public String generateExplanation(String plan, String risk, String recommendation) {

        return """
                Business Analysis Summary:

                Current Situation:
                %s

                Risk Assessment:
                %s

                Recommended Action:
                %s

                Conclusion:
                Based on the observed performance and operational indicators, the organization should prioritize closing execution gaps and aligning resources with target expectations.
                """.formatted(plan, risk, recommendation);
    }
}
