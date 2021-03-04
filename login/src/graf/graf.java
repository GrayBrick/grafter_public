package graf;

import java.io.File;
import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import connection.login;
import connection.registration;
import text_processing.rgb;


public class graf extends JavaPlugin implements Listener
{	
	server serv = new server(25001);
	static ArrayList<String>	        allowedIp = new ArrayList<>();
	public static FileConfiguration		Players;
    public static File                  fileconfig;
    FileConfiguration                   config;
	
	public void onEnable()
	{
		server.putIp(allowedIp);
		Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		Bukkit.getWorld("world").setPVP(false);
		Bukkit.getServer().getPluginManager().registerEvents(new events(), this);
		
		File fileplayer = new File("plugins/Players.yml");
		fileconfig= new File("plugins/config.yml");
		try
		{
			fileconfig.createNewFile();
			fileplayer.createNewFile();
		} catch (Exception localException) {}

        config = getcon();

		try {

			for (int i = 0; i < chat.color.length; i++)
				chat.color[i] = ChatColor.of(config.getString("color_" + i));

		} catch(Exception ex) {};
		
		BukkitTask time = new BukkitRunnable()
		{
            @Override
            public void run()
            {
            	try
				{
					serv.startServer();
				} catch (Exception ex) {}
       		
            }
        }.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("graf"));
		
		Players = player.getconPlayer();
	}
	
	public void onDisable()
	{		
		try
		{
			serv.ss.close();
		} catch(Exception ex) {}
		
		player.saveconPlayer();
	}
	
	public boolean onCommand(CommandSender sender, Command c, String s, String[] args)
	{
		Player p = (Player) sender;
		if (c.getName().equalsIgnoreCase("test"))
		{

			String plato[][] = new String[][]{
					new String[]{
							" ██████╗ ██████╗  █████╗ ███████╗████████╗███████╗██████╗     ",
							"██╔════╝ ██╔══██╗██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗    ",
							"██║  ███╗██████╔╝███████║█████╗     ██║   █████╗  ██████╔╝    ",
							"██║   ██║██╔══██╗██╔══██║██╔══╝     ██║   ██╔══╝  ██╔══██╗    ",
							"╚██████╔╝██║  ██║██║  ██║██║        ██║   ███████╗██║  ██║    ",
							" ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝        ╚═╝   ╚══════╝╚═╝  ╚═╝    "
					},
					new String[]{
							"                               ▄▄▄▄                           ",
							"  ▄▄█▀▀▀█▄█                  ▄█▀ ▀▀██▀▀██▀▀███                ",
							"▄██▀     ▀█                  ██▀  █▀   ██   ▀█                ",
							"██▀       ▀▀███▄███ ▄█▀██▄  █████      ██      ▄▄█▀██▀███▄███ ",
							"██           ██▀ ▀▀██   ██   ██        ██     ▄█▀   ██ ██▀ ▀▀ ",
							"██▄    ▀████ ██     ▄█████   ██        ██     ██▀▀▀▀▀▀ ██     ",
							"▀██▄     ██  ██    ██   ██   ██        ██     ██▄    ▄ ██     ",
							"  ▀▀███████▄████▄  ▀████▀██▄████▄    ▄████▄    ▀█████▀████▄   ",
							"                                                              ",
							"                                                              "
					},
					new String[]{
							" ▄▄▄▄▄▄▄ ▄▄▄▄▄▄   ▄▄▄▄▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄   ",
							"█       █   ▄  █ █      █       █       █       █   ▄  █  ",
							"█   ▄▄▄▄█  █ █ █ █  ▄   █    ▄▄▄█▄     ▄█    ▄▄▄█  █ █ █  ",
							"█  █  ▄▄█   █▄▄█▄█ █▄█  █   █▄▄▄  █   █ █   █▄▄▄█   █▄▄█▄ ",
							"█  █ █  █    ▄▄  █      █    ▄▄▄█ █   █ █    ▄▄▄█    ▄▄  █",
							"█  █▄▄█ █   █  █ █  ▄   █   █     █   █ █   █▄▄▄█   █  █ █",
							"█▄▄▄▄▄▄▄█▄▄▄█  █▄█▄█ █▄▄█▄▄▄█     █▄▄▄█ █▄▄▄▄▄▄▄█▄▄▄█  █▄█"
					},
					new String[]{
									"█▀▀█ █▀▀▄ █▀▀▄ █▀▀ ▀▀█▀▀ █▀▀ █▀▀▄",
									"█ ▄▄ █▄▄▀ █▄▄█ █▀    █   █▀▀ █▄▄▀",
									"█▄▄▀ ▀ ▀▀ ▀  ▀ ▀     █   ▀▀▀ ▀ ▀▀"
					}
			};




			int index = 3;//(int)(Math.random() * plato.length);

			final int count[] = new int[]{0};

			ChatColor list_colors[] = new ChatColor[]{
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor(),
					rgb.randomColor()
			};

			final int time = Integer.parseInt(args[0]);

			BukkitTask adh = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					try {
						count[0]++;
						for (int i = 0; i < plato[index].length; i++)
						{
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("123\n123").create());
						}
						if (count[0] == time)
							cancel();
					} catch (Exception ex) {}

				}
			}.runTaskTimer(Bukkit.getPluginManager().getPlugin("graf"), 0, 0);

			return true;
		}
//			double R = Double.parseDouble(args[0]);
//			double r = Double.parseDouble(args[1]);
//			double d = Double.parseDouble(args[2]);
//			
//			double d1;
//			double d2;
//			double m;
//			double a;
//			double b;
//			double s1;
//			double s2;
//			
//			Thread myThready = null;
//	
//			long l1 = new Date().getTime();
//			
//			for (int i = 0; i < Math.sqrt(Integer.parseInt(args[3])); i++)
//			{
//				myThready = new Thread(new Runnable()
//				{
//					public void run()
//					{
//						for (int i = 0; i < Math.sqrt(Integer.parseInt(args[3])); i++)
//						{
//							if (d > R + r)
//								return ;
//							double d1 = (d * d - r * r + R * R)/(2 * d);
//							double d2 = (d * d + r * r - R * R)/(2 * d);
//							
//							double m = Math.sqrt(R * R - d1 * d1);
//							
//							double a = Math.toDegrees(Math.acos((2 * R * R - 4 * m * m) / (2 * R * R)));
//							double b = Math.toDegrees(Math.acos((2 * r * r - 4 * m * m) / (2 * r * r)));
//							
//							double s1 = R * R * (Math.PI * a - 180 * Math.sin(Math.toRadians(a))) / 360;
//							double s2 = r * r * (Math.PI * b - 180 * Math.sin(Math.toRadians(b))) / 360;
//						}
//					}
//				});
//				myThready.start();
//			}
//			
//			long l2 = 0;
//			while (myThready.isAlive())
//				l2 = new Date().getTime();
//			System.out.println("stop: " + (l2 - l1));
//			
//			long l11 = new Date().getTime();
//			
//			for (int i = 0; i < Integer.parseInt(args[3])
//					; i++)
//			{
//							if (d > R + r)
//								return true;
//							d1 = (d * d - r * r + R * R)/(2 * d);
//							d2 = (d * d + r * r - R * R)/(2 * d);
//							
//							m = Math.sqrt(R * R - d1 * d1);
//							
//							a = Math.toDegrees(Math.acos((2 * R * R - 4 * m * m) / (2 * R * R)));
//							b = Math.toDegrees(Math.acos((2 * r * r - 4 * m * m) / (2 * r * r)));
//							
//							s1 = R * R * (Math.PI * a - 180 * Math.sin(Math.toRadians(a))) / 360;
//							s2 = r * r * (Math.PI * b - 180 * Math.sin(Math.toRadians(b))) / 360;
//			}
//			
//			long l21 = new Date().getTime();
//			System.out.println("stop: " + (l21 - l11));
//			
//			//System.out.println("\nd1: " + d1 + "\nd2: " + d2 + "\nm : " + m + "\na : " + a + "\nb : " + b + "\ns1: " + s1 + "\ns2: " + s2 + "\ns : " + (s1 + s2));
//			return true;
//		}
	    if (c.getName().equalsIgnoreCase("reg") || c.getName().equalsIgnoreCase("register") || c.getName().equalsIgnoreCase("r"))
	    	registration.reg(args, new player(p));

	    if (c.getName().equalsIgnoreCase("l") || c.getName().equalsIgnoreCase("log") || c.getName().equalsIgnoreCase("login"))
	    	login.log(args, new player(p));
	    return true;
	}
	
	public static String time(long x) {
		String s = null;
		x /= 1000;
		
		if(x<60) {
			s = x+"с.";
		} else
		if(x<3600) {
			s = Math.round(x/60)+"м. "+(x-Math.round(x/60)*60)+"с.";
		} else 
	    if(x<86400) {
	    	s = Math.round(x/3600)+"ч. "+Math.round((x-Math.round(x/3600)*3600)/60)+"м. "+(x-Math.round(x/60)*60)+"с. ";
	    } else {
	    	s = Math.round(x/86400)+"д. "+(x-Math.round(x/86400)*86400)/3600+"ч. "+Math.round((x-Math.round(x/3600)*3600)/60)+"м. "+(x-Math.round(x/60)*60)+"с. ";
	    }
		return s;
	}

	public  synchronized static FileConfiguration getcon()
	{
		File file = new File("plugins/config.yml");
		return YamlConfiguration.loadConfiguration(file);
	}
}

