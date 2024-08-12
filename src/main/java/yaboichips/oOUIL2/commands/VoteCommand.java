package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.getRole;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Player player : commandSender.getServer().getOnlinePlayers()){
            if (getRole(player) == Role.JURY){
                player.sendMessage(commandSender.getName() + " voted for " + strings[0] + " as the " + strings[1]);
            }
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Player voted = commandSender.getServer().getPlayer(strings[0]);
        Role votedRole = Role.getRoleByName(strings[1]);
        Role playerRole = getRole(voted);
        Objective voteObjective = scoreboard.getObjective("votes");
        if (voteObjective == null) {
            voteObjective = scoreboard.registerNewObjective("votes", "dummy", "Votes");
        } else {
            Score votes = voteObjective.getScore(commandSender.getName());
            if (votes.getScore() > 0) {
                votes.setScore(votes.getScore() - 1);
                if (votedRole == playerRole) {
                    voted.damage(50);
                }
            } else {
                commandSender.sendMessage("You are out of votes for today!");
            }
        }
        return true;
    }

    public static class VoteTabCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
            final List<String> completions = new ArrayList<>();
            if (args.length == 2) {
                Role.roles.forEach(role -> completions.add(role.getName()));
                return completions;
            }
            if (args.length == 1) {
                return null;
            }
            return Collections.emptyList();
        }
    }
}

