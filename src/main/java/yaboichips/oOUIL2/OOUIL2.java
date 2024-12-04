package yaboichips.oOUIL2;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import yaboichips.oOUIL2.commands.*;
import yaboichips.oOUIL2.roles.Role;

import java.util.*;

public final class OOUIL2 extends JavaPlugin implements Listener {

    private final Set<UUID> frozenPlayers = new HashSet<>();
    public static Team MAYOR_TEAM;


    @Override
    public void onEnable() {
        Role.init();
        getServer().getPluginManager().registerEvents(this, this);
        Scoreboard scoreboard = getServer().getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam("mayor") != null) {
            scoreboard.getTeam("mayor").unregister();
        }


        if (MAYOR_TEAM == null) {
            MAYOR_TEAM = scoreboard.registerNewTeam("mayor");
        }

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
        this.getCommand("setlives").setExecutor(new SetLivesCommand());
        this.getCommand("gift").setExecutor(new GiftCommand());
        this.getCommand("task").setTabCompleter(new TaskTabCompleter());
        this.getCommand("task").setExecutor(new TaskCommand());
        this.getCommand("recall").setExecutor(new RecallCommand());
        this.getCommand("track").setExecutor(new TrackCommand(this));
        this.getCommand("getrole").setExecutor(new GetRoleCommand());
        this.getCommand("setrole").setExecutor(new SetRoleCommand());
        this.getCommand("setrole").setTabCompleter(new SetRoleCommand.SetRoleTabCompleter());

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }


    @Override
    public void onDisable() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objects.requireNonNull(scoreboard.getTeam("mayor")).unregister();
    }

    public static String getRole(Player player) {
        if (player.getPersistentDataContainer().has(Keys.ROLE)) {
            return player.getPersistentDataContainer().get(Keys.ROLE, PersistentDataType.STRING);
        }
        return Role.TESTIFICATE;
    }

    public static void setRole(Player player, String role) {
        player.getPersistentDataContainer().set(Keys.ROLE, PersistentDataType.STRING, role);
    }

    public static void setLives(Player player, int i) {
        player.getPersistentDataContainer().set(Keys.LIVES, PersistentDataType.INTEGER, i);
    }

    public static int getLives(Player player) {
        if (player.getPersistentDataContainer().has(Keys.LIVES)) {
            return player.getPersistentDataContainer().get(Keys.LIVES, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static void setVotes(Player player, int i) {
        player.getPersistentDataContainer().set(Keys.VOTES, PersistentDataType.INTEGER, i);
    }

    public static int getVotes(Player player) {
        if (player.getPersistentDataContainer().has(Keys.VOTES)) {
            return player.getPersistentDataContainer().get(Keys.VOTES, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static void setVoteCount(Player player, int i) {
        player.getPersistentDataContainer().set(Keys.VOTES_FOR, PersistentDataType.INTEGER, i);
    }

    public static int getVoteCount(Player player) {
        if (player.getPersistentDataContainer().has(Keys.VOTES_FOR)) {
            return player.getPersistentDataContainer().get(Keys.VOTES_FOR, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static void setUses(Player player, int i) {
        player.getPersistentDataContainer().set(Keys.USES, PersistentDataType.INTEGER, i);
    }

    public static int getUses(Player player) {
        if (player.getPersistentDataContainer().has(Keys.USES)) {
            return player.getPersistentDataContainer().get(Keys.USES, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static void setSaved(Player player, boolean i) {
        player.getPersistentDataContainer().set(Keys.SAVED, PersistentDataType.BOOLEAN, i);
    }

    public static boolean getSaved(Player player) {
        if (player.getPersistentDataContainer().has(Keys.SAVED)) {
            return player.getPersistentDataContainer().get(Keys.SAVED, PersistentDataType.BOOLEAN);
        }
        return false;
    }

    public static void setComplete(Player player, boolean i) {
        player.getPersistentDataContainer().set(Keys.COMPLETE, PersistentDataType.BOOLEAN, i);
    }

    public static boolean getComplete(Player player) {
        if (player.getPersistentDataContainer().has(Keys.COMPLETE)) {
            return player.getPersistentDataContainer().get(Keys.COMPLETE, PersistentDataType.BOOLEAN);
        }
        return false;
    }

    public static void setGuarded(Player player, boolean i) {
        player.getPersistentDataContainer().set(Keys.GUARDED, PersistentDataType.BOOLEAN, i);
    }

    public static boolean getGuarded(Player player) {
        if (player.getPersistentDataContainer().has(Keys.GUARDED)) {
            return player.getPersistentDataContainer().get(Keys.GUARDED, PersistentDataType.BOOLEAN);
        }
        return false;
    }

    public static void setTarget(Player player, boolean i) {
        player.getPersistentDataContainer().set(Keys.TARGET, PersistentDataType.BOOLEAN, i);
    }

    public static boolean getTarget(Player player) {
        if (player.getPersistentDataContainer().has(Keys.TARGET)) {
            return player.getPersistentDataContainer().get(Keys.TARGET, PersistentDataType.BOOLEAN);
        }
        return false;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player killed = event.getPlayer();
        System.out.println(killed.getName() + " Respawned");
        killed.kickPlayer(killed.getName());
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Player killed = event.getEntity();
        Player killer = killed.getKiller();
        for (Player playerz : onlinePlayers) {
            if (getRole(playerz).equals(Role.SEER)) {
                playerz.sendMessage(killed.getName() + " has perished at " + killed.getLastDeathLocation().getX() + ", " + killed.getLastDeathLocation().getZ());
                playerz.playSound(playerz.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
            }
        }

        if (killer != null && getRole(killed) == Role.TRAP) {
            System.out.println(killer.getName() + " Died to The Trap");
            killer.damage(50);
        }

        if (killer != null && OOUIL2.getRole(killer) == Role.SERIAL_KILLER) {
            setUses(killer, getUses(killer) - 1);
            System.out.println(killer.getName() + "Got a Serial Killer Kill and needs " + getUses(killer) + " more");
        }

        if (killer != null && OOUIL2.getRole(killer) == Role.LIAR) {
            setComplete(killer, true);
            for (Player playerz : onlinePlayers) {
                if (OOUIL2.getRole(playerz) == Role.ACCOMPLICE) {
                    setComplete(playerz, true);
                }
            }
        }
        if (killer != null && OOUIL2.getRole(killer) == Role.ASSASSIN) {
            if (killed == StartCommand.target) {
                setComplete(killer, true);
            }
        }

        if (!getSaved(killed)) {
            setLives(killed, getLives(killed) - 1);
        } else {
            setSaved(killed, false);
        }
        for (Player playerz : onlinePlayers) {
            if (getGuarded(killed)) {
                for (Player guard : onlinePlayers) {
                    if (getRole(guard) == Role.BODYGUARD) {
                        setGuarded(playerz, false);
                        guard.setHealth(0);
                    }
                }
            }
        }
        if (getLives(killed) <= 0) {
            killed.setGameMode(GameMode.SPECTATOR);
            killed.sendTitle("YOU HAVE PERISHED!", "Better luck next season ;)", 30, 60, 30);
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
                        setComplete(damager, true);
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
