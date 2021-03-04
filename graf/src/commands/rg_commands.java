package commands;

import GUI.PlayerMe;
import clans.Clan;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.graf;
import own.player;
import own.reg;
import text_processing.rgb;
import text_processing.text;
import text_processing.time;

import java.util.*;

public class rg_commands {

	public static boolean rg(player p, String []args)
	{
		if (args.length == 0)
			return rg_help(p);

		if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c"))
			return rg_claim(p, args);

		if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i"))
			return rg_info(p, args);

		if (args[0].equalsIgnoreCase("list"))
			return PlayerMe.rg(p, p, 1);

		if (args[0].equalsIgnoreCase("clear"))
			return rg_clear(p, args);

		if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d"))
			return rg_delete(p, args);

		if (args[0].equalsIgnoreCase("mark") || args[0].equalsIgnoreCase("m"))
			return rg_mark(p, args);

		if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("s"))
			return rg_show(p, args);

		if (args[0].equalsIgnoreCase("addmember") || args[0].equalsIgnoreCase("am"))
			return rg_am(p, args);

		if (args[0].equalsIgnoreCase("removemember") || args[0].equalsIgnoreCase("rm"))
			return rg_rm(p, args);

		if (args[0].equalsIgnoreCase("set"))
			return rg_set(p, args);

		if (args[0].equalsIgnoreCase("find"))
			return rg_find(p, args);

		if (args[0].equalsIgnoreCase("up"))
			return rg_up(p, args);

		if (args[0].equalsIgnoreCase("arena"))
			return rg_arena(p, args);

