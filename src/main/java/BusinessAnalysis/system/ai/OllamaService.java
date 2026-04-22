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

        // 🔥 Extract numbers from plan string (since you embedded them there)
        String prompt = """
            You are a senior business consultant.

            Business Analysis Data:
            %s

            Risk:
            %s

            Recommendation:
            %s

            Tasks:
            1. Explain the situation using the numerical data provided
            2. Highlight why the risk is critical based on performance and capacity gap
            3. Suggest clear, actionable business steps

            Rules:
            - MUST use numbers (performance, team gap, productivity)
            - Be concise (4-6 lines)
            - Sound like a real consultant (not generic)

            Output:
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