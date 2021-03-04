package commands;

import Item.Item;
import clans.Clan;
import clans.SortClans;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import own.chat;
import own.graf;
import own.player;
import own.reg;
import text_processing.rgb;
import text_processing.rgb_map;

import java.text.DecimalFormat;
import java.util.*;

public class test {
	
	static int count = 0;
	
	public static boolean test_commands(player p, String[] args)
	{
		if (args.length == 0)
			return len_0(p, args);
		if (args.length == 1)
			return len_1(p, args);
		return len_more_1(p, args);
	}

	public static class ClanMap extends MapRenderer
	{
		public boolean render = false;

		private static class Position
        {
            int         x;
            int         z;
            int         distance;
            byte        color;

            public Position(int x, int z, int distance, byte color)
            {
                this.x = x;
                this.z = z;
                this.distance = distance;
                this.color = color;
            }
        }

		@Override
		public void render(MapView map, MapCanvas canvas, Player p)
		{
			if (render)
				return ;
			else
				render = true;

			ArrayList<Position> clans_map = new ArrayList<>();
			for (Clan clan : Clan.clans)
                clans_map.add(new Position(clan.getCenter().getBlockX(), clan.getCenter().getBlockZ(), clan.getRadius(), clan.getColor()));

			for (int x = 0; x < 128; x++)
			{
				for (int z = 0; z < 128; z++)
				{
					int real_x = 0;
					int real_z = 0;

					if (map.getScale().equals(MapView.Scale.FARTHEST))
					{
						real_x = (map.getCenterX() - 1024) + (x * 16);
						real_z = (map.getCenterZ() - 1024) + (z * 16);
					}
					else
					if (map.getScale().equals(MapView.Scale.FAR))
					{
						real_x = (map.getCenterX() - 512) + (x * 8);
						real_z = (map.getCenterZ() - 512) + (z * 8);
					}
					else
					if (map.getScale().equals(MapView.Scale.NORMAL))
					{
						real_x = (map.getCenterX() - 256) + (x * 4);
						real_z = (map.getCenterZ() - 256) + (z * 4);
					}
					else
					if (map.getScale().equals(MapView.Scale.CLOSE))
					{
						real_x = (map.getCenterX() - 128) + (x * 2);
						real_z = (map.getCenterZ() - 128) + (z * 2);
					}
					else
					if (map.getScale().equals(MapView.Scale.CLOSEST))
					{
						real_x = (map.getCenterX() - 64) + x;
						real_z = (map.getCenterZ() - 64) + z;
					}

					if (real_x >= graf.size_world || real_x <= -graf.size_world || real_z >= graf.size_world || real_z <= -graf.size_world)
					{
//						canvas.setPixel(x, z, (byte) 116);
//						continue;
					}

					Biome biome = new Location(Bukkit.getWorld("world"), real_x, 245, real_z).getBlock().getBiome();

                    byte color = getBiomColor(biome);

					canvas.setPixel(x, z, color);

					for (Position pos : clans_map)
                    {
                        int mx = Math.abs(pos.x - real_x);
                        int mz = Math.abs(pos.z - real_z);
                        int distance = (int) Math.sqrt((mx * mx) + (mz * mz));

                        int shir = 32;

                        if (distance < pos.distance && (x + z) % shir > shir / 2)
                            canvas.setPixel(x, z, pos.color);
						if (distance < pos.distance && (((x + z) % shir) == (shir - 16) || ((x + z) % shir) == 0))
							canvas.setPixel(x, z, (byte) (pos.color + 1));
                    }
				}
			}

			for (int x = 0; x < 128; x++)
			{
				for (int z = 0; z < 128; z++)
				{
					int real_x = 0;
					int real_z = 0;

					if (map.getScale().equals(MapView.Scale.FARTHEST))
					{
						real_x = (map.getCenterX() - 1024) + (x * 16);
						real_z = (map.getCenterZ() - 1024) + (z * 16);
					}
					else
					if (map.getScale().equals(MapView.Scale.FAR))
					{
						real_x = (map.getCenterX() - 512) + (x * 8);
						real_z = (map.getCenterZ() - 512) + (z * 8);
					}
					else
					if (map.getScale().equals(MapView.Scale.NORMAL))
					{
						real_x = (map.getCenterX() - 256) + (x * 4);
						real_z = (map.getCenterZ() - 256) + (z * 4);
					}
					else
					if (map.getScale().equals(MapView.Scale.CLOSE))
					{
						real_x = (map.getCenterX() - 128) + (x * 2);
						real_z = (map.getCenterZ() - 128) + (z * 2);
					}
					else
					if (map.getScale().equals(MapView.Scale.CLOSEST))
					{
						real_x = (map.getCenterX() - 64) + x;
						real_z = (map.getCenterZ() - 64) + z;
					}

					if (real_x >= graf.size_world || real_x <= -graf.size_world || real_z >= graf.size_world || real_z <= -graf.size_world)
					{
//						canvas.setPixel(x, z, (byte) 116);
//						continue;
					}

					for (Position pos : clans_map)
					{
						int mx = Math.abs(pos.x - real_x);
						int mz = Math.abs(pos.z - real_z);
						int distance = (int) Math.sqrt((mx * mx) + (mz * mz));

						int shir = 32;

						if (distance > pos.distance - (pos.distance / shir) && distance <= pos.distance)
							canvas.setPixel(x, z, (byte) 116);
						if (distance > pos.distance - ((pos.distance / shir) / 2) && distance <= pos.distance)
							canvas.setPixel(x, z, (byte) 34);
						if (distance > pos.distance - ((pos.distance / shir) / 4) && distance <= pos.distance)
							canvas.setPixel(x, z, (byte) 116);
					}
				}
			}
//
//			for (String uid : graf.Clans.getKeys(false))
//            {
//                Clan clan = Clan.getClan(uid);
//                if (clan.isExist())
//                {
//                    Location loc = clan.getCenter();
//                    int x = (loc.getBlockX() - (map.getCenterX() - 512)) / 8;
//                    int z = (loc.getBlockZ() - (map.getCenterZ() - 512)) / 8;
//
//                    canvas.setPixel(x, z, (byte) (clan.getColor() + 10));
//                }
//            }
		}
	}

	public static byte getBiomColor(Biome biome)
    {
        byte color = 0;
        if (biome.equals(Biome.RIVER) || biome.equals(Biome.FROZEN_RIVER))
        {
            color = 50;
        }
        else if (biome.equals(Biome.JUNGLE) || biome.equals(Biome.JUNGLE_EDGE) || biome.equals(Biome.JUNGLE_HILLS) || biome.equals(Biome.BAMBOO_JUNGLE_HILLS) || biome.equals(Biome.BAMBOO_JUNGLE) || biome.equals(Biome.MODIFIED_JUNGLE)  || biome.equals(Biome.MODIFIED_JUNGLE_EDGE))
        {
            color = -124;
        }
        else if (biome.equals(Biome.OCEAN) || biome.equals(Biome.DEEP_OCEAN))
        {
            color = 48;
        }
        else if (biome.equals(Biome.COLD_OCEAN) || biome.equals(Biome.DEEP_COLD_OCEAN))
        {
            color = -126;
        }
        else if (biome.equals(Biome.DEEP_LUKEWARM_OCEAN) || biome.equals(Biome.LUKEWARM_OCEAN))
        {
            color = -35;
        }
        else if (biome.equals(Biome.WARM_OCEAN) || biome.equals(Biome.DEEP_WARM_OCEAN))
        {
            color = -34;
        }
        else if (biome.equals(Biome.DEEP_FROZEN_OCEAN) || biome.equals(Biome.FROZEN_OCEAN) || biome.equals(Biome.ICE_SPIKES))
        {
            color = 70;
        }
        else if (biome.equals(Biome.BEACH))
        {
            color = 9;
        }
        else if (biome.equals(Biome.SNOWY_BEACH))
        {
            color = 22;
        }
        else if (biome.equals(Biome.SNOWY_TUNDRA) || biome.equals(Biome.SNOWY_MOUNTAINS) || biome.equals(Biome.SNOWY_TAIGA) || biome.equals(Biome.SNOWY_TAIGA_HILLS) || biome.equals(Biome.SNOWY_TAIGA_MOUNTAINS))
        {
            color = 34;
        }
        else if (biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS) || biome.equals(Biome.DESERT_LAKES))
        {
            color = 10;
        }
        else if (biome.equals(Biome.PLAINS) || biome.equals(Biome.SUNFLOWER_PLAINS))
        {
            color = -121;
        }
        else if (biome.equals(Biome.WOODED_HILLS) || biome.equals(Biome.FOREST) || biome.equals(Biome.FLOWER_FOREST) || biome.equals(Biome.BIRCH_FOREST) || biome.equals(Biome.BIRCH_FOREST_HILLS) || biome.equals(Biome.DARK_FOREST) || biome.equals(Biome.DARK_FOREST_HILLS) || biome.equals(Biome.TALL_BIRCH_FOREST) || biome.equals(Biome.TALL_BIRCH_HILLS))
        {
            color = 28;
        }
        else if (biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_PLATEAU) || biome.equals(Biome.SHATTERED_SAVANNA) || biome.equals(Biome.SHATTERED_SAVANNA_PLATEAU))
        {
            color = 75;
        }
        else if (biome.equals(Biome.MOUNTAIN_EDGE) || biome.equals(Biome.MOUNTAINS) || biome.equals(Biome.GRAVELLY_MOUNTAINS) || biome.equals(Biome.MODIFIED_GRAVELLY_MOUNTAINS) || biome.equals(Biome.TAIGA_MOUNTAINS) || biome.equals(Biome.WOODED_MOUNTAINS) || biome.equals(Biome.STONE_SHORE))
        {
            color = 24;
        }
        else if (biome.equals(Biome.SWAMP) || biome.equals(Biome.SWAMP_HILLS))
        {
            color = 109;
        }
        else if (biome.equals(Biome.TAIGA) || biome.equals(Biome.TAIGA_HILLS))
        {
            color = 31;
        }
        else if (biome.equals(Biome.GIANT_TREE_TAIGA_HILLS) || biome.equals(Biome.GIANT_TREE_TAIGA) || biome.equals(Biome.GIANT_SPRUCE_TAIGA) || biome.equals(Biome.GIANT_SPRUCE_TAIGA_HILLS))
        {
            color = -107;
        }
        else if (biome.equals(Biome.MUSHROOM_FIELD_SHORE) || biome.equals(Biome.MUSHROOM_FIELDS))
        {
            color = -43;
        }
		else if (biome.name().contains("BADLANDS"))
		{
			color = -108;
		}
        return color;
    }

	public static boolean len_0(player p, String[] args)
	{
		for (int i = 0; i < 500; i++)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					TNTPrimed en = (TNTPrimed) Bukkit.getWorld("world").spawnEntity(p.pl.getLocation(), EntityType.PRIMED_TNT);
					en.setFuseTicks((int) (Math.random() * 20 * 30));
				}
			}.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), i / 2);
		}


