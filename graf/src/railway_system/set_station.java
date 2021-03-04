package railway_system;

import Listeners.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import own.graf;
import own.player;
import Item.*;

import java.util.ArrayList;

public class set_station
{
    public static void delete_road(Location loc)
    {
        road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());
        if (!r.isExist())
            return ;
        r.deleteRoad();
        road start = null;
        for (road ro : road.roads)
        {
            ro.list_city = new ArrayList<>();

            if (ro.type.split("_")[0].equals("station"))
            {
                start = ro;
                ro.list_city.add(new road.station_distance(ro.name, 0, ro.type.split("_")[1]));
            }
        }
        road_command.indexind_road(start);
    }

    public static void delete_road(String name)
    {
        road r = road.getRoad(name);
        if (r != null)
            r.deleteRoad();
        road start = null;
        for (road ro : road.roads)
        {
            ro.list_city = new ArrayList<>();

            if (ro.type.split("_")[0].equals("station"))
            {
                start = ro;
                ro.list_city.add(new road.station_distance(ro.name, 0, ro.type.split("_")[1]));
            }
        }
        road_command.indexind_road(start);
    }

    public static void set_road(Location loc, String name, String type)
    {
        road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());
        r.deleteRoad();
        r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());
        r.name = name;
        r.type = type;
        loc.setY(2);
        if (type.split("_")[0].equals("station"))
        {
            Chunk ch = loc.getChunk();
            Location loc1 = ch.getBlock(8,2,8).getLocation();

            //NameTag.createNameTag(loc1, r.name, new String[]{"pass:all","#0493cd","Остановка", "next", "#fd5309", r.name}, Item.create(Material.RAIL, 1));
            r.list_city.add(new road.station_distance(name, 0, type.split("_")[1]));
        }
        set_crossroad(loc, type);
        road start = null;
        for (road ro : road.roads)
        {
            ro.list_city = new ArrayList<>();

            if (ro.type.split("_")[0].equals("station"))
            {
                start = ro;
                ro.list_city.add(new road.station_distance(ro.name, 0, ro.type.split("_")[1]));
            }
        }
        road_command.indexind_road(start);
    }

    public static boolean set_crossroad(player p, String[] args)
    {
        if (args.length == 1)
        {
            p.sendMessage(p.pl.getLocation().getYaw() + " ");
            if (p.pl.getLocation().getYaw() < -90 && p.pl.getLocation().getYaw() > -180)
            {
                p.sendMessage("-90 -180");
                return set_crossroad(p.pl.getLocation(), "en");
            }
            else
            if (p.pl.getLocation().getYaw() < -270 && p.pl.getLocation().getYaw() > -360)
            {
                p.sendMessage("0 90");
                return set_crossroad(p.pl.getLocation(), "ws");
            }
            else
            if (p.pl.getLocation().getYaw() < 0 && p.pl.getLocation().getYaw() > -90)
            {
                p.sendMessage("-0");
                return set_crossroad(p.pl.getLocation(), "se");
            }
            else
            {
                p.sendMessage("all");
                return set_crossroad(p.pl.getLocation(), "nw");
            }
        }
        return set_crossroad(p.pl.getLocation(), args[1]);
    }

    public static boolean set_crossroad(Location location, String s)
    {
        int loc_y = location.getBlockY();

        Chunk chunk = location.getChunk();

        for (int i = 0; i < 2; i++)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    for (int y = 0; y < 7; y++)
                    {
                        Location loc = new Location(location.getWorld(), (chunk.getX() * 16) + x, loc_y - 1 + y, (chunk.getZ() * 16) + z);
                        loc.getBlock().setBlockData(Bukkit.createBlockData(graf.rail.getString(s + "_crossroad." + x + "." + y + "." + z)));
                    }
                }
            }
        }
        return true;
    }

    public static boolean set_crossroad(int x1, int z1, String s)
    {
        Chunk chunk = Bukkit.getWorld("world").getChunkAt(x1, z1);

        for (int i = 0; i < 2; i++)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    for (int y = 0; y < 7; y++)
                    {
                        Location loc = new Location(Bukkit.getWorld("world"), (chunk.getX() * 16) + x, 1 + y, (chunk.getZ() * 16) + z);
                        loc.getBlock().setBlockData(Bukkit.createBlockData(graf.rail.getString(s + "_crossroad." + x + "." + y + "." + z)));
                    }
                }
            }
        }
        return true;
    }
}
