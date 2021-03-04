package graf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class server {

	ServerSocket	ss;
	int				port;
	
	public server(int port)
	{
		this.port = port;
	}
	
	public void startServer(ProxyServer server) throws Exception
	{
		ss = new ServerSocket(port);    
	    
	    System.out.println(ChatColor.DARK_PURPLE + "The server is running on port: " + ChatColor.AQUA + port);
	    
	    while (true)
	    {
	    	Socket cs = ss.accept();

	    	if (!(graf.allowedIp.contains(cs.getInetAddress().toString())))
	    	{
	    		cs.close();
	    		break ;
	    	}

	    	BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
	    	OutputStreamWriter out = new OutputStreamWriter(cs.getOutputStream());
	    	
		    String []str = in.readLine().split("@");
		    
		    if (str[0].equals("goto"))
		    {
		    	System.out.println(str[0] + " " + str[1] + " " + str[2]);
		    	server.getPlayer(str[2]).connect(server.getServerInfo(str[1]));
		    }
		    	
		    out.write("exit");
		    out.flush();
		    
		    out.close();		    
		    in.close();
		    
		    cs.close();
	    }
	}
	
	public static void putIp(ArrayList<String> allowedIp)
	{
		allowedIp.add("/127.0.0.1");
		allowedIp.add("/192.168.1.9");
		allowedIp.add("/192.168.1.10");
	}
}
