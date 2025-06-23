package dev.aduxx.aDUXXZGLOSZENIA;

import reports.ReportStorage;

import java.util.*;

public class ReportManager {

    private static final Map<String, Set<String>> reports = new HashMap<>();
    private static final Map<String, String> reportedBy = new HashMap<>();


    private static final Map<String, Integer> reportCounts = new HashMap<>();
    private static final Map<String, String> lastReasons = new HashMap<>();


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


        int newCount = getReportCount(reported) + 1;
        reportCounts.put(reportedLower, newCount);


        lastReasons.put(reportedLower, reason);


        ReportStorage.set(reported, newCount, reason);
        ReportStorage.saveData();
    }


    public static String getReporter(String reported) {
        if (reported == null) return null;
        return reportedBy.get(reported.toLowerCase());
    }


    public static void removeReport(String reported) {
        if (reported == null) return;
        String key = reported.toLowerCase();
        reportedBy.remove(key);
        reportCounts.remove(key);
        lastReasons.remove(key);

        ReportStorage.set(key, 0, "");
        ReportStorage.saveData();
    }


    public static void removeAllReportsBy(String reporter) {
        if (reporter == null) return;
        reports.remove(reporter.toLowerCase());
    }


    public static int getReportCount(String player) {
        String key = player.toLowerCase();
        return reportCounts.containsKey(key) ? reportCounts.get(key) : ReportStorage.getCount(key);
    }


    public static String getLastReason(String player) {
        String key = player.toLowerCase();
        return lastReasons.containsKey(key) ? lastReasons.get(key) : ReportStorage.getLastReason(key);
    }
}
