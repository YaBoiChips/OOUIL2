package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.getRole;
import static yaboichips.oOUIL2.roles.Role.JESTER;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>();
        Role.roles.forEach(role -> completions.add(role.getName()));
        if (completions.contains(strings[1])) {

        for (Player player : commandSender.getServer().getOnlinePlayers()) {
            if (getRole(player) == Role.JURY) {
                player.sendMessage(commandSender.getName() + " voted for " + strings[0] + " as the " + strings[1]);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
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
                        Objective voteCountObjective = scoreboard.getObjective("votecount");
                        if (voteCountObjective == null) {
                            voteCountObjective = scoreboard.registerNewObjective("votecount", "dummy", "VoteCount");
                        } else {
                            Score voteCount = voteCountObjective.getScore(voted);
                            voteCount.setScore(voteCount.getScore() + 1);
                            if (commandSender instanceof Player player) {
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 2);
                                player.sendMessage("vote made");
                            }
                        }
                        if (voteCountObjective.getScore(voted.getName()).getScore() > 2) {
                            voted.setHealth(0);
                            voteCountObjective.getScore(voted.getName()).setScore(0);
                        }
                    } else if (getRole(voted) == JESTER) {
                        Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                        if (usedRoleObjective == null) {
                            usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                        } else {
                            if (votedRole != JESTER) {
                                Score used = usedRoleObjective.getScore(voted.getName());
                                if (used.getScore() > 0) {
                                    used.setScore(used.getScore() - 1);
                                    voted.sendMessage("You got a vote!");
                                    voted.playSound(voted, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3);
                                }
                            }
                        }
                    }
                } else {
                    commandSender.sendMessage("You are out of votes for today!");
                }
            }
        } else {
            return false;
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

