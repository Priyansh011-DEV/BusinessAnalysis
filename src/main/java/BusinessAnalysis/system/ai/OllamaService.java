package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Profile("local")
@Qualifier("ollamaService")
public class OllamaService implements AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateExplanation(String plan, String risk, String recommendation) {

        // 🔥 Clean, dynamic, non-generic prompt
        String prompt = """
                You are a senior business consultant.

                Business Context:
                - Growth Strategy: %s
                - Identified Risk: %s

                Tasks:
                1. Explain what this situation means in a real business context
                2. Explain why this risk is critical
                3. Suggest actionable steps to address it

                Constraints:
                - Keep response concise (3-4 lines)
                - Use professional, executive-level tone with some simple understanding words.
                - Avoid generic phrases

                Output:
                """.formatted(plan, risk);

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