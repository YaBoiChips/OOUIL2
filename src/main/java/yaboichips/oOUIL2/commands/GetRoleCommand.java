package yaboichips.oOUIL2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import yaboichips.oOUIL2.Keys;
import yaboichips.oOUIL2.OOUIL2;

import static org.bukkit.Bukkit.getServer;

public class GetRoleCommand implements CommandExecutor {
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
        if (args.length != 1) {
            player.sendMessage("Usage: /getrole <player>");
            return true;
        }

        // Get the target player and number of lives
        Player targetPlayer = getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage("Player not found.");
            return true;
        }

        // Set the metadata for the target player
        String role = OOUIL2.getRole(targetPlayer);
        sender.sendMessage(targetPlayer.getName() + " is the " + role);

        return true;
    }
}
