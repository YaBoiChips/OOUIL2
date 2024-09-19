package yaboichips.oOUIL2.commands;

import org.bukkit.*;
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

    public static Player guarded;
    public static Player target;

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
                    if (player.getGameMode() == GameMode.SURVIVAL) {
                        if (!roleList.isEmpty()) {
                            Collections.shuffle(roleList);
                            Score score = roleObjective.getScore(player.getName());
                            Role role = roleList.getFirst();
                            score.setScore(role.getValue());
                            player.sendTitle("You are The " + role.getName(), "good luck :)", 40, 20, 40);
                            player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);
                            roleList.removeFirst();
                        } else {
                            Score score = roleObjective.getScore(player.getName());
                            Role role = TESTIFICATE;
                            score.setScore(role.getValue());
                            player.sendTitle("You are The " + role.getName(), "good luck :)", 40, 20, 40);
                            player.playSound(player, Sound.ITEM_TOTEM_USE, 1, 1);
                        }
                    }
                }
            }
            if (voteObjective == null) {
                voteObjective = scoreboard.registerNewObjective("votes", "dummy", "Votes");
            } else {
                for (Player player : players) {
                    if (player.getGameMode() == GameMode.SURVIVAL) {
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
            }
            Objective targetScore = scoreboard.getObjective("target");
            if (targetScore == null) {
                targetScore = scoreboard.registerNewObjective("target", "dummy", "Target");
            }
            for (Player player : players) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (getRole(player) != ASSASSIN) {
                        targetScore.getScore(player.getName()).setScore(1);
                        target = player;
                        break;
                    }
                }
            }
            Objective guardObjective = scoreboard.getObjective("guard");
            if (guardObjective == null) {
                guardObjective = scoreboard.registerNewObjective("guard", "dummy", "Guard");
            } else {
                for (Player player : players) {
                    if (player.getGameMode() == GameMode.SURVIVAL) {
                        guardObjective.getScore(player.getName()).setScore(0);
                    }
                }

                for (Player player : players) {
                    if (player.getGameMode() == GameMode.SURVIVAL) {
                        Collections.shuffle(players);
                        if (getRole(player) != BODYGUARD) {
                            guardObjective.getScore(player.getName()).setScore(1);
                            guarded = player;
                            break;
                        }
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
            player.getInventory().remove(Material.WRITTEN_BOOK);
            Score used = usedRoleObjective.getScore(player.getName());
            if (getRole(player) == SWAPPER) {
                used.setScore(3);
            } else if (getRole(player) == JESTER) {
                used.setScore(3);
            } else {
                used.setScore(1);
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

    public void giveItems(Player player) {
        Role role = getRole(player);
        Server server = player.getServer();
        Player liar = null;
        if (role == Role.MAYOR) {
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
            liar = player;
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Liar!\\\\n\\\\nYou MUST kill someone by the end of the session or YOU die\\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == ACCOMPLICE) {
            if (liar != null) {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Accomplice!\\\\n\\\\nYou MUST ensure The Liar has made a kill by the end of the session, or you will die with him. The Liar is " + liar.getName() + " \\\\n\\\\nGood Luck :)\"]]'],title:Role,author:NOMAD}]");
            } else {
                server.dispatchCommand(server.getConsoleSender(),
                        "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Accomplice!\\\\n\\\\nAnd there is no Liar, yipeeeeee a care free day\\\\n\\\\nbetter not die ;)\"]]'],title:Role,author:NOMAD}]");
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else {
                    usedRoleObjective.getScore(player.getName()).setScore(0);
                }
            }
        }
        if (role == WATCHER) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Watcher\\\\n\\\\nYou get to teleport to someone in spectator mode for 15 seconds and see what you can find out about them\\\\n\\\\n/watch <player>\"]]'],title:Role,author:NOMAD}]");
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
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                } else {
                    usedRoleObjective.getScore(player.getName()).setScore(0);
                }
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
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Seer\\\\n\\\\nYou have the weight on your shoulders to know that someone has died. You don\\\\\\'t know who but you will know when it happens\\\\n\\\\nIsn\\\\\\'t that fun ;)\"]]'],title:Role,author:NOMAD}]");
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
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Trap\\\\n\\\\nIf you die the person who kills you will be stuck in place for a minuite.\\\\n\\\\nTry to get them in a sticky situation!\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == JURY) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Jury\\\\n\\\\nPretty simple you can see all the votes that are made. Use that to your advantage and manipulate the votes to how YOU want. Just like a REAL democracy\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == NURSE) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Nurse\\\\n\\\\nYou can use          /transplant to transplant a life to someone else IF they have more than 1 life and you are within 5 blocks of them. Be careful, the person you take the life from will be alerted of what you\\\\\\'re doing!\"]]'],title:Role,author:NOMAD}]");
        }
        if (role == ANGEL) {
            server.dispatchCommand(server.getConsoleSender(),
                    "give " + player.getName() + " written_book[written_book_content={pages:['[[\"You are The Angel\\\\n\\\\nYou can use /gift to give a life to anyone but yourself. Use this however you see fit. Bargain, get favors done. But be warned people can still vote you out\\\\n\\\\n:)\"]]'],title:Role,author:NOMAD}]");
        }
    }
}
