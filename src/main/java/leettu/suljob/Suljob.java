package leettu.suljob;

import org.bukkit.GameMode;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

public final class Suljob extends JavaPlugin implements Listener,CommandExecutor {
    @Override
    public void onEnable() {
        getLogger().info("on0.1");

        getCommand("게임시작").setExecutor(new command());
    }
    @Override
    public void onDisable() {
        getLogger().info("술잡 오프");
    }

}
