package text_processing;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;

import java.util.Date;
import java.util.Objects;

public class rgb
{
	public static ChatColor[] rand_colors = new ChatColor[]{
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),
			getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor(),getRandColor()
	};

	public static int getRgbInt(int i)
	{
		if (i > 255)
			return 255;
		return Math.max(i, 0);
	}

	public static String get_color(int r, int g, int b)
	{
		r = getRgbInt(r);
		g = getRgbInt(g);
		b = getRgbInt(b);

		return "#" + (Integer.toHexString(r).length() == 1 ? "0" + Integer.toHexString(r) : Integer.toHexString(r))
				   + (Integer.toHexString(g).length() == 1 ? "0" + Integer.toHexString(g) : Integer.toHexString(g))
		           + (Integer.toHexString(b).length() == 1 ? "0" + Integer.toHexString(b) : Integer.toHexString(b));
	}

	public static ChatColor get_Color(int r, int g, int b)
	{
		r = getRgbInt(r);
		g = getRgbInt(g);
		b = getRgbInt(b);

		return ChatColor.of("#" + (Integer.toHexString(r).length() == 1 ? "0" + Integer.toHexString(r) : Integer.toHexString(r))
				+ (Integer.toHexString(g).length() == 1 ? "0" + Integer.toHexString(g) : Integer.toHexString(g))
				+ (Integer.toHexString(b).length() == 1 ? "0" + Integer.toHexString(b) : Integer.toHexString(b)));
	}

	public static int[] get_rgb(String hex) //обязательно с #
	{
		String[] str = hex.split("");
		return new int[]{
				Integer.parseInt(str[1] + str[2], 16),
				Integer.parseInt(str[3] + str[4], 16),
				Integer.parseInt(str[5] + str[6], 16)
		};
	}

	public static String gradient(int[] color0, int[] color1, String text)
	{
		if (text.equalsIgnoreCase("test"))
		{
			//text = "|".repeat(2080);
		}

		int length_text = text.split("").length;

		StringBuilder text_ret = new StringBuilder();

		int count = 0;

		for (String part : text.split(""))
		{
			count++;
			int i = 0;

			int []col = new int[3];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i++];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i++];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i];

			if ((count - 1) % 160 == 0 && count > 10)
				text_ret.append("\n");
			text_ret.append(ChatColor.of(get_color(col[0], col[1], col[2]))).append(part);
		}

		return text_ret.toString();
	}

	public static String gradient(String color_0, String color_1, String text)
	{
//		if (text.equalsIgnoreCase("test"))
//		{
//			text = "";
//			for (int i = 0; i < 2080; i++)
//				text += "|";
//		}

		int[] color0 = get_rgb(color_0);
		int[] color1 = get_rgb(color_1);

		int length_text = text.split("").length;

		StringBuilder text_ret = new StringBuilder();

		int count = 0;

		for (String part : text.split(""))
		{
			count++;
			int i = 0;

			int []col = new int[3];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i++];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i++];
			col[i] = (int)(((double) (color1[i] - color0[i]) / length_text) * count) + color0[i];

			if ((count - 1) % 160 == 0 && count > 10)
				text_ret.append("\n");
			text_ret.append(ChatColor.of(get_color(col[0], col[1], col[2]))).append(part);
		}

		return text_ret.toString();
	}

	public static String gradientLight(String color_0, String text)
	{
		int[] color0 = get_rgb(color_0);

		int length_text = text.split("").length;

		StringBuilder text_ret = new StringBuilder();

		int count = 0;

		double plus = 0;

		for (String part : text.split(""))
		{
			count++;
			int i = 0;

			plus += (150.0 / length_text);

			int []col = new int[3];
			int interval = 24;

			int rand_1 = (int) (Math.random() * interval) - interval / 2;
			int rand_2 = (int) (Math.random() * interval) - interval / 2;
			int rand_3 = (int) (Math.random() * interval) - interval / 2;

			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i] + rand_1);

			//Bukkit.getPlayer("_GreyBrick_").sendMessage(col[0] + " ");

			if ((count - 1) % 160 == 0 && count > 10)
				text_ret.append("\n");
			text_ret.append(ChatColor.of(get_color(col[0], col[1], col[2]))).append(part);
		}

		return text_ret.toString();
	}

	public static String gradientLight(ChatColor color_0, String text)
	{
		int[] color0 = get_rgb(color_0.getName());

		int length_text = text.split("").length;

		StringBuilder text_ret = new StringBuilder();

		int count = 0;

		double plus = 0;

		boolean light = false;

		for (String part : text.split(""))
		{
			count++;
			int i = 0;

			plus += (150.0 / length_text);

			int []col = new int[3];

			int interval = 30;

			if (part.equals("#") && light)
			{
				light = false;
				continue;
			}

			if (part.equals("#") && !light)
			{
				light = true;
				continue;
			}

			if (light)
			{
				text_ret.append(rgb.getGradientInterval(color_0, color_0.getName().equalsIgnoreCase(chat.color[0].getName()) ? chat.color[1] : chat.color[0] , 0.8)).append(ChatColor.BOLD).append(part);
				continue ;
			}


			int rand_1 = (int) (Math.random() * interval) - interval / 2;
			int rand_2 = (int) (Math.random() * interval) - interval / 2;
			int rand_3 = (int) (Math.random() * interval) - interval / 2;

			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i] + rand_1);

			if ((count - 1) % 160 == 0 && count > 10)
				text_ret.append("\n");
			text_ret.append(ChatColor.RESET).append(ChatColor.of(get_color(col[0], col[1], col[2]))).append(part);
		}

		return text_ret.toString();
	}

	public static String gradientLightInt(ChatColor color_0, String text)
	{
		int[] color0 = get_rgb(color_0.getName());

		int length_text = text.split("").length;

		StringBuilder text_ret = new StringBuilder();

		int count = 0;

		double plus = 0;

		for (String part : text.split(""))
		{
			if (Character.isDigit(part.codePointAt(0)))
			{
				text_ret.append(rgb.getGradientInterval(color_0, ChatColor.of("#FFFFFF"), 0.8)).append(part);
				continue ;
			}
			count++;
			int i = 0;

			plus += (150.0 / length_text);

			int []col = new int[3];
			int interval = 24;

			int rand_1 = (int) (Math.random() * interval) - interval / 2;
			int rand_2 = (int) (Math.random() * interval) - interval / 2;
			int rand_3 = (int) (Math.random() * interval) - interval / 2;

			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i++] + rand_1);
			col[i] = (int)(color0[i] + rand_1);

			if ((count - 1) % 160 == 0 && count > 10)
				text_ret.append("\n");
			text_ret.append(ChatColor.of(get_color(col[0], col[1], col[2]))).append(part);
		}

		return text_ret.toString();
	}

	public static ChatColor getGradientInterval(ChatColor color0, ChatColor color1, double procent)
	{
		int i = 0;

		int[] col0 = get_rgb(color0.getName());
		int[] col1 = get_rgb(color1.getName());

		int []ret = new int[]{
				(int)((double) (col1[i] - col0[i]) * procent) + col0[i++],
				(int)((double) (col1[i] - col0[i]) * procent) + col0[i++],
				(int)((double) (col1[i] - col0[i]) * procent) + col0[i],
		};
//
//		int []ret = new int[]{
//				col1[i] < col0[i] ? (int)((double) (col1[i] - col0[i]) * procent) + col0[i++] : (int)((double) (col0[i] - col1[i]) * procent) + col1[i++],
//				col1[i] < col0[i] ? (int)((double) (col1[i] - col0[i]) * procent) + col0[i++] : (int)((double) (col0[i] - col1[i]) * procent) + col1[i++],
//				col1[i] < col0[i] ? (int)((double) (col1[i] - col0[i]) * procent) + col0[i++] : (int)((double) (col0[i] - col1[i]) * procent) + col1[i++],
//		};

		//Bukkit.getPlayer("_GreyBrick_").sendMessage(color0 + "[]" + " " + color1 + "[]" + ChatColor.of(get_color(ret[0], ret[1], ret[2])) + procent);

		return ChatColor.of(get_color(ret[0], ret[1], ret[2]));
	}

	public static int getGradientInterval(int color0, int color1, int procent)
	{
		return (int)(((double) (color1 - color0) / 100.0) * procent) + color0;
	}

	public static void gradientTitle(Player p, String []text, ChatColor []colors, double speed, int time, double interval_a,double interval_b)
	{
		long			mark0 = new Date().getTime();
		final double	[]count = new double[]{0, 0};

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int interval = text[0].split("").length * 10 / colors.length;

				p.sendTitle(gradientShift(text[0], colors, (int)(interval * interval_a), (int)count[0]), gradientShift(text[1].substring(0, Math.min(text[1].length(), (int) count[1])), colors, (int)(interval * interval_b), (int)count[0]), 0, 10, 30);

				count[0] += speed;
				count[1] += 0.3;

				if (new Date().getTime() - mark0 >= time)
					cancel();
			}
		}.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 1);
	}

	public static String gradientShift(String text, ChatColor []colors, int interval, int shift)
	{
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < text.split("").length; i++)
		{
			ret.append(ChatColor.of(get_color(

					get255int(colors, interval, shift, i, 1, 2)

					,
					get255int(colors, interval, shift, i, 3, 4)

					,
					get255int(colors, interval, shift, i, 5, 6)))).append(text.split("")[i]);
		}

		return ret.toString();
	}

	public static String gradientShiftRand(String text, int interval, int shift)
	{
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < text.split("").length; i++)
		{
			ret.append(ChatColor.of(get_color(

					get255int(rgb.rand_colors, interval, shift, i, 1, 2)

					,
					get255int(rgb.rand_colors, interval, shift, i, 3, 4)

					,
					get255int(rgb.rand_colors, interval, shift, i, 5, 6)))).append(text.split("")[i]);
		}

		return ret.toString();
	}

	public static int get255int(ChatColor []colors, int interval, int shift, int i, int a, int b)
	{
		int col0 = Integer.parseInt(colors[getColorShift(colors, interval, shift + i, 0)].getName().split("")[a] + colors[getColorShift(colors, interval, shift + i, 0)].getName().split("")[b], 16);
		int col1 = Integer.parseInt(colors[getColorShift(colors, interval, shift + i, 1)].getName().split("")[a] + colors[getColorShift(colors, interval, shift + i, 1)].getName().split("")[b], 16);


		return getGradientInterval(col0, col1, (int)(100 * ((((double)(shift + i) % interval) / interval))));
	}

	public static int getColorShift(ChatColor []colors, int interval, int shift, int color_id)
	{
		int col_0 = (shift / interval) % colors.length;
		if (color_id == 0)
			return col_0;
		else
			return col_0 == colors.length - 1 ? 0 : col_0 + 1;
	}

	public static ComponentBuilder sendMessageGradient(ChatColor color0, ChatColor color1, String text, HoverEvent hover, ClickEvent click)
	{
		ComponentBuilder mess = new ComponentBuilder("");

		int count = 0;

		for (String litter : text.split(""))
		{
			if (litter.equals("\n"))
				count = 0;
			mess.append(new ComponentBuilder(litter).color(getGradientInterval(color0, color1, (double)count++/text.split("").length)).event(hover).event(click).create());
		}
		return mess;
	}

	public static ComponentBuilder sendMessageColor(ChatColor color0, String text, HoverEvent hover, ClickEvent click)
	{
		ComponentBuilder mess = new ComponentBuilder("");
		StringBuilder add_str = new StringBuilder();
		String[] str = text.split("");
		int i = 0;

		boolean color = false;

		int[] color_int = get_rgb(color0.getName());

		for (int m : color_int)
			i += m;
		ChatColor color2 = null;

		if ((i / 3) > (255 * 0.75))
			color2 = rgb.getGradientInterval(color0, ChatColor.of("#000000"), 0.30);
		else
			color2 = rgb.getGradientInterval(color0, ChatColor.of("#FFFFFF"), 0.30);

		i = 0;

		while (i < str.length)
		{
			if (!color)
			{
				if (!str[i].equals("#"))
					add_str.append(str[i]);
				else
				{
					mess.append(new ComponentBuilder(add_str.toString()).color(color0).create());
					add_str = new StringBuilder();
					color = true;
				}
			}
			else
			{
				if (!str[i].equals("#"))
					add_str.append(str[i]);
				else
				{

					mess.append(new ComponentBuilder(add_str.toString()).color(color2).create());
					add_str = new StringBuilder();
					color = false;
				}
			}
			i++;
		}
		if (color)
			mess.append(new ComponentBuilder(add_str.toString()).color(color2).create());
		else
			mess.append(new ComponentBuilder(add_str.toString()).color(color0).create());

		mess.event(hover).event(click);

		return mess;
	}

	public static ChatColor getRandColor()
	{
		return ChatColor.of(get_color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)));
	}
}
