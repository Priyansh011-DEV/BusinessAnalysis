package BusinessAnalysis.system.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Qualifier("geminiService")
public class GeminiService {
}
