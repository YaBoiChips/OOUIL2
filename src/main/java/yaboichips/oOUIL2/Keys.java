package yaboichips.oOUIL2;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class Keys {
    public static final Plugin PLUGIN = OOUIL2.getPlugin(OOUIL2.class);
    public static final NamespacedKey ROLE = new NamespacedKey(PLUGIN, "role");
    public static final NamespacedKey LIVES = new NamespacedKey(PLUGIN, "lives");
    public static final NamespacedKey VOTES = new NamespacedKey(PLUGIN, "votes");
    public static final NamespacedKey VOTES_FOR = new NamespacedKey(PLUGIN, "votesFor");
    public static final NamespacedKey USES = new NamespacedKey(PLUGIN, "uses");
    public static final NamespacedKey TARGET = new NamespacedKey(PLUGIN, "target");
    public static final NamespacedKey GUARDED = new NamespacedKey(PLUGIN, "guarded");
    public static final NamespacedKey SAVED = new NamespacedKey(PLUGIN, "saved");
    public static final NamespacedKey COMPLETE = new NamespacedKey(PLUGIN, "complete");

}
