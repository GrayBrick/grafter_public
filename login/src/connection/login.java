package connection;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import graf.chat;
import graf.client;
import graf.graf;
import graf.player;
import net.md_5.bungee.api.ChatColor;
import text_processing.rgb;

public class login {

	public static String gotoIp = "127.0.0.1";

	public static void login(PlayerJoinEvent e)
	{
		Player pl = e.getPlayer();
		pl.setOp(false);
		if (!registration.isASCII(pl.getName()))
		{
			String str = new String();
			for (int i = 0; i < pl.getName().length(); i++)
				if (pl.getName().charAt(i) != pl.getName().getBytes(StandardCharsets.US_ASCII)[i])
					str += "" + ChatColor.RED + pl.getName().charAt(i);
				else
					str += "" + ChatColor.GREEN + pl.getName().charAt(i);
			pl.kickPlayer(chat.color[0] + "Вы использовали не верные символы в нике\n" + str);
			return ;
		}
		pl.teleport(new Location(Bukkit.getWorld("world"), 8.5, 10, 8.5));
		new Location(Bukkit.getWorld("world"), 8.5, 9, 8.5).getBlock().setType(Material.QUARTZ_BLOCK);
		e.setJoinMessage(null);
		player p = new player(pl);

//		if (if_ban(p) || if_ip(p))
//			return;

		pl.getInventory().clear();
		pl.setGameMode(GameMode.ADVENTURE);
		pl.setWalkSpeed((float) 0.2);
		pl.setFlySpeed((float) 0.2);
		if (p.getpas() == null)
		{
			rgb.gradientTitle(p.pl, new String[]{"Зарегистрируйтесь", ""}, chat.title_inv, 3, 5000, 0.9, 0.9);
			registration.registration(p);
			return ;
		}
		rgb.gradientTitle(p.pl, new String[]{"Войдите в аккаунт", "/l <ваш пароль>"}, chat.title_inv, 3, 5000, 0.9, 0.9);
		final int []count = new int[1];
		count[0] = 0;
		new BukkitRunnable()
		{
            @Override
            public void run()
            {
            	if (!pl.isOnline())
            		cancel();
            	if (count[0] == 6)
            	{
            		p.pl.kickPlayer(chat.color[0] +"Вы долго входили, попробуйте еще раз");
            		cancel();
            	}
            	count[0]++;
            	p.sendMessage(chat.strCommand("Привет! Для входа используй команду:", "/l",  new String[]{"пароль"}));
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("graf"), 0, 20 * 5);
	}
	
	public static boolean if_ban(player p)
	{
		if (p.getTryTime() > new Date().getTime())
		{
			p.pl.kickPlayer(chat.color[0] + "Подожди еще " + chat.color[1] + graf.time(p.getTryTime() - new Date().getTime()) + chat.color[0] + " прежде чем ввести пароль еще раз\n");
			return true;
		}
		return false;
	}
	
	public static boolean if_ip(player p)
	{
    	if (p.getip().equals(p.pl.getAddress().getHostString()))
    	{
    		gotoserver(p.p);
    		return true;
    	}
    	return false;
	}
	
	public static void log(String[] args, player p)
	{
		if (p.getpas() == null)
		{
			rgb.gradientTitle(p.pl, new String[]{"Для начала зарегистрируйся"});
			return ;
		}
		if (args.length != 1)
			return ;
		if (!args[0].equals(p.getpas()))
		{
			if (p.gettry() == 1)
			{
				if (p.gettryb() != 1)
				{
					p.pl.kickPlayer(chat.color[0] + "Ты 3 раза неверно ввел пароль");
					p.settry(3);
					p.settryb(p.gettryb() - 1);
					return ;
				}
				p.setTryTime(new Date().getTime() + 1000 * 60 * 5);
				p.settry(3);
				p.settryb(3);
				p.pl.kickPlayer(chat.color[0] + "Подожди еще " + chat.color[1] + graf.time(p.getTryTime() - new Date().getTime()) + chat.color[0] + " прежде чем ввести пароль еще раз\n");
			}
			p.pl.sendTitle(chat.color[0] + "Пароль не верный", chat.color[1] + "У тебя еще " + (p.gettry() - 1) + (p.gettry() - 1 != 1 ? " попытки" : " попытка"), 10, 70, 20);
			p.settry(p.gettry() - 1);
			return ;
		}
		p.settry(3);
		p.settryb(3);
		gotoserver(p.p);
		p.setip(p.pl.getAddress().getHostString());
	}
	
	public static void move(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (e.getFrom().getY() != e.getTo().getY()
		 || e.getFrom().getX() != e.getTo().getX()
		 || e.getFrom().getZ() != e.getTo().getZ())
				e.setCancelled(true);
	}
	
	public static void gotoserver(String player)
	{
		try
		{
			client.sendMes(gotoIp, 25000, "goto@sb0@" + player);
		} catch(Exception ex) {}	
	}
}
