package yaboichips.oOUIL2;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import yaboichips.oOUIL2.commands.*;
import yaboichips.oOUIL2.roles.Role;

import java.util.*;

public final class OOUIL2 extends JavaPlugin implements Listener {

    private final Set<UUID> frozenPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        Role.init();
        getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("start").setExecutor(new StartCommand());
        this.getCommand("vote").setTabCompleter(new VoteCommand.VoteTabCompleter());
        this.getCommand("vote").setExecutor(new VoteCommand());
        this.getCommand("save").setExecutor(new SaveCommand());
        this.getCommand("watch").setExecutor(new WatchCommand());
        this.getCommand("end").setExecutor(new EndCommand());
        this.getCommand("swap").setExecutor(new SwapCommand());
        this.getCommand("scan").setExecutor(new ScanCommand());
        this.getCommand("transplant").setExecutor(new TransplantCommand());
        this.getCommand("checklives").setExecutor(new CheckLivesCommand());
        this.getCommand("gift").setExecutor(new GiftCommand());
        this.getCommand("task").setTabCompleter(new TaskTabCompleter());
        this.getCommand("task").setExecutor(new TaskCommand());

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard scoreboard = manager.getMainScoreboard();
            Objective livesObjective = scoreboard.getObjective("lives");
            if (livesObjective == null) {
                livesObjective = scoreboard.registerNewObjective("lives", "dummy", "Lives");
                Objective savedObjective = scoreboard.getObjective("saved");
                if (savedObjective == null) {
                    savedObjective = scoreboard.registerNewObjective("saved", "dummy", "Saved");
                }
                Objective voteObjective = scoreboard.getObjective("votes");
                if (voteObjective == null) {
                    voteObjective = scoreboard.registerNewObjective("votes", "dummy", "Votes");
                }
                Objective roleObjective = scoreboard.getObjective("role");
                if (roleObjective == null) {
                    roleObjective = scoreboard.registerNewObjective("role", "dummy", "Role");
                }
                Objective target = scoreboard.getObjective("target");
                if (target == null) {
                    target = scoreboard.registerNewObjective("target", "dummy", "Target");
                }
            }
        }
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static Role getRole(Player player) {
        Objective roleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("role");
        Score score = roleObjective.getScore(player.getName());
        return Role.getRoleByValue(score.getScore());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : onlinePlayers) {
            if (getRole(player) == Role.SEER) {
                player.sendMessage("Someone has perished!");
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
            }
        }
        Player killed = event.getEntity();
        Player killer = killed.getKiller();

        if (killer != null && getRole(killed) == Role.TRAP) {
            freezePlayer(killer);
        }

        Player player = event.getEntity();
        Objective savedObjective = scoreboard.getObjective("saved");
        if (savedObjective == null) {
            savedObjective = scoreboard.registerNewObjective("saved", "dummy", "Saved");
        }

        if (OOUIL2.getRole(player) == Role.LIAR) {
            Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
            if (usedRoleObjective == null) {
                usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
            } else {
                usedRoleObjective.getScore(player.getName()).setScore(0);
                for (Player playerz : onlinePlayers) {
                    if (OOUIL2.getRole(playerz) == Role.ACCOMPLICE) {
                        usedRoleObjective.getScore(playerz.getName()).setScore(0);
                    }
                }
            }
        }
        if (OOUIL2.getRole(player) == Role.ASSASSIN) {
            Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
            if (usedRoleObjective == null) {
                usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
            } else {
                if (killed == StartCommand.target) {
                    usedRoleObjective.getScore(player.getName()).setScore(0);
                }
            }
        }

        Objective livesObjective = scoreboard.getObjective("lives");
        Score lives = livesObjective.getScore(player.getName());
        Score savedScore = savedObjective.getScore(player.getName());
        if (savedScore.getScore() != 1) {
            lives.setScore(lives.getScore() - 1);
        } else {
            savedScore.setScore(0);
        }
        Objective guardObjective = scoreboard.getObjective("guard");
        if (guardObjective == null) {
            guardObjective = scoreboard.registerNewObjective("guard", "dummy", "Guard");
        } else {
            for (Player playerz : onlinePlayers) {
                if (guardObjective.getScore(playerz.getName()).getScore() == 1) {
                    for (Player guard : onlinePlayers) {
                        if (getRole(guard) == Role.BODYGUARD) {
                            guard.damage(50);
                            guardObjective.getScore(playerz.getName()).setScore(0);
                        }
                    }
                }
            }
            for (Player playerz : onlinePlayers) {
                guardObjective.getScore(playerz.getName()).setScore(0);

            }
        }

        if (lives.getScore() <= 0) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle("YOU HAVE PERISHED!", "Better luck next season ;)", 30, 60, 30);
        }
    }

    private void freezePlayer(Player player) {
        frozenPlayers.add(player.getUniqueId());
        Location freezeLocation = player.getLocation();
        player.sendMessage("You are frozen in place for 1 minute!");

        new BukkitRunnable() {
            @Override
            public void run() {
                frozenPlayers.remove(player.getUniqueId());
                player.sendMessage("You are no longer frozen!");
            }
        }.runTaskLater(this, 60 * 20L); // 1 minute * 60 seconds * 20 ticks per second

        new BukkitRunnable() {
            @Override
            public void run() {
                if (frozenPlayers.contains(player.getUniqueId())) {
                    player.teleport(freezeLocation);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 0L, 1L); // Repeat every tick
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player clicked) {
            Player player = event.getPlayer();
            Material type = player.getInventory().getItemInMainHand().getType();
            if (type == Material.SPYGLASS) {
                player.sendMessage(clicked.getName() + " is The " + getRole(clicked).getName());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.getInventory().remove(type);
                clicked.sendMessage("You've been detected");
            }
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player target) {
            ItemStack weapon = damager.getInventory().getItemInMainHand();
            if (getRole(damager) == Role.ESPUR) {
                if (weapon.getType() == Material.IRON_SWORD) {
                    ItemMeta meta = weapon.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Espur Blade")) {
                        scheduleDeath(target);
                        damager.getInventory().remove(weapon);
                        Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                        if (usedRoleObjective == null) {
                            usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                        } else {
                            usedRoleObjective.getScore(damager.getName()).setScore(0);
                        }
                    }
                }
            }
        }
    }

    private void scheduleDeath(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.setHealth(0);
                    player.sendMessage("You have been killed by the Espur Blade.");
                }
            }
        }.runTaskLater(this, 10 * 60 * 20L);
    }
}
