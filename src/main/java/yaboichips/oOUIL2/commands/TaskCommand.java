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

import java.util.ArrayList;
import java.util.List;

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

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.getObjective("usedrole");

        if (objective == null) {
            player.sendMessage("The scoreboard 'usedrole' does not exist.");
            return true;
        }

        Score score = objective.getScore(player.getName());
        int playerScore = score.getScore();
        if (args[0].equalsIgnoreCase("status")) {
            if (playerScore == 0) {
                player.sendMessage("Complete");
            } else {
                player.sendMessage("Incomplete");
            }
        } else if (args[0].equalsIgnoreCase("override")) {
            if (score.getScore() > 0) {
                score.setScore(0);
            } else {
                score.setScore(1);
            }
            if (playerScore == 0) {
                player.sendMessage("Your task status has been set to Incomplete");
            } else {
                player.sendMessage("Your task status has been set to Complete");
            }
        } else {
            player.sendMessage("Invalid argument. Usage: /task <status|override>");
        }

        return true;
    }
}
