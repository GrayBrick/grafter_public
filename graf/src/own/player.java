package own;

import Listeners.BonusEntityDeath;
import clans.Clan;
import clans.SortClans;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import text_processing.rgb;
import text_processing.rgb_map;
import text_processing.time;

import java.io.File;
import java.util.*;

public class player {

	public static ArrayList<player> Players = new ArrayList<>();
	public static int RegsDef = 9;
	public static int MaxRegsAllow = 200;
	public static String basePrefix = rgb.gradientLight(chat.color[1], "житель");
	public static String basePostfix = "";

	public static ArrayList<BossBar> bars = new ArrayList<>();

	public String p;
	public Player pl;

	public int				allowRegs;
	public int				blockRegs;
	public int				permision;
	public int				x;
	public int				z;
	public int				expand_x;
	public int				expand_z;
	public boolean			mark = false;
	public Location			home;
	public int				tp_points;
	public long				time_game;
	public int				money;

	public String			prefix;
	public String			name;
	public String			postfix;
	public boolean			grad_prefix;
	public boolean			grad_name;
	public boolean			grad_postfix;

	public String			prefix_col0;
	public String			name_col0;
	public String			postfix_col0;

	public String			prefix_col1;
	public String			name_col1;
	public String			postfix_col1;

	public reg				show_reg;
	public boolean			show_message = true;

	public Location			death;
	public long				death_time;
	public long				tp_time;
	public long				mute_time;
	public String			station_go;
	public String			station_from;

	public boolean			go_to_station;
	public Location			speed_loc_1;
	public Location			speed_loc_2;

	public int				DeathEntity;
	public int				BreakBlock;

	public boolean			timerBar = false;
	public String			typeBar;

	public String			clan_uid;
	public clans.GUI		clan_gui;
	public byte				rank;
	public long				time_clan_include;
	public boolean			invite_in_clan = false;
	public boolean			kazna_give = false;
	public boolean			kazna_take = false;

	public boolean			prison = false;

	public boolean			inArena = false;
	public boolean			queueArena = false;

	public ScoreInfo		score;
	public BossBar			bar_hp;
	public TreeMap<String, Long> bar_list = new TreeMap<>();

	public static class ScoreInfo
	{
		public boolean	info;
		public boolean	info_clan;
		public boolean	info_reg;
		public boolean	info_all;
		public boolean	info_entity;
		public boolean	info_block;
		public boolean	info_money;

		public	ScoreInfo(boolean info, boolean info_clan, boolean info_reg, boolean info_all, boolean info_entity, boolean info_block, boolean info_money)
		{
			this.info = info;
			this.info_clan = info_clan;
			this.info_reg = info_reg;
			this.info_all = info_all;
			this.info_entity = info_entity;
			this.info_block = info_block;
			this.info_money = info_money;
		}

		public	ScoreInfo()
		{
			this.info = true;
			this.info_clan = true;
			this.info_reg = true;
			this.info_all = true;
			this.info_entity = true;
			this.info_block = true;
			this.info_money = true;
		}
	}

//	public static class queArena
//	{
//		public Location	from;
//
//		public	queArena(boolean info, boolean info_clan, boolean info_reg, boolean info_all, boolean info_entity, boolean info_block, boolean info_money)
//		{
//			this.info = info;
//			this.info_clan = info_clan;
//			this.info_reg = info_reg;
//			this.info_all = info_all;
//			this.info_entity = info_entity;
//			this.info_block = info_block;
//			this.info_money = info_money;
//		}
//
//		public	ScoreInfo()
//		{
//			this.info = true;
//			this.info_clan = true;
//			this.info_reg = true;
//			this.info_all = true;
//			this.info_entity = true;
//			this.info_block = true;
//			this.info_money = true;
//		}
//	}

	public static player getPlayer(Player p)
	{
		if (Players != null)
		{
			for (int i = 0; i < Players.size(); i++)
			{
				if (Players.get(i) != null && Players.get(i).p.equals(p.getName()))
					return Players.get(i);
			}
		}
		return new player(p);
	}

