package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.List;

public class EndCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : onlinePlayers) {
            player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1, 1f);
            player.sendTitle("Congratulations", "one session closer...", 20,10,20);
            if (OOUIL2.getRole(player) == Role.LIAR) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else if (usedRoleObjective.getScore(player.getName()).getScore() > 0) {
                    player.setHealth(0);
                    for (Player playerz : onlinePlayers) {
                        if (OOUIL2.getRole(playerz) == Role.ACCOMPLICE) {
                            playerz.setHealth(0);
                        }
                    }
                }
            } else if (OOUIL2.getRole(player) == Role.ASSASSIN) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else if (usedRoleObjective.getScore(player.getName()).getScore() > 0) {
                    player.setHealth(0);
                }
            } else if (OOUIL2.getRole(player) == Role.JESTER) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else if (usedRoleObjective.getScore(player.getName()).getScore() > 0) {
                    player.setHealth(0);
                }
            } else if (OOUIL2.getRole(player) == Role.ESPUR) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else if (usedRoleObjective.getScore(player.getName()).getScore() > 0) {
                    player.setHealth(0);
                }
            }
        }
        return true;
    }
}
