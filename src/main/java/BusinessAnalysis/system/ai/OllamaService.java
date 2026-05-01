package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Profile("prod")
public class OllamaService implements AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateExplanation(String plan, String risk, String recommendation) {

        //  Extract numbers from plan string (since you embedded them there)
        String prompt = """
                You are a senior business consultant analyzing performance gaps between past results and future targets.
                
                    DATA PROVIDED:
                    - Situation: %s
                    - Risk: %s
                    - Recommendation: %s
                
                    INSTRUCTIONS:
                    1. Analyze the performance gap using available numerical indicators
                    2. Explain WHY the risk exists using performance, capacity, or execution gaps
                    3. Justify the recommendation logically
                    4. Use evidence from the data (not generic statements)
                
                    STRICT RULES:
                    - MUST reference numbers if available (performance, gap, productivity)
                    - If numbers are missing, infer logically from context
                    - NO generic advice
                    - Sound like a real consultant
                    - Keep response 4–6 lines
                    - Be precise and analytical
                
                    OUTPUT:
                    A concise, data-driven business explanation combining situation, risk reasoning, and action steps.
            """.formatted(plan, risk, recommendation);

        String url = "http://localhost:11434/api/generate";

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3");
        body.put("prompt", prompt);
        body.put("stream", false);

        Map response = restTemplate.postForObject(url, body, Map.class);

        if (response != null && response.get("response") != null) {
            return response.get("response").toString().trim();
        }

        return "AI response unavailable. Using fallback insight.";
    }
}