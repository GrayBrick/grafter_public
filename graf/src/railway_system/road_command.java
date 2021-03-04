package railway_system;

import Item.Item;
import Listeners.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import own.chat;
import own.player;
import text_processing.rgb;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class road_command implements Listener
{
    public static boolean road(player p, String[] args)
    {
        if (p.permision == 10)
        {
            if (args.length == 6)
            {
                if (args[0].equalsIgnoreCase("set"))
                {
                    int cx1 = Integer.parseInt(args[2]);
                    int cz1 = Integer.parseInt(args[3]);

                    int cx2 = Integer.parseInt(args[4]);
                    int cz2 = Integer.parseInt(args[5]);

                    for (int x = 0; x < (Math.abs(cx1 - cx2) + 1); x++)
                    {
                        for (int z = 0; z < (Math.abs(cz1 - cz2) + 1); z++)
                        {
                            p.sendMessage(p.pl.getWorld().getChunkAt(Math.min(cx1, cx2), Math.min(cz1, cz2)).getBlock(8, 8, 8).getLocation() + "");
                            set_station.set_road(p.pl.getWorld().getChunkAt(Math.min(cx1, cx2) + x, Math.min(cz1, cz2) + z).getBlock(8, 8, 8).getLocation(),  road.getRandName(), args[1]);
                        }
                    }
                }
            }
            if (args.length == 5)
            {
                if (args[0].equalsIgnoreCase("delete"))
                {
                    int cx1 = Integer.parseInt(args[1]);
                    int cz1 = Integer.parseInt(args[2]);

                    int cx2 = Integer.parseInt(args[3]);
                    int cz2 = Integer.parseInt(args[4]);

                    for (int x = 0; x < (Math.abs(cx1 - cx2) + 1); x++)
                    {
                        for (int z = 0; z < (Math.abs(cz1 - cz2) + 1); z++)
                        {
                            set_station.delete_road(p.pl.getWorld().getChunkAt(Math.min(cx1, cx2) + x, Math.min(cz1, cz2) + z).getBlock(8, 8, 8).getLocation());
                        }
                    }
                }
                return true;
            }
            if (args.length == 3)
            {
                if (args[0].equalsIgnoreCase("set"))
                {
                    set_station.set_road(p.pl.getLocation(),  args[1], args[2]);
                }
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("set"))
                {
                    set_station.set_road(p.pl.getLocation(), road.getRandName(), args[1]);
                    return true;
                }
            }
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("delete"))
                {
                    set_station.delete_road(p.pl.getLocation());
                }
                if (args[0].equalsIgnoreCase("index"))
                {
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
                    indexind_road(start);
                }
                if (args[0].equalsIgnoreCase("name"))
                {
                    road r = road.getRoad(p.pl.getLocation().getChunk().getX(), p.pl.getLocation().getChunk().getZ());
                    p.sendMessage(r.name + " " + r.type);
                }
                if (args[0].equalsIgnoreCase("update"))
                {
                    for (road ro : road.roads)
                    {
                        if (ro.type.split("_")[0].equals("station"))
                        {
                            NameTag.deleteNameTag(ro.name);
                            Chunk ch = Bukkit.getWorld("world").getChunkAt(ro.x, ro.z);
                            Location loc1 = ch.getBlock(8,2,8).getLocation();

                            if (loc1.getChunk().isLoaded())
                                NameTag.createNameTag(loc1, ro.name, new String[]{"pass:all","#0493cd","Станция", "next", "#fd5309", ro.name}, Item.create(Material.RAIL, 1));
                        }
                        set_station.set_crossroad(ro.x, ro.z, ro.type);
                    }
                }
            }
        }
        if (args[0].equalsIgnoreCase("find"))
        {
            road ro = null;
            int distance = -1;
            for (road r : road.roads)
            {
                if (r.type.split("_")[0].equals("station"))
                {
                    Location loc = p.pl.getWorld().getChunkAt(r.x, r.z).getBlock(8, 3, 8).getLocation();
                    if (distance > loc.distance(p.pl.getLocation()) || distance == -1)
                    {
                        ro = r;
                        distance = (int) loc.distance(p.pl.getLocation());
                    }
                }
            }
            Location loc = p.pl.getWorld().getChunkAt(ro.x, ro.z).getBlock(8, 3, 8).getLocation();
            p.pl.sendMessage(rgb.gradientLight(chat.color[0], "Ближайшее метро " + ro.name + " находится в " + distance + " " + chat.getNormForm(distance, "метре", "метрах", "метрах") + " от сюда\nПо координатам " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
        }
        return true;
    }

    public static void  indexind_road(road r)
    {
        if (r.type.equals("xz"))
        {
            road_facing rf = get_crossroad(r, "e");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "n");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "w");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "s");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.equals("e"))
        {
            road_facing rf = get_crossroad(r, "s");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "e");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "n");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.equals("n"))
        {
            road_facing rf = get_crossroad(r, "e");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "n");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "w");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.equals("w"))
        {
            road_facing rf = get_crossroad(r, "n");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "w");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "s");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.equals("s"))
        {
            road_facing rf = get_crossroad(r, "w");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "s");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "e");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.equals("s"))
        {
            road_facing rf = get_crossroad(r, "w");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "s");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            rf = get_crossroad(r, "e");
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }

        if (r.type.split("_")[0].equals("station"))
        {
            road_facing rf = get_crossroad(r, r.type.split("_")[1]);
            if (rf != null && rf.r.addCity(r.list_city, rf.facing, rf.r.getDistance(r)))
                indexind_road(rf.r);

            return ;
        }
    }

    public static road_facing get_crossroad(road r, String facing)
    {
        int x = r.x + (facing.equals("n") ? 0 : facing.equals("s") ? 0 : facing.equals("w") ? -1 : 1);
        int z = r.z + (facing.equals("n") ? -1 : facing.equals("s") ? 1 : facing.equals("w") ? 0 : 0);

        String un_facing = facing.equals("e") ? "w" : facing.equals("w") ? "e" : facing.equals("s") ? "n" : "s";

        road next = road.getRoad(x, z);

        if (!next.isExist())
            return null;

        if (next.type.split("_")[0].equals("station"))
        {
            if (!next.type.split("_")[1].equals(un_facing))
                return null;
        }

        if (next.type.split("_")[0].equals("z") || next.type.split("_")[0].equals("x"))
            return get_crossroad(next, facing);

        if (next.type.split("_")[0].equals("se"))
        {
            if (facing.equals("n"))
                return get_crossroad(next, "e");
            else
                return get_crossroad(next, "s");
        }

        if (next.type.split("_")[0].equals("ws"))
        {
            if (facing.equals("e"))
                return get_crossroad(next, "s");
            else
                return get_crossroad(next, "w");
        }

        if (next.type.split("_")[0].equals("nw"))
        {
            if (facing.equals("s"))
                return get_crossroad(next, "w");
            else
                return get_crossroad(next, "n");
        }

        if (next.type.split("_")[0].equals("en"))
        {
            if (facing.equals("w"))
                return get_crossroad(next, "n");
            else
                return get_crossroad(next, "e");
        }

        return new road_facing(next, un_facing);
    }

    static class road_facing
    {
        public road     r;
        public String   facing;

        public road_facing(road r, String facing)
        {
            this.r = r;
            this.facing = facing;
        }
    }

    @EventHandler
    public void Active(BlockRedstoneEvent e)
    {
        Location loc = e.getBlock().getLocation();
        if (loc.getWorld().getName().equals("world") && loc.getBlockY() == 2 && e.getNewCurrent() == 15)
        {
            int x = loc.getBlockX() > 0 ? (loc.getBlockX() % 16) : (16 - (Math.abs(loc.getBlockX()) % 16));
            int z = loc.getBlockZ() > 0 ? (loc.getBlockZ() % 16) : (16 - (Math.abs(loc.getBlockZ()) % 16));

            if ((x == 3 && z == 9) || (x == 9 && z == 12) || (x == 12 && z == 6) || (x == 6 && z == 3))
                activate_rail(loc, x, z, e);
            else
            if ((x == 6 && z == 9) || (x == 9 && z == 6) || (x == 6 && z == 6) || (x == 9 && z == 9))
            {
                end_go(loc, x, z);
                e.setNewCurrent(0);
            }

            {
                Chunk ch = loc.getChunk();

                road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());

                if (!r.isExist())
                    return ;
                for (Entity en : loc.getWorld().getNearbyEntities(ch.getBlock(x, 2, z).getBoundingBox()))
                {
                    if (en instanceof Minecart)
                    {
                        if (en.getPassengers().size() == 0)
                        {
                            en.remove();
                            return ;
                        }
                        if (r.type.equals("z") || r.type.equals("x"))
                        {
                            if (((Minecart) en).getMaxSpeed() == 0.4)
                                ((Minecart) en).setMaxSpeed(1.5);
                        }
                        else
                        {
                            ((Minecart) en).setMaxSpeed(0.4);
                        }
                    }
                }
            }
        }
    }

    private void end_go(Location loc, int x, int z)
    {
        Chunk ch = loc.getChunk();
        road r = road.getRoad(ch.getX(), ch.getZ());

        if (!r.isExist() || !r.type.split("_")[0].equals("station"))
            return ;

        ArrayList<player> ps = new ArrayList<>();
        ArrayList<Minecart> cars = new ArrayList<>();

        String facing = r.type.split("_")[1];

        for (Entity en : loc.getWorld().getNearbyEntities(BoundingBox.of(loc.getBlock())))
        {
            if (en instanceof Minecart)
            {
                cars.add((Minecart) en);
                if (en.getPassengers().size() != 0 && en.getPassengers().get(0) instanceof Player)
                    ps.add(player.getPlayer((Player) en.getPassengers().get(0)));
            }
        }

        ps.forEach(player -> player.go_to_station = false);

        switch (facing)
        {
            case "e":
            {
                cars.forEach(Entity::remove);
                Location loc_tp = ch.getBlock(4, 2, 6).getLocation();

                loc_tp.setX(loc_tp.getX() + 0.5);
                loc_tp.setZ(loc_tp.getZ() + 0.5);

                ps.forEach(player -> player.pl.teleport(loc_tp));
                ps.forEach(player -> player.sendMessageG(rgb.gradientLight(chat.color[0], "Вы приехали на станцию " + r.name)));
                break ;
            }

            case "w":
            {
                cars.forEach(Entity::remove);
                Location loc_tp = ch.getBlock(11, 2, 9).getLocation();

                loc_tp.setX(loc_tp.getX() + 0.5);
                loc_tp.setZ(loc_tp.getZ() + 0.5);

                ps.forEach(player -> player.pl.teleport(loc_tp));
                ps.forEach(player -> player.sendMessageG(rgb.gradientLight(chat.color[0], "Вы приехали на станцию " + r.name)));
                break ;
            }

            case "n":
            {
                cars.forEach(Entity::remove);
                Location loc_tp = ch.getBlock(6, 2, 11).getLocation();

                loc_tp.setX(loc_tp.getX() + 0.5);
                loc_tp.setZ(loc_tp.getZ() + 0.5);

                ps.forEach(player -> player.pl.teleport(loc_tp));
                ps.forEach(player -> player.sendMessageG(rgb.gradientLight(chat.color[0], "Вы приехали на станцию " + r.name)));
                break ;
            }

            case "s":
            {
                cars.forEach(Entity::remove);
                Location loc_tp = ch.getBlock(9, 2, 4).getLocation();

                loc_tp.setX(loc_tp.getX() + 0.5);
                loc_tp.setZ(loc_tp.getZ() + 0.5);

                ps.forEach(player -> player.pl.teleport(loc_tp));
                ps.forEach(player -> player.sendMessageG(rgb.gradientLight(chat.color[0], "Вы приехали на станцию " + r.name)));
                break ;
            }
        }
    }

    @EventHandler
    public void click(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Location loc = e.getClickedBlock().getLocation();
            if (loc.getBlockY() == 3)
            {
                road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());

                if (!r.isExist() || !r.type.split("_")[0].equals("station"))
                    return ;

                int x = loc.getBlockX() > 0 ? (loc.getBlockX() % 16) : (16 - (Math.abs(loc.getBlockX()) % 16));
                int z = loc.getBlockZ() > 0 ? (loc.getBlockZ() % 16) : (16 - (Math.abs(loc.getBlockZ()) % 16));

                if (r.type.split("_")[1].equals("n"))
                {
                    if (x == 9 && z == 10)
                        set_inv_station(e.getPlayer());
                    return ;
                }

                if (
                        (r.type.split("_")[1].equals("w") && x == 10 && z == 6) ||
                        (r.type.split("_")[1].equals("e") && x == 5 && z == 9) ||
                        (r.type.split("_")[1].equals("s") && x == 6 && z == 5))
                        set_inv_station(e.getPlayer());
            }
        }
    }

    @EventHandler
    private static void click_station(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equals(rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), "Доступные Станции")))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                player p = player.getPlayer((Player) e.getWhoClicked());
                p.station_go = e.getCurrentItem().getItemMeta().getDisplayName();

                Location loc = p.pl.getLocation();
                Chunk ch = loc.getChunk();
                road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());

                p.pl.closeInventory();

                p.go_to_station = true;
                p.station_from = r.name;


                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!p.go_to_station)
                        {
                            cancel();
                            return ;
                        }
                        if (p.go_to_station && p.pl.getVehicle() == null)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Не выходи во время движения"));
                            p.go_to_station = false;
                            road r = road.getRoad(p.station_from);
                            Chunk ch = p.pl.getWorld().getChunkAt(r.x, r.z);

                            switch (r.type.split("_")[1])
                            {
                                case "e":
                                {
                                    Location loc_tp = ch.getBlock(4, 2, 6).getLocation();

                                    loc_tp.setX(loc_tp.getX() + 0.5);
                                    loc_tp.setZ(loc_tp.getZ() + 0.5);

                                    p.pl.teleport(loc_tp);
                                    break ;
                                }

                                case "w":
                                {
                                    Location loc_tp = ch.getBlock(11, 2, 9).getLocation();

                                    loc_tp.setX(loc_tp.getX() + 0.5);
                                    loc_tp.setZ(loc_tp.getZ() + 0.5);

                                    p.pl.teleport(loc_tp);
                                    break ;
                                }

                                case "n":
                                {
                                    Location loc_tp = ch.getBlock(6, 2, 11).getLocation();

                                    loc_tp.setX(loc_tp.getX() + 0.5);
                                    loc_tp.setZ(loc_tp.getZ() + 0.5);

                                    p.pl.teleport(loc_tp);
                                    break ;
                                }

                                case "s":
                                {
                                    Location loc_tp = ch.getBlock(9, 2, 4).getLocation();

                                    loc_tp.setX(loc_tp.getX() + 0.5);
                                    loc_tp.setZ(loc_tp.getZ() + 0.5);

                                    p.pl.teleport(loc_tp);
                                    break ;
                                }
                            }
                            cancel();
                            return ;
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 5);

                if (r.type.split("_")[1].equals("n"))
                {
                    Minecart minecart = (Minecart) loc.getWorld().spawnEntity(ch.getBlock(9, 2, 9).getLocation(), EntityType.MINECART);
                    minecart.setCustomName(p.p);
                    minecart.addPassenger(p.pl);
                    minecart.setVelocity(new Vector(0, 0, -2));
                    return ;
                }

                if (r.type.split("_")[1].equals("s"))
                {
                    Minecart minecart = (Minecart) loc.getWorld().spawnEntity(ch.getBlock(6, 2, 6).getLocation(), EntityType.MINECART);
                    minecart.setCustomName(p.p);
                    minecart.addPassenger(p.pl);
                    minecart.setVelocity(new Vector(0, 0, 2));
                    return ;
                }

                if (r.type.split("_")[1].equals("w"))
                {
                    Minecart minecart = (Minecart) loc.getWorld().spawnEntity(ch.getBlock(9, 2, 6).getLocation(), EntityType.MINECART);
                    minecart.setCustomName(p.p);
                    minecart.addPassenger(p.pl);
                    minecart.setVelocity(new Vector(-2, 0, 0));
                    return ;
                }

                if (r.type.split("_")[1].equals("e"))
                {
                    Minecart minecart = (Minecart) loc.getWorld().spawnEntity(ch.getBlock(6, 2, 9).getLocation(), EntityType.MINECART);
                    minecart.setCustomName(p.p);
                    minecart.addPassenger(p.pl);
                    minecart.setVelocity(new Vector(2, 0, 0));
                    return ;
                }
            }
        }
    }

    public static void set_inv_station(Player p)
    {
        road rd = road.getRoad(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ());

        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), "Доступные Станции"));

        int i = 0;

        DecimalFormat df = new DecimalFormat("###.##");

        for (road r : road.roads)
        {
            if (r.type.split("_")[0].equals("station") && !rd.name.equals(r.name))
            {
                for (road.station_distance stat : rd.list_city)
                {
                    if (stat.station.equals(r.name))
                        inv.setItem(i++, Item.create(Material.ACACIA_SIGN, r.name, new String[]{
                                rgb.gradientLight(chat.color[1], "Направиться к станции " + r.name),
                                rgb.gradientLight(chat.color[1], df.format((stat.distance * 16.0) / 1000.0) + " километров")}));
                }
            }
        }
        p.openInventory(inv);
    }

    public static void activate_rail(Location loc, int x, int z, BlockRedstoneEvent e)
    {
        Chunk ch = loc.getChunk();

        road r = road.getRoad(loc.getChunk().getX(), loc.getChunk().getZ());

        if (!r.isExist() || !(r.type.equals("xz") || r.type.equals("e") || r.type.equals("w") || r.type.equals("s") || r.type.equals("n")))
            return ;

        e.setNewCurrent(0);

        BoundingBox bdb = BoundingBox.of(ch.getBlock(5,2,5), ch.getBlock(10,2,10));

        new BukkitRunnable()
		{
			@Override
			public void run()
			{
                if (loc.getWorld().getNearbyEntities(bdb).size() == 0)
                {
                    cancel();
                    return ;
                }

                ArrayList<player> ps = new ArrayList<>();
                ArrayList<Minecart> cars = new ArrayList<>();

                player p = null;

                for (Entity en : loc.getWorld().getNearbyEntities(bdb))
                {
                    if (en instanceof Minecart)
                    {
                        cars.add((Minecart) en);
                        if (en.getPassengers().size() == 0)
                            en.remove();
                        if (en.getPassengers().size() != 0 && en.getPassengers().get(0) instanceof Player)
                        {
                            ps.add(player.getPlayer((Player) en.getPassengers().get(0)));

                            p = player.getPlayer((Player) en.getPassengers().get(0));

                            String facing = null;

                            for (road.station_distance stat : r.list_city)
                            {
                                if (p.station_go.equals(stat.station))
                                    facing = stat.facing;
                            }

                            int time = 3;

                            switch (facing)
                            {
                                case "e" :
                                {
                                    Block b = ch.getBlock(10, 2, 9);
                                    if (BoundingBox.of(b).overlaps(p.pl.getBoundingBox()))
                                    {
                                        b.setBlockData(Bukkit.createBlockData("rail[shape=east_west]"));
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                b.setBlockData(Bukkit.createBlockData("rail[shape=north_west]"));
                                            }
                                        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                                    }
                                    break ;
                                }

                                case "n" :
                                {
                                    Block b = ch.getBlock(9, 2, 5);
                                    if (BoundingBox.of(b).overlaps(p.pl.getBoundingBox()))
                                    {
                                        b.setBlockData(Bukkit.createBlockData("rail[shape=north_south]"));
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                b.setBlockData(Bukkit.createBlockData("rail[shape=south_west]"));
                                            }
                                        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                                    }
                                    break ;
                                }

                                case "w" :
                                {
                                    Block b = ch.getBlock(5, 2, 6);
                                    if (BoundingBox.of(b).overlaps(p.pl.getBoundingBox()))
                                    {
                                        b.setBlockData(Bukkit.createBlockData("rail[shape=east_west]"));
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                b.setBlockData(Bukkit.createBlockData("rail[shape=south_east]"));
                                            }
                                        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                                    }
                                    break ;
                                }

                                case "s" :
                                {
                                    Block b = ch.getBlock(6, 2, 10);
                                    if (BoundingBox.of(b).overlaps(p.pl.getBoundingBox()))
                                    {
                                        b.setBlockData(Bukkit.createBlockData("rail[shape=north_south]"));
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                b.setBlockData(Bukkit.createBlockData("rail[shape=north_east]"));
                                            }
                                        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                                    }
                                    break ;
                                }
                            }
                        }
                    }
                }
			}
		}.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 4, 1);
    }
}
