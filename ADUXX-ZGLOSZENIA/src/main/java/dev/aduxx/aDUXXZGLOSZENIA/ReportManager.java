package dev.aduxx.aDUXXZGLOSZENIA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReportManager {
    private static final Map<String, Set<String>> reports = new HashMap<>();

    public static boolean hasReported(String reporter, String reported) {
        return reports.containsKey(reporter) && reports.get(reporter).contains(reported);
    }

    public static void addReport(String reporter, String reported, String reason) {
        reports.computeIfAbsent(reporter, k -> new HashSet<>()).add(reported);

    }
}