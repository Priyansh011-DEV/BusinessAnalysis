package BusinessAnalysis.system.util;

import BusinessAnalysis.system.model.KPIData;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KPIextractionService {

    public KPIData extract(String text) {

        long targetRevenue = extractTargetRevenue(text);
        long achievedRevenue = extractAchievedRevenue(text);
        int teamSize = extractTeamSize(text);

        System.out.println("=== EXTRACTED DATA ===");
        System.out.println("Target Revenue: " + targetRevenue);
        System.out.println("Achieved Revenue: " + achievedRevenue);
        System.out.println("Team Size: " + teamSize);

        return KPIData.builder()
                .revenueTarget(targetRevenue)
                .achievedRevenue(achievedRevenue)
                .teamSize(teamSize)
                .build();
    }

    // 🔥 TARGET REVENUE (OKR)
    private long extractTargetRevenue(String text) {

        String[] patterns = {
                "scale revenue to",
                "target revenue",
                "revenue target",
                "goal revenue",
                "revenue goal",
                "forecast revenue",
                "expected revenue",
                "planned revenue",
                "arr target",
                "annual recurring revenue",
                "revenue objective",
                "objective.*revenue",
                "okr.*revenue"
        };

        for (String keyword : patterns) {
            Pattern pattern = Pattern.compile(
                    keyword + "[^\\d$]*\\$?([0-9,.]+)([MK]|million|k|cr)?",
                    Pattern.CASE_INSENSITIVE
            );

            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return parseValue(matcher.group(1), matcher.group(2));
            }
        }

        return 0;
    }

    // 🔥 ACHIEVED REVENUE (KPR)
    private long extractAchievedRevenue(String text) {

        String[] patterns = {
                "total revenue",
                "q1 total",
                "quarter revenue",
                "revenue achieved",
                "actual revenue",
                "reported revenue"
        };

        long best = 0;

        for (String keyword : patterns) {

            Pattern pattern = Pattern.compile(
                    keyword + "[^\\d$]*\\$?([0-9,.]+)([MK]|million|k|cr)?",
                    Pattern.CASE_INSENSITIVE
            );

            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                long value = parseValue(matcher.group(1), matcher.group(2));
                if (value > best) {
                    best = value;
                }
            }
        }

        return best;
    }

    // 🔥 TEAM SIZE (STRICT + SAFE)
    private int extractTeamSize(String text) {

        // 🔹 PRIORITY: summary section
        Pattern summaryPattern = Pattern.compile(
                "team size[^\\d]*(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher summaryMatcher = summaryPattern.matcher(text);

        if (summaryMatcher.find()) {
            return Integer.parseInt(summaryMatcher.group(1));
        }

        // 🔹 fallback: headcount keyword
        Pattern headcountPattern = Pattern.compile(
                "headcount[^\\d]*(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher headcountMatcher = headcountPattern.matcher(text);

        if (headcountMatcher.find()) {
            return Integer.parseInt(headcountMatcher.group(1));
        }

        // ❌ NO random fallback anymore
        return 0;
    }

    // 🔥 VALUE PARSER
    private long parseValue(String number, String unit) {

        double value = Double.parseDouble(number.replace(",", ""));

        if (unit != null) {
            unit = unit.toLowerCase();

            switch (unit) {
                case "m":
                case "million":
                    value *= 1_000_000;
                    break;

                case "k":
                    value *= 1_000;
                    break;

                case "cr":
                    value *= 10_000_000;
                    break;
            }
        }

        return (long) value;
    }
}