package graf;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.scheduler.BungeeScheduler;
import net.md_5.bungee.scheduler.BungeeTask;
import text_processing.rgb;

public class graf extends Plugin implements Listener {

	server serv = new server(25000);
	static ArrayList<String>	allowedIp = new ArrayList<String>();
	Thread r;
	
	@Override
    public void onEnable() {
		
		server.putIp(allowedIp);
		r = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					serv.startServer(getProxy());
				} catch (Exception ex) {}
			}
		});
		r.start();	
		getProxy().getPluginManager().registerListener(this, this);
		System.out.println(ChatColor.AQUA + "Start plugin.");
    }

    @Override
    public void onDisable() {
    	
    }

	@EventHandler
	public void shake(ProxyPingEvent e)
	{
		String str = "";

		String []args = new String[]{"8", "▄▀", "ch", "1", "█▀▄", "1",  "▄▀▄",  "1",  "█▀", "1", "▀█▀", "1", "██▀", "1", "█▀▄", "\\n", "8", "▀▄█", "1", "█▀▄", "1", "█▀█", "1", "█▀", "1", "ch", "█", "ch", "1", "█▄▄", "1", "█▀▄"};

		for (String il : args)
		{
			try
			{
				for (int i = 0; i < Integer.parseInt(il); i++)
					str += " ";
			} catch(Exception ex)
			{
				if (il.equalsIgnoreCase("ch"))
					str += "{";//ChatColor.of("#000000") + "█" + ChatColor.RESET + "";
				else
				if (il.equalsIgnoreCase("\\n"))
					str += "\n";
				else
					str += il;
			}
		}

		ChatColor col0 = rgb.randomColor();
		ChatColor col1 = rgb.randomColor();

		e.getResponse().setDescription(rgb.gradient(col0.getName(), col1.getName(), str.split("\n")[0]) + "\n" +
				rgb.gradient(col0.getName(), col1.getName(), str.split("\n")[1]));

		System.out.println(rgb.gradient(col0.getName(), col1.getName(), str.split("\n")[0]) + "\n" +
				rgb.gradient(col0.getName(), col1.getName(), str.split("\n")[1]));

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
				}
		};

		int index = (int)(Math.random() * 3);

		for (int i = 0; i < plato[index].length; i++)
		{
			String buffer = "";

			for (String p : plato[index][i].split(""))
			{
				if (p.equals(" "))
					buffer += ChatColor.BLACK + "█";
				else if (!p.equals("█") && index == 0)
					buffer += ChatColor.YELLOW + p;
				else
					buffer += ChatColor.GOLD + p;
			}

			plato[index][i] = buffer;
		}

		ServerPing.PlayerInfo []plinfo = new ServerPing.PlayerInfo[plato[index].length];

		for (int i = 0; i < plato[index].length; i++)
				plinfo[i] = new ServerPing.PlayerInfo(plato[index][i], (String) null);

		ServerPing.Players pl = new ServerPing.Players(e.getResponse().getPlayers().getOnline() + 50, e.getResponse().getPlayers().getOnline(), plinfo);

		e.getResponse().setPlayers(pl);

		e.getResponse().getVersion().setName(ChatColor.AQUA + "Только 1.16.2");
	}
//
//    @EventHandler
//    public void join(PreLoginEvent e)
//	{
//		String ip = e.getConnection().getVirtualHost().getHostString();
//		System.out.println(ChatColor.AQUA + "UP: " + ip);
//		String message;
//		try {
//			message = client.sendMes("192.168.1.9", 25001, "getBan@" + ip);
//		} catch (Exception ex) {
//			message = "null";
//		}
//		if (message != null && !message.equals("null"))
//			e.getConnection().disconnect(message);
//	}
}
    
//    @EventHandler
//    public void join(PostLoginEvent e)
//    {
//    	ProxiedPlayer p = e.getPlayer();
//    	String message;
//    	try
//    	{
//    		message = client.sendMes("127.0.0.1", 25001, "getIp@" + p.getName());
//    	}catch(Exception ex) {
//    		message = "null";
//    	}
//    	if (message != null && !message.equals("null") && p.getAddress().getHostString().equals(message + " 1"))  		
//    	{
//    		System.out.println(p.getName() + " to sb");
//    		if (p.getServer() != null && !p.getServer().getInfo().getName().equals("sb0"))
//    			p.connect(getProxy().getServerInfo("sb0"));
//    	}
//    	else
//    	{
//    		System.out.println(p.getName() + " to login");
//    		p.connect(getProxy().getServerInfo("login"));
//    	}  		
//    }
//}

