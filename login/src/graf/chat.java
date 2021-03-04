package graf;

import net.md_5.bungee.api.ChatColor;
import text_processing.rgb;

public class chat {

	public static ChatColor []MessageColor = new ChatColor[]{
			ChatColor.of("#818181"),
			ChatColor.of("#FFFFFF"),
	};

	public static ChatColor []title = new ChatColor[]{
			ChatColor.of("#ffcb00"),
			ChatColor.of("#fff500"),
			ChatColor.of("#640cab"),
			ChatColor.of("#2e16b1")
	};

	public static ChatColor[] color = {
			title[0],
			title[2],
			title[1],
			title[3]};

	public static ChatColor []title_inv = new ChatColor[]{
			title[0],
			title[2],
			title[1],
			title[3]
	};


	public static String strCommand(String comment, String command, String[] args)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(rgb.gradientLight(color[0].getName(), comment)).append("\n    ").append(rgb.gradientLight(color[1].getName(), command));
		if (args != null)
			for (String str : args)
				ret.append(" ").append(color[2]).append("<").append(rgb.gradientLight(color[1].getName(), str)).append(color[2]).append(">");
		return ret.toString();
	}

	public static int getCountChar(String str, char ch)
	{
		int	count = 0;

		for (char litter : str.toCharArray())
		{
			if (litter == ch)
				count++;
		}
		return count;
	}
}
