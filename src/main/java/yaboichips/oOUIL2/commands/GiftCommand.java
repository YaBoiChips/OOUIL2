package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.List;

public class GiftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
        if (usedRoleObjective == null) {
            usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
        }
        if (usedRoleObjective.getScore(commandSender.getName()).getScore() == 1) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (OOUIL2.getRole(commandSender.getServer().getPlayer(commandSender.getName())) == Role.ANGEL) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Objective livesObjective = scoreboard.getObjective("lives");
                if (livesObjective == null) {
                    livesObjective = scoreboard.registerNewObjective("lives", "dummy", "Lives");
                }
                if (commandSender.getServer().getPlayer(strings[0]) != commandSender) {
                    Player saved = commandSender.getServer().getPlayer(strings[0]);
                    for (Player player : players) {
                        if (player == saved) {
                            Score score = livesObjective.getScore(player.getName());
                            if (score.getScore() < 1){
                                commandSender.getServer().dispatchCommand(commandSender.getServer().getConsoleSender(), "gamemode survival " + saved.getName());
                            }
                            score.setScore(score.getScore() + 1);
                            saved.sendTitle("You have been Gifted a Life by The Angel", "Say Thanks :)", 30, 40, 30);
                            saved.playSound(saved, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            usedRoleObjective.getScore(commandSender.getName()).setScore(0);
                        }
                    }
                } else {
                    commandSender.sendMessage("You cannot gift to yourself");
                }
            } else {
                commandSender.sendMessage("You are NOT The Angel");
            }
        } else {
            commandSender.sendMessage("You can only gift once");
        }
        return true;
    }
}
