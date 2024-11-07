package yaboichips.oOUIL2.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.*;
import static yaboichips.oOUIL2.roles.Role.JESTER;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>(Role.roles);
        if (completions.contains(strings[1])) {

            for (Player player : commandSender.getServer().getOnlinePlayers()) {
                if (getRole(player) == Role.JURY) {
                    player.sendMessage(commandSender.getName() + " voted for " + strings[0] + " as the " + strings[1]);
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                }
            }
            Player sender = (Player) commandSender;
            Player voted = commandSender.getServer().getPlayer(strings[0]);
            String votedRole = Role.getRoleByName(strings[1]);
            String playerRole = getRole(voted);

            if (getVotes(sender) > 0) {
                setVotes(sender, getVotes(sender) - 1);
                if (votedRole != Role.MAYOR) {
                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 2);
                sender.sendMessage("You Voted!");
                    if (votedRole == playerRole) {
                        setVoteCount(voted, getVoteCount(voted) + 1);
                        System.out.println(sender.getName() + " voted for " + voted.getName() + " and was correct, they have "+ getVoteCount(voted) + " votes against them");
                        if (getVoteCount(voted) > 2) {
                            System.out.println(voted.getName() + " was voted out");
                            voted.setHealth(0);
                            setVoteCount(voted, 0);
                        }
                    } else if (getRole(voted) == JESTER) {
                        if (votedRole != JESTER) {
                            if (getUses(voted) > 0) {
                                setUses(voted, getUses(voted) - 1);
                                voted.sendMessage("You got a vote!");
                                voted.playSound(voted, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3);
                            }
                        }
                    }
                } else {
                    commandSender.sendMessage("You cannot vote for the mayor!");

                }
            } else {
                commandSender.sendMessage("You are out of votes for today!");
            }

        } else {
            commandSender.sendMessage("/vote <player>");
        }
        return true;
    }

    public static class VoteTabCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
            if (args.length == 2) {
                return new ArrayList<>(Role.roles);
            }
            if (args.length == 1) {
                return null;
            }
            return Collections.emptyList();
        }
    }
}

