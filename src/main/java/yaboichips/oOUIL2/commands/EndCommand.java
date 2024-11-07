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

public class EndCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : onlinePlayers) {
            player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1, 1f);
            player.sendTitle("Congratulations", "one session closer...", 20, 10, 20);
            if (OOUIL2.getRole(player) == Role.LIAR) {
                if (!getComplete(player)) {
                    for (Player playerz : onlinePlayers) {
                        if (OOUIL2.getRole(playerz) == Role.ACCOMPLICE) {
                            playerz.setHealth(0);
                        }
                    }
                    player.setHealth(0);
                }
            }
            if (OOUIL2.getRole(player) == Role.ASSASSIN) {
                if (!getComplete(player)) {
                    player.setHealth(0);
                }
            }
            if (OOUIL2.getRole(player) == Role.JESTER) {
                if (getUses(player) > 0) {
                    player.setHealth(0);
                }
            }
            if (OOUIL2.getRole(player) == Role.ESPUR) {
                if (!getComplete(player)) {
                    player.setHealth(0);
                }
            }
            if (OOUIL2.getRole(player) == Role.SERIAL_KILLER) {
                if (getUses(player) > 0) {
                    player.setHealth(0);
                }
            }
            if (OOUIL2.getRole(player) == Role.MAYOR) {
                MAYOR_TEAM.removeEntry(player.getName());
            }
            setRole(player, Role.TESTIFICATE);
        }
        unregister();
        return true;
    }

    public static void unregister() {
        StartCommand.target = null;
        StartCommand.guarded = null;
        TrackCommand.trackUsage.clear();
    }
}

