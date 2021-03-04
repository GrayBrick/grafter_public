package Contest;

import org.bukkit.Location;
import own.graf;
import own.player;

import java.io.File;
import java.util.ArrayList;

public class Contest
{
    public static class Arena
    {
        public ArrayList<Location> spawn_points;

        public Arena(ArrayList<Location> spawn_points)
        {
            this.spawn_points = spawn_points;
        }
    }
    public ArrayList<player> que_players = new ArrayList<>();

    public static void set(String file_name)
    {
        graf.getcon("Contest/Arena");
    }
}
