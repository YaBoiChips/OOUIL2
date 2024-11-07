package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import yaboichips.oOUIL2.Keys;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.*;

public class SaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player sender) {
            if (getUses(sender) == 1) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (OOUIL2.getRole(sender) == Role.DOCTOR) {
                    Player saved = commandSender.getServer().getPlayer(strings[0]);
                    for (Player player : players) {
                        if (player == saved) {
                            setSaved(player, true);
                            saved.sendTitle("You have been SAVED by The Doctor", "Say Thanks :)", 30, 40, 30);
                            saved.playSound(saved, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1, 1);
                            setUses(sender, getUses(sender) - 1);
                        }
                    }
                } else {
                    commandSender.sendMessage("You are NOT The Doctor");
                }
            } else {
                commandSender.sendMessage("You can only save once");
            }
        }
        return true;
    }
}
