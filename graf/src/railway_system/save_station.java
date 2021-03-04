package railway_system;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import own.graf;
import own.player;

public class save_station
{

    public static boolean save_crossroad(player p, String[] args)
    {
        return save_crossroad(p.pl.getLocation(), args[1]);
    }

    public static boolean save_crossroad(Location location, String s)
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
                        graf.rail.set(s + "_crossroad." + x + "." + y + "." + z, loc.getBlock().getBlockData().getAsString());
                    }
                }
            }
        }
        return true;
    }
}
