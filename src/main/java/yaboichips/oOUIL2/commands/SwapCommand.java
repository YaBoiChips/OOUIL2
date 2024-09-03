package yaboichips.oOUIL2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import yaboichips.oOUIL2.OOUIL2;

import static yaboichips.oOUIL2.roles.Role.SWAPPER;

public class SwapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (OOUIL2.getRole(player) == SWAPPER) {
                Objective usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("usedrole");
                if (usedRoleObjective == null) {
                    usedRoleObjective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("usedrole", "dummy", "Used Role");
                }
                if (usedRoleObjective.getScore(sender.getName()).getScore() >= 1) {
                    if (command.getName().equalsIgnoreCase("swap")) {
                        if (args.length != 2) {
                            sender.sendMessage("Usage: /swap <player1> <player2>");
                            return true;
                        }

                        Player player1 = Bukkit.getPlayer(args[0]);
                        Player player2 = Bukkit.getPlayer(args[1]);

                        if (player1 == null || player2 == null) {
                            sender.sendMessage("Both players must be online.");
                        } else {
                            startCountdown(sender, player1, player2);
                            usedRoleObjective.getScore(sender.getName()).setScore(usedRoleObjective.getScore(sender.getName()).getScore() - 1);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage("This command can only be used once.");
                }
            } else {
                sender.sendMessage("This command can only be used by The Swapper.");
            }
        }
        return false;
    }

    private void startCountdown(CommandSender sender, Player player1, Player player2) {
        new BukkitRunnable() {
            int countdown = 5; // 5-second countdown

            @Override
            public void run() {
                if (countdown > 0) {
                    // Notify players and sender of the countdown
                    String message = "Swapping in " + countdown + " seconds...";
                    float pitch = 1.0f + (5 - countdown) * 0.2f;
                    player1.sendMessage(message);
                    player2.sendMessage(message);
                    player1.playSound(player1, Sound.BLOCK_LEVER_CLICK, 1, pitch);
                    player2.playSound(player2, Sound.BLOCK_LEVER_CLICK, 1, pitch);

                    countdown--;
                } else {
                    // Swap players when countdown reaches 0
                    Location loc1 = player1.getLocation();
                    Location loc2 = player2.getLocation();


                    player1.teleport(loc2);
                    player2.teleport(loc1);
                    player1.playSound(player1, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
                    player2.playSound(player2, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);


                    String swapMessage = "Successfully swapped " + player1.getName() + " and " + player2.getName() + "!";
                    String swappingMessage = "Your location has been swapped with another player!";
                    sender.sendMessage(swapMessage);
                    player1.sendMessage(swappingMessage);
                    player2.sendMessage(swappingMessage);

                    this.cancel(); // Stop the countdown task
                }
            }
        }.runTaskTimer(OOUIL2.getPlugin(OOUIL2.class), 0, 20); // Schedule task every 20 ticks (1 second)
    }
}
