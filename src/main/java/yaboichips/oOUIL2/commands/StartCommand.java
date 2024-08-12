package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.getRole;
import static yaboichips.oOUIL2.roles.Role.*;

public class StartCommand implements CommandExecutor {

    public StartCommand() {
        super();
        init();
    }

    public static void assignPlayers(List<Player> players) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard scoreboard = manager.getMainScoreboard();
            Objective roleObjective = scoreboard.getObjective("role");
            Objective voteObjective = scoreboard.getObjective("votes");
            if (roleObjective == null) {
                roleObjective = scoreboard.registerNewObjective("role", "dummy", "Role");
            } else {
                List<Role> roleList = new ArrayList<>(roles);
                for (Player player : players) {
                    Collections.shuffle(roleList);
                    Score score = roleObjective.getScore(player.getName());
                    Role role = roleList.getFirst();
                    score.setScore(role.getValue());
                    player.sendMessage("You are The " + role.getName());
                    roleList.removeFirst();
                }
            }
            if (voteObjective == null) {
                voteObjective = scoreboard.registerNewObjective("votes", "dummy", "Votes");
            } else {
                for (Player player : players) {
                    Score votes = voteObjective.getScore(player.getName());
                    if (getRole(player) == MAYOR) {
                        votes.setScore(2);
                    } else if (getRole(player) == DETECTIVE) {
                        votes.setScore(0);
                    } else {
                        votes.setScore(1);
                    }
                }
            }
            Objective guardObjective = scoreboard.getObjective("guard");
            if (guardObjective == null) {
                guardObjective = scoreboard.registerNewObjective("guard", "dummy", "Guard");
            } else {
                for (Player player : players) {
                    guardObjective.getScore(player.getName()).setScore(0);
                }

                for (Player player : players) {
                    if (getRole(player) != BODYGUARD) {
                        guardObjective.getScore(player.getName()).setScore(1);
                        break;
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        assignPlayers(onlinePlayers);

        Objective usedRoleObjective = scoreboard.getObjective("usedrole");
        if (usedRoleObjective == null) {
            usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
        }
        for (Player player : onlinePlayers) {
            Score used = usedRoleObjective.getScore(player.getName());
            used.setScore(1);
            giveItems(player);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                sender.getServer().getOnlinePlayers().forEach(player -> {
                            player.sendTitle("Session is OVER", "make it back to spawn!" ,30, 50 ,30);
                            player.playSound(player, Sound.ITEM_TOTEM_USE, 1 ,1);
                        }
                );
            }
        }.runTaskLater(OOUIL2.getPlugin(OOUIL2.class), 60 * 60 * 20L);
        return true;
    }

    public void giveItems(Player player) {
        Role role = getRole(player);
        Server server = player.getServer();
        if (role == Role.MAYOR) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Mayor!\\\\n\\\\nYou get to vote TWICE! (We love democracy)\\\\n\\\\nDon\\\\\\'t let the power go to your head ;)\"]]'],title:\"The Mayor\",author:NOMAD}]");
        }
        if (role == ESPUR) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " iron_sword[custom_name='[\"\",{\"text\":\"Espur Blade\",\"italic\":false}]',lore=['[\"\",{\"text\":\"Use this blade to kill someone after 15 minuites\",\"italic\":false}]']]");
        }
    }
}