//		p.sendMessage(Clan.isCanCreateClan(
//                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockX(),
//                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockZ()
//        ) + "");
//		p.sendMessage(reg.getReg(p.pl.getLocation()).getCenter() + "");
		return true;
	}

	public static MapView getClanMap(int x, int z, int i, int map_count)
	{
		MapView []mapV = new MapView[]{Bukkit.getMap(1000 + map_count)};
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				MapView mv = mapV[0]; //Bukkit.getMap(600 + (x * 10) + z);
				if (i == 4)
					mv.setScale(MapView.Scale.FARTHEST);
				else
				if (i == 8)
					mv.setScale(MapView.Scale.FAR);
				else
				if (i == 16)
					mv.setScale(MapView.Scale.NORMAL);
				else
				if (i == 32)
					mv.setScale(MapView.Scale.CLOSE);
				else
				if (i == 64)
					mv.setScale(MapView.Scale.CLOSEST);

				int size = 2;
				if (mv.getScale().equals(MapView.Scale.FARTHEST))
				{
					mv.setCenterX(((x * 2048) + 1024) - 4096 * size);
					mv.setCenterZ(((z * 2048) + 1024) - 4096 * size);
				}
				if (mv.getScale().equals(MapView.Scale.FAR))
				{
					mv.setCenterX(((x * 1024) + 512) - 4096 * size);
					mv.setCenterZ(((z * 1024) + 512) - 4096 * size);
				}
				if (mv.getScale().equals(MapView.Scale.NORMAL))
				{
					mv.setCenterX(((x * 512) + 256) - 4096 * size);
					mv.setCenterZ(((z * 512) + 256) - 4096 * size);
				}
				if (mv.getScale().equals(MapView.Scale.CLOSE))
				{
					mv.setCenterX(((x * 256) + 128) - 4096 * size);
					mv.setCenterZ(((z * 256) + 128) - 4096 * size);
				}
				if (mv.getScale().equals(MapView.Scale.CLOSEST))
				{
					mv.setCenterX(((x * 128) + 64) - 4096 * size);
					mv.setCenterZ(((z * 128) + 64) - 4096 * size);
				}
				mv.setUnlimitedTracking(false);
				mv.setTrackingPosition(false);
				mv.getRenderers().clear();
				mv.addRenderer(new ClanMap());
				mv.setLocked(true);

			}
		}.runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")));

		return mapV[0];
	}
	
	public static boolean len_1(player p, String[] args)
	{
//		p.pl.getActivePotionEffects().clear();
//		p.pl.addPotionEffect(new PotionEffect(PotionEffectType., 100, 100, true));
		p.pl.getActivePotionEffects().forEach(a -> p.sendMessageE(a.getType().getName() + ""));

		return true;
	}

	public static void setMap(int x, int z, int i, int map_count, Location loc)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
                ItemStack map_item = Item.create(Material.FILLED_MAP, 1);

                MapView mv = getClanMap(x, z, i, map_count);
                MapMeta mapMeta = (MapMeta) map_item.getItemMeta();
                mapMeta.setMapView(mv);
                map_item.setItemMeta(mapMeta);

                loc.add(x, 0, z);
                boolean f = false;
                for (Entity en : loc.getWorld().getNearbyEntities(BoundingBox.of(loc.getBlock())))
                {
                    if (en instanceof ItemFrame)
                    {
                        ItemFrame frame = (ItemFrame) en;
                        ItemStack map_item_frame = frame.getItem();
                        if (map_item_frame.getType().equals(Material.FILLED_MAP))
                        {
                            map_item_frame.setItemMeta(mapMeta);
                        }
                        else
                            frame.setItem(map_item);
                        f = true;
                        break ;
                    }
                }
                if (!f)
                {
                    ItemFrame frame = (ItemFrame) loc.getWorld().spawnEntity(loc, EntityType.ITEM_FRAME);
                    frame.setVisible(true);
                    frame.setItem(map_item);
                }
			}
		}.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), map_count);
	}

	public static void test_go(ArrayList<Location> loc_list)
	{
		Particle part = Particle.BARRIER;//.values()[(int) (Math.random() * Particle.values().length)];
		final int[] count1 = new int[]{0};
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (count1[0]++ > 10)
					cancel();
				ArrayList<Material> blocks = new ArrayList<>();

				blocks.add(Material.WHITE_CONCRETE);
				blocks.add(Material.CYAN_CONCRETE);
				blocks.add(Material.WHITE_CONCRETE);
				blocks.add(Material.MAGENTA_CONCRETE);
				blocks.add(Material.ORANGE_CONCRETE);
				blocks.add(Material.LIME_CONCRETE);

				final int[]count = new int[]{0};

				while (count[0] < loc_list.size())
				{
					for (Player pl : Bukkit.getOnlinePlayers())
					{
						pl.spawnParticle(part, loc_list.get(count[0]), 1);
					}
					count[0]++;
				}
				cancel();
			}
		}.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 60);
		Bukkit.getPlayer("_GreyBrick_").sendMessage(part + "");
	}
	
	public static boolean len_more_1(player p, String[] args){
		if (args.length == 5)
		{
			final int[] count = new int[]{0};

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					String s1 = "";
					for (int i = 0; i < Integer.parseInt(args[2]); i++)
						s1 += " ";
					String s2 = "";
					for (int i = 0; i < Integer.parseInt(args[3]); i++)
						s2 += " ";
					String s3 = "";
					for (int i = 0; i < Integer.parseInt(args[4]); i++)
						s3 += "\n";
					count[0]++;
					p.sendMessage(rgb.gradientShift(s3 + s1 + "Добро пожаловать на сервер GrafTer!" + s1 + "\n" + s2 + "Приятной игры" + s2, chat.title, Integer.parseInt(args[0]), count[0]));
					if (count[0] > Integer.parseInt(args[1]))
					{
						p.sendMessage(s3 + "");
						cancel();
					}
				}
			}.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 2);

			return true;
		}
