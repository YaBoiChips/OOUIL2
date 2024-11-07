package yaboichips.oOUIL2.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import yaboichips.oOUIL2.Keys;

public class CheckLivesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.getPersistentDataContainer().has(Keys.LIVES)) {
                int lives = player.getPersistentDataContainer().get(Keys.LIVES, PersistentDataType.INTEGER);
                displayActionBar(player, ChatColor.RED + "Lives: " + lives);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);
            }
        }
        return true;
    }

    private void displayActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)); // Spigot API method to send action bar message
    }
}
