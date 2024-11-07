package yaboichips.oOUIL2.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import yaboichips.oOUIL2.Keys;
import yaboichips.oOUIL2.OOUIL2;
import yaboichips.oOUIL2.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static yaboichips.oOUIL2.OOUIL2.*;
import static yaboichips.oOUIL2.roles.Role.*;

public class StartCommand implements CommandExecutor {

    public static Player guarded;
    public static Player target;

    public StartCommand() {
        super();
        init();
    }

    public void assignPlayers(List<Player> players) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            List<String> roleList = new ArrayList<>(roles);
            for (Player player : players) {
                if (Boolean.TRUE.equals(player.getPersistentDataContainer().get(Keys.TARGET, PersistentDataType.BOOLEAN))) {
                    player.getPersistentDataContainer().set(Keys.TARGET, PersistentDataType.BOOLEAN, false);
                }
                if (Boolean.TRUE.equals(player.getPersistentDataContainer().get(Keys.GUARDED, PersistentDataType.BOOLEAN))) {
                    player.getPersistentDataContainer().set(Keys.GUARDED, PersistentDataType.BOOLEAN, false);
                }
                if (OOUIL2.getRole(player) == Role.MAYOR) {
                    MAYOR_TEAM.removeEntry(player.getName());
                }
                setComplete(player, false);
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (!roleList.isEmpty()) {
                        Collections.shuffle(roleList);
                        String role = roleList.getFirst();
                        setRole(player, role);
                        player.sendTitle("You are The " + role, "good luck :)", 40, 20, 40);
                        player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);
                        roleList.removeFirst();
                    }
                }
            }
            for (Player player : players) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (getRole(player).equals(MAYOR)) {
                        setVotes(player, 2);
                    } else if (getRole(player).equals(DETECTIVE)) {
                        setVotes(player, 0);
                    } else {
                        setVotes(player, 1);
                    }
                }
            }
            for (Player player : players) {
                Collections.shuffle(players);
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (!getRole(player).equals(ASSASSIN)) {
                        player.getPersistentDataContainer().set(Keys.TARGET, PersistentDataType.BOOLEAN, true);
                        target = player;
                        break;
                    }
                }
            }

            for (Player player : players) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    Collections.shuffle(players);
                    if (getRole(player) != BODYGUARD) {
                        setGuarded(player, true);
                        guarded = player;
                        break;
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EndCommand.unregister();

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        assignPlayers(onlinePlayers);


        for (Player player : onlinePlayers) {
            player.getInventory().remove(Material.WRITTEN_BOOK);
            if (getRole(player) == SWAPPER) {
                setUses(player, 3);
            } else if (getRole(player) == JESTER) {
                setUses(player, 3);
            } else if (getRole(player) == JURY) {
                setUses(player, 3);
            } else if (getRole(player) == SERIAL_KILLER) {
                setUses(player, 3);
            } else {
                setUses(player, 1);
            }
            giveItems(player);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                sender.getServer().getOnlinePlayers().forEach(player -> {
                            player.sendTitle("Session is OVER", "make it back to spawn!", 30, 50, 30);
                            player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);
                        }
                );
            }
        }.runTaskLater(OOUIL2.getPlugin(OOUIL2.class), 120 * 60 * 20L);
        return true;
    }

    public static void setPlayerNametagPurple(Player player) {
        MAYOR_TEAM.setColor(ChatColor.DARK_PURPLE); // Set the team's color to purple

        // Add the player to the team to apply the color to their nametag
        MAYOR_TEAM.addEntry(player.getName());
    }

    public void giveItems(Player player) {
        String role = getRole(player);
        Server server = player.getServer();
        boolean isLiarPresent = false;
        Player liar = null;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (getRole(onlinePlayer) == LIAR) {
                isLiarPresent = true;
                liar = onlinePlayer;
                break;
            }
        }
        if (role == Role.MAYOR) {
            setPlayerNametagPurple(player);
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Mayor!\\\\n\\\\nYou get to vote TWICE! (We love democracy)\\\\n\\\\nDon\\\\\\'t let the power go to your head ;)\"]]'],title:\"Role\",author:NOMAD}]");
        }
        if (role == ESPUR) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " iron_sword[custom_name='[\"\",{\"text\":\"Espur Blade\",\"italic\":false}]',lore=['[\"\",{\"text\":\"Use this blade to kill someone after 15 minuites\",\"italic\":false}]']]");
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Espur!\\\\n\\\\nYou MUST kill someone by the end of the session or YOU die,\\\\n\\\\nBut you\\\\\\'ve made off easier than the liar. The sword given to you will dissapear on hit and kill your target after 15 minutes\\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == LIAR) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Liar!\\\\n\\\\nYou MUST kill someone by the end of the session or YOU die\\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == ACCOMPLICE) {
            if (isLiarPresent) {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Accomplice!\\\\n\\\\nYou MUST ensure The Liar has made a kill by the end of the session, or you will die with him. The Liar is " + liar.getName() + " \\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
            } else {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Accomplice!\\\\n\\\\nAnd there is no Liar, yipeeeeee a care free day\\\\n\\\\nbetter not die ;)\"]]'],title:Role,author:NOMAD}]");
                setComplete(player, true);
            }
        }
        if (role == WATCHER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Watcher!\\\\n\\\\nYou get to teleport to someone in spectator mode for 15 seconds and see what you can find out about them!\\\\n/Watch <player>\\\\nYou can also recall with a 10 min cooldown\\\\n/recall set\\\\n/recall\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == BODYGUARD) {
            if (guarded != null) {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Bodyguard\\\\n\\\\nYour goal is to make sure " + guarded.getName() + " doesn\\\\\\'t die. If they die, YOU DIE\\\\n\\\\nP.S probably don\\\\\\'t let them know you\\\\\\'re they\\\\\\'re bodyguard, they can still report you!\"]]'],title:Role,author:NOMAD}]");
            }
        }
        if (role == JESTER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Jester\\\\n\\\\nYour goal is to have people vote you out. Act as sus as possible. If people vote for you as anything you will not die BUT if they do not you will die at the end of the round!\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == ASSASSIN) {
            if (target != null) {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Assassin\\\\n\\\\nYour target is " + target.getName() + ". You MUST kill them before the end of the round or you will die!\\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
            } else {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Assassin!\\\\n\\\\nAnd there is no Target, you should never see this msg but if you do, yipeeeeee a care free day\\\\n\\\\nbetter not die ;)\"]]'],title:Role,author:NOMAD}]");
                setComplete(player, true);
            }
        }
        if (role == DETECTIVE) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Detective\\\\n\\\\nYou get to /scan a person to find out there role! But it\\\\\\'s not as easy as it sounds. You must be within 30 blocks of the player you are scanning! The player will also be alerted to being scanned.\\\\nOh also you cant vote :)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == TESTIFICATE) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Testificate\\\\n\\\\nJust a boring old regular human. Nothing special to it\\\\n\\\\nHave fun with nothing to do! Maybe go make some friends :)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == SEER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Seer\\\\n\\\\nYou have the weight on your shoulders to know that someone has died. you know who and where but don't spill how you know!\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == SWAPPER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Swapper\\\\n\\\\nYou can use /swap <player> <player> to switch the locations of 2 players. You can do this up to 3 times so have fun messing with people\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == DOCTOR) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Doctor\\\\n\\\\nYou can use /save <player> to save someone, if they die they dont lose a life! Use it wisely\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == TRAP) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Trap\\\\n\\\\nIf you die the person who kills you will be stuck in place for a minute.\\\\n\\\\nTry to get them in a sticky situation!\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == JURY) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Jury\\\\n\\\\nPretty simple you can see all the votes that are made. Use that to your advantage and manipulate the votes to how YOU want. Just like a REAL democracy... Also you can /scan 3 times to see if someone is guilty or innocent\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == NURSE) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Nurse\\\\n\\\\nYou can use          /transplant to transplant a life to someone else IF they have more than 1 life and you are within 5 blocks of them. Be careful, the person you take the life from will be alerted of what you\\\\\\'re doing!\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == ANGEL) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Angel\\\\n\\\\nYou can use /gift to give a life to anyone but yourself. Use this however you see fit. Bargain, get favors done. But be warned people can still vote you out\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == SERIAL_KILLER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Serial Killer!\\\\n\\\\nYou have to kill 3 people... Have fun with it...\\\\n\\\\n\",{\"text\":\"FOR ME\",\"color\":\"dark_red\"}]]'],title:Role,author:NOMAD}]");
        }
    }
}
