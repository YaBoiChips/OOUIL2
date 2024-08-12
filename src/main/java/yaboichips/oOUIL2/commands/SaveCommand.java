package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.List;

public class SaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
        if (usedRoleObjective == null) {
            usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
        }
        if (usedRoleObjective.getScore(commandSender.getName()).getScore() == 1) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (OOUIL2.getRole(commandSender.getServer().getPlayer(commandSender.getName())) == Role.DOCTOR) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Objective savedObjective = scoreboard.getObjective("saved");
                if (savedObjective == null) {
                    savedObjective = scoreboard.registerNewObjective("saved", "dummy", "Saved");
                }
                Player saved = commandSender.getServer().getPlayer(strings[0]);
                for (Player player : players) {
                    if (player == saved) {
                        savedObjective.getScore(player.getName()).setScore(1);
                        saved.sendTitle("You have been SAVED by The Doctor", "Say Thanks :)", 30, 40, 30);
                        saved.playSound(saved, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1 ,1);
                        usedRoleObjective.getScore(commandSender.getName()).setScore(0);
                    }
                }
            } else {
                commandSender.sendMessage("You are NOT The Doctor");
            }
        } else {
            commandSender.sendMessage("You can only save once");
        }
        return true;
    }
}
