package Listeners;

import clans.Clan;
import clans.GUIKazna;
import commands.message;
import commands.rg_commands;
import commands.set;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.TabCompleteEvent;
import own.player;

import own.chat;
import text_processing.rgb;
import text_processing.time;

import java.util.Date;
import java.util.Objects;

public class ChatEvent implements Listener
{
    @EventHandler
    public void Chat(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);
        player p = player.getPlayer(e.getPlayer());
        if (p.prison)
        {
            sendM(p, "!Я читер с впн");
            return ;
        }
        if (p.clan_gui != null && p.clan_gui.inter)
        {
            clans.GUI.interName(p, e.getMessage());
            return ;
        }
        if (p.invite_in_clan)
        {
            clans.GUI.invitePlayer(p, e.getMessage());
            return ;
        }
        if (p.kazna_take || p.kazna_give)
        {
            GUIKazna.interSkits(p, e.getMessage());
            return ;
        }
        if (e.getMessage().equalsIgnoreCase("отмена") && p.queueArena)
        {
            p.queueArena = false;
            return ;
        }
        sendM(p, e.getMessage());
    }

    public static void sendM(player player, String em)
    {
        if (player.mute_time > new Date().getTime())
        {
            player.ErrorMessage("Вам запрещено говорить еще " + time.getTime(player.mute_time, new Date().getTime()));
            return ;
        }
        if (em.getBytes()[0] == 33 && em.length() != 1 && em.getBytes()[1] != 33)
        {
            StringBuilder mes;
            char[] m = em.toCharArray();
            int	x = 0;
            mes = new StringBuilder();
            for (char ch : m)
                if (x++ != 0)
                    mes.append(ch);
            for (Player pl : Bukkit.getOnlinePlayers())
                messageg(player, pl, mes.toString(), false);
            return ;
        }
        if (em.getBytes()[0] == 33 && em.length() > 2)
        {
            Clan clan = player.getClan();
            if (clan == null)
            {
                player.ErrorMessage("#!!# используется для написания сообщения в #чат фракции#, вы не состоите в #фракции");
                return ;
            }
            StringBuilder mes;
            char[] m = em.toCharArray();
            int	x = 0;
            mes = new StringBuilder();
            for (char ch : m)
                if (x++ > 1)
                    mes.append(ch);
            for (player pl : clan.getAll())
                pl.pl.spigot().sendMessage(message_clan(player, pl.pl, chat.MessageColor[1], mes.toString()).create());
            return ;
        }
        Location loc1  = player.pl.getLocation();
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            Location loc  = pl.getLocation();
            if (Objects.equals(loc.getWorld(), loc1.getWorld()) && loc1.distance(loc) < 200)
                messagel(player, pl, em);
        }
    }

    private static ComponentBuilder message_clan(player p, Player pl, ChatColor color1, String em) {

        ComponentBuilder mess =
                message.NamePrep(
                        p,
                        false, true)
                        .append(
                                new ComponentBuilder(" : ")
                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(time.getNowTime()).create()))
                                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null))
                                        .color(chat.color[1]).create());

        mess.append(rgb.sendMessageColor(color1, em, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create()), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).create());

        return mess;
    }

    public static void messageg(player p, Player pl, String em, boolean show)
    {
        ComponentBuilder m1 = null;
        if (p.pl.getWorld().getName().equals("world"))
            m1 = mess(p, pl, chat.MessageColor[0], em, show, true);
        if (p.pl.getWorld().getName().equals("world_nether"))
            m1 = mess(p, pl, chat.MessageColor[2], em, show, true);
        if (p.pl.getWorld().getName().equals("world_the_end"))
            m1 = mess(p, pl, chat.MessageColor[4], em, show, true);
        pl.spigot().sendMessage(m1.create());
    }

    private static ComponentBuilder mess(player p, Player pl, ChatColor color1, String em, boolean show, boolean global) {

        String[] list_em = em.split("");
        ComponentBuilder mess =
                message.NamePrep(
                p,
                global, false)
                .append(
                new ComponentBuilder(" : ")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(time.getNowTime()).create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null))
                        .color(chat.color[1]).create());
        if (!show && list_em.length > 30)
        {
            String em_copy = em;

            StringBuilder emBuilder = new StringBuilder();
            for (int i = 0; i < 30; i++)
                emBuilder.append(list_em[i]);
            em = emBuilder.toString();
            mess.append(
                    new ComponentBuilder(em).color(color1)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(em_copy)
                                    .create())
            ).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/sendfromto " + p.p + " " + pl.getName() + " " + em_copy)
            ).create())
                    .append(rgb.sendMessageGradient(chat.color[0], chat.color[1], "⋮⋮⋮",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(em_copy).create()),
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/sendfromto " + p.p + " " + pl.getName() + " " + em_copy))
                            .create()
            );
        }
        else
        {
            mess.append(rgb.sendMessageColor(color1, em, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create()), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).create());
            if (show && color1.equals(chat.MessageColor[0]))
                mess.append(rgb.sendMessageGradient(chat.color[1], chat.color[0], "⋮⋮⋮", null, null)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null))
                        .create()
                ).create();
        }
        return mess;
    }

    public static void messagel(player p, Player pl, String em)
    {
        ComponentBuilder m1 = null;
        if (p.pl.getWorld().getName().equals("world"))
            m1 = mess(p, pl, chat.MessageColor[1], em, true, false);
        if (p.pl.getWorld().getName().equals("world_nether"))
            m1 = mess(p, pl, chat.MessageColor[3], em, true, false);
        if (p.pl.getWorld().getName().equals("world_the_end"))
            m1 = mess(p, pl, chat.MessageColor[5], em, true, false);
        pl.spigot().sendMessage(m1.create());
    }

    public static String shiftList(String []args, int shift)
    {
        StringBuilder ret = new StringBuilder();
        for (int i = shift; i < args.length; i++)
            ret.append(args[i]).append(
                    i != args.length - 1 ? " " : ""
            );
        return ret.toString();
    }

    @EventHandler
    public boolean TabCompl(TabCompleteEvent e) {
        player p = player.getPlayer((Player) e.getSender());

        if (e.getBuffer().split(" ")[0].equalsIgnoreCase("/rg"))
            return rg_commands.rg_tab(e, 1);
        if (e.getBuffer().split(" ")[0].equalsIgnoreCase("/set") && p.permision == 10)
            return set.set_tab(e);

        return true;
    }
}
