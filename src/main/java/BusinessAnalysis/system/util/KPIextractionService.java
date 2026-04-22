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

        KPIData data = KPIData.builder()
                .revenueTarget(targetRevenue)
                .achievedRevenue(achievedRevenue)
                .teamSize(teamSize)
                .build();

        // Debug logs
        System.out.println("Target Revenue: " + targetRevenue);
        System.out.println("Achieved Revenue: " + achievedRevenue);
        System.out.println("Team Size: " + teamSize);

        return data;
    }

    // 🔹 TARGET revenue (if explicitly mentioned)
    private long extractTargetRevenue(String text) {
        Pattern pattern = Pattern.compile(
                "target[^\\d$]*\\$?([0-9,.]+)([MK]|million|k|cr)?",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return parseValue(matcher.group(1), matcher.group(2));
        }

        return 0;
    }

    // 🔹 ACHIEVED revenue (find best candidate like total/Q1 total)
    private long extractAchievedRevenue(String text) {

        Pattern pattern = Pattern.compile(
                "(total revenue|q1 total)[^\\d$]*\\$?([0-9,.]+)([MK]|million|k|cr)?",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        long best = 0;

        while (matcher.find()) {
            long value = parseValue(matcher.group(2), matcher.group(3));
            if (value > best) {
                best = value; // pick highest relevant value
            }
        }

        return best;
    }

    // 🔹 TEAM SIZE (prefer summary, not department totals)
    private int extractTeamSize(String text) {

        // Try summary first
        Pattern pattern = Pattern.compile(
                "team size[^\\d]*(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        // fallback → total row
        Pattern fallback = Pattern.compile(
                "total[^\\d]*(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher fallbackMatcher = fallback.matcher(text);

        if (fallbackMatcher.find()) {
            return Integer.parseInt(fallbackMatcher.group(1));
        }

        return 0;
    }

    // 🔥 CORE: handles M / K / Cr / raw numbers
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