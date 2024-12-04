package yaboichips.oOUIL2.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static yaboichips.oOUIL2.OOUIL2.setRole;

public class SetRoleCommand implements CommandExecutor {
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
            player.sendMessage("Usage: /setrole <player> <role>");
            return true;
        }

        // Get the target player and number of lives
        Player targetPlayer = getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage("Player not found.");
            return true;
        }

        // Set the metadata for the target player
        OOUIL2.setRole(targetPlayer, args[1]);
        sender.sendMessage(targetPlayer.getName() + " is now the " + args[1]);
        targetPlayer.getInventory().remove(Material.WRITTEN_BOOK);
        StartCommand.giveItems(targetPlayer);
        player.sendTitle("You are The " + args[1], "good luck :)", 40, 20, 40);
        player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);
        return true;
    }


    public static class SetRoleTabCompleter implements TabCompleter {

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
