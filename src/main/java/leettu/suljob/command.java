package leettu.suljob;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class command implements CommandExecutor, Listener {




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("게임시작")) {
            if (player.isOp()) {
                if (args.length == 0) {
                    Bukkit.broadcastMessage("게임 시작!");
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setGameMode(GameMode.ADVENTURE);
                        String teamPlayer = onlinePlayer.getName();
                        String teamLeaveCommand = "team leave " + teamPlayer;
                        player.sendMessage(teamLeaveCommand);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamLeaveCommand);


                    }


                }
            }
        } else {
            player.sendMessage("op가 아님");
        }
        return false;
    }

}