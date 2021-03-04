package commands;

import clans.Clan;
import clans.Oper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import own.chat;
import own.player;
import own.graf;
import railway_system.save_station;
import railway_system.set_station;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class set {
	
	public static boolean set_commands(player p, String[] args)
	{
		if (args.length == 0)
		{
			for (Player pl : Bukkit.getOnlinePlayers())
				pl.sendMessage(rgb.gradientLight(chat.color[0], "Через #2# секунды будут #2# поочередных #фриза#, просто админ опять придумывает какю-то #дичь# и просит у вас прощения"));
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					p.pl.getServer().reload();
				}
			}.runTaskLaterAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 40);
			return true;
		}
		if (args[0].equalsIgnoreCase("permision"))
			return permision(args);
		if (args[0].equalsIgnoreCase("color"))
			return color(p, args);
		if (args[0].equalsIgnoreCase("allowregs"))
			return allowregs(args);
		if (args[0].equalsIgnoreCase("spawn"))
			return set_spawn(p, args);
		if (args[0].equalsIgnoreCase("tpPoints"))
			return set_tpPoints(p, args);
		if (args[0].equalsIgnoreCase("nametag"))
			return set_nametag(p, args);
		if (args[0].equalsIgnoreCase("speed"))
			return set_speed(p, args);
		if (args[0].equalsIgnoreCase("money"))
			return set_money(p, args);
		if (args[0].equalsIgnoreCase("prefix"))
			return set_prefix(p, args);
		if (args[0].equalsIgnoreCase("postfix"))
			return set_postfix(p, args);
		if (args[0].equalsIgnoreCase("name"))
			return set_name(p, args);
		if (args[0].equalsIgnoreCase("save_crossroad"))
			return save_station.save_crossroad(p, args);
		if (args[0].equalsIgnoreCase("crossroad"))
			return set_station.set_crossroad(p, args);
		if (args[0].equalsIgnoreCase("flySpeed"))
			return set_flySpeed(p, args);
		if (args[0].equalsIgnoreCase("clear"))
			return set_clear();
		if (args[0].equalsIgnoreCase("fraction"))
			return set_fraction(p, args);
		if (args[0].equalsIgnoreCase("iteminfo"))
			return set_iteminfo(p, args);
		if (args[0].equalsIgnoreCase("entity"))
			return set_entity(p, args);
		if (args[0].equalsIgnoreCase("map"))
			return set_map(p, args);

		return true;
	}

	private static boolean set_entity(player p, String[] args)
	{
		if (args.length == 1)
		{
			int count = 0;
			for (World world : Bukkit.getWorlds())
			{
				count += world.getEntities().size();
			}
			p.sendMessage("Существ во всех мирах: " + count);
			return true;
		}
		return true;
	}

	private static boolean set_map(player p, String[] args)
	{
		Clan.clans.sort((a, b) -> b.getInfl() - a.getInfl());

		int i = 2;
		int	map_count = 0;

		for (int x = 0; x < i * 4; x++)
		{
			for (int z = 0; z < i * 4; z++)
			{
				test.setMap(x, z, i, map_count++, p.pl.getLocation());
			}
		}
		return true;
	}

	private static boolean set_iteminfo(player p, String[] args)
	{
		p.Message(p.pl.getItemInHand() + "");
		return true;
	}

	private static boolean set_fraction(player p, String []args)
	{
		if (args[1].equalsIgnoreCase("win"))
		{
			Oper.intersectionClans(Clan.getClanName(args[2]), Clan.getClanName(args[3]));
		}
		if (args[1].equalsIgnoreCase("infl"))
		{
			Clan clan = Clan.getClanName(args[2]);
			if (clan != null)
			{
				clan.setInfl(Integer.parseInt(args[3]));
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("count"))
		{
			p.sendMessage("Всего кланов: " + Clan.clans.size());
			return true;
		}
		if (args[1].equalsIgnoreCase("list"))
		{
			Clan.clans.forEach(a -> p.sendMessage(a.getName()));
			return true;
		}
		if (args[1].equalsIgnoreCase("all"))
		{
			for (String uid : graf.Clans.getKeys(false))
			{
				if (Clan.getClan(uid) == null || !Clan.getClan(uid).isExist())
					p.sendMessage("null - " + uid);
				else
					p.sendMessage( Clan.getClan(uid).getName() + " - " + uid);
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("delete"))
		{
			Clan clan = Clan.getClanName(args[2]);
			if (clan != null)
				clan.deleteClan();
		}
		return true;
	}

	private static boolean set_clear()
	{
		for (Entity en : Bukkit.getWorld("world").getEntities())
		{
			if (en instanceof ArmorStand && en.isInvulnerable())
				en.remove();
			if (en instanceof Item && en.isInvulnerable())
				en.remove();
			if (en instanceof WanderingTrader && en.isInvulnerable())
				en.remove();
		}
		return true;
	}

	private static boolean set_flySpeed(player p, String[] args)
	{
		p.pl.setFlySpeed(Float.parseFloat(args[1]));
		return true;
	}

	private static boolean set_prefix(player p, String[] args)
	{
		player pl = player.getPlayer(args[1]);
		String str = "";

		ChatColor col0 = chat.color[0];
		ChatColor col1 = chat.color[0];

		boolean f = false;

		for (int i = 2; i < args.length; i++)
		{
			if (args[i].equalsIgnoreCase("null"))
			{
				pl.prefix = null;
				return true;
			}
			if (args[i].equalsIgnoreCase("gtrue"))
			{
				pl.grad_prefix = true;
			}
			else if (args[i].equalsIgnoreCase("gfalse"))
			{
				pl.grad_prefix = false;
			}
			else
			if (args[i].split("")[0].equals("#"))
			{
				if (args[i].split("-").length == 1)
				{
					str += ChatColor.of(args[i].split("-")[0]);
				}
				else
				{
					f = true;
					col0 = ChatColor.of(args[i].split("-")[0]);
					col1 = ChatColor.of(args[i].split("-")[1]);
					pl.prefix_col0 = col0.getName();
					pl.prefix_col1 = col1.getName();
				}
			}
			else
				str += args[i] + (args.length - 1 == i ? "" : " ");
		}
		if (!pl.grad_prefix)
			if (f)
				str = rgb.gradient(col0.getName(), col1.getName(), str);
		pl.prefix = str;
		return true;
	}

	private static boolean set_postfix(player p, String[] args)
	{
		player pl = player.getPlayer(args[1]);
		String str = "";

		ChatColor col0 = chat.color[0];
		ChatColor col1 = chat.color[0];

		boolean f = false;

		for (int i = 2; i < args.length; i++)
		{
			if (args[i].equalsIgnoreCase("null"))
			{
				pl.postfix = null;
				return true;
			}
			if (args[i].equalsIgnoreCase("gtrue"))
			{
				pl.grad_postfix = true;
			}
			else if (args[i].equalsIgnoreCase("gfalse"))
			{
				pl.grad_postfix = false;
			}
			else
			if (args[i].split("")[0].equals("#"))
			{
				if (args[i].split("-").length == 1)
				{
					str += ChatColor.of(args[i].split("-")[0]);
				}
				else
				{
					f = true;
					col0 = ChatColor.of(args[i].split("-")[0]);
					col1 = ChatColor.of(args[i].split("-")[1]);
					pl.postfix_col0 = col0.getName();
					pl.postfix_col1 = col1.getName();
				}
			}
			else
				str += args[i] + (args.length - 1 == i ? "" : " ");
		}
		if (!pl.grad_postfix)
			if (f)
				str = rgb.gradient(col0.getName(), col1.getName(), str);
		pl.postfix = " " + str;
		return true;
	}

	private static boolean set_name(player p, String[] args)
	{
		player pl = player.getPlayer(args[1]);
		String str = "";

		ChatColor col0 = chat.color[0];
		ChatColor col1 = chat.color[0];

		boolean f = false;

		for (int i = 2; i < args.length; i++)
		{
			if (args[i].equalsIgnoreCase("null"))
			{
				pl.name = null;
				return true;
			}
			if (args[i].equalsIgnoreCase("gtrue"))
			{
				pl.grad_name = true;
			}
			else if (args[i].equalsIgnoreCase("gfalse"))
			{
				pl.grad_name = false;
			}
			else
			if (args[i].split("")[0].equals("#"))
			{
				if (args[i].split("-").length == 1)
				{
					pl.name_col0 = args[i].split("-")[0];
					pl.name_col1 = args[i].split("-")[0];
					if (!(args[i].split("-")[0].equalsIgnoreCase("#ffffff")))
						str += ChatColor.of(args[i].split("-")[0]);
				}
				else
				{
					f = true;
					col0 = ChatColor.of(args[i].split("-")[0]);
					col1 = ChatColor.of(args[i].split("-")[1]);
					pl.name_col0 = col0.getName();
					pl.name_col1 = col1.getName();
				}
			}
			else
				str += args[i] + (args.length - 1 == i ? "" : " ");
		}
		if (!pl.grad_name)
			if (f)
				str = rgb.gradient(col0.getName(), col1.getName(), str);
		pl.name = str;
		return true;
	}

	private static boolean set_money(player p, String[] args)
	{
		player pl = player.getPlayer(args[1]);
		pl.money += Integer.parseInt(args[2]);
		if (pl.isOnline())
			pl.sendMessageG(rgb.gradientLight(chat.color[0], "Графит скинул вам " + Integer.parseInt(args[2]) + " скитов, теперь у вас их " + pl.money));
		p.sendMessageG("теперь там " + pl.money + " очков");
		return true;
	}

	private static boolean set_speed(player p, String[] args)
	{
		Bukkit.getPlayer(args[1]).setWalkSpeed(Float.parseFloat(args[2]));
		return true;
	}

	private static boolean set_nametag(player p, String[] args)
	{
		if (args[1].equalsIgnoreCase("list"))
		{
			for (String name : graf.config.getConfigurationSection("dont_pickup").getKeys(false))
			{
				if (graf.config.get("dont_pickup." + name + ".world") != null)
				{
					p.sendMessage(name + "\n    " + graf.config.get("dont_pickup." + name + ".world") + "\n    " +
							graf.config.get("dont_pickup." + name + ".x") + "\n    " +
							graf.config.get("dont_pickup." + name + ".y") + "\n    " +
							graf.config.get("dont_pickup." + name + ".z"));
				}
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("delete"))
		{
			Location loc = new Location(
					Bukkit.getWorld(graf.config.getString("dont_pickup." + args[2] + ".world")),
					graf.config.getDouble("dont_pickup." + args[2] + ".x"),
					graf.config.getDouble("dont_pickup." + args[2] + ".y"),
					graf.config.getDouble("dont_pickup." + args[2] + ".z")
			);
			for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.01, 0.01, 0.01))
			{
				if (en instanceof Item)
				{
					for (Entity ent : en.getPassengers())
						ent.remove();
					en.remove();
				}
			}
			graf.config.set("dont_pickup." + args[2], "");
			return true;
		}
		if (args[1].equalsIgnoreCase("mv"))
		{
			for (String name : graf.config.getConfigurationSection("dont_pickup").getKeys(false))
			{
				if (graf.config.get("dont_pickup." + name + ".world") != null && name.equals(args[2]))
				{
					Location loc = p.pl.getLocation();
					graf.config.set("dont_pickup." + args[2] + ".world",loc.getWorld().getName());
					graf.config.set("dont_pickup." + args[2] + ".x",loc.getX());
					graf.config.set("dont_pickup." + args[2] + ".y",loc.getY());
					graf.config.set("dont_pickup." + args[2] + ".z",loc.getZ());
				}
			}
			return true;
		}
		String name = args[1];
		if (graf.config.get("dont_pickup." + name + ".world") != null)
		{
			Location loc = new Location(
					Bukkit.getWorld(graf.config.getString("dont_pickup." + name + ".world")),
					graf.config.getDouble("dont_pickup." + name + ".x"),
					graf.config.getDouble("dont_pickup." + name + ".y"),
					graf.config.getDouble("dont_pickup." + name + ".z")
			);
			for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.01, 0.01, 0.01))
			{
				if (en instanceof Item)
				{
					for (Entity ent : en.getPassengers())
						ent.remove();
					en.remove();
				}
			}
		}

		ChatColor col1 = null;
		ChatColor col2 = null;

		boolean f = false;
		boolean glow = false;
		int pass = 0;
		int	string_i = 0;

		String []string = {" "," "," "};

		for (int i = 2; i < args.length; i++)
		{
			if (args[i].equals("next"))
			{
				string_i++;
			}
			else
			if (args[i].split(":")[0].equals("pass"))
			{
				if (args[i].split(":")[1].equals("big"))
					pass = 1;
				if (args[i].split(":")[1].equals("small"))
					pass = 2;
				if (args[i].split(":")[1].equals("all"))
					pass = 3;
				if (args[i].split(":")[1].equals("full"))
					pass = 4;
			}
			else
			if (args[i].equals("glow"))
			{
				glow = true;
			}
			else
			if (args[i].split("")[0].equals("#") && !f)
			{
				if (args[i].split("-").length == 1)
					string[string_i] += ChatColor.of(args[i]);
				else
				{
					col1 = ChatColor.of(args[i].split("-")[0]);
					col2 = ChatColor.of(args[i].split("-")[1]);
					f = true;
				}
			}
			else
			if (args[i].split("")[0].equals("&") && !f)
			{
				if (args[i].split("-").length == 1)
				{
					string[string_i] += chat.color[Integer.parseInt(args[i].split("")[1])];
				}
				else
				{
					col1 = chat.color[Integer.parseInt(args[i].split("-")[0].split("")[1])];
					col2 = chat.color[Integer.parseInt(args[i].split("-")[1].split("")[1])];
					f = true;
				}
			}
			else
			{
				string[string_i] += args[i] + (i == args.length - 1 ? "" : " ");
			}
		}
		if (f)
		{
			string[0] = rgb.gradient(col1.getName(), col2.getName(), string[0]);
			string[1] = rgb.gradient(col1.getName(), col2.getName(), string[1]);
			string[2] = rgb.gradient(col1.getName(), col2.getName(), string[2]);
		}

		Item item = p.pl.getWorld().dropItem(p.pl.getLocation(), p.pl.getItemInHand());
		item.setGravity(false);
		item.setCustomNameVisible(true);
		item.setInvulnerable(true);
		item.teleport(p.pl.getLocation());
		item.setVelocity(new Vector(0,0,0));
		if (glow)
			item.setGlowing(true);

		Location loc = p.pl.getLocation();

		graf.config.set("dont_pickup." + name + ".world",loc.getWorld().getName());
		graf.config.set("dont_pickup." + name + ".x",loc.getX());
		graf.config.set("dont_pickup." + name + ".y",loc.getY());
		graf.config.set("dont_pickup." + name + ".z",loc.getZ());
		graf.config.set("dont_pickup." + name + ".item", p.pl.getItemInHand());
		graf.config.set("dont_pickup." + name + ".glowing", glow);
		graf.config.set("dont_pickup." + name + ".pass", pass);
		graf.config.set("dont_pickup." + name + ".string", string[0]);
		graf.config.set("dont_pickup." + name + ".string1", string[1]);
		graf.config.set("dont_pickup." + name + ".string2", string[2]);

		if (pass == 0)
			item.setCustomName(string[0]);
		else
		{
			if (pass == 3)
			{
				item.addPassenger(armor_new(string[0], loc, 0));
				item.addPassenger(armor_new(string[1], loc, 1));
				return true;
			}
			if (pass == 4)
			{
				item.addPassenger(armor_new(string[0], loc, 0));
				item.addPassenger(armor_new(string[1], loc, 1));
				item.setCustomName(string[2]);
				return true;
			}
			item.addPassenger(armor_new(string[0], loc, pass == 2 ? 1 : 0));
		}

		return true;
	}

	public static ArmorStand armor_new(String string, Location loc, int size)
	{
		ArmorStand arm = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		arm.setVisible(false);
		arm.setGravity(false);
		arm.setInvulnerable(true);
		arm.setCustomNameVisible(true);
		arm.setCustomName(string);
		if (size == 1)
			arm.setSmall(true);

		return arm;
	}

	public static boolean permision(String[] args)
	{
		player to = player.getPlayer(args[1]);
		to.permision = Integer.parseInt(args[2]);
		return true;
	}
	
	public static boolean color(player p, String[] args)
	{
		try
		{
			p.sendMessage("Цвет (" + chat.color[Integer.parseInt(args[1])] + "?"  + ChatColor.RESET + ") "
					+ ChatColor.RESET + " был заменен на (" +
					ChatColor.of(rgb.get_color(
							Integer.parseInt(args[2]),
							Integer.parseInt(args[3]),
							Integer.parseInt(args[4]))) + "?" + ChatColor.RESET + ") ");
			p.sendMessage("Пример как было:\n" + chat.strCommand("Привет! Для входа используй команду:", "/l",  new String[]{"пароль"}));

			chat.color[Integer.parseInt(args[1])] = ChatColor.of(rgb.get_color(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));

			try {
				FileConfiguration config = graf.getcon();
				config.set("color_" + Integer.parseInt(args[1]), rgb.get_color(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
				graf.savecon(config);
			} catch(Exception ignored) {}
			p.sendMessage("Пример как стало:\n" + chat.strCommand("Привет! Для входа используй команду:", "/l",  new String[]{"пароль"}));
			return true;
		} catch (Exception ex)
		{
			p.sendMessage("Цвет (" + chat.color[Integer.parseInt(args[1])] + "?"  + ChatColor.RESET + ") "
					+ ChatColor.RESET + " был заменен на (" +
					ChatColor.of(args[2]) + "?" + ChatColor.RESET + ") ");
			p.sendMessage("Пример как было:\n" + chat.strCommand("Привет! Для входа используй команду:", "/l",  new String[]{"пароль"}));

			chat.color[Integer.parseInt(args[1])] = ChatColor.of(args[2]);

			try {
				FileConfiguration config = graf.getcon();
				config.set("color_" + Integer.parseInt(args[1]), args[2]);
				graf.savecon(config);
			} catch(Exception ignored) {}
			p.sendMessage("Пример как стало:\n" + chat.strCommand("Привет! Для входа используй команду:", "/l",  new String[]{"пароль"}));
			return true;
		}
	}

	public static boolean allowregs(String[] args)
	{
		player to = player.getPlayer(args[1]);
		to.allowRegs = Integer.parseInt(args[2]);
		return true;
	}

	public static boolean set_tab(TabCompleteEvent e)
	{
		String[] list = new String[]{
				"permision",
				"color",
				"allowregs",
				"nametag",
				"tppoints",
				"speed",
				"money",
				"prefix",
				"postfix",
				"name",
				"crossroad",
				"flySpeed",
				"fraction",
				"clear",
				"iteminfo",
				"map",
				"entity"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > 1 && chat.getCountChar(e.getBuffer(), ' ') == 1) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[1].getBytes()) {
					x++;
					if (s.getBytes().length > x && b != s.getBytes()[x])
						f = 1;
				}
				if (f == 0)
					flist.add(s);
			}
			e.setCompletions(flist);
			return true;
		}
		if (e.getBuffer().split(" ").length <= 2 && chat.getCountChar(e.getBuffer(), ' ') == 1)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}

		return true;
	}

	public static boolean set_spawn(player p, String []args)
	{
		if (args.length == 2)
		{
			Location loc = p.pl.getLocation();

			graf.config.set("spawn." + args[1] + ".world", loc.getWorld().getName());
			graf.config.set("spawn." + args[1] + ".x", loc.getX());
			graf.config.set("spawn." + args[1] + ".y", loc.getY());
			graf.config.set("spawn." + args[1] + ".z", loc.getZ());
			graf.config.set("spawn." + args[1] + ".yaw", loc.getYaw());
			graf.config.set("spawn." + args[1] + ".pitch", loc.getPitch());

			return true;
		}
		Location loc = p.pl.getLocation();

		graf.config.set("spawn.world", loc.getWorld().getName());
		graf.config.set("spawn.x", loc.getX());
		graf.config.set("spawn.y", loc.getY());
		graf.config.set("spawn.z", loc.getZ());
		graf.config.set("spawn.yaw", loc.getYaw());
		graf.config.set("spawn.pitch", loc.getPitch());

		graf.spawn = loc;

		return true;
	}

	public static boolean set_tpPoints(player p, String []args)
	{
		player pl = player.getPlayer(args[1]);
		pl.tp_points += Integer.parseInt(args[2]);
		if (pl.isOnline())
			pl.sendMessageG(rgb.gradientLight(chat.color[0], "Эндеры скинулись вам на " + Integer.parseInt(args[2]) + " очков телепортации, теперь у вас их " + pl.tp_points));
		p.sendMessageG("теперь там " + pl.tp_points + " очков");
		return true;
	}
}
