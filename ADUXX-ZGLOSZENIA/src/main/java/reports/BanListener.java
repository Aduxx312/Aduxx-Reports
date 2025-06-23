package reports;

import dev.aduxx.aDUXXZGLOSZENIA.Main;
import dev.aduxx.aDUXXZGLOSZENIA.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class BanListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        handleBanCommand(event.getMessage());
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        handleBanCommand(event.getCommand());
    }

    private void handleBanCommand(String commandRaw) {
        String command = commandRaw.toLowerCase();
        if (command.startsWith("/")) command = command.substring(1);


        if (!(command.startsWith("ban ") ||
                command.startsWith("banip ") ||
                command.startsWith("tempban ") ||
                command.startsWith("tempbanip "))) {
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length < 2) {
            return;
        }

        String bannedName = parts[1];

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            String reporterName = ReportManager.getReporter(bannedName);
            if (reporterName == null) {
                return;
            }

            Player reporter = Bukkit.getPlayerExact(reporterName);
            if (reporter == null || !reporter.isOnline()) {
                return;
            }

            String format = Main.getInstance().getConfig().getString("bossBarFormat", "&cGracz którego zgłosiłeś otrzymał bana!").replace("&", "§");
            String colorStr = Main.getInstance().getConfig().getString("bossBarColor", "RED");
            String styleStr = Main.getInstance().getConfig().getString("bossBarStyle", "SOLID");
            int durationSeconds = Main.getInstance().getConfig().getInt("bossBarDuration", 15);

            BarColor color;
            BarStyle style;
            try {
                color = BarColor.valueOf(colorStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                color = BarColor.RED;
            }

            try {
                style = BarStyle.valueOf(styleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                style = BarStyle.SOLID;
            }

            BossBar bossBar = Bukkit.createBossBar(format, color, style);
            bossBar.addPlayer(reporter);
            bossBar.setVisible(true);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                bossBar.removePlayer(reporter);
                bossBar.setVisible(false);
            }, durationSeconds * 20L);

            ReportManager.removeReport(bannedName);
        }, 20L);
    }
}
