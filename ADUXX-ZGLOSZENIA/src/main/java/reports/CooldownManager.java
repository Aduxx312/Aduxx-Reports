package reports;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {


    private static final Map<String, Long> cooldowns = new HashMap<>();

    /**
     *
     *
     * @param playerName
     * @param cooldownSeconds
     * @return true
     */
    public static boolean hasCooldown(String playerName, int cooldownSeconds) {
        long currentTime = System.currentTimeMillis();

        if (!cooldowns.containsKey(playerName)) {
            return false;
        }

        long lastTime = cooldowns.get(playerName);
        long elapsed = currentTime - lastTime;

        return elapsed < (cooldownSeconds * 1000L);
    }

    /**
     *
     *
     * @param playerName
     */
    public static void setCooldown(String playerName) {
        cooldowns.put(playerName, System.currentTimeMillis());
    }

    /**
     *
     *
     * @param playerName
     * @param cooldownSeconds
     * @return true
     */
    public static long getRemaining(String playerName, int cooldownSeconds) {
        if (!cooldowns.containsKey(playerName)) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long lastTime = cooldowns.get(playerName);
        long remainingMillis = (cooldownSeconds * 1000L) - (currentTime - lastTime);

        return Math.max(0, remainingMillis / 1000);
    }

    /**
     *
     *
     * @param playerName
     */
    public static void resetCooldown(String playerName) {
        cooldowns.remove(playerName);
    }
}
