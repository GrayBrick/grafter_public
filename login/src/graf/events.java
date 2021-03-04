package graf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import connection.login;;

public class events implements Listener
{
	@EventHandler
	public void join(PlayerJoinEvent e)
	{
		login.login(e);
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e)
	{
		login.move(e);
	}
}
