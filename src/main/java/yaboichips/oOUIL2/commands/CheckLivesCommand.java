package yaboichips.oOUIL2.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class CheckLivesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective livesObjective = scoreboard.getObjective("lives");
        for (Player player : Bukkit.getOnlinePlayers()) {
            int lives = livesObjective.getScore(player.getName()).getScore();
            displayActionBar(player, ChatColor.RED + "Lives: " + lives);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);
        }
        return true;
    }

    private void displayActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)); // Spigot API method to send action bar message
    }
}
