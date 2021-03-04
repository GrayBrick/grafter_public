package own;

import clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import text_processing.rgb;
import text_processing.time;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class reg {
	
	public static ArrayList<reg>	Regs = new ArrayList<>();
	public static int				start_price_unblock_chunk = 5;
	public static int				add_price_unblock_chunk = 3;

	public static class				arena
	{
		public boolean				isExist;
		public int[] 				center;
		public byte					radius;
		public byte					distance_up;
		public byte					distance_down;
		public ArrayList<Location> 	points;

		public arena(int[] center, byte radius, byte distance_up, byte distance_down, ArrayList<Location> points)
		{
			this.center = center;
			this.radius = radius;
			this.distance_up = distance_up;
			this.distance_down = distance_down;
			this.isExist = true;
		}

		public boolean inArena(Location loc)
		{
			int		x = loc.getBlockX();
			int		z = loc.getBlockZ();
			byte	y = (byte) loc.getBlockY();

			if (center[1] + distance_up < y ||
					center[1] - distance_down > y)
				return false;

			if (Math.sqrt(Math.pow(Math.abs(x - center[0]),2) + Math.pow(Math.abs(z - center[2]),2)) > radius)
				return false;

			return true;
		}
	}
	
	public int					x;
	public int					z;
	public int					x1;
	public int					z1;
	public String				name;
	public String				owner;
	public ArrayList<String>	members;
	public ArrayList<String>	children;
	public long					time_create;
	public long					time_keep;

	public ArrayList<arena>		arenas = new ArrayList<>();

	public Boolean				spawn_monster;
	public Boolean				player_damage;
	public Boolean				pvp;
	public Boolean				sethome;
	public Boolean				arenda;
	public Boolean				arable_dry;
	public Boolean				break_block;
	
	public static reg getReg(int x, int z)
	{
		for (reg region : Regs)
		{
			if (x >= region.x && x <= (region.x + region.x1) && z >= region.z && z <= (region.z + region.z1))
				return region;
		}
		return new reg(x, z);
	}

	public static reg getReg(String name)
	{
		for (reg regs : Regs)
		{
			if (regs.name == null)
				continue ;
			if (regs.name.equals(name))
				return regs;
		}
		return null;
	}

	public static reg getReg(Location loc)
	{
		if (!loc.getWorld().getName().equals("world"))
			return null;
		return getReg(loc.getChunk().getX(), loc.getChunk().getZ());
	}
	
	public reg(int x, int z)
	{
		this.x = x;
		this.z = z;
		iniReg();
	}
	
	private void iniReg()
	{
		x1 = graf.Regions.getInt(x + "." + z + ".x1");
		z1 = graf.Regions.getInt(x + "." + z + ".z1");

		if (graf.Regions.getString(x + "." + z + ".owner") != null)
			owner = graf.Regions.getString(x + "." + z + ".owner");

		if (graf.Regions.contains(x + "." + z + ".time_create"))
			time_create = graf.Regions.getLong(x + "." + z + ".time_create");
		else
			time_create = new Date().getTime();

		if (graf.Regions.contains(x + "." + z + ".time_keep"))
			time_keep = graf.Regions.getLong(x + "." + z + ".time_keep");
		else
			time_keep = (1000 * 60 * 60 * 24 * 3) + new Date().getTime();

		if (graf.Regions.contains(x + "." + z + ".flag.spawn_monster"))
			spawn_monster = graf.Regions.getBoolean(x + "." + z + ".flag.spawn_monster");
		else
			spawn_monster = true;

		if (graf.Regions.contains(x + "." + z + ".flag.player_damage"))
			player_damage = graf.Regions.getBoolean(x + "." + z + ".flag.player_damage");
		else
			player_damage = true;

		if (graf.Regions.contains(x + "." + z + ".flag.pvp"))
			pvp = graf.Regions.getBoolean(x + "." + z + ".flag.pvp");
		else
			pvp = false;

		if (graf.Regions.contains(x + "." + z + ".flag.sethome"))
			sethome = graf.Regions.getBoolean(x + "." + z + ".flag.sethome");
		else
			sethome = true;

		if (graf.Regions.contains(x + "." + z + ".flag.arenda"))
			arenda = graf.Regions.getBoolean(x + "." + z + ".flag.arenda");
		else
			arenda = true;

		if (graf.Regions.contains(x + "." + z + ".flag.arable_dry"))
			arable_dry = graf.Regions.getBoolean(x + "." + z + ".flag.arable_dry");
		else
			arable_dry = true;

		if (graf.Regions.contains(x + "." + z + ".flag.break_block"))
			break_block = graf.Regions.getBoolean(x + "." + z + ".flag.break_block");
		else
			break_block = true;

		if (graf.Regions.contains(x + "." + z + ".arenas"))
		{
			for (String id : graf.Regions.getConfigurationSection(x + "." + z + ".arenas").getKeys(false))
			{
				if (graf.Regions.getBoolean(x + "." + z + ".arenas." + id + ".exist"))
				{
					ArrayList<Location> points = new ArrayList<>();

					if (graf.Regions.contains(x + "." + z + ".arenas." + id + ".points"))
					{
						for (String name : graf.Regions.getConfigurationSection(x + "." + z + ".arenas." + id + ".points").getKeys(false))
						{
							points.add(new Location(
									Bukkit.getWorld("world"),
									graf.Regions.getDouble(x + "." + z + ".arenas." + id + ".points." + name + ".x"),
									graf.Regions.getDouble(x + "." + z + ".arenas." + id + ".points." + name + ".y"),
									graf.Regions.getDouble(x + "." + z + ".arenas." + id + ".points." + name + ".z"),
									(float) graf.Regions.getDouble(x + "." + z + ".arenas." + id + ".points." + name + ".yaw"),
									(float) graf.Regions.getDouble(x + "." + z + ".arenas." + id + ".points." + name + ".pitch")
							));
						}
					}
					Bukkit.getPlayer("_GreyBrick_").sendMessage(points.size() + "");

					arenas.add(new arena(
							new int[]{
									graf.Regions.getInt(x + "." + z + ".arenas." + id + ".x"),
									graf.Regions.getInt(x + "." + z + ".arenas." + id + ".y"),
									graf.Regions.getInt(x + "." + z + ".arenas." + id + ".z")
							},
							(byte) graf.Regions.getInt(x + "." + z + ".arenas." + id + ".radius"),
							(byte) graf.Regions.getInt(x + "." + z + ".arenas." + id + ".d_up"),
							(byte) graf.Regions.getInt(x + "." + z + ".arenas." + id + ".d_down"),
							points
					));
				}
			}
		}


		members = new ArrayList<>();
		members.addAll(graf.Regions.getStringList(x + "." + z + ".members"));

		children = new ArrayList<>();
		children.addAll(graf.Regions.getStringList(x + "." + z + ".children"));

		if (graf.Regions.getString(x + "." + z + ".name") != null)
			name = graf.Regions.getString(x + "." + z + ".name");

		Regs.add(this);
	}
	
	private void saveReg(FileConfiguration config)
	{
		if (owner == null)
			return ;
		config.set(x + "." + z + ".x1", x1);
		config.set(x + "." + z + ".z1", z1);

		config.set(x + "." + z + ".owner", owner);

		List<String> list = new ArrayList<>(members);
		config.set(x + "." + z + ".members", list);

		list = new ArrayList<>(children);
		config.set(x + "." + z + ".children", list);

		config.set(x + "." + z + ".name", name);

		config.set(x + "." + z + ".time_create", time_create);
		config.set(x + "." + z + ".time_keep", time_keep);

		config.set(x + "." + z + ".flag.spawn_monster", spawn_monster);
		config.set(x + "." + z + ".flag.player_damage", player_damage);
		config.set(x + "." + z + ".flag.pvp", pvp);
		config.set(x + "." + z + ".flag.sethome", sethome);
		config.set(x + "." + z + ".flag.arenda", arenda);
		config.set(x + "." + z + ".flag.arable_dry", arable_dry);
		config.set(x + "." + z + ".flag.break_block", break_block);

		for (arena ar : arenas)
		{
			if (ar.isExist)
			{
				String id = ar.center[0] + " " + ar.center[1] + " " + ar.center[2];
				config.set(x + "." + z + ".arenas." + id + ".exist", ar.isExist);

				config.set(x + "." + z + ".arenas." + id + ".x", ar.center[0]);
				config.set(x + "." + z + ".arenas." + id + ".y", ar.center[1]);
				config.set(x + "." + z + ".arenas." + id + ".z", ar.center[2]);

				config.set(x + "." + z + ".arenas." + id + ".radius", ar.radius);
				config.set(x + "." + z + ".arenas." + id + ".d_up", ar.distance_up);
				config.set(x + "." + z + ".arenas." + id + ".d_down", ar.distance_down);

				for (Location loc : ar.points)
				{
					String name = loc.getBlockX() + " " + loc.getBlockY() + loc.getBlockZ();
					config.set(x + "." + z + ".arenas." + id + ".points." + name + ".x", loc.getX());
					config.set(x + "." + z + ".arenas." + id + ".points." + name + ".y", loc.getY());
					config.set(x + "." + z + ".arenas." + id + ".points." + name + ".z", loc.getZ());
					config.set(x + "." + z + ".arenas." + id + ".points." + name + ".yaw", loc.getYaw());
					config.set(x + "." + z + ".arenas." + id + ".points." + name + ".pitch", loc.getPitch());
				}
			}
		}
	}

	public void deleteRegion()
	{
		graf.Regions.set(x + "." + z, null);
		Regs.remove(this);
	}

	public static void deleteRegion(String name)
	{
		for (reg region : Regs)
		{
			if (region.name != null && region.name.equals(name))
			{
				graf.Regions.set(region.x + "." + region.z, null);
				Regs.remove(region);
				return ;
			}
		}
	}

	public ArrayList<Clan> getClans()
	{
		return Clan.getClanLocation((int)(x + (x1 + 1) / 2.0) * 16, (int)(z + (z1 + 1) / 2.0) * 16);
	}

	public reg intersection()
	{
		for (reg region : Regs)
		{
			if (region.equals(this))
				continue ;
			if ((x + x1) < region.x || x > (region.x1 + region.x))
				continue ;
			if ((z + z1) < region.z || z > (region.z1 + region.z))
				continue ;
			return region;
		}
		return null;
	}

	public void addmember(player p)
	{
		members.add(p.p);
	}

	public void removemember(player p)
	{
		members.remove(p.p);
	}

	public boolean isExist()
	{
		if (owner == null)
		{
			deleteRegion();
			return false;
		}
		return true;
	}

	public int getSize()
	{
		return (x1 + 1) * (z1 + 1);
	}

	public Location getCenter()
	{
		return new Location(Bukkit.getWorld("world"), (x + (x1 + 1) / 2.0) * 16, 100, (z + (z1 + 1) / 2.0) * 16);
	}

	public static int getPriceUnblockChunk(int day)
	{
		return (( 2 * start_price_unblock_chunk + (add_price_unblock_chunk * (day - 1))) / 2) * day;
	}

	public static int getPriceUnblockChunk(int day_first, int day_last)
	{
		int an = start_price_unblock_chunk + (day_first - 1) * add_price_unblock_chunk;

		return ((2 * an + add_price_unblock_chunk * (day_last - day_first)) / 2) * (day_last - day_first + 1);
	}

	public static void arendaOper()
	{
		for (reg region : Regs)
		{
			if (region.arenda)
			{
				if (region.time_keep < new Date().getTime())
				{
					for (String mem : region.members)
					{
						player p = player.getPlayer(mem);
						if (p.pl != null)
							p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Регион #" + region.name + "# удален"), rgb.gradientLight(chat.color[0], "за неуплату аренды"),20, 60, 20);
					}
					player p = player.getPlayer(region.owner);
					p.blockRegs += region.getSize();
					if (p.pl != null)
						p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Регион #" + region.name + "# удален"), rgb.gradientLight(chat.color[0], "за неуплату аренды"),20, 60, 20);
					region.deleteRegion();
				}
				else
				if (region.time_keep < (new Date().getTime() + (long)(1000 * 60 * 60 * 24)))
				{
					for (String mem : region.members)
					{
						player p = player.getPlayer(mem);
						if (p.pl != null)
						{
							p.Message("Регион #" + region.name + " # будет удален через " + time.getTime(region.time_keep, new Date().getTime()));
							p.Message("Вы можете #продлить# аренду используя /#rg up# <#количество дней#>");
							p.Message("Или сообщите об этом #владельцу#, пусть он платит)");
						}

					}
					player p = player.getPlayer(region.owner);
					p.blockRegs += region.getSize();
					if (p.pl != null)
					{
						p.Message("Регион #" + region.name + " # будет удален через " + time.getTime(region.time_keep, new Date().getTime()));
						p.Message("Вы можете #продлить# аренду используя /#rg up# <#количество дней#>");
					}
				}
			}
		}
	}
	
	public  synchronized static void getconRegs()
	{
		File file = new File("plugins/Regs.yml");
		graf.Regions =  YamlConfiguration.loadConfiguration(file);
		new BukkitRunnable()
		{
            @Override
            public void run()
            {
            	while (true)
            	{
            		if (graf.Players != null)
            			break ;           		
            	}
            	for (String s : graf.Regions.getKeys(false))
        		{
        			for (String s1 : Objects.requireNonNull(graf.Regions.getConfigurationSection(s)).getKeys(false))
        			{
        				getReg(Integer.parseInt(s), Integer.parseInt(s1));
        			}
        		}
            }
        }.runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")));
	}
	
	public static void saveconRegs()
	{
		File file = new File("plugins/Regs.yml");
		try
		{
			file.delete();
			file.createNewFile();
		} catch(Exception ignored){};

		FileConfiguration buffer = YamlConfiguration.loadConfiguration(new File("plugins/Regs.yml"));

		for (reg region : Regs)
		{
//			try
//			{
				region.saveReg(buffer);
//			} catch(Exception ex){}
		}


		try {
			buffer.save(file);
		} catch (Exception ignored) {}
	}
}
