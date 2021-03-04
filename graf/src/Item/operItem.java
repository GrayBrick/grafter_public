package Item;

import org.bukkit.Material;

public class operItem
{
	public static int getId(Material m)
	{
		int	x;
		
		x = -1;
		while (++x < Material.values().length)
			if (Material.values()[x].equals(m))
				return (x);
		return (0);
	}
}
