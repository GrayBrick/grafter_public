package text_processing;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import own.chat;
import own.graf;

import java.io.File;
import java.util.ArrayList;

public class text
{
    public static void info_message(Player sender, String[] main, String[] post) {

        for (int i = 0; i < main.length; i++)
        {
            ComponentBuilder m1 = new ComponentBuilder()
                    .append(rgb.sendMessageColor(chat.color[0],
                    main[i],
                    new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(post[i]).color(chat.color[1]).create()),
                    null
                    ).create())
            .append(add_open(sender));
            sender.spigot().sendMessage(m1.create());
        }
    }

    public static BaseComponent[] add_open(Player p)
    {
        return rgb.sendMessageGradient(chat.color[0], chat.color[1], "⋮⋮⋮",
            null,
            null)
            .create();
    }

    public static BaseComponent[] add_String(String main, String post)
    {
        return new
                ComponentBuilder(main)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(post).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, null))
                .color(chat.color[1]).create();
    }

    public static BaseComponent[] add_String(ComponentBuilder main, String post)
    {
        return new
                ComponentBuilder(main)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(post).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, null))
                .color(chat.color[1]).create();
    }

    public static BaseComponent[] add_String(String main, ComponentBuilder post)
    {
        return new
                ComponentBuilder(main)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(post).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, null))
                .color(chat.color[1]).create();
    }

    public static BaseComponent[] add_String(ComponentBuilder main, ComponentBuilder post)
    {
        return new
                ComponentBuilder(main)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(post).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, null))
                .color(chat.color[1]).create();
    }
}
