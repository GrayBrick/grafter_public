package railway_system;

import Listeners.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import own.graf;
import own.reg;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class road
{
    public static ArrayList<road>   roads = new ArrayList<>();

    public String                       type;
    public String                       name;

    public ArrayList<station_distance>  list_city;

    public int                          x;
    public int                          z;

    public static class station_distance
    {
        public String   station;
        public int      distance;
        public String   facing;

        public station_distance(String station, int distance, String facing)
        {
            this.station = station;
            this.distance = distance;
            this.facing = facing;
        }
    }

    public static String getRandName()
    {
        while (true)
        {
            boolean f = false;
            String name = Integer.toHexString((int) (Math.random() * 20000000));
            for (road r : roads)
            {
                if (r.name.equals(name))
                {
                    f = true;
                    break ;
                }
            }
            if (!f)
                return name;
        }
    }

    public boolean addCity(ArrayList<station_distance> list, String facing, int distance)
    {
        boolean f = false;
        for (station_distance sd : list)
        {
            boolean f1 = false;
            for (station_distance sd1 : list_city)
            {
                if (sd.station.equals(sd1.station))
                {
                    f1 = true;
                    if ((sd.distance + distance) < sd1.distance)
                    {
                        f = true;
                        sd1.distance = sd.distance + distance;
                        sd1.facing = facing;
                    }
                }
            }
            if (!f1)
            {
                f = true;
                list_city.add(new station_distance(sd.station, sd.distance + distance, facing));
            }
        }
        return f;
    }

    public int  getDistance(road r)
    {
        int xd = Math.abs(x - r.x);
        int zd = Math.abs(z - r.z);

        return xd + zd;
    }

    public static road getRoad(int x, int z)
    {
        for (road r : roads)
        {
            if (r.x == x && r.z == z)
                return r;
        }
        return new road(x, z);
    }

    public static road getRoad(String name)
    {
        for (road r : roads)
        {
            if (r.name.equals(name))
                return r;
        }
        return null;
    }

    public road(int x, int z)
    {
        this.x = x;
        this.z = z;
        iniRoad();
    }

    public road(int x, int z, String name)
    {
        this.x = x;
        this.z = z;
        this.name = name;
    }

    public boolean isExist()
    {
        if (name == null)
        {
            this.deleteRoad();
            return false;
        }
        return true;
    }

    private void iniRoad()
    {
        name = graf.rail.getString(x + "." + z + ".name");
        type = graf.rail.getString(x + "." + z + ".type");

        List<String> citys = (List<String>) graf.rail.getList(x + "." + z + ".list_city");

        list_city = new ArrayList<>();
        if (citys != null)
        {
            for (String str : citys)
                list_city.add(new station_distance(str.split("-")[0], Integer.parseInt(str.split("-")[1]), str.split("-")[2]));
        }

        roads.add(this);
    }

    public void deleteRoad()
    {
        if (type != null && name != null && type.split("_")[0].equals("station"))
            NameTag.deleteNameTag(name);
        graf.rail.set(x + "." + z, null);
        roads.remove(this);
    }

    private void saveRoad(FileConfiguration config)
    {
        if (name == null)
            return ;
        config.set(x + "." + z + ".name", name);
        config.set(x + "." + z + ".type", type);

        List<String> list = new ArrayList<>();

        if (list_city != null)
            for (station_distance sd : list_city)
                list.add(sd.station + "-" + sd.distance + "-" + sd.facing);

        config.set(x + "." + z + ".list_city", list);
    }

    public synchronized static void getconRail()
    {
        File file = new File("plugins/rail.yml");
        graf.rail =  YamlConfiguration.loadConfiguration(file);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (String s : graf.rail.getKeys(false))
                {
                    if (!Character.isDigit(s.charAt(s.length() - 1)))
                        continue ;
                    for (String s1 : Objects.requireNonNull(graf.rail.getConfigurationSection(s)).getKeys(false))
                    {
                        if (!Character.isDigit(s1.charAt(s1.length() - 1)))
                            continue ;
                        getRoad(Integer.parseInt(s), Integer.parseInt(s1));
                    }
                }
            }
        }.runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")));
    }

    public static void saveconRoads()
    {
        for (road r : roads)
            r.saveRoad(graf.rail);
    }
}
