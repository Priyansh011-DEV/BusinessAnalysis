package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("prod")
public class GeminiService implements AIService {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    @Value("${GEMINI_API_KEY}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

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
                - Keep response between 4–6 lines
                - Be sharp, analytical, and direct
                
                OUTPUT FORMAT:
                A concise business explanation combining situation, risk reasoning, and action steps.
            """.formatted(plan, risk, recommendation);

        String url = apiUrl + "?key=" + apiKey;

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> request = new HashMap<>();
        request.put("contents", List.of(content));

        try {
            Map response = restTemplate.postForObject(url, request, Map.class);

            List candidates = (List) response.get("candidates");
            Map first = (Map) candidates.get(0);
            Map contentMap = (Map) first.get("content");
            List parts = (List) contentMap.get("parts");

            return ((Map) parts.get(0)).get("text").toString().trim();

        } catch (Exception e) {
            //  production fallback
            return "refer to risk and recommendation for immediate actions";
        }
    }
}
