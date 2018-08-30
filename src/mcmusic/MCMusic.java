package mcmusic;

import mcmusic.commands.cmdMusic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MCMusic extends JavaPlugin {
    public static MCMusic plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getPluginCommand("music").setExecutor(new cmdMusic());

        getDataFolder();

    }

}
