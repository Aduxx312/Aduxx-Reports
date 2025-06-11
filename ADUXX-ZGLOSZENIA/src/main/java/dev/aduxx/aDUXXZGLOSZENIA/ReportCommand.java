package dev.aduxx.aDUXXZGLOSZENIA;



import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Main.getInstance().getConfig().getString("usage").replace("&", "§"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Main.getInstance().getConfig().getString("not-found-player")
                    .replace("&", "§")
                    .replace("[PLAYER]", args[0]));
            return true;
        }

        if (ReportManager.hasReported(player.getName(), target.getName())) {
            player.sendMessage(Main.getInstance().getConfig().getString("same-report")
                    .replace("&", "§"));
            return true;
        }

        ReportGui.openReportGUI(player, target.getName());
        return true;
    }
}
