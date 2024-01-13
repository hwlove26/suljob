package leettu.suljob;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Suljob extends JavaPlugin implements Listener,CommandExecutor, TabCompleter {

    private File configFile;
    private FileConfiguration config;
    private  List<Player> onlinePlayer = new ArrayList<>();
    private List<Player> runner = new ArrayList<>();
    private Boolean bossWin;
    private Player boss;
    private Player randomPlayer;
    private int time;
    private Location bossSpawn;
    @Override
    public void onEnable() {
        getLogger().info("on0.1");
        getCommand("게임시작").setExecutor(this);
        getCommand("설정").setExecutor(this);
        onlinePlayer.addAll(Bukkit.getOnlinePlayers());
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        bossSpawn = config.getLocation("bossSpawn");

    }
    @Override
    public void onDisable() {
        getLogger().info("술잡 오프");
        config.set("bossSpawn", bossSpawn);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getRandomPlayer() {
        if (onlinePlayer.isEmpty()) {
            getLogger().warning("플레이어가 없음");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(onlinePlayer.size());
        randomPlayer = onlinePlayer.get(randomIndex);
        getLogger().info(randomPlayer.getName());
    }

    public Player getRandom(){
        return randomPlayer;
    }

    public void givePhantomDance(Player player) {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName("유령무희");
            itemStack.setItemMeta(itemMeta);

            player.getInventory().addItem(itemStack);
            player.sendMessage("유령무희를 받았다");
        }
    }

    public void slowness(Player player) {
        PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 99999 * 20, 10);
        player.addPotionEffect(slow);
    }

    public void darkness(Player player) {
        PotionEffect dark = new PotionEffect(PotionEffectType.DARKNESS, 99999 * 20, 1);
        player.addPotionEffect(dark);
    }

    public void heal(Player player) {
        PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 255);
        player.addPotionEffect(regen);
    }
    public void clearEffect(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("설정")){
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("술레위치")){
                        bossSpawn = player.getLocation();
                        player.sendMessage("1");
                        player.sendMessage("2");
                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("술레 스폰이 " + bossSpawn + "로 저장됨");
                    }
                }
                else {
                    player.sendMessage("술레위치");

                }
            } else {
                player.sendMessage("op가 아님");
            }
        }

        if (command.getName().equalsIgnoreCase("게임시작")) {
            if (player.isOp()) {
                if (args.length == 0) {
                    Bukkit.broadcastMessage("게임 시작!");
                    onlinePlayer.addAll(Bukkit.getOnlinePlayers());
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setGameMode(GameMode.ADVENTURE);
                        String teamPlayer = onlinePlayer.getName();
                        String teamLeaveCommand = "team leave " + teamPlayer;
                        player.sendMessage(teamLeaveCommand);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamLeaveCommand);
                        onlinePlayer.setMaxHealth(20);
                    }
                    bossWin = false;
                    boss = null;
                    runner = null;
                    time = 0;
                    getRandomPlayer();
                    boss = getRandom();
                    Bukkit.broadcastMessage("boss is " + boss.getName());
                    givePhantomDance(boss);
                    slowness(boss);
                    darkness(boss);
                    clearEffect(boss);
                    String bossName = boss.getName();
                    String bossTeamJoin = "team join boss " + bossName;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossTeamJoin);
                    boss.setMaxHealth(80);
                    heal(boss);
                    boss.teleport(bossSpawn);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
                        if(isboss) {

                        }
                    }

                }
            } else {
                player.sendMessage("op가 아님");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(command.getName().equalsIgnoreCase("설정")) {
            if (args.length == 1) {
                completions.add("술레위치");
            }
        }
        return  completions;
    }

}
