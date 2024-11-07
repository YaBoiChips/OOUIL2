package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yaboichips.oOUIL2.OOUIL2;

import static yaboichips.oOUIL2.OOUIL2.*;
import static yaboichips.oOUIL2.roles.Role.*;

public class ScanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (getUses(player) >= 1) {

                if (args.length != 1) {
                    sender.sendMessage("Usage: /scan <player>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    player.sendMessage("Player not found.");
                    return true;
                }
                if (OOUIL2.getRole(player) == DETECTIVE) {
                    // Check if the target player is within 30 blocks
                    if (player.getLocation().distance(target.getLocation()) <= 30) {
                        player.sendMessage("You have scanned " + target.getName() + " and they are The " + OOUIL2.getRole(target));
                        player.playSound(player, Sound.BLOCK_LEVER_CLICK, 1, 1.5f);
                        target.sendMessage("You are being scanned!");
                        target.playSound(target, Sound.ITEM_GOAT_HORN_SOUND_5, 1, 1);
                        setUses(player, getUses(player) - 1);

                    } else {
                        player.sendMessage("Player " + target.getName() + " is too far away (must be within 30 blocks).");
                    }
                } else {
                    if (OOUIL2.getRole(player) == JURY) {
                        if (player.getLocation().distance(target.getLocation()) <= 30) {
                            player.sendMessage("You have scanned " + target.getName() + " and they are " + goodOrBad(target));
                            player.playSound(player, Sound.BLOCK_LEVER_CLICK, 1, 1.5f);
                            target.sendMessage("You are being scanned!");
                            target.playSound(target, Sound.ITEM_GOAT_HORN_SOUND_5, 1, 1);
                            setUses(player, getUses(player) - 1);

                        } else {
                            player.sendMessage("Player " + target.getName() + " is too far away (must be within 30 blocks).");
                        }
                    }
                }
            } else {
                player.sendMessage("You are out of uses");
            }

        }
        return true;
    }
    public String goodOrBad(Player player){
        if (getRole(player).equals(ESPUR) || getRole(player).equals(LIAR) || getRole(player).equals(ASSASSIN) || getRole(player).equals(SERIAL_KILLER) || getRole(player).equals(JESTER) || getRole(player).equals(ACCOMPLICE)){
            return ChatColor.RED + "Guilty";
        }
        return ChatColor.GREEN + "Innocent";
    }
}
