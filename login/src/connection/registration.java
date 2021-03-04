package connection;

import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import graf.chat;
import graf.player;
import net.md_5.bungee.api.ChatColor;
import text_processing.rgb;

public class registration {
	
	public static void registration(player p)
	{
		final int []count = new int[1];
		count[0] = 0;
		BukkitTask r1 = new BukkitRunnable()
		{
            @Override
            public void run()
            {
            	if (!p.pl.isOnline())
            		cancel();
            	if (count[0] == 7)
            	{
            		p.pl.kickPlayer(chat.color[0] + "Вы долго регистрируетесь, попробуйте еще раз");
            		cancel();
            	}
            	count[0]++;
            	p.sendMessage(chat.strCommand("Привет! Для регистрации используй команду:", "/reg",  new String[]{"пароль", "повторение пароля"}));
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("graf"), 0, 20 * 5);
	}
	
	public static void reg(String[] args, player p)
	{
		if (p.getpas() != null || args.length != 2)
		{
			return ;
		}
		if (!args[0].equals(args[1]))
		{
			String str = new String();
			for (int i = 0; i < (args[0].length() > args[1].length() ? args[0].length() : args[1].length()); i++)
			{
				if (i >= args[0].length() || i >= args[1].length() || args[0].charAt(i) != args[1].charAt(i))
					str += "" + ChatColor.RED + (args[0].length() > args[1].length() ? args[0].charAt(i) : args[1].charAt(i));
				else
					str += "" + ChatColor.GREEN + (args[0].length() > args[1].length() ? args[0].charAt(i) : args[1].charAt(i));
			}
			rgb.gradientTitle(p.pl, new String[]{"Пароли не совпадают", str}, true, false);
			return ;
		}
		if (!isASCII(args[0]))
		{
			String str = new String();
			for (int i = 0; i < args[0].length(); i++)
				if (args[0].charAt(i) != args[0].getBytes(StandardCharsets.US_ASCII)[i])
					str += "" + ChatColor.RED + args[0].charAt(i);
				else
					str += "" + ChatColor.GREEN + args[0].charAt(i);
			rgb.gradientTitle(p.pl, new String[]{"Пароль содержит недопустимые символы", str}, true, false);
			return ;
		}
		if (args[0].length() < 7)
		{
			rgb.gradientTitle(p.pl, new String[]{"Пароль короче 7 семволов", ""}, true, false);
			return ;
		}
		p.setpas(args[0]);
		p.settry(3);
		p.settryb(3);
		login.gotoserver(p.p);
		p.setip(p.pl.getAddress().getHostString());
	}
	
	public static boolean isASCII(String str)
	{
		byte[] b = str.getBytes(StandardCharsets.US_ASCII);
		if (!str.equals(new String(b)))
			return false;
		return true;
	}
}
