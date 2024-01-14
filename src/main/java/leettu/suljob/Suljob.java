package leettu.suljob;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Suljob extends JavaPlugin implements Listener,CommandExecutor, TabCompleter {

    private File configFile;
    private boolean timestart;
    private FileConfiguration config;
    private  List<Player> onlinePlayer = new ArrayList<>();
    private List<Player> runner = new ArrayList<>();
    private Boolean bossWin;
    private Player boss;
    private Player randomPlayer;
    private int time;
    private Location bossSpawn;
    private Location runnerSpawn;
    private Location lobby;
    private int remainNum;
    private int t;
    private int tt;
    @Override
    public void onEnable() {
        getLogger().info("on0.1");
        getCommand("게임시작").setExecutor(this);
        getCommand("설정").setExecutor(this);
        getCommand("클");
        getCommand("디버그");
        onlinePlayer.addAll(Bukkit.getOnlinePlayers());
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        bossSpawn = config.getLocation("bossSpawn");
        runnerSpawn = config.getLocation("runnerSpawn");
        lobby = config.getLocation("lobby");
        time = config.getInt("time");

    }
    @Override
    public void onDisable() {
        getLogger().info("술잡 오프");
        config.set("bossSpawn", bossSpawn);
        config.set("runnerSpawn", runnerSpawn);
        config.set("lobby", lobby);
        config.set("time", time);
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
            itemMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "유령 무희" );
            itemMeta.setLore(java.util.Arrays.asList(ChatColor.DARK_RED + "이 영혼들을 너에게 바칠게"));
            itemMeta.setCustomModelData(5);
            AttributeModifier damageModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attackDamage",
                    5.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attack_speed",
                    -3.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);

            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

            itemMeta.setUnbreakable(true);
            itemStack.setItemMeta(itemMeta);


            player.getInventory().addItem(itemStack);
        }
    }

    public void giveToySword(Player player) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "장난감 칼" );
            itemMeta.setLore(java.util.Arrays.asList(ChatColor.WHITE + "어렸을 때로 돌아간 기분이야."));
            itemMeta.setCustomModelData(5);
            AttributeModifier damageModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attackDamage",
                    2.5,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attack_speed",
                    -3.3,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);

            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

            itemMeta.setUnbreakable(true);
            itemStack.setItemMeta(itemMeta);


            player.getInventory().addItem(itemStack);
        }
    }

    public void slowness(Player player) {
        PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 999999 * 20, 10);
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

    public void teamJoin(Player player, String team){
        String teamJoin = "team join " + player + " " +team;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamJoin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("디버그")) {
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("item")){
                        giveToySword(player);
                        givePhantomDance(player);
                    }

                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("add")) {
                        remainNum = remainNum + Integer.parseInt(args[1]);
                    }
                }

            }
        }

        if (command.getName().equalsIgnoreCase("설정")){
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("술레위치")){
                        bossSpawn = player.getLocation();
                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("술레 스폰이 " + bossSpawn + "로 저장됨");
                    } else if (args[0].equalsIgnoreCase("플레이어위치")) {
                        runnerSpawn = player.getLocation();
                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("플레이어 스폰이 " + runnerSpawn + "로 저장됨");
                    } else if (args[0].equalsIgnoreCase("로비")) {
                        lobby = player.getLocation();
                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("로비가 " + lobby + "로 저장됨");
                    }
                }
                else if(args.length == 2){
                    if (args[0].equalsIgnoreCase("time")) {
                        time = Integer.parseInt(args[1]);
                        player.sendMessage("시간이 " + time + "초로 저장됨");
                    }
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
                    getRandomPlayer();
                    boss = getRandom();
                    Bukkit.broadcastMessage("boss is " + boss.getName());
                    givePhantomDance(boss);
                    slowness(boss);
                    darkness(boss);
                    String bossName = boss.getName();
                    String bossTeamJoin = "team join boss " + bossName;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossTeamJoin);
                    boss.setMaxHealth(80);
                    heal(boss);
                    boss.teleport(bossSpawn);
                    remainNum = 1;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!boss.equals(onlinePlayer)) {
                            teamJoin(onlinePlayer, "player");
                            runner.add(onlinePlayer);
                            onlinePlayer.teleport(runnerSpawn);
                            darkness(onlinePlayer);
                            heal(onlinePlayer);
                            remainNum = remainNum + 1;
                            giveToySword(onlinePlayer);
                        }
                    }
                    t = 10;
                    tt = time;
                    timestart = false;

                    // Escape game loop
                    Bukkit.getScheduler().runTaskTimer(this, () -> {
                        if (t > 0) {
                            Bukkit.broadcastMessage("술레가 나올때까지: " + t);
                            t--;
                        } else {
                            Bukkit.broadcastMessage("탈출");

                            // Start countdown loop
                            Bukkit.getScheduler().runTaskTimer(this, () -> {
                                if (tt == 60) {
                                    Bukkit.broadcastMessage("시간이 60초 남았습니다");
                                } else if (tt <= 5) {
                                    Bukkit.broadcastMessage("시간이 " + tt + "초 남았습니다");
                                }

                                if (tt <= 0 || remainNum == 0) {
                                    // Countdown completed or no players left, exit the loop
                                    Bukkit.getScheduler().cancelTasks(this);

                                    // Broadcast winner based on the escapeWin flag
                                    if (bossWin) {
                                        Bukkit.broadcastMessage("도망자가 승리함!");
                                    } else {
                                        Bukkit.broadcastMessage("술레가 승리함!");
                                    }

                                    // Execute additional commands after the game
                                    Bukkit.getScheduler().runTaskLater(this, () -> {
                                        Bukkit.broadcastMessage("Additional logic after escape game completion");

                                        // Execute other commands as needed
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "attribute @a minecraft:generic.max_health base set 20");
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear @a");
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team leave @a");

                                        // Reset players
                                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                            onlinePlayer.setGameMode(GameMode.ADVENTURE);
                                            onlinePlayer.teleport(lobby); // Change to your lobby location
                                            clearEffect(onlinePlayer);
                                            onlinePlayer.getInventory().clear();
                                        }

                                    }, 20L * 10); // 10 seconds delay after escape game completion
                                }
                                time--;

                            }, 0L, 20L); // 20 ticks per second

                            // Cancel the escape game task
                            Bukkit.getScheduler().cancelTasks(this);
                        }
                    }, 0L, 20L);




                    return true;


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
                completions.add("플레이어위치");
                completions.add("로비");
            }
        }
        return  completions;
    }

}
