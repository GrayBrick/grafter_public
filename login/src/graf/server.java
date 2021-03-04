package graf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class server {

	public ServerSocket	ss;
	int					port;
	
	public server(int port)
	{
		this.port = port;
	}
	
	public void startServer() throws Exception
	{
		ss = new ServerSocket(port);    
	    
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "The server is running on port: " + ChatColor.AQUA + port);
	    
	    while (true)
	    {
	    	Socket cs = ss.accept();
	    	
	    	if (!graf.allowedIp.contains(cs.getInetAddress().toString()))
	    	{
	    		cs.close();
	    		break ;
	    	}
		    
	    	BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
	    	OutputStreamWriter out = new OutputStreamWriter(cs.getOutputStream());
	    	
		    String []str = in.readLine().split("@");
		    
		    System.out.println(str[0]);
		    
		    if (str[0].equals("getBan"))
		    {	    	
		    	player p = new player(str[1], 0);
				if (p.getTryTime() > new Date().getTime())
				{
					out.write("Подожди еще " + graf.time(p.getTryTime() - new Date().getTime()) + " прежде чем ввести пароль еще раз\n");
				}
				else
				{
					out.write("null\n");
				}
		    } else 
		    if (str[0].equals("getIp"))
		    {
		    	player p = new player(str[1]);
		    	out.write(p.getip());
		    }
		    
		    out.flush();
		    
		    out.close();		    
		    in.close();
		    
		    cs.close();
		    System.out.println("close connect");
	    }
	}
	
	public static void putIp(ArrayList<String> allowedIp)
	{
		allowedIp.add("/127.0.0.1");
		allowedIp.add("/192.168.1.10");
		allowedIp.add("/192.168.1.9");
	}
}
