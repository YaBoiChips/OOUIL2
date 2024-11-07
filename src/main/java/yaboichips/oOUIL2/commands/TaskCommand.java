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
import org.bukkit.scoreboard.ScoreboardManager;
import yaboichips.oOUIL2.OOUIL2;

import java.util.ArrayList;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.setComplete;
import static yaboichips.oOUIL2.OOUIL2.setUses;

public class TaskCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /task <status|override>");
            return true;
        }


        boolean playerScore = OOUIL2.getComplete(player);
        if (args[0].equalsIgnoreCase("status")) {
            if (playerScore) {
                player.sendMessage("Complete");
            } else {
                player.sendMessage("Incomplete");
            }
        } else if (args[0].equalsIgnoreCase("override")) {
            setComplete(player, !playerScore);
            if (!OOUIL2.getComplete(player)) {
                player.sendMessage("Your task status has been set to Incomplete");
            } else {
                player.sendMessage("Your task status has been set to Complete");
            }
        } else {
            player.sendMessage("Invalid argument. Usage: /task <status | override>");
        }

        return true;
    }
}
