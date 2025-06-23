package reports;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReportStorage {

    private static final Map<String, Integer> reportCounts = new HashMap<>();
    private static final Map<String, String> lastReasons = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    public static void setup(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "reports.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Nie udało się utworzyć reports.yml!");
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadData();
    }

    private static void loadData() {
        reportCounts.clear();
        lastReasons.clear();

        if (config.isConfigurationSection("reports")) {
            for (String key : config.getConfigurationSection("reports").getKeys(false)) {
                int count = config.getInt("reports." + key + ".count", 0);
                String reason = config.getString("reports." + key + ".lastReason", null);
                reportCounts.put(key.toLowerCase(), count);
                if (reason != null) {
                    lastReasons.put(key.toLowerCase(), reason);
                }
            }
        }
    }

    public static void saveData() {
        for (String key : reportCounts.keySet()) {
            config.set("reports." + key + ".count", reportCounts.get(key));
            config.set("reports." + key + ".lastReason", lastReasons.getOrDefault(key, ""));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getCount(String player) {
        return reportCounts.getOrDefault(player.toLowerCase(), 0);
    }

    public static String getLastReason(String player) {
        return lastReasons.get(player.toLowerCase());
    }

    public static void set(String player, int count, String reason) {
        String key = player.toLowerCase();
        reportCounts.put(key, count);
        lastReasons.put(key, reason);
    }
}
