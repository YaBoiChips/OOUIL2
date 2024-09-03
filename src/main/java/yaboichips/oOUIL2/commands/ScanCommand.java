package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import yaboichips.oOUIL2.OOUIL2;

import static yaboichips.oOUIL2.roles.Role.DETECTIVE;
import static yaboichips.oOUIL2.roles.Role.SWAPPER;

public class ScanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (OOUIL2.getRole(player) == DETECTIVE) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                }
                if (usedRoleObjective.getScore(sender.getName()).getScore() == 1) {
                    if (args.length != 1) {
                        sender.sendMessage("Usage: /scan <player>");
                        return true;
                    }
                    
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target == null) {
                        player.sendMessage("Player not found.");
                        return true;
                    }

                    // Check if the target player is within 30 blocks
                    if (player.getLocation().distance(target.getLocation()) <= 30) {
                        player.sendMessage("You have scanned " + target.getName() + " and they are The " + OOUIL2.getRole(target).getName());
                        target.sendMessage("You are being scanned!");
                        target.playSound(target, Sound.ITEM_GOAT_HORN_SOUND_5, 1, 1);
                        usedRoleObjective.getScore(sender.getName()).setScore(0);

                    } else {
                        player.sendMessage("Player " + target.getName() + " is too far away (must be within 30 blocks).");
                    }
                } else {
                    sender.sendMessage("This command can only be used once.");
                }
            } else {
                sender.sendMessage("This command can only be used by The Detective.");
            }
        }
        return true;
    }
}
