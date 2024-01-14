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
    private FileConfiguration config;
    private  List<Player> onlinePlayer = new ArrayList<>();
    private List<Player> runner = new ArrayList<>();
    private Boolean bossWin;
    private Player boss;
    private Player randomPlayer;
    private int time;
    private Location bossSpawn;
    private Location runnerSpawn;
    private int remainNum;
    @Override
    public void onEnable() {
        getLogger().info("on0.1");
        getCommand("게임시작").setExecutor(this);
        getCommand("설정").setExecutor(this);
        getCommand("클");
        onlinePlayer.addAll(Bukkit.getOnlinePlayers());
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        bossSpawn = config.getLocation("bossSpawn");
        runnerSpawn = config.getLocation("runnerSpawn");

    }
    @Override
    public void onDisable() {
        getLogger().info("술잡 오프");
        config.set("bossSpawn", bossSpawn);
        config.set("runnerSpawn", runnerSpawn);
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
        player.sendMessage("slow");
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

        if (command.getName().equalsIgnoreCase("클")){
            clearEffect(player);
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
                    String bossName = boss.getName();
                    String bossTeamJoin = "team join boss " + bossName;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossTeamJoin);
                    boss.setMaxHealth(80);
                    heal(boss);
                    boss.teleport(bossSpawn);
                    remainNum = 0;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!boss.equals(onlinePlayer)) {
                            teamJoin(onlinePlayer, "player");
                            runner.add(onlinePlayer);
                            onlinePlayer.teleport(runnerSpawn);
                            darkness(onlinePlayer);
                            heal(onlinePlayer);
                            remainNum = remainNum ++;
                            giveToySword(onlinePlayer);
                        }
                    }
                    for (int i =10; i > 0; i--) {
                        Bukkit.broadcastMessage(ChatColor.RED + "술레가 나올때까지: " + i);
                        try {
                            Thread.sleep(2000); // Wait for 20 ticks (1 second)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Bukkit.broadcastMessage(runner + " 도망쳐!");
                    Bukkit.broadcastMessage("남은 인원 : " + remainNum);
                    clearEffect(boss);
                    darkness(boss);
                    for (int i = 0; i < 60; i++) {
                        if (time == 60) {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "시간이 60초 남았습니다");
                        } else if (time <= 5 && time >= 1) {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "시간이 " + time + "초 남았습니다");
                        }

                        // Check if escapeWinner variable is set to true
                        if (bossWin) {
                            Bukkit.broadcastMessage(ChatColor.GREEN + "술레가 승리함");
                            break;
                        }

                        // Check if no players are left
                        if (Bukkit.getOnlinePlayers().size() == 0) {
                            Bukkit.broadcastMessage(ChatColor.RED + "술레가 승리함");
                            break;
                        }

                        try {
                            Thread.sleep(20000); // Wait for 20 ticks (1 second)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        time--;
                    }

                    return true;


                    }

                } else {
                player.sendMessage("op가 아님");
                }
            } return false;
        }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(command.getName().equalsIgnoreCase("설정")) {
            if (args.length == 1) {
                completions.add("술레위치");
                completions.add("플레이어위치");
            }
        }
        return  completions;
    }

}
