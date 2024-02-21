package leettu.suljob;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
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
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Suljob extends JavaPlugin implements Listener,CommandExecutor, TabCompleter {

    private File configFile;
    private boolean waitFinish;
    private boolean timestart;
    private String bossName;
    private FileConfiguration config;
    private  List<Player> onlinePlayer = new ArrayList<>();
    private List<Player> runner = new ArrayList<>();
    private Boolean playerWin;
    private Player boss;
    private Player randomPlayer;
    private Location randomLocation;
    private int time;
    private Location bossSpawn;
    private List<Location> runnerSpawn = new ArrayList<>();
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
        getCommand("test");
        getServer().getPluginManager().registerEvents(this,this);
        onlinePlayer.addAll(Bukkit.getOnlinePlayers());
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        bossSpawn = config.getLocation("bossSpawn");
        lobby = config.getLocation("lobby");
        time = config.getInt("time");
        loadRunner();


    }
    @Override
    public void onDisable() {
        getLogger().info("술잡 오프");
        config.set("bossSpawn", bossSpawn);
        config.set("lobby", lobby);
        config.set("time", time);
        saveRunner();
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRunner() {
        runnerSpawn.clear();

        ConfigurationSection locationSection = getConfig().getConfigurationSection("savedLocations");
        if (locationSection != null) {
            for (String key : locationSection.getKeys(false)) {
                Object locObject = locationSection.get(key);

                if (locObject instanceof Location) {
                    runnerSpawn.add((Location) locObject);
                } else {
                    getLogger().warning("Invalid location format in the config: " + key);
                }
            }
        }
    }

    private void saveRunner() {
        FileConfiguration config = getConfig();
        ConfigurationSection locationSection = config.createSection("savedLocations");

        for (int i = 0; i < runnerSpawn.size(); i++) {
            getLogger().warning("" + i);
            String key = "location" + i;
            Location location = runnerSpawn.get(i);
            locationSection.set(key, location);
        }

        saveConfig();
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

    public void getRandomLocation() {
        if (runnerSpawn.isEmpty()) {
            getLogger().warning("no location saved");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(runnerSpawn.size());
        randomLocation = runnerSpawn.get(randomIndex);
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

    private void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "testGUI");
        player.openInventory(gui);
    }

    private void openWeaponGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "WeaponGUI");

        ItemStack PhantomDance = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta itemMetaP = PhantomDance.getItemMeta();

        if (itemMetaP != null) {
            itemMetaP.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "유령 무희" );
            itemMetaP.setLore(java.util.Arrays.asList(ChatColor.DARK_RED + "이 영혼들을 너에게 바칠게"));
            itemMetaP.setCustomModelData(5);
            AttributeModifier damageModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attackDamage",
                    5.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMetaP.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attack_speed",
                    -3.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMetaP.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);

            itemMetaP.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

            itemMetaP.setUnbreakable(true);
            PhantomDance.setItemMeta(itemMetaP);

            ItemStack itemStack = new ItemStack(Material.IRON_AXE);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "철제 도끼" );
                itemMeta.setLore(java.util.Arrays.asList(ChatColor.DARK_AQUA+ "" + ChatColor.BOLD + "이 서늘하고도 묵직한 감각.. 이거야...."));
                itemMeta.setCustomModelData(5);
                AttributeModifier damageModifierE = new AttributeModifier(
                        UUID.randomUUID(),
                        "generic.attackDamage",
                        14,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND
                );
                itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifierE);

                AttributeModifier attackSpeedModifierE = new AttributeModifier(
                        UUID.randomUUID(),
                        "generic.attack_speed",
                        -3.8,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND
                );
                itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifierE);

                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

                itemMeta.setUnbreakable(true);
                itemStack.setItemMeta(itemMeta);

            }

            gui.setItem(5, PhantomDance);
            gui.setItem(3, itemStack);

            player.openInventory(gui);
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

    public void giveIronAxe(Player player) {
        ItemStack itemStack = new ItemStack(Material.IRON_AXE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "철제 도끼" );
            itemMeta.setLore(java.util.Arrays.asList(ChatColor.DARK_AQUA+ "" + ChatColor.BOLD + "이 서늘하고도 묵직한 감각.. 이거야...."));
            itemMeta.setCustomModelData(5);
            AttributeModifier damageModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attackDamage",
                    14,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "generic.attack_speed",
                    -3.8,
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

    public void giveEcho(Player player) {
        ItemStack itemStack = new ItemStack(Material.ECHO_SHARD);
        ItemMeta itemMeta = itemStack.getItemMeta();



        player.getInventory().addItem(itemStack);

    }

    public void slowness(Player player) {
        PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 999999 * 20, 10);
        player.addPotionEffect(slow);
    }

    public void darkness(Player player) {
        PotionEffect dark = new PotionEffect(PotionEffectType.DARKNESS, 99999 * 20, 1);
        player.addPotionEffect(dark);
    }

    public void nightVision(Player player) {
        PotionEffect night = new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255);
        player.addPotionEffect(night);
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
        String playerName = player.getName();
        String teamJoin = "team join " + team + " " + playerName;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamJoin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        /*if (command.getName().equalsIgnoreCase("test")) {
            openGUI(player);
            openWeaponGUI(player);
        }*/

        if (command.getName().equalsIgnoreCase("디버그")) {
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("item")){
                        giveToySword(player);
                        givePhantomDance(player);
                    } else if (args[0].equalsIgnoreCase("boss")) {
                        Bukkit.broadcastMessage("1");
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
                        runnerSpawn.add(player.getLocation());
                        saveRunner();
                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("플레이어 스폰이 " + runnerSpawn + "로 저장됨");
                    } else if (args[0].equalsIgnoreCase("플레이어위치초기화")) {
                        runnerSpawn = new ArrayList<>();
                    }
                    else if (args[0].equalsIgnoreCase("로비")) {
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
                        onlinePlayer.getInventory().clear();
                        String teamPlayer = onlinePlayer.getName();
                        String teamLeaveCommand = "team leave " + teamPlayer;
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamLeaveCommand);
                        onlinePlayer.setMaxHealth(20);
                    }
                    remainNum = 0;
                    playerWin = false;
                    boss = null;
                    runner = new ArrayList<>();
                    getRandomPlayer();
                    boss = getRandom();
                    Bukkit.broadcastMessage("술레는 " + boss.getName() + "입니다");
                    nightVision(boss);
                    slowness(boss);
                    darkness(boss);
                    bossName = boss.getName();
                    String bossTeamJoin = "team join boss " + bossName;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), bossTeamJoin);
                    boss.setMaxHealth(80);
                    heal(boss);
                    boss.teleport(bossSpawn);
                    giveEcho(boss);
                    openWeaponGUI(boss);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!onlinePlayer.equals(boss)) {
                            teamJoin(onlinePlayer, "player");
                            runner.add(onlinePlayer);
                            getRandomLocation();
                            onlinePlayer.teleport(randomLocation);
                            nightVision(onlinePlayer);
                            darkness(onlinePlayer);
                            heal(onlinePlayer);
                            remainNum = remainNum + 1;
                            giveToySword(onlinePlayer);
                        }
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard player set bs2 xy 4");
                    t = 10;
                    tt = time;
                    timestart = true;
                    waitFinish = false;

                    Bukkit.getScheduler().runTaskTimer(this, () -> {

                        if (timestart && waitFinish) {
                            clearEffect(boss);
                            nightVision(boss);
                            darkness(boss);
                            Bukkit.broadcastMessage("술레가 풀려남");
                            timestart = false;
                        }
                        if (t <= 0) {
                            if (tt <= 0) {
                                playerWin = true;
                            }
                            if (tt <= 0 || remainNum <= 0 || playerWin) {
                                Bukkit.getScheduler().cancelTasks(this);
                                if (playerWin) {
                                    Bukkit.broadcastMessage("플레이어가 승리함");
                                } else {
                                    Bukkit.broadcastMessage("술레가 승리함");
                                }
                                boss.setMaxHealth(20);
                                boss = null;
                                runner = new ArrayList<>();
                                tt = time;
                                getRandomPlayer();
                                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                    onlinePlayer.setGameMode(GameMode.ADVENTURE);
                                    onlinePlayer.teleport(lobby);
                                    clearEffect(onlinePlayer);
                                    onlinePlayer.getInventory().clear();
                                    String teamPlayer = onlinePlayer.getName();
                                    String teamLeaveCommand = "team leave " + teamPlayer;
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), teamLeaveCommand);
                                    onlinePlayer.setMaxHealth(20);
                                }

                            }
                            else {
                                if (tt == 60){
                                    Bukkit.broadcastMessage("60초 남았습니다");
                                } else if (tt <= 5) {
                                    Bukkit.broadcastMessage(ChatColor.RED+ "" + tt + "초 남았습니다");
                                }
                                tt--;
                                waitFinish = true;
                            }
                        }
                        else {
                            Bukkit.broadcastMessage("술레가 나오기까지 : " + t);
                            t--;
                        }
                    },0L, 20L);




                    return true;


                    }

                } else {
                player.sendMessage("op가 아님");
                }
            }
        return false;
        }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        String playerName = player.getName();
        if (player.equals(boss)) {
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage("술레 " + playerName + "이/가 쓰러졌습니다!");
            playerWin = true;
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            remainNum--;
            Bukkit.broadcastMessage(remainNum + "명 남았습니다");

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("WeaponGUI")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.NETHERITE_HOE) {
                Player player = (Player) event.getWhoClicked();
                givePhantomDance(player);
                player.closeInventory();
            }
            if (clickedItem != null && clickedItem.getType() == Material.IRON_AXE) {
                Player player = (Player) event.getWhoClicked();
                giveIronAxe(player);
                player.closeInventory();
            }
        }
    }






    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(command.getName().equalsIgnoreCase("설정")) {
            if (args.length == 1) {
                completions.add("술레위치");
                completions.add("플레이어위치");
                completions.add("플레이어위치초기화");
                completions.add("로비");
                completions.add("time");
            }
        }

        if(command.getName().equalsIgnoreCase("디버그")) {
            if (args.length == 1) {
                completions.add("item");
            }
        }
        return  completions;
    }

}
