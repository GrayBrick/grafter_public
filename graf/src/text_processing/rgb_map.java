package text_processing;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class rgb_map
{
    public static TreeMap<Byte, ChatColor> byte_to_rgb = new TreeMap<>();
    public static ArrayList<Byte>   sort_byte = new ArrayList<>();

    public static ChatColor getTon(ChatColor color, int i)
    {
        if (i == 1)
        {
            return rgb.get_Color(
                    (int) (Integer.parseInt(color.getName().split("")[1] + color.getName().split("")[2], 16) * 1.22),
                    (int) (Integer.parseInt(color.getName().split("")[3] + color.getName().split("")[4], 16) * 1.22),
                    (int) (Integer.parseInt(color.getName().split("")[5] + color.getName().split("")[6], 16) * 1.22));
        }
        if (i == 2)
        {
            return rgb.get_Color(
                    (int) (Integer.parseInt(color.getName().split("")[1] + color.getName().split("")[2], 16) * 1.22 * 1.16),
                    (int) (Integer.parseInt(color.getName().split("")[3] + color.getName().split("")[4], 16) * 1.22 * 1.16),
                    (int) (Integer.parseInt(color.getName().split("")[5] + color.getName().split("")[6], 16) * 1.22 * 1.16));
        }
        if (i == 3)
        {
            return rgb.get_Color(
                    (int) (Integer.parseInt(color.getName().split("")[1] + color.getName().split("")[2], 16) * 1.22 * 1.16 * 0.52),
                    (int) (Integer.parseInt(color.getName().split("")[3] + color.getName().split("")[4], 16) * 1.22 * 1.16 * 0.52),
                    (int) (Integer.parseInt(color.getName().split("")[5] + color.getName().split("")[6], 16) * 1.22 * 1.16 * 0.52));
        }
        return null;
    }

    public static void fill()
    {
        byte i = 4;
        byte_to_rgb.put(i++, ChatColor.of("#587c27"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#aca272"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#8a8a8a"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#b20000"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#6f6fb2"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#747474"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#005600"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#b2b2b2"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#72757f"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#694b35"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#4e4e4e"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#2c2cb2"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#635331"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#b2afaa"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#965824"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#7c3496"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#476b96"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#9f9f24"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#588e11"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#a85873"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#343434"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#6b6b6b"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#34586b"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#582b7c"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#24347c"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#473424"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#475824"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#6b2424"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#111111"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#aea635"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#3f9894"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        i = -128;
        byte_to_rgb.put(i++, ChatColor.of("#3359b2"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#009727"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#5a3b22"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#4e0100"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#917b70"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#6f3819"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#683c4b"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#4e4b60"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#815c19"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#475125"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#6f3536"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#271c18"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#5e4a44"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#3c3f3f"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#55323d"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#342a3f"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#342318"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#34381d"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#632920"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#1a0f0b"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#832122"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#672b43"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#3f1114"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#0f575d"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#276361"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#3b1f2a"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        byte_to_rgb.put(i++, ChatColor.of("#0e7d5c"));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 2)), 1));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 3)), 2));
        byte_to_rgb.put(i++, getTon(byte_to_rgb.get((byte) (i - 4)), 3));

        int m = 0;
        for (byte b : byte_to_rgb.keySet())
        {
            if (m++ % 4 == 0)
                sort_byte.add(b);
        }


        sort_byte.sort((a, b) -> {


            int[] col_a = rgb.get_rgb(byte_to_rgb.get(a).getName());
            int[] col_b = rgb.get_rgb(byte_to_rgb.get(b).getName());

            float hsv_a[] = new float[3];
            Color.RGBtoHSB(col_a[0], col_a[1], col_a[2], hsv_a);

            float hsv_b[] = new float[3];
            Color.RGBtoHSB(col_b[0], col_b[1], col_b[2], hsv_b);

            return (int) (hsv_a[0] * 100.0 + hsv_a[2] * 5.0 - hsv_b[0] * 100.0 - hsv_b[2] * 5.0);
        });
    }
}
