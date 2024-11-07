package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.*;

public class GiftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player sender) {
            if (getUses(sender) == 1) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (OOUIL2.getRole(sender) == Role.ANGEL) {
                    if (commandSender.getServer().getPlayer(strings[0]) != commandSender) {
                        Player saved = commandSender.getServer().getPlayer(strings[0]);
                        for (Player player : players) {
                            if (player == saved) {
                                commandSender.getServer().dispatchCommand(commandSender.getServer().getConsoleSender(), "gamemode survival " + saved.getName());
                                setLives(saved, getLives(saved) + 1);
                                saved.sendTitle("You have been Gifted a Life by The Angel", "Say Thanks :)", 30, 40, 30);
                                saved.playSound(saved, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                setUses(sender, 0);
                            }
                        }
                    } else {
                        commandSender.sendMessage("You cannot gift to yourself");
                    }
                } else {
                    commandSender.sendMessage("You are NOT The Angel");
                }
            } else {
                commandSender.sendMessage("You can only gift once");
            }
        }
        return true;
    }
}