	public static player getPlayer(String p)
	{
		for (player pl : Players)
		{
			if (pl.p.equals(p))
			{
				try
				{
					if ( Bukkit.getPlayer(p).isOnline())
						pl.pl = Bukkit.getPlayer(p);
				} catch (Exception ignored){}

				return pl;
			}
		}
		return new player(p);
	}

	public player(Player p)
	{
		this.p = p.getName();
		this.pl = p;
		iniPlayer();
	}
	public player(String p)
	{
		this.p = p;
		this.pl = Bukkit.getPlayer(p);
		iniPlayer();
	}

	public static boolean me(player p, String[] args)
	{
		if (p.permision == 10 && args.length == 1)
		{
			player pl = player.getPlayer(args[0]);
			int count = 0;
			for (reg region : reg.Regs)
			{
				if (region.owner != null && region.owner.equals(pl.p))
				{
					count += region.getSize();
				}
			}
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Используйется/всего " + count + "/" + pl.allowRegs + " чанков для привата"));
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Скитов: " + pl.money));
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Очков телепортации: " + pl.tp_points));
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Времени с момента регистрации: " + time.getTime(new Date().getTime(), pl.time_game)));
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Убил существ: " + pl.DeathEntity + " коэф " + BonusEntityDeath.getCoef(pl.DeathEntity)));
			p.sendMessage(rgb.gradientLightInt(chat.color[0], "Сломал опытных блоков: " + pl.BreakBlock + " коэф " + BonusEntityDeath.getCoef(pl.BreakBlock)));
			return true;
		}
		int count = 0;
		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p.p))
			{
				count += region.getSize();
			}
		}
		p.sendMessage(rgb.gradientLightInt(chat.color[0], "Используйется/всего " + count + "/" + p.allowRegs + " чанков для привата"));
		p.sendMessage(rgb.gradientLightInt(chat.color[0], "Скитов: " + p.money));
		p.sendMessage(rgb.gradientLightInt(chat.color[0], "Очков телепортации: " + p.tp_points));
		return true;
	}

	private void iniPlayer()
	{
		allowRegs = graf.Players.getInt(p + ".allowregs") == 0 ? RegsDef : graf.Players.getInt(p + ".allowregs");
		blockRegs = graf.Players.getInt(p + ".blockRegs");
		permision = graf.Players.getInt(p + ".permision");
		time_game = graf.Players.getLong(p + ".time_game");

		prefix = graf.Players.getString(p + ".tab.prefix");
		name = graf.Players.getString(p + ".tab.name");
		postfix = graf.Players.getString(p + ".tab.postfix");

		grad_prefix = graf.Players.getBoolean(p + ".tab.grad_prefix");
		grad_name = graf.Players.getBoolean(p + ".tab.grad_name");
		grad_postfix = graf.Players.getBoolean(p + ".tab.grad_postfix");
		mute_time = graf.Players.getLong(p + ".mute_time");

		station_go = graf.Players.getString(p + ".station_go");
		station_from = graf.Players.getString(p + ".station_from");
		go_to_station = graf.Players.getBoolean(p + ".go_to_station");

		DeathEntity = graf.Players.getInt(p + ".DeathEntity");
		BreakBlock = graf.Players.getInt(p + ".BreakBlock");

		clan_uid = graf.Players.getString(p + ".clan_uid");
		rank = (byte) graf.Players.getInt(p + ".rank");
		time_clan_include = graf.Players.getLong(p + ".time_clan_include");

		if (!graf.Players.contains(p + ".score.info"))
		{
			score = new ScoreInfo();
		}
		else
		{
			score = new ScoreInfo(
					graf.Players.getBoolean(p + ".score.info"),
					graf.Players.getBoolean(p + ".score.info_clan"),
					graf.Players.getBoolean(p + ".score.info_reg"),
					graf.Players.getBoolean(p + ".score.info_all"),
					graf.Players.getBoolean(p + ".score.info_entity"),
					graf.Players.getBoolean(p + ".score.info_block"),
					graf.Players.getBoolean(p + ".score.info_money")
			);
		}


		if (time_game != 0)
		{
			tp_points = graf.Players.getInt(p + ".tp_points");
			money = graf.Players.getInt(p + ".money");

			prefix_col0 = graf.Players.getString(p + ".tab.prefix_col0");
			name_col0 = graf.Players.getString(p + ".tab.name_col0");
			postfix_col0 = graf.Players.getString(p + ".tab.postfix_col0");

			prefix_col1 = graf.Players.getString(p + ".tab.prefix_col1");
			name_col1 = graf.Players.getString(p + ".tab.name_col1");
			postfix_col1 = graf.Players.getString(p + ".tab.postfix_col1");

			typeBar = graf.Players.getString(p + ".typeBar");
		}
		else
		{
			time_game = new Date().getTime();
			tp_points = 1000;
			money = 100;

			prefix_col0 = "#ffffff";
			name_col0 = "#ffffff";
			postfix_col0 = "#ffffff";

			prefix_col1 = "#ffffff";
			name_col1 = "#ffffff";
			postfix_col1 = "#ffffff";

			typeBar = "location all";
		}
		if (graf.Players.get(p + ".home.world") != null)
			home = new Location(
					Bukkit.getWorld(graf.Players.getString(p + ".home.world")),
					graf.Players.getDouble(p + ".home.x"),
					graf.Players.getDouble(p + ".home.y"),
					graf.Players.getDouble(p + ".home.z"),
					(float)graf.Players.getDouble(p + ".home.yaw"),
					(float)graf.Players.getDouble(p + ".home.pitch"));
		try
		{
			if ( Bukkit.getPlayer(p).isOnline())
				pl = Bukkit.getPlayer(p);
		} catch (Exception ignored){}

		Players.add(this);
	}
	private void savePlayer()
	{
		JSONObject jo = new JSONObject();

		JSONObject region = new JSONObject();
		{
			graf.Players.set(p + ".allowregs", allowRegs);
			region.put("allowregs", allowRegs);

			graf.Players.set(p + ".blockRegs", blockRegs);
			region.put("blockRegs", blockRegs);
		}
		jo.put("region", region);

		JSONObject me = new JSONObject();
		{
			graf.Players.set(p + ".permision", permision);
			me.put("permision", permision);

			graf.Players.set(p + ".tp_points", tp_points);
			me.put("tp_points", tp_points);

			graf.Players.set(p + ".time_game", time_game);
			me.put("time_game", time_game);

			graf.Players.set(p + ".money", money);
			me.put("money", money);

			graf.Players.set(p + ".DeathEntity", DeathEntity);
			me.put("DeathEntity", DeathEntity);

			graf.Players.set(p + ".BreakBlock", BreakBlock);
			me.put("BreakBlock", BreakBlock);

			graf.Players.set(p + ".typeBar", typeBar);
			me.put("typeBar", typeBar);

			graf.Players.set(p + ".clan_uid", clan_uid);
			me.put("clan_uid", clan_uid);

			graf.Players.set(p + ".rank", rank);
			me.put("rank", rank);

			graf.Players.set(p + ".time_clan_include", time_clan_include);
			me.put("time_clan_include", time_clan_include);
		}
		jo.put("info", me);




		JSONObject tab = new JSONObject();
		{
			graf.Players.set(p + ".tab.prefix", prefix);
			tab.put("prefix", prefix);

			graf.Players.set(p + ".tab.name", name);
			tab.put("name", name);

			graf.Players.set(p + ".tab.postfix", postfix);
			tab.put("postfix", postfix);


			graf.Players.set(p + ".tab.grad_prefix", grad_prefix);
			tab.put("grad_prefix", grad_prefix);

			graf.Players.set(p + ".tab.grad_name", grad_name);
			tab.put("grad_name", grad_name);

			graf.Players.set(p + ".tab.grad_postfix", grad_postfix);
			tab.put("grad_postfix", grad_postfix);


			graf.Players.set(p + ".tab.prefix_col0", prefix_col0);
			tab.put("prefix_col0", prefix_col0);

			graf.Players.set(p + ".tab.name_col0", name_col0);
			tab.put("name_col0", name_col0);

			graf.Players.set(p + ".tab.postfix_col0", postfix_col0);
			tab.put("postfix_col0", postfix_col0);


			graf.Players.set(p + ".tab.prefix_col1", prefix_col1);
			tab.put("prefix_col1", prefix_col1);

			graf.Players.set(p + ".tab.name_col1", name_col1);
			tab.put("name_col1", name_col1);

			graf.Players.set(p + ".tab.postfix_col1", postfix_col1);
			tab.put("postfix_col1", postfix_col1);
		}
		jo.put("tab", tab);


		graf.Players.set(p + ".mute_time", mute_time);
		jo.put("mute_time", mute_time);


		JSONObject station = new JSONObject();
		{
			graf.Players.set(p + ".station_go", station_go);
			station.put("station_go", station_go);

			graf.Players.set(p + ".station_from", station_from);
			station.put("station_from", station_from);

			graf.Players.set(p + ".go_to_station", go_to_station);
			station.put("go_to_station", go_to_station);
		}
		jo.put("station", station);

		JSONObject Jscore = new JSONObject();
		{
			graf.Players.set(p + ".score.info", score.info);
			Jscore.put("info", score.info);

			graf.Players.set(p + ".score.info_clan", score.info_clan);
			Jscore.put("info_clan", score.info_clan);

			graf.Players.set(p + ".score.info_reg", score.info_reg);
			Jscore.put("info_reg", score.info_reg);

			graf.Players.set(p + ".score.info_all", score.info_all);
			Jscore.put("info_all", score.info_all);

			graf.Players.set(p + ".score.info_entity", score.info_entity);
			Jscore.put("info_entity", score.info_entity);

			graf.Players.set(p + ".score.info_block", score.info_block);
			Jscore.put("info_block", score.info_block);

			graf.Players.set(p + ".score.info_money", score.info_money);
			Jscore.put("info_money", score.info_money);
		}
		jo.put("score", Jscore);

		JSONObject Jhome = new JSONObject();
		if (home != null)
		{
			graf.Players.set(p + ".home.world", home.getWorld().getName());
			Jhome.put("world", home.getWorld().getName());

			graf.Players.set(p + ".home.x", home.getX());
			Jhome.put("x", home.getX());

			graf.Players.set(p + ".home.y", home.getY());
			Jhome.put("y", home.getY());

			graf.Players.set(p + ".home.z", home.getZ());
			Jhome.put("z", home.getZ());

			graf.Players.set(p + ".home.yaw", home.getYaw());
			Jhome.put("yaw", home.getYaw());

			graf.Players.set(p + ".home.pitch", home.getPitch());
			Jhome.put("pitch", home.getPitch());
		}
		jo.put("home", Jhome);

		graf.JPlayers.json.put(p, jo);
	}

	// metods start

	public ArrayList<reg> getRegs()
	{
		ArrayList<reg> list = new ArrayList<>();

		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p))
			{
				list.add(region);
				continue ;
			}
			for (String name : region.members)
			{
				if (name.equals(p))
				{
					list.add(region);
					break ;
				}
			}
		}
		return list;
	}

	public ArrayList<reg> getRegsOwner()
	{
		ArrayList<reg> list = new ArrayList<>();

		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p))
				list.add(region);
		}
		return list;
	}

	public int getRegsCount()
	{
		int count = 0;

		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p))
				count++;
		}
		return count;
	}

	public ArrayList<reg> getRegsCountAll()
	{
		ArrayList<reg> regs = new ArrayList<>();

		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p))
				regs.add(region);
		}
		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.members.contains(p))
				regs.add(region);
		}
		return regs;
	}

	public int getChunkCount()
	{
		int count = 0;
		for (reg region : reg.Regs)
		{
			if (region.owner != null && region.owner.equals(p))
			{
				count += region.getSize();
			}
		}
		return count;
	}

	public Clan getClan()
	{
		if (clan_uid != null && clan_uid != "null")
			return Clan.getClan(clan_uid);
		return null;
	}

	public void clickSound()
	{
		pl.playSound(pl.getLocation(), Sound.values()[219], 1, (float)0.69);
	}

	public void sendMessageG(String s)
	{
		pl.sendMessage(s);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				try {
					int interval = 100;
					pl.playSound(pl.getLocation(), Sound.values()[212], 1, (float)0.55);
					Thread.sleep(interval);
					pl.playSound(pl.getLocation(), Sound.values()[212], 1, (float)0.69);
					Thread.sleep(interval);
					pl.playSound(pl.getLocation(), Sound.values()[212], 1, (float)0.83);
					Thread.sleep(interval);
					pl.playSound(pl.getLocation(), Sound.values()[212], 1, (float)1.12);
					Thread.sleep(interval);
				} catch (InterruptedException ignored) {}

			}
		}.runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")));
	}

	public void sendMessageE(String s)
	{
		pl.playSound(
				pl.getLocation(),
				Sound.values()[40],
				1,
				(float)1.5);
		pl.sendMessage(s);
	}

	public void sendMessage(String s, int sound_id)
	{
		pl.playSound(pl.getLocation(),
				Sound.values()[sound_id],
				1,
				1);
		pl.sendMessage(s);
	}

	public void ErrorMessage(String s)
	{
		if (show_message)
		{
			show_message = false;
			sendMessageE(rgb.gradientLight(chat.color[0], s));
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					show_message = true;
				}
			}.runTaskLaterAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 40);
		}
	}

	public void Message(String s)
	{
		sendMessageE(rgb.gradientLight(chat.color[0], s));
	}

	public void sendMessage(String s)
	{
		pl.sendMessage(s);
	}

	public int getArea()
	{
		int a = Math.max(x, expand_x) - Math.min(x, expand_x) + 1;
		int b = Math.max(z, expand_z) - Math.min(z, expand_z) + 1;

		return a * b;
	}

	public void setHome()
	{
		reg region = reg.getReg(pl.getLocation());
		if (!region.isExist())
		{
			region.deleteRegion();
			home = pl.getLocation();
			return;
		}
		if (!region.sethome)
		{
			sendMessageE(rgb.gradientLight(chat.color[0], "Вы не можете установить тут точку дома"));
			return ;
		}
		home = pl.getLocation();
		sendMessageG(rgb.gradientLight(chat.color[0], "Вы успешно установили точку дома"));
	}

	public void toHome()
	{
		pl.teleport(home);
	}

	public static boolean isExist(String name)
	{
		for (String player : graf.Players.getKeys(false))
		{
			if (player.equals(name))
				return true;
		}
		return false;
	}

	public boolean isOnline()
	{
		try
		{
			Bukkit.getPlayer(p).isOnline();
			return true;
		}catch(Exception ignored){};
		return false;
	}
