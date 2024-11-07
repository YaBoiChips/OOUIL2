package yaboichips.oOUIL2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import yaboichips.oOUIL2.Keys;

import static org.bukkit.Bukkit.getServer;

public class SetLivesCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player is an OP
        if (!player.isOp()) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        // Check if the command has the correct number of arguments
        if (args.length != 2) {
            player.sendMessage("Usage: /setlives <player> <number>");
            return true;
        }

        // Get the target player and number of lives
        Player targetPlayer = getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage("Player not found.");
            return true;
        }

        int lives;
        try {
            lives = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("The number of lives must be an integer.");
            return true;
        }

        // Set the metadata for the target player
        targetPlayer.getPersistentDataContainer().set(Keys.LIVES, PersistentDataType.INTEGER, lives);
        player.sendMessage("Set " + targetPlayer.getName() + "'s lives to " + lives);

        return true;
    }
}