//		Location loc = p.pl.getLocation();
//
//		loc.setX(loc.getX() + 2);
//		loc.setY(loc.getY() + 2);
//
//		p.pl.spawnParticle(Particle.values()[29], loc, 100, new Particle.DustOptions(Color.fromRGB(
//				rgb.get_rgb(args[0])[0],
//				rgb.get_rgb(args[0])[1],
//				rgb.get_rgb(args[0])[2]
//		), Integer.parseInt(args[1])));

//		if (args.length == 4)
//		{
//			rgb.gradientTitle(p.pl, new String[]{"Grafter", "Добро пожаловать"}, chat.title_inv, Double.parseDouble(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
//			return true;
//		}
		ItemStack map_item = Item.create(Material.FILLED_MAP, 1);

		MapMeta mapMeta = (MapMeta) map_item.getItemMeta();
		mapMeta.setMapView(getClanMap(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 4, 600));
		map_item.setItemMeta(mapMeta);

		p.pl.getInventory().addItem(map_item);

		if (args[0].equalsIgnoreCase("s"))
		{
			DecimalFormat df = new DecimalFormat("###.##");
			if (args.length == 4)
			{

				count = Integer.parseInt(args[3]);
				p.sendMessage( df.format(0.5 + (1.5 * (1 - (p.pl.getLocation().getPitch() + 90) / 180.0))) + " ");
				p.pl.playSound(p.pl.getLocation(),
						Sound.values()[Integer.parseInt(args[3])],
						Float.parseFloat(args[1]),
						(float) (0.5 + (1.5 * (1 - (p.pl.getLocation().getPitch() + 90) / 180.0))));
			}
			else
			{
				p.sendMessage( df.format(0.5 + (1.5 * (1 - (p.pl.getLocation().getPitch() + 90) / 180.0))) + " " + count + " " + Sound.values()[count].name());
				p.pl.playSound(p.pl.getLocation(),
						Sound.values()[count++],
						Float.parseFloat(args[1]),
						(float) (0.5 + (1.5 * (1 - (p.pl.getLocation().getPitch() + 90) / 180.0))));
			}
		}
		if (args[0].equalsIgnoreCase("load"))
		{
			timer(Integer.parseInt(args[1]));
		}
		return true;
	}

	public static void timer(int i)
	{
		final int[] count = new int[]{0,0};
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (count[0] != i)
					count[0]++;
				else
				{
					count[0] = 0;
					count[1]++;
				}
				if (Bukkit.getPlayer("_GreyBrick_").isOnline())
					Bukkit.getPlayer("_GreyBrick_").teleport(new Location(Bukkit.getWorld("world"), count[0] * 32 - graf.size_world, 100, count[1] * 32 - graf.size_world));
				//Bukkit.getPlayer("_GreyBrick_").sendMessage(count[0] + " " + count[1]);
				if (count[0] == i && count[1] == i)
					cancel();
			}
		}.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 1);
	}
}
