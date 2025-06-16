package dev.aduxx.aDUXXZGLOSZENIA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReportManager {


    private static final Map<String, Set<String>> reports = new HashMap<>();


    private static final Map<String, String> reportedBy = new HashMap<>();

    public static boolean hasReported(String reporter, String reported) {
        reporter = reporter.toLowerCase();
        reported = reported.toLowerCase();
        return reports.containsKey(reporter) && reports.get(reporter).contains(reported);
    }

    public static void addReport(String reporter, String reported, String reason) {
        String reporterLower = reporter.toLowerCase();
        String reportedLower = reported.toLowerCase();

        reports.computeIfAbsent(reporterLower, k -> new HashSet<>()).add(reportedLower);
        reportedBy.put(reportedLower, reporter);
    }

    public static String getReporter(String reported) {
        if (reported == null) return null;
        return reportedBy.get(reported.toLowerCase());
    }

    public static void removeReport(String reported) {
        if (reported == null) return;
        reportedBy.remove(reported.toLowerCase());
    }

    public static void removeAllReportsBy(String reporter) {
        if (reporter == null) return;
        reports.remove(reporter.toLowerCase());
    }
}
