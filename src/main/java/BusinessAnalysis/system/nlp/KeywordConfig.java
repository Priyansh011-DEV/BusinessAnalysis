package BusinessAnalysis.system.nlp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Map;

@Component
public class KeywordConfig {
    private Map<String, Map<String, Integer>> config;

    @PostConstruct
    public void load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream is = getClass()
                .getResourceAsStream("/keyword-config.json");

        config = mapper.readValue(is,
                new TypeReference<>() {});
    }

    public Map<String, Integer> get(String category) {
        return config.get(category);
    }
}
