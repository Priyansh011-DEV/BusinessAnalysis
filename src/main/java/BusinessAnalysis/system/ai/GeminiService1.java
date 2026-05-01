package BusinessAnalysis.system.ai;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("local")
public class GeminiService1 implements AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // FIX 1: Inject RestTemplate as a Spring Bean instead of instantiating manually
    private final RestTemplate restTemplate;

    @Autowired
    public GeminiService1(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String generateExplanation(String plan, String risk, String recommendation) {

        String prompt = """
                You are a senior business consultant analyzing performance gaps between past results and future targets.
                
                DATA PROVIDED:
                - Situation: %s
                - Risk: %s
                - Recommendation: %s
                
                INSTRUCTIONS:
                1. Analyze the performance gap using numerical indicators present in the situation
                2. Explain WHY the risk exists using data (performance ratio, team gap, productivity)
                3. Justify the recommendation logically based on the gap
                4. Use evidence from the text (not generic statements)
                
                STRICT RULES:
                - MUST reference numbers (percentages, team gap, productivity)
                - MUST sound like a real consultant report
                - NO generic phrases like "improve performance"
                - Keep response between 3–5 lines
                - Be sharp, analytical, and direct
                - // Add this to your prompt string
                      "STRICT RULE: Output ONLY the final 3-5 line business explanation. Do not include drafts, reasoning, or labels like 'Draft 1' or 'Final Output'."
                
                OUTPUT FORMAT:
                A concise business explanation combining situation, risk reasoning, and action steps.
            """.formatted(plan, risk, recommendation);

        String urlWithKey = apiUrl + "?key=" + apiKey;

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        // FIX 2: Set Content-Type header explicitly
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            Map<?, ?> response = restTemplate.postForObject(urlWithKey, requestEntity, Map.class);

            // FIX 3: Null checks at every level before accessing indexes
            if (response == null) {
                System.err.println("❌ Gemini API Error: null response received");
                return "refer to risk and recommendation for immediate actions";
            }

            List<?> candidates = (List<?>) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                System.err.println("❌ Gemini API Error: no candidates in response");
                return "refer to risk and recommendation for immediate actions";
            }

            Map<?, ?> first = (Map<?, ?>) candidates.get(0);
            if (first == null) {
                System.err.println("❌ Gemini API Error: first candidate is null");
                return "refer to risk and recommendation for immediate actions";
            }

            Map<?, ?> contentMap = (Map<?, ?>) first.get("content");
            if (contentMap == null) {
                System.err.println("❌ Gemini API Error: content missing in candidate");
                return "refer to risk and recommendation for immediate actions";
            }

            List<?> parts = (List<?>) contentMap.get("parts");
            if (parts == null || parts.isEmpty()) {
                System.err.println("❌ Gemini API Error: parts missing in content");
                return "refer to risk and recommendation for immediate actions";
            }

            Object textObj = ((Map<?, ?>) parts.get(0)).get("text");
            if (textObj == null) {
                System.err.println("❌ Gemini API Error: text missing in part");
                return "refer to risk and recommendation for immediate actions";
            }

            return textObj.toString().trim();

        } catch (Exception e) {
            System.err.println("❌ Gemini API Error: " + e.getMessage());
            return "refer to risk and recommendation for immediate actions";
        }
    }
}