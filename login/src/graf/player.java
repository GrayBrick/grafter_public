package graf;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import graf.graf;

public class player {
	
	public String p;
	public Player pl;
	
	public player(Player p)
	{
		this.p = p.getName();
		this.pl = p;
	}
	public player(String p)
	{
		this.p = p;
		this.pl = Bukkit.getPlayer(p);
	}
	public player(String ip, int i)
	{
		for (String name : graf.Players.getKeys(false))
		{
			if (ip.equals(graf.Players.getString(name + ".ip")))
			{
				this.p = name;
				this.pl = Bukkit.getPlayer(p);
				return ;
			}
		}
	}
	
	// metods start
	
	public String getpas() {
		return graf.Players.getString(p + ".password");
	}
	public void setpas(String arg1) {
		graf.Players.set(p + ".password", arg1);
	}
	public int gettry() {
		return graf.Players.getInt(p + ".try");
	}
	public void settry(int arg1) {
		graf.Players.set(p + ".try", arg1);
	}
	public int gettryb() {
		return graf.Players.getInt(p + ".tryb");
	}
	public void settryb(int arg1) {
		graf.Players.set(p + ".tryb", arg1);
	}
	public long getTryTime()
	{
		return graf.Players.getLong(p + ".trytime");
	}
	public String getip() {
		return graf.Players.getString(p + ".ip");
	}
	public void setip(String arg1) {
		graf.Players.set(p + ".ip", arg1);
	}
	public void setTryTime(long id)
	{
		graf.Players.set(p + ".trytime", id);
	}
	public void sendMessage(String s)
	{
		pl.sendMessage(s);
	}
	
	// metods end
	
	public  synchronized static FileConfiguration getconPlayer()
	{
		File file = new File("plugins/Players.yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static void saveconPlayer()
	{
		File file = new File("plugins/Players.yml");
		try {
			graf.Players.save(file);
		} catch (Exception e) {}
	}
}
