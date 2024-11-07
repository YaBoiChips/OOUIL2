package yaboichips.oOUIL2;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.*;

import static org.bukkit.Bukkit.getLogger;


public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!player.getPersistentDataContainer().has(Keys.LIVES)) {
            player.getPersistentDataContainer().set(Keys.LIVES, PersistentDataType.INTEGER, 3);
            getLogger().info("Set 'lives' metadata to 3 for player: " + event.getPlayer().getName());
        }
    }
}
