package yaboichips.oOUIL2.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yaboichips.oOUIL2.roles.Role;

import java.util.HashMap;
import java.util.UUID;

import static yaboichips.oOUIL2.OOUIL2.getRole;

public class RecallCommand implements CommandExecutor {

    // Store player UUIDs and their recall locations
    private final HashMap<UUID, Location> recallLocations = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TIME = 10 * 60 * 1000; // 10 minutes in milliseconds
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        if (getRole(player) == Role.WATCHER) {

            if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
                // Set the recall location to the player's current location
                recallLocations.put(playerUUID, player.getLocation());
                player.sendMessage("Recall location set!");
                return true;

            } else if (args.length == 0) {
                // Check if the player has a recall location set
                if (!recallLocations.containsKey(playerUUID)) {
                    player.sendMessage("You haven't set a recall location. Use /recall set to set one.");
                    return true;
                }
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(playerUUID)) {
                long lastUsed = cooldowns.get(playerUUID);
                long timeLeft = COOLDOWN_TIME - (currentTime - lastUsed);

                if (timeLeft > 0) {
                    long minutes = timeLeft / 60000;
                    long seconds = (timeLeft % 60000) / 1000;
                    player.sendMessage("You must wait " + minutes + " minutes and " + seconds + " seconds before using /recall again.");
                    return true;
                }
            }

            // Teleport the player to their recall location
            player.teleport(recallLocations.get(playerUUID));
            player.sendMessage("Teleported to your recall location!");

            // Set the cooldown
            cooldowns.put(playerUUID, currentTime);
            return true;
        }

            // If the command usage was incorrect
            player.sendMessage("Usage: /recall or /recall set");
        }
        player.sendMessage("you are not the watcher");
        return false;
    }
}