		return true;
	}

	private static boolean rg_arena(player p, String[] args)
	{
		if (p.permision != 10)
			return true;
		reg region = reg.getReg(p.pl.getLocation());
		if (!region.isExist())
		{
			region.deleteRegion();
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Тут нет региона"));
			return true;
		}
		if (args[1].equalsIgnoreCase("create"))
		{
			region.arenas.add(new reg.arena(
					new int[]{
							p.pl.getLocation().getBlockX(),
							p.pl.getLocation().getBlockY(),
							p.pl.getLocation().getBlockZ()
					},
					Byte.parseByte(args[2]),
					Byte.parseByte(args[3]),
					Byte.parseByte(args[4]),
					new ArrayList<>()
			));
		}

		if (args[1].equalsIgnoreCase("addpoint"))
		{
			for (reg.arena ar : region.arenas)
			{
				if (ar.inArena(p.pl.getLocation()))
				{
					if (ar.points == null || ar.points.size() == 0)
					{
						ArrayList<Location> points = new ArrayList<>();
						points.add(p.pl.getLocation());
						ar.points = points;
						p.Message("Точка арены #задана");
						return true;
					}
					ar.points.add(p.pl.getLocation());
					return true;
				}
			}
			p.Message("Тут нет #арены");
			return true;
		}

		if (args[1].equalsIgnoreCase("removepoints"))
		{
			for (reg.arena ar : region.arenas)
			{
				if (ar.inArena(p.pl.getLocation()))
				{
					ar.points = null;
					return true;
				}
			}
			p.Message("Тут нет #арены");
			return true;
		}

		if (args[1].equalsIgnoreCase("delete"))
			region.arenas.clear();

		return true;
	}

	private static boolean rg_up(player p, String[] args)
	{
		reg region = reg.getReg(p.pl.getLocation());
		if (!region.isExist())
		{
			region.deleteRegion();
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Тут нет региона"));
			return true;
		}
		if (!region.owner.equals(p.p) && !region.members.contains(p.p))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Вы не состоите в этом регионе"));
			return true;
		}
		int arenda_day_now = (int) ((region.time_keep - new Date().getTime()) / (1000 * 60 * 60 * 24));
		int day_up = 0;
		try
		{
			day_up = Integer.parseInt(args[1]);
		} catch(Exception ex)
		{
			p.ErrorMessage("Используйте#:# /#rg up #<#количество дней#>#");
			return true;
		}
		if (day_up < 1)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Продлить можно минимум на #1# день"));
			return true;
		}

		int price = region.getSize() * reg.getPriceUnblockChunk(arenda_day_now + 1, arenda_day_now + day_up);

		if (arenda_day_now + day_up > 30)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Максимальное время аренды #30# дней"));
			return true;
		}
		if (p.money < price)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает #" + (price - p.money) + "# " + chat.getNormFormSkit(price - p.money)));
			return true;
		}
		region.time_keep += (day_up * (long)(1000 * 60 * 60 * 24));
		p.money -= price;
		p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы продлили время аренды на #" + day_up + "# " + chat.getNormForm(day_up, "день", "дня", "дней") + " за #" + price + "# " + chat.getNormFormSkit(price)));
		p.sendMessage(rgb.gradientLight(chat.color[0], "Теперь время аренды региона #" + region.name + "# составляет " + time.getTime(region.time_keep, new Date().getTime())));
		return true;
	}

	private static boolean rg_find(player p, String[] args)
	{
		reg rg = null;
		int	distance = -1;
		for (reg region : reg.Regs)
		{
			if (distance == -1 || p.pl.getWorld().getChunkAt(region.x, region.z).getBlock(8, (int) p.pl.getLocation().getY(), 8).getLocation().distance(p.pl.getLocation()) < distance)
			{
				rg = region;
				distance = (int) p.pl.getWorld().getChunkAt(region.x, region.z).getBlock(8, (int) p.pl.getLocation().getY(), 8).getLocation().distance(p.pl.getLocation());
			}
		}
		p.sendMessage(rgb.gradientLight(chat.color[0], "Ближайший регион " + rg.name + " по координатам " + p.pl.getWorld().getChunkAt(rg.x, rg.z).getBlock(8, (int) p.pl.getLocation().getY(), 8).getLocation()));
		return true;
	}

	private static boolean rg_set(player p, String[] args)
	{
		if (p.permision != 10)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Ты не можешь этого делать"));
			return true;
		}
		reg region = reg.getReg(p.pl.getLocation());
		if (!region.isExist())
		{
			region.deleteRegion();
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Тут нет региона"));
			return true;
		}
		if (args[1].equalsIgnoreCase("flag"))
		{
			if (args[2].equalsIgnoreCase("spawn_monster"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.spawn_monster = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.spawn_monster = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("player_damage"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.player_damage = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.player_damage = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("pvp"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.pvp = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.pvp = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("sethome"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.sethome = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.sethome = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("arenda"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.arenda = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.arenda = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("сохнет_пашня"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.arable_dry = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.arable_dry = false;
					return true;
				}
				return true;
			}
			if (args[2].equalsIgnoreCase("break_block"))
			{
				if (args[3].equalsIgnoreCase("true"))
				{
					region.break_block = true;
					return true;
				}
				if (args[3].equalsIgnoreCase("false"))
				{
					region.break_block = false;
					return true;
				}
				return true;
			}
		}
		if (args[1].equalsIgnoreCase("time_arenda"))
		{
			region.time_keep = new Date().getTime() + Long.parseLong(args[2]);
		}
		return true;
	}

	public static boolean check_name(String name)
	{
		for (reg rg : reg.Regs)
		{
			if (name.equalsIgnoreCase(rg.name))
				return true;
		}
		return false;
	}
	
	public static boolean rg_help(player p)
	{
		p.sendMessageE(
				chat.strCommand("Приват чанка в котором вы находитесь:", "/rg claim",  new String[]{"название"}) + "\n" + chat.strCommand("Просмотр чанка в реальном мире:", "F3 + G",  null) + "\n" +
			       chat.strCommand("Информация о чанке в котором вы находитесь:", "/rg info или /rg i",  null) + "\n" +
				   chat.strCommand("Приват нескольких чанков сразу:", "/rg mark или /rg m",  null) + "\n" +
				   chat.strCommand("Добавить игрока:", "/rg addmember или /rg am",  new String[]{"регион", "игрок"}) + "\n" +
				   chat.strCommand("Выгнать игрока:", "/rg removemember или /rg rm",  new String[]{"регион", "игрок"}) + "\n" +
				   chat.strCommand("Продлить аренду региона:", "/rg up",  new String[]{"количество дней"})
		);
		return true;
	}

	public static boolean rg_claim(player p, String []args)
	{
		if (!p.pl.getWorld().getName().equals("world"))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"В аду регионы пока не работают"));
			return true;
		}
		if (p.blockRegs != 0)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"У вас #" + p.blockRegs + "# " + chat.getNormForm(p.blockRegs, "заблокированный чанк", "заблокированных чанка", "заблокированных чанков") +
					"\nРазблокируйте их у #Альберта#, он ходит на спавне"));
			return true;
		}
		if (args.length != 2)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Укажите название привата: ") + rgb.gradientLight(chat.color[1],"/rg claim <название>"));
			return true;
		}
		if (chat.getCountChar(args[1], ':') != 0)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Запрещено использовать символ ':'"));
			return true;
		}
		if (args[1].length() >= 25)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Ваше название не может быть длинее 24 символов"));
			return true;
		}
		if (check_name(args[1]))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Это имя уже занято"));
			return true;
		}
		if (p.mark)
		{
			claim_region(p, args);
			return true;
		}
		if (p.allowRegs != -1 && p.getRegsCount() == p.allowRegs)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Вы достигли максимального количества приватов: ") + rgb.gradientLight(chat.color[1],p.allowRegs + ""));
			return true;
		}
		Chunk ch = p.pl.getLocation().getChunk();
		reg region = reg.getReg(ch.getX(), ch.getZ());
		if (region.owner != null)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Этот чанк недоступен для привата"));
			return true;
		}

		region.name = args[1];
		region.owner = p.p;
		p.sendMessageG(rgb.gradientLight(chat.color[0],"Вы создали приват с именем ") + rgb.gradientLight(chat.color[1],args[1]));
		p.ErrorMessage("Время аренды региона #3# дня#,# подробнее #/i rg");
		return true;
	}

	public static void claim_region(player p, String []args)
	{
		if (!allowPrivatArea(p))
			return ;

		int x = Math.min(p.x, p.expand_x);
		int expand_x = Math.max(p.x, p.expand_x) - Math.min(p.x, p.expand_x);

		int z = Math.min(p.z, p.expand_z);
		int expand_z = Math.max(p.z, p.expand_z) - Math.min(p.z, p.expand_z);

		reg region = reg.getReg(x, z);

		if (region.owner != null)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Ваш регион пересекает другой регион с названием " + region.name));
			return ;
		}

		region.name = args[1];

		region.x1 = expand_x;
		region.z1 = expand_z;

		if (region.intersection() != null && region.intersection().isExist())
		{
			region.deleteRegion();
			p.sendMessageE(rgb.gradientLight(chat.color[0], "Ваш регион пересекает другой регион с названием " + region.intersection().name));
			return ;
		}

		region.owner = p.p;
		p.sendMessageG(rgb.gradientLight(chat.color[0],"Вы создали приват с именем ") + rgb.gradientLight(chat.color[1],args[1]));
	}

	public static boolean rg_info(player p, String []args)
	{
		if (!p.pl.getWorld().getName().equals("world"))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"В аду регионы пока не работают"));
			return true;
		}
		Chunk ch = p.pl.getLocation().getChunk();
		reg region = reg.getReg(ch.getX(), ch.getZ());

		p.sendMessage(rgb.gradientLight(chat.color[0],"Регион: ") + ChatColor.RESET + (region.name == null ? rgb.gradientLight(chat.color[1], "мировой") : rgb.gradientLight(chat.color[1], region.name)));

		text.info_message(
				p.pl,
				new String[]{
						"    Владелец",
						"    Участники",
						"    Площадь",
						"    Флаги",
						"    Время аренды",
						"    Фракции"
						},
				new String[]{
						(region.owner == null ? "никто" : region.owner),
						region.members.isEmpty() ? "никого" : from_list(region.members),
						(region.x1 + 1) * (region.z1 + 1) + " чанков" + "\n" + (region.x1 + 1) * (region.z1 + 1) * 256 + " блоков" + "\n" + (region.x1 + 1) + " на " + (region.z1 + 1) + " чанков",
						"спавн монстрев " + (region.spawn_monster ? "включен\n" : "выключен\n") +
								"урон игроку " + (region.player_damage ? "включен\n" : "выключен\n") +
								"урон между игроками " + (region.pvp ? "включен\n" : "выключен\n") +
								"установка дома " + (region.sethome ? "включена\n" : "выключена\n") +
								"плата за аренду " + (region.arenda ? "включена\n" : "выключена\n") +
								(region.arable_dry ? "пашня сохнет\n" : "пашня не сохнет\n") +
								(region.break_block ? "блоки ломать можно тем кто в привате\n" : "блоки ломать нельзя\n"),
						region.arenda ? "Осталось времени аренды " + time.getTime(region.time_keep, new Date().getTime()) : "пару лет уж точно",
						from_list(region.getClans()),
				}
				);

		Location loc = new Location(p.pl.getWorld(), region.x * 16, 0, region.z * 16);

		highlight_region(p.pl, loc, (region.x1 + 1) * 16, (region.z1 + 1) * 16);

		if (region.owner == null)
			region.deleteRegion();
		return true;
	}

	public static <T extends Object> String from_list(ArrayList<T> list)
	{
		if (list.size() == 0)
			return "";
		if (list.get(0) instanceof String)
		{
			String ret = "";
			for (Object str : list)
				ret += str + (list.get(list.size() - 1) != str ? "\n" : "");
			return ret;
		}
		if (list.get(0) instanceof Clan)
		{
			String ret = "";
			for (Object str : list)
				ret += ((Clan)str).getName() + (list.get(list.size() - 1) != ((Clan)str).getName() ? "\n" : "");
			return ret;
		}
		return "";
	}

	public static boolean rg_clear(player p, String []args)
	{
		if (!p.p.equals("_GreyBrick_"))
			return true;
			if (args[1].equals("9623"))
			{
				reg.Regs.clear();
				return true;
			}
		return true;
	}

	public static boolean rg_delete(player p, String []args)
	{
		if (args.length != 2)
		{
			p.sendMessageE(chat.strCommand("Используйте: ", "/rg delete" , new String[]{"название региона"}));
			return true;
		}
		for (reg region : reg.Regs)
		{
			if (region.name != null && region.name.equals(args[1]))
			{
				if (region.owner.equals(p.p))
				{
					if (region.time_keep - new Date().getTime() < ((1000 * 60 * 60 * 24 * 3) - 1000 * 60 * 60))
					{
						p.ErrorMessage("Время аренды региона должна быть #больше# чем #2# дня и #23# часа");
						return true;
					}
					region.deleteRegion();
					p.sendMessageG(rgb.gradientLight(chat.color[0],"Регион ") + rgb.gradientLight(chat.color[1], args[1]) + rgb.gradientLight(chat.color[0], " удален"));
					return true;
				}
				p.sendMessageE(rgb.gradientLight(chat.color[0], "Вы не являетесь владельцем этого региона"));
				return true;
			}
		}
		p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас нет такого региона"));
		return true;
	}

	public static boolean rg_mark(player p, String []args)
	{
		if (!p.pl.getWorld().getName().equals("world"))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"В аду регионы пока не работают"));
			return true;
		}
		if (args.length != 2 || !(args[1].equals("1") || args[1].equals("2") || args[1].equals("off")))
		{
			p.sendMessageE(rgb.getGradientInterval(chat.color[1], rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.9), 0.25) + "? "
					+ rgb.gradientLight(chat.color[0], "Встаньте в первый чанк и отметьте его: ") + rgb.gradientLight(chat.color[1], "/rg mark 1 "));
			p.sendMessage(rgb.getGradientInterval(chat.color[1], rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.9), 0.50) + "? "
					+ rgb.gradientLight(chat.color[0], "Встаньте во второй чанк и отметьте его: ") + rgb.gradientLight(chat.color[1], "/rg mark 2 "));
			p.sendMessage(rgb.getGradientInterval(chat.color[1], rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.9), 0.75) + "? "
					+ rgb.gradientLight(chat.color[0], "Используйте : ") + rgb.gradientLight(chat.color[1], "/rg claim ") + rgb.gradientLight(chat.color[0], "для привата выделенного региона"));
			p.sendMessage(rgb.getGradientInterval(chat.color[1], rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.9), 1) + "? "
					+ rgb.gradientLight(chat.color[0], "Для сброса отметок используйте: ") + rgb.gradientLight(chat.color[1], "/rg mark off "));
			return true;
		}
		Chunk ch = p.pl.getLocation().getChunk();
		if (args[1].equals("1"))
		{
			p.x = ch.getX();
			p.z = ch.getZ();
			p.mark = true;
			p.sendMessage(rgb.gradientLight(chat.color[0], "Вы отметили этот чанк с координатами ") + chat.color[1] + ch.getX() + " " + ch.getZ());
			if (p.expand_x == 0 && p.expand_z == 0)
			{
				p.sendMessage(rgb.gradientLight(chat.color[0], "Отметьте теперь второй чанк"));
			}
			else
			{
				p.sendMessage(rgb.gradientLight(chat.color[0], "Площадь выделенного региона ") + rgb.gradientLight(chat.color[1], p.getArea() + ""));
				allowPrivatArea(p);
			}
			return true;
		}
		if (args[1].equals("2"))
		{
			p.expand_x = ch.getX();
			p.expand_z = ch.getZ();
			p.mark = true;
			p.sendMessage(rgb.gradientLight(chat.color[0], "Вы отметили этот чанк с координатами ") + chat.color[1] + ch.getX() + " " + ch.getZ());
			if (p.x == 0 && p.z == 0)
			{
				p.sendMessage(rgb.gradientLight(chat.color[0], "Отметьте теперь первый чанк"));
			}
			else
			{
				p.sendMessage(rgb.gradientLight(chat.color[0], "Площадь выделенного региона ") + rgb.gradientLight(chat.color[1], p.getArea() + ""));
				allowPrivatArea(p);
			}
			return true;
		}
		p.x = 0;
		p.z = 0;
		p.expand_x = 0;
		p.expand_z = 0;
		p.mark = false;
		p.sendMessage(rgb.gradientLight(chat.color[0], "Вы сбросили отметки, теперь вы можете приватить отдельные чанки"));
		return true;
	}

	public static boolean allowPrivatArea(player p)
	{
		if (p.allowRegs != -1 && (p.allowRegs - p.getChunkCount()) < p.getArea())
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас не хватает разрешенных чанков для привата этого региона"));
			p.sendMessage(
					rgb.gradientLight(chat.color[0], "Вы можете заприватить еще ")
							+ rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.5)
							+ (p.allowRegs - p.getChunkCount())
							+ rgb.gradientLight(chat.color[0], " чанков из ")
							+ rgb.getGradientInterval(chat.color[1], ChatColor.of("#FFFFFF"), 0.5)
							+ p.allowRegs);
			return false;
		}
		return true;
	}

	public static void highlight_region(Player p, Location p_loc, int x, int z)
	{
		Location loc = p.getLocation();
		for (int i = 0; i <= x; i++)
		{
			if (Math.abs(loc.getBlockX() - (p_loc.getBlockX() + i)) >= 64)
				continue ;
			for (int i1 = 0; i1 <= 256; i1++)
			{
				if ((Math.abs(loc.getY() - i1)) >= 13)
					continue ;
				for (int i2 = 0; i2 <= z; i2++)
				{
					if (Math.abs(loc.getBlockZ() - (p_loc.getBlockZ() + i2)) >= 64)
						continue ;

					Location loc_part = new Location(p_loc.getWorld(), p_loc.getBlockX() + i, i1, p_loc.getBlockZ() + i2);

					if ((i == 0 || i == x || (loc_part.getBlockX() % 16 == 0 && loc_part.getBlockZ() % 16 == 0)) || (i2 == 0 || i2 == z))
					{
						if (loc.distance(loc_part) < 16) {
							if ((loc_part.getChunk().getX() + loc_part.getChunk().getZ()) % 2 == 0)
								p.spawnParticle(Particle.values()[26], loc_part, 1, 0, 0, 0, 0);
							else
								p.spawnParticle(Particle.values()[65], loc_part, 1, 0, 0, 0, 0);
							continue ;
						}
						if (loc.distance(loc_part) < 24 && (i + i1 + i2) % 2 == 0) {
							if ((loc_part.getChunk().getX() + loc_part.getChunk().getZ()) % 2 == 0)
								p.spawnParticle(Particle.values()[26], loc_part, 1, 0, 0, 0, 0);
							else
								p.spawnParticle(Particle.values()[65], loc_part, 1, 0, 0, 0, 0);
							continue ;
						}
						if (loc.distance(loc_part) < 32 && (i + i1 + i2) % 4 == 0) {
							if ((loc_part.getChunk().getX() + loc_part.getChunk().getZ()) % 2 == 0)
								p.spawnParticle(Particle.values()[26], loc_part, 1, 0, 0, 0, 0);
							else
								p.spawnParticle(Particle.values()[65], loc_part, 1, 0, 0, 0, 0);
							continue ;
						}
						if (loc.distance(loc_part) < 48 && (i + i1 + i2) % 6 == 0) {
							if ((loc_part.getChunk().getX() + loc_part.getChunk().getZ()) % 2 == 0)
								p.spawnParticle(Particle.values()[26], loc_part, 1, 0, 0, 0, 0);
							else
								p.spawnParticle(Particle.values()[65], loc_part, 1, 0, 0, 0, 0);
							continue ;
						}
						if (loc.distance(loc_part) < 64 && (i + i1 + i2) % 8 == 0) {
							if ((loc_part.getChunk().getX() + loc_part.getChunk().getZ()) % 2 == 0)
								p.spawnParticle(Particle.values()[26], loc_part, 1, 0, 0, 0, 0);
							else
								p.spawnParticle(Particle.values()[65], loc_part, 1, 0, 0, 0, 0);
							continue ;
						}
					}
				}
			}
		}
	}

	public static boolean rg_show(player p, String []args)
	{
		if (!p.pl.getWorld().getName().equals("world"))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"В аду регионы пока не работают"));
			return true;
		}
		if ((args.length == 1 && p.show_reg != null) || args.length == 2 && args[1].equalsIgnoreCase("off"))
		{
			if (p.show_reg == null)
			{
				p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас нет показываемых регионов"));
				return true;
			}
			p.show_reg = null;
			p.sendMessageG(rgb.gradientLight(chat.color[0], "Показ региона прекратился"));
			return true;
		}
		if ((args.length == 1 && p.show_reg == null) || args.length == 1 && args[1].equalsIgnoreCase("on"))
		{
			if (p.show_reg != null)
			{
				p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас уже включен показ региона"));
				return true;
			}
			reg region = reg.getReg(p.pl.getLocation().getChunk().getX(), p.pl.getLocation().getChunk().getZ());
			if (region.owner == null)
			{
				p.sendMessageE(rgb.gradientLight(chat.color[0], "Тут нет заприваченого региона"));
				return true;
			}

			Location loc = new Location(p.pl.getWorld(), region.x * 16, 0, region.z * 16);

			p.show_reg = region;

			int interval = 5 + region.x1 * region.z1 / 20;

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					highlight_region(p.pl, loc, (region.x1 + 1) * 16, (region.z1 + 1) * 16);
					if (!p.pl.isOnline() || p.show_reg == null)
						cancel();
				}
			}.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, interval);

			p.sendMessageG(chat.strCommand("Вы включили показ региона, чтобы выключить используйте", "/rg show off", null));
		}

		return true;
	}

	public static boolean rg_am(player p, String []args)
	{
		if (args.length != 3)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Используйте: ") + rgb.gradientLight(chat.color[1],"/rg " + args[0] + " <регион> <игрок>"));
			return true;
		}
		reg region = reg.getReg(args[1]);
		if (region == null)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"У вас нет такого региона"));
			return true;
		}
		if (!region.owner.equals(p.p))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Вы не являетесь владельцем этого региона"));
			return true;
		}
		if (!player.isExist(args[2]))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Игрок не появлялся не сервере"));
			return true;
		}
		if (args[2].equals(p.p))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Чо еще?"));
			return true;
		}
		if (region.members.contains(args[2]))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Уже участник"));
			return true;
		}
		player add_player = player.getPlayer(args[2]);
		region.addmember(add_player);
		if (add_player.isOnline())
			add_player.sendMessage(rgb.gradientLight(chat.color[0], "Вас добавили в регион ") + rgb.gradientLight(chat.color[1], args[1]));
		p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы добавили игрока ") + rgb.gradientLight(chat.color[1], args[2]) + rgb.gradientLight(chat.color[0], " в регион ") + rgb.gradientLight(chat.color[1], args[1]));
		return true;
	}

	public static boolean rg_rm(player p, String []args)
	{
		if (args.length != 3)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Используйте: ") + rgb.gradientLight(chat.color[1],"/rg " + args[0] + " <регион> <игрок>"));
			return true;
		}
		reg region = reg.getReg(args[1]);
		if (region == null)
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"У вас нет такого региона"));
			return true;
		}
		if (!region.owner.equals(p.p))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Вы не являетесь владельцем этого региона"));
			return true;
		}
		if (!region.members.contains(args[2]))
		{
			p.sendMessageE(rgb.gradientLight(chat.color[0],"Не является участником"));
			return true;
		}
		player add_player = player.getPlayer(args[2]);
		region.removemember(add_player);
		if (add_player.isOnline())
			add_player.sendMessageE(rgb.gradientLight(chat.color[0], "Вас выгнали из региона ") + rgb.gradientLight(chat.color[1], args[1]));
		p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы выгняли игрока ") + rgb.gradientLight(chat.color[1], args[2]) + rgb.gradientLight(chat.color[0], " из региона ") + rgb.gradientLight(chat.color[1], args[1]));
		return true;
	}

	public static boolean rg_tab(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"claim",
				"list",
				"delete",
				"info",
				"help",
				"mark",
				"show",
				"addmember",
				"removemember",
				"set",
				"up",
				"arena"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				s.toLowerCase();
				e.getBuffer().split(" ")[i].toLowerCase();
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("d") || e.getBuffer().split(" ")[i].equalsIgnoreCase("delete"))
			return rg_tab_delete(e, i + 1);
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("m") || e.getBuffer().split(" ")[i].equalsIgnoreCase("mark"))
			return rg_tab_mark(e, i + 1);
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("s") || e.getBuffer().split(" ")[i].equalsIgnoreCase("show"))
			return rg_tab_show(e, i + 1);
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("am") || e.getBuffer().split(" ")[i].equalsIgnoreCase("addmember"))
			return rg_tab_am(e, i + 1);
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("rm") || e.getBuffer().split(" ")[i].equalsIgnoreCase("removemember"))
			return rg_tab_rm(e, i + 1);
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("set"))
			return rg_tab_set(e, i + 1);

		e.setCancelled(true);
		return true;
	}

	public static boolean rg_tab_delete(TabCompleteEvent e, int i)
	{
		player p = player.getPlayer((Player) e.getSender());
		String[] list = new String[p.getRegsCount()];
		ArrayList<reg> regsOwner = p.getRegsOwner();
		for (int m = 0; m < p.getRegsCount(); m++)
			list[m] = regsOwner.get(m).name;

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		e.setCancelled(true);
		return true;
	}

	public static boolean rg_tab_mark(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"1",
				"2",
				"off"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		e.setCancelled(true);
		return true;
	}

	public static boolean rg_tab_show(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"off",
				"on"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		e.setCancelled(true);
		return true;
	}

	public static boolean rg_tab_am(TabCompleteEvent e, int i)
	{
		player p = player.getPlayer((Player) e.getSender());
		String[] list = new String[p.getRegsCount()];
		ArrayList<reg> regsOwner = p.getRegsOwner();
		for (int m = 0; m < p.getRegsCount(); m++)
			list[m] = regsOwner.get(m).name;

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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

		ArrayList<String> list_name = new ArrayList<>();
		for (String name : list)
			list_name.add(name);

		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		else if (list_name.contains(e.getBuffer().split(" ")[2]))
			return rg_tab_am_member(e, i + 1);
		return true;
	}

	public static boolean rg_tab_am_member(TabCompleteEvent e, int i)
	{
		String[] list = new String[graf.Players.getKeys(false).size()];
		int m = 0;
		for (String name : graf.Players.getKeys(false))
			list[m++] = name;

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		return true;
	}

	public static boolean rg_tab_rm(TabCompleteEvent e, int i)
	{
		player p = player.getPlayer((Player) e.getSender());
		String[] list = new String[p.getRegsCount()];
		ArrayList<reg> regsOwner = p.getRegsOwner();
		for (int m = 0; m < p.getRegsCount(); m++)
			list[m] = regsOwner.get(m).name;

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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

		ArrayList<String> list_name = new ArrayList<>();
		for (String name : list)
			list_name.add(name);

		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		else if (list_name.contains(e.getBuffer().split(" ")[2]))
			return rg_tab_rm_member(e, i + 1);
		return true;
	}

	public static boolean rg_tab_rm_member(TabCompleteEvent e, int i)
	{
		reg region = reg.getReg(e.getBuffer().split(" ")[2]);
		String[] list = new String[region.members.size() == 0 ? 1 : region.members.size()];
		for (int m = 0; m < region.members.size(); m++)
			list[m] = region.members.get(m);

		if (region.members.size() == 0)
			list[0] = "участников нет в регионе " + e.getBuffer().split(" ")[2];

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		return true;
	}

	public static boolean rg_tab_set(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"flag",
				"time_arenda"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("flag"))
			return rg_tab_set_flag(e, i + 1);
		return true;
	}

	public static boolean rg_tab_set_flag(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"spawn_monster",
				"player_damage",
				"pvp",
				"sethome",
				"arenda",
				"сохнет_пашня",
				"break_block"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		else if (e.getBuffer().split(" ")[i].equalsIgnoreCase("spawn_monster") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("player_damage") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("pvp") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("sethome") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("arenda") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("сохнет_пашня") ||
				e.getBuffer().split(" ")[i].equalsIgnoreCase("break_block"))
			return rg_tab_set_flag_spawn_monster(e, i + 1);
		return true;
	}

	public static boolean rg_tab_set_flag_spawn_monster(TabCompleteEvent e, int i)
	{
		String[] list = new String[]{
				"true",
				"false"
		};

		ArrayList<String> flist = new ArrayList<>();
		if (e.getBuffer().split(" ").length > i && chat.getCountChar(e.getBuffer(), ' ') == i) {
			for (String s : list) {
				int x;
				char f;

				x = -1;
				f = 0;
				for (byte b : e.getBuffer().split(" ")[i].getBytes()) {
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
		if (e.getBuffer().split(" ").length <= (i + 1) && chat.getCountChar(e.getBuffer(), ' ') == i)
		{
			e.setCompletions(Arrays.asList(list));
			return true;
		}
		return true;
	}
}
