package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

public class WatchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (OOUIL2.getRole(player) == Role.WATCHER) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                }
                if (usedRoleObjective.getScore(sender.getName()).getScore() == 1) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            watchPlayer(player, target);
                            usedRoleObjective.getScore(sender.getName()).setScore(0);
                            return true;
                        } else {
                            player.sendMessage("The specified player is not online.");
                        }
                    } else {
                        player.sendMessage("Usage: /watch <player>");
                    }
                } else {
                    sender.sendMessage("This command can only be used once.");
                }
                } else {
                    sender.sendMessage("This command can only be used by The Watcher.");
                }
            }

        return false;
    }

    private void watchPlayer(Player player, Player target) {
        Location originalLocation = player.getLocation();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target);
        player.playSound(player, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(originalLocation);
                player.setGameMode(GameMode.SURVIVAL);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage("You have been returned to your original location.");
            }
        }.runTaskLater(OOUIL2.getPlugin(OOUIL2.class), 15 * 20L); // 15 seconds * 20 ticks per second
    }
}
