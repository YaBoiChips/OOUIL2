package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.OOUIL2;

import static yaboichips.oOUIL2.roles.Role.NURSE;

public class TransplantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (OOUIL2.getRole(player) == NURSE) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                }
                if (usedRoleObjective.getScore(sender.getName()).getScore() == 1) {
                    if (args.length != 2) {
                        sender.sendMessage("Usage: /transplant <player> <player>");
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[0]);
                    Player target2 = Bukkit.getPlayer(args[1]);

                    if (target == null || target2 == null) {
                        player.sendMessage("Player not found.");
                        return true;
                    }

                    // Check if the target player is within 30 blocks
                    if (player.getLocation().distance(target.getLocation()) <= 5) {
                        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                        Objective livesObjective = scoreboard.getObjective("lives");
                        Score targetLives = livesObjective.getScore(target.getName());
                        Score target2Lives = livesObjective.getScore(target2.getName());
                        if (targetLives.getScore() > 1) {
                            targetLives.setScore(targetLives.getScore() - 1);
                            target2Lives.setScore(target2Lives.getScore() + 1);
                            player.sendMessage("You took a life from " + target.getName() + " and gave it to " + target2.getName());
                            target.sendMessage("A life is being taken from you!");
                            target2.sendMessage("You have gained a life, say thanks to the nurse");
                            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1, 0.1f);
                            target.playSound(target, Sound.ITEM_GOAT_HORN_SOUND_2, 1, 1);
                            usedRoleObjective.getScore(sender.getName()).setScore(0);
                        }
                        else {
                            player.sendMessage(target.getName() + " doesn't have enough lives");
                        }
                    } else {
                        player.sendMessage("Player " + target.getName() + " is too far away (must be within 5 blocks).");
                    }
                } else {
                    sender.sendMessage("This command can only be used once.");
                }
            } else {
                sender.sendMessage("This command can only be used by The Nurse.");
            }
        }
        return true;
    }
}