//
//	public static void spawnFox(Location loc, Player p)
//	{
//		Bee rf = (Bee) loc.getWorld().spawnEntity(loc, EntityType.BEE);
//
//		rf.setInvulnerable(true);
//		rf.setAgeLock(true);
//		rf.setBaby();
//		//rf.set
//		rf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(100);
//		rf.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(8000);
//		rf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
//		rf.setCustomName(rgb.gradient("#FF0000", "#ffffff", "Грибок"));
//	}

	public long tptime() {
		return graf.Players.getLong(p + ".tptime");
	}
	public void tptime(long b) {
		graf.Players.set(p + ".tptime", b);
	}

	public boolean tp() {
		return graf.Players.getBoolean(p + ".tp");
	}
	public void tp(boolean b) {
		graf.Players.set(p + ".tp", b);
	}

	public Location tploc()
	{
		if (graf.Players.getString(p + ".tploc") == null)
			return null;
		return new Location(Bukkit.getWorld(graf.Players.getString(p + ".tploc.world")),graf.Players.getDouble(p + ".tploc.x"),graf.Players.getDouble(p + ".tploc.y"),graf.Players.getDouble(p + ".tploc.z"));
	}
	public void tploc(Location loc) {
		if (loc == null)
		{
			graf.Players.set(p + ".tploc", null);
			return ;
		}
		graf.Players.set(p + ".tploc.world", loc.getWorld().getName());
		graf.Players.set(p + ".tploc.x", loc.getX());
		graf.Players.set(p + ".tploc.y", loc.getY());
		graf.Players.set(p + ".tploc.z", loc.getZ());
	}

	public String getPrefix()
	{
		if (prefix == null)
			return basePrefix;
		return prefix;
	}

	public String getPrefixFull()
	{
		return  (pl.getWorld().getName().equals("world") ? chat.MessageColor[1] : pl.getWorld().getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˻" +
				getPrefix() +
				(pl.getWorld().getName().equals("world") ? chat.MessageColor[1] : pl.getWorld().getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˺ ";
	}

	public static String getPrefixFull(World world, String prefix)
	{
		return  (world.getName().equals("world") ? chat.MessageColor[1] : world.getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˻" +
				prefix +
				(world.getName().equals("world") ? chat.MessageColor[1] : world.getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˺ ";
	}

	public String getPostfix()
	{
		if (postfix == null)
			return basePostfix;
		return postfix;
	}

	public String getName()
	{
		if (name == null)
			return chat.MessageColor[1] + p;
		return name;
	}

	public void clanInclude(Clan clan, byte rank)
	{
		clan_uid = clan.uid;
		this.rank = rank;
		prefix = rgb.gradientLight(rgb_map.byte_to_rgb.get(clan.getColor()), clan.getName());
		grad_name = false;
		grad_prefix = false;
		name = rgb.gradient(Clan.getColorRank(rank).getName(), "#ffffff", Clan.chat_rank + " " + p);
		time_clan_include = new Date().getTime();
	}

	public void clanLeave()
	{
		clan_uid = null;
		rank = 0;
		prefix = null;
		name = null;
		time_clan_include = 0;
	}

	public void newRank(byte rank)
	{
		this.rank = rank;
		name = rgb.gradient(Clan.getColorRank(rank).getName(), "#ffffff", Clan.chat_rank + " " + p);
		switch (rank)
		{
			case 50 :
			{
				pl.sendTitle(chat.color[0] + "Вы теперь Глава", getClan().getChatColor() + getClan().getName(), 20, 60, 20);
				for (String mem : getClan().getMembers())
				{
					player.getPlayer(mem).sendMessageG(rgb.gradientLight(chat.color[0], "У фракции новый Глава #" + p));
				}
				break ;
			}
			case 45 :
			{
				pl.sendTitle(chat.color[0] + "Вы теперь Заместитель", getClan().getChatColor() + getClan().getName(), 20, 60, 20);
				for (String mem : getClan().getMembers())
				{
					player.getPlayer(mem).sendMessageG(rgb.gradientLight(chat.color[0], "У фракции новый Заместитель #" + p));
				}
				break ;
			}
			case 41 :
			{
				pl.sendTitle(chat.color[0] + "Вы теперь Командир", getClan().getChatColor() + getClan().getName(), 20, 60, 20);
				for (String mem : getClan().getMembers())
				{
					player.getPlayer(mem).sendMessageG(rgb.gradientLight(chat.color[0], "У фракции новый Командир #" + p));
				}
				break ;
			}
			case 2 :
			{
				pl.sendTitle(chat.color[0] + "Вы теперь Боец", getClan().getChatColor() + getClan().getName(), 20, 60, 20);
				break ;
			}
		}
	}

	public boolean clanCanModer(player p)
	{
		if (rank >= 40 && rank > p.rank && p != this)
			return true;
		return false;
	}

	public boolean clanCanInviteMember()
	{
		if (rank > 40)
			return true;
		return false;
	}

	public String getRank()
	{
		if (rank == 50)
			return "Глава";
		if (rank == 45)
			return "Заместитель";
		if (rank == 41)
			return "Командир";
		if (rank == 2)
			return "Боец";
		if (rank == 1)
			return "Новенький";
		return "никто";
	}

	// metods end

	public  synchronized static void getconPlayer()
	{
		File file = new File("plugins/Players.yml");
		graf.Players =  YamlConfiguration.loadConfiguration(file);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					if (graf.Regions != null)
						break ;
				}
				for (String s : graf.Players.getKeys(false))
				{
					getPlayer(s);
				}
			}
		}.runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")));
	}

	public static void saveconPlayer()
	{
		for (player p : Players)
			p.savePlayer();

		graf.JPlayers.saveJson();

		File file = new File("plugins/Players.yml");
		try {
			graf.Players.save(file);
		} catch (Exception ignored) {}
	}
}
