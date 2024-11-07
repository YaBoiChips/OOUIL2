package yaboichips.oOUIL2.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import yaboichips.oOUIL2.OOUIL2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static yaboichips.oOUIL2.OOUIL2.getRole;
import static yaboichips.oOUIL2.roles.Role.ASSASSIN;
import static yaboichips.oOUIL2.roles.Role.BODYGUARD;

public class TrackCommand implements CommandExecutor {

    private final OOUIL2 plugin;
    public static Map<UUID, Integer> trackUsage = new HashMap<>();

    public TrackCommand(OOUIL2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getRole(player) != BODYGUARD && getRole(player) != ASSASSIN) {
                player.sendMessage(ChatColor.RED + "You Cannot Use This Command!");
                return false;
            }
            Player target = null;
            if (getRole(player) == ASSASSIN) {
                target = StartCommand.target;
            }
            if (getRole(player) == BODYGUARD) {
                target = StartCommand.guarded;
            }
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "Player " + target  + " is not online.");
                return true;
            }

            // Check if the player has used the /track command more than 3 times
            UUID playerUUID = player.getUniqueId();
            int usageCount = trackUsage.getOrDefault(playerUUID, 0);

            if (usageCount >= 3) {
                player.sendMessage(ChatColor.RED + "You have used the /track command 3 times already.");
                return true;
            }

            // Increment the usage count
            trackUsage.put(playerUUID, usageCount + 1);

            // Send the target player's coordinates to the command sender
            player.sendMessage(ChatColor.GREEN + target.getName() + "'s location:");
            player.sendMessage(ChatColor.YELLOW + "X: " + target.getLocation().getBlockX());
            player.sendMessage(ChatColor.YELLOW + "Y: " + target.getLocation().getBlockY());
            player.sendMessage(ChatColor.YELLOW + "Z: " + target.getLocation().getBlockZ());

            // Make the target player glow only for the command sender for 20 seconds
            player.sendMessage(ChatColor.GREEN + "You are now tracking " + target.getName() + " for 20 seconds.");

            // Schedule to remove the glow effect after 20 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.removePotionEffect(PotionEffectType.GLOWING);
                }
            }.runTaskLater(plugin, 20 * 20);
        }
        return true;
    }
}

