package own;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class chat {

	public static ChatColor []MessageColor = new ChatColor[]{
			ChatColor.of("#818181"),
			ChatColor.of("#FFFFFF"),
			ChatColor.of("#834444"),
			ChatColor.of("#FF4949"),
			ChatColor.of("#7B4483"),
			ChatColor.of("#C900E8"),
	};

	public static ChatColor []title = new ChatColor[]{
			ChatColor.of("#ffffff"),
			ChatColor.of("#ff0000"),
			ChatColor.of("#fd5309"),
			ChatColor.of("#26FFF7")
	};

	public static ChatColor[] color = {
			ChatColor.of("#ffffff"),
			ChatColor.of("#ff0000"),
			ChatColor.of("#fd5309"),
			ChatColor.of("#26FFF7")};

	public static ChatColor []title_inv = new ChatColor[]{
			title[0],
			title[2],
			title[1],
			title[3]
	};

	public static String square = rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), "⋮⋮⋮");

	public static ComponentBuilder squareC = rgb.sendMessageGradient(chat.color[0], chat.color[1], "⋮⋮⋮", null, null);

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

	public static String getNormForm(int i, String очко, String очка, String очков) // очко очка очков 1 2 5
	{
		int norm_i = i % 100;

		if (norm_i > 10 && norm_i < 20)
			return очков;
		if (norm_i % 10 == 1)
			return очко;
		if (norm_i % 10 == 2 || norm_i % 10 == 3 || norm_i % 10 == 4)
			return очка;

		return очков;
	}

	public static String getNormFormSkit(int i) // очко очка очков 1 2 5
	{
		int norm_i = i % 100;

		if (norm_i > 10 && norm_i < 20)
			return "скитов";
		if (norm_i % 10 == 1)
			return "скит";
		if (norm_i % 10 == 2 || norm_i % 10 == 3 || norm_i % 10 == 4)
			return "скита";

		return "скитов";
	}

	public static String int_s(int i)
	{
		String []str = Integer.toString(i).split("");
		Collections.reverse(Arrays.asList(str));
		ArrayList<String> ret = new ArrayList<>();
		for (int x = 0; x < str.length; x++)
		{
			ret.add(str[x]);
			if (x % 3 == 2 && x < (str.length - 1))
				ret.add(".");
		}
		Collections.reverse(ret);
		String strin = "";
		for (String a : ret)
		{
			strin += a;
		}
		return strin;
	}

	public static boolean mute(player p, String[] args)
	{
		player pl = player.getPlayer(args[0]);
		pl.mute_time = new Date().getTime() + Integer.parseInt(args[1]) * 1000 * 60;
		return true;
	}
}
