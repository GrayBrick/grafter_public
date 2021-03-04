package own;

import Contest.GUIcontest;
import GUI.PlayerMe;
import Json.J;
import Listeners.*;
import clans.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import commands.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import railway_system.road;
import railway_system.road_command;
import railway_system.set_station;
import text_processing.rgb;
import text_processing.rgb_map;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static Listeners.X_ray_fix.chunk_list;

public class graf extends JavaPlugin {

    public static FileConfiguration Players;
    public static FileConfiguration Regions;
    public static FileConfiguration config;
    public static FileConfiguration rail;
    public static FileConfiguration Clans;
    public static J JPlayers;

    public static Location spawn;

    public static int size_world = 4000;

    public static final String DB_URL = "jdbc:h2:./test_sql/test";
    public static final String DB_Driver = "org.h2.Driver";

    @Override
    public void onEnable() {

        //sql
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("[SQL] Connected.");
            connection.close();       // отключение от БД
            System.out.println("[SQL] Disconnected.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("[SQL] JDBC driver for your DBMS is not found!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("[SQL] SQL error !");
        }
        //end sql
        File file = new File("spigot.yml");
        FileConfiguration help = YamlConfiguration.loadConfiguration(file);

        help.set("messages.unknown-command", rgb.gradientLight(chat.color[0], "Команды не существует, информация о сервере: ") + rgb.gradientLight(chat.color[1], "/i"));

        try {
            help.save(file);
        } catch (Exception ignored) {
        }

        File filePlayer = new File("plugins/Players.yml");
        File fileRegs = new File("plugins/Regs.yml");
        File fileconfig = new File("plugins/config.yml");
        File filerail = new File("plugins/rail.yml");
        File fileclan = new File("plugins/Clans.yml");

        File dirarena = new File("plugins/Contest");
        File dirgrafter = new File("plugins/Grafter");
        File filearena = new File("plugins/Contest/Arena.yml");

        try {
            dirarena.mkdir();
            dirgrafter.mkdir();
            filearena.createNewFile();
            filePlayer.createNewFile();
            fileRegs.createNewFile();
            fileconfig.createNewFile();
            filerail.createNewFile();
            fileclan.createNewFile();
        } catch (Exception ignored) {
        }

        player.getconPlayer();
        reg.getconRegs();
        config = getcon();
        road.getconRail();
        Clan.getconClan();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    JPlayers = new J("plugins/Grafter/Players.json");
                    } catch (Exception ex) {
                }
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 20);


        spawn = new Location(
                Bukkit.getWorld(graf.config.getString("spawn.world")),
                graf.config.getDouble("spawn.x"),
                graf.config.getDouble("spawn.y"),
                graf.config.getDouble("spawn.z"),
                (float) graf.config.getDouble("spawn.yaw"),
                (float) graf.config.getDouble("spawn.pitch"));

        Bukkit.getServer().getPluginManager().registerEvents(new Player_L(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ActionPrivate(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new craftTpPoints(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NameTag(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TraderOpenTrade(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new road_command(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BonusEntityDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FixBag(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMe(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new X_ray_fix(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUInewRank(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUIbeacon(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUIKazna(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUIcontest(), this);

        craftTpPoints.addRecipe();

        Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
        rgb_map.fill();
        timer.timer();
    }

    @Override
    public void onDisable() {

        for (X_ray_fix.Chunk_bloks chb : chunk_list)
            chb.show();

        savecon(config);

        road.saveconRoads();
        savecon(rail, "rail");

        player.saveconPlayer();
        reg.saveconRegs();
        player.Players.clear();
        reg.Regs.clear();
        road.roads.clear();
        Clan.saveconClan();

        //JPlayers.saveJson();
    }

    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        if (sender instanceof Player) {
            player p = player.getPlayer((Player) sender);

            if (p.clan_gui != null && p.clan_gui.inter)
                return true;
            if (p.invite_in_clan)
                return true;
            if (p.kazna_take || p.kazna_give)
                return true;

            if (c.getName().equalsIgnoreCase("rg"))
                return rg_commands.rg(p, args);
            if (c.getName().equalsIgnoreCase("m") || c.getName().equalsIgnoreCase("tell") || c.getName().equalsIgnoreCase("w"))
                return message.mmes(p, args);
            if (c.getName().equalsIgnoreCase("sendfromto"))
                ChatEvent.messageg(player.getPlayer(args[0]), Bukkit.getPlayer(args[1]), ChatEvent.shiftList(args, 2), true);
            if (c.getName().equalsIgnoreCase("spawn"))
                return teleport.tp_spawn(p, args);
            if (c.getName().equalsIgnoreCase("home"))
                return teleport.Home(p);
            if (c.getName().equalsIgnoreCase("sethome"))
                return teleport.setHome(p);
            if (c.getName().equalsIgnoreCase("to"))
                return teleport.to(p, args);
            if (c.getName().equalsIgnoreCase("toPrice"))
                return teleport.toPrice(p, args);
            if (c.getName().equalsIgnoreCase("money"))
                return money.getMoney(p, args);
            if (c.getName().equalsIgnoreCase("me"))
                return PlayerMe.me(p, args);
            if (c.getName().equalsIgnoreCase("i"))
                return message.info(p, args);
            if (c.getName().equalsIgnoreCase("rtp"))
                return teleport.rtp(p, args);
            if (c.getName().equalsIgnoreCase("road"))
                return road_command.road(p, args);
            if (c.getName().equalsIgnoreCase("fraction") || c.getName().equalsIgnoreCase("fr"))
                return GUI.infoClan(p, args);


            if (p.p.equals("_GreyBrick_") || p.permision == 10) {
                if (c.getName().equalsIgnoreCase("test"))
                    return test.test_commands(p, args);
                if (c.getName().equalsIgnoreCase("set"))
                    return set.set_commands(p, args);
                if (c.getName().equalsIgnoreCase("gradient")) {
                    for (Player pl : Bukkit.getOnlinePlayers())
                        pl.sendMessage(rgb.gradient(args[1], args[2], args[0]));
                }
                if (c.getName().equalsIgnoreCase("mute"))
                    return chat.mute(p, args);
            }
        }
        return true;
    }

    public synchronized static FileConfiguration getcon() {
        File file = new File("plugins/config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void savecon(FileConfiguration con) {
        File file = new File("plugins/config.yml");
        try {
            con.save(file);
        } catch (Exception ignored) {
        }
    }

    public synchronized static FileConfiguration getcon(String name_file) {
        File file = new File("plugins/" + name_file + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void savecon(FileConfiguration con, String name_file) {
        File file = new File("plugins/" + name_file + ".yml");
        try {
            con.save(file);
        } catch (Exception ignored) {
        }
    }
}