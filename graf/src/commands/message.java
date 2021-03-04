package commands;

import clans.Clan;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import own.chat;
import own.player;
import text_processing.rgb;
import text_processing.time;

import java.util.Date;

public class message
{
    public static boolean mmes(player p, String[] args)
    {
        if (p.mute_time > new Date().getTime())
        {
            p.ErrorMessage("Вам запрещено говорить еще " + time.getTime(p.mute_time, new Date().getTime()));
            return true;
        }
        if (args.length < 2)
        {
            p.sendMessageE(chat.strCommand("Используйте:", "/m", new String[]{"ник", "сообщение"}));
            return true;
        }
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (pl.getName().equals(args[0]))
            {
                StringBuilder msg = new StringBuilder();
                int		x = 0;
                while (++x < args.length)
                {
                    msg.append(args[x]);
                    msg.append(" ");
                }
                if (args[0].equals(p.p))
                    return true;
                privatmessage(p, player.getPlayer(pl), msg.toString());
                return true;
            }
        }
        p.sendMessageE(rgb.gradientLight(chat.color[0], "Игрок ") + rgb.gradientLight(chat.color[1],args[0]) + rgb.gradientLight(chat.color[0]," не в игре"));
        return true;
    }

    public static void privatmessage(player sender, player recipient, String em)
    {
        ComponentBuilder forsender = new ComponentBuilder("").append(
                new ComponentBuilder("˻")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                rgb.sendMessageColor(chat.color[0], "Это личные сообщения", null, null).create()))
                        .color(chat.color[0])
                        .append("M").color(chat.color[1])
                        .append("˺ ").color(chat.color[0])
                        .create()
        ).append(
                new ComponentBuilder("➤ ").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder().create()
                )).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(chat.color[1]).create()
        ).append(
                NamePrep(recipient, false, false).create()
        ).append(
                new ComponentBuilder(" : ").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(chat.color[1]).create()
        ).append(
                new ComponentBuilder(em).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(ChatColor.WHITE).create()
        );

        ComponentBuilder forrecipient = new ComponentBuilder("").append(
                new ComponentBuilder("˻")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                rgb.sendMessageColor(chat.color[0], "Это личные сообщения", null, null).create()))
                        .color(chat.color[0])
                        .append("M").color(chat.color[1])
                        .append("˺ ").color(chat.color[0])
                        .create()
        ).append(
                NamePrep(sender, false, false).create()
        ).append(
                new ComponentBuilder(" ➤").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder().create()
                )).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(chat.color[1]).create()
        ).append(
                new ComponentBuilder(" : ").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(chat.color[1]).create()
        ).append(
                new ComponentBuilder(em).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, null)).color(ChatColor.WHITE).create()
        );

        recipient.pl.spigot().sendMessage(forrecipient.create());
        sender.pl.spigot().sendMessage(forsender.create());
    }

    public static ComponentBuilder NamePrep(player sender, boolean global, boolean clan)
    {
        if (sender.name_col0.equalsIgnoreCase("#ffffff") && sender.name_col1.equalsIgnoreCase("#ffffff"))
        {
            ChatColor color_g = sender.pl.getWorld().getName().equals("world") ? chat.MessageColor[0] : sender.pl.getWorld().getName().equals("world_nether") ? chat.MessageColor[2] : chat.MessageColor[4];
            ChatColor color_l = sender.pl.getWorld().getName().equals("world") ? chat.MessageColor[1] : sender.pl.getWorld().getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5];

            return new ComponentBuilder(sender.rank != 0 ? Clan.chat_rank+  " " : "").color(Clan.getColorRank(sender.rank)).append(
                    clan ?
                            new ComponentBuilder("˻")
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    rgb.sendMessageColor(chat.color[0], "Это сообщения для фракции", null, null).create()))
                            .color(chat.color[0])
                            .append(sender.getClan().getName()).color(sender.getClan().getChatColor())
                            .append("˺ ").color(chat.color[0])
                            .create()
                            : new ComponentBuilder("").create()
            ).append(rgb.sendMessageGradient(
                    chat.color[0],
                    global ?
                            color_g
                            :
                            color_l
                    ,
                    sender.p,
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            rgb.sendMessageGradient(
                                    chat.color[0],
                                    global ?
                                            color_g
                                            :
                                            color_l
                                    ,
                                    "Отправить сообщение ", null, null).append(
                                    rgb.sendMessageGradient(
                                            global ?
                                                    color_g
                                                    :
                                                    color_l
                                            ,
                                            chat.color[0],
                                            sender.p, null, null).create()
                            ).create()
                    ),
                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/m " + sender.p + " ")
            ).create());
        }
        return new ComponentBuilder(sender.rank != 0 ? Clan.chat_rank +  " " : "").color(Clan.getColorRank(sender.rank)).append(
                clan ?
                        new ComponentBuilder("˻")
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        rgb.sendMessageColor(chat.color[0], "Это сообщения для фракции", null, null).create()))
                                .color(chat.color[0])
                                .append(sender.getClan().getName()).color(sender.getClan().getChatColor())
                                .append("˺ ").color(chat.color[0])
                                .create()
                        : new ComponentBuilder("").create()
        ).append(rgb.sendMessageGradient(
                ChatColor.of(sender.name_col0),
                ChatColor.of(sender.name_col1),
                sender.p,
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageGradient(
                                chat.color[0],
                                ChatColor.of(sender.name_col0),
                                "Отправить сообщение ", null, null).append(
                                rgb.sendMessageGradient(
                                        ChatColor.of(sender.name_col0),
                                        ChatColor.of(sender.name_col1),
                                        sender.p, null, null).create()
                        ).create()
                ),
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/m " + sender.p + " ")
        ).create());
    }

    public static boolean info(player p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "Наводи мышкой на интересующую информацию и ") + rgb.gradientLight(chat.color[1], "нажимай"));
            ComponentBuilder mess = new ComponentBuilder("   - "
            ).color(chat.color[1]
            ).append(
                    rgb.sendMessageColor(chat.color[0],
                            "Работа с чатом",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    rgb.sendMessageColor(chat.color[0],
                                            "Информация о чате ",
                                            null,
                                            null
                                    ).append(
                                                    rgb.sendMessageColor(chat.color[1],
                                                            " *жми*",
                                                            null,
                                                            null
                                                    ).create()
                                    ).create()
                            ),
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/i chat")
                    ).create()
            );
            p.pl.spigot().sendMessage(mess.create());

            mess = new ComponentBuilder("   - "
            ).color(chat.color[1]
            ).append(
                    rgb.sendMessageColor(chat.color[0],
                            "Приват регионов",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    rgb.sendMessageColor(chat.color[0],
                                            "Информация о работе с регионами ",
                                            null,
                                            null
                                    ).append(
                                            rgb.sendMessageColor(chat.color[1],
                                                    " *жми*",
                                                    null,
                                                    null
                                            ).create()
                                    ).create()
                            ),
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/i rg")
                    ).create()
            );
            p.pl.spigot().sendMessage(mess.create());

            mess = new ComponentBuilder("   - "
            ).color(chat.color[1]
            ).append(
                    rgb.sendMessageColor(chat.color[0],
                            "Телепортация по миру",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    rgb.sendMessageColor(chat.color[0],
                                            "Информация о перемещении в мире ",
                                            null,
                                            null
                                    ).append(
                                            rgb.sendMessageColor(chat.color[1],
                                                    " *жми*",
                                                    null,
                                                    null
                                            ).create()
                                    ).create()
                            ),
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/i tp")
                    ).create()
            );
            p.pl.spigot().sendMessage(mess.create());

            mess = new ComponentBuilder("   - "
            ).color(chat.color[1]
            ).append(
                    rgb.sendMessageColor(chat.color[0],
                            "Информация о вас",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    rgb.sendMessageColor(chat.color[0],
                                            "Ваши данные на этом сервере доступны по команде ",
                                            null,
                                            null
                                    ).append(
                                            rgb.sendMessageColor(chat.color[1],
                                                    " /me",
                                                    null,
                                                    null
                                            ).create()
                                    ).create()
                            ),
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/me")
                    ).create()
            );
            p.pl.spigot().sendMessage(mess.create());
            return true;
        }
        if (args[0].equalsIgnoreCase("chat"))
        {
            p.Message("\n\n        #-#_#-#_#Чат#_#-#_#-");
            p.Message("\n#1#. Данный квадрат #⋮⋮⋮# информирует о #кликабельности# чата");
            p.Message("#2#. " + "Используй #!# перед сообщением для оповещения #всех игроков");
            p.Message("#3#. Сообщения отправленные в #глобальный# чат#,# ограничиваются #20# символами#,# для просмотра сообщения нажмите на #квадратик#");
            p.Message("#4#. Для отправки сообщения конкретному #игроку# используйте /#m #<#ник#># сообщение# или #нажмити# на ник игрока в чате");

            return true;
        }
        if (args[0].equalsIgnoreCase("rg"))
        {
            p.Message("\n\n        #-#_#-#_#Регионы#_#-#_#-");
            p.Message("\n#1#. Для защиты территории используется команда /#rg claim#, которая #защищает# чанк");
            p.Message("#2#. #Чанк# - это территория #16# на #16# блоков не ограниченная по высоте, посмотреть на чанк можно с помощью #F3# + #G#");
            p.Message("#3#. На начальном этапе игрок может заприватить #9# чанков");
            p.Message("#4#. Купить #дополнительные# чанки привата можно у торговца #Альберта# который гуляет на #спавне");
            p.Message("#5#. После привата региона вам дается #3# дня #аренды#, если время аренды #истечет#, то ваш регион #удалят# " +
                    "и чанки из которых состоял регион #заблокируются#, \nдля разблокировки которых вам прийдется #выкупить# их у " +
                    "#Альберта# за цену равную продлению аренды одного чанка на #3# дня");
            p.Message("#6#. Регион нельзя #удалить# если время аренды меньше чем #2# дня и #23# часа");
            p.Message("#7#. Дополнительная #информация# по регионам /#rg");

            return true;
        }
        if (args[0].equalsIgnoreCase("tp"))
        {
            p.Message("\n\n        #-#_#-#_#Телепортация#_#-#_#-");
            p.Message("\n#1#. Для телепортации вам понадобятся #очки телепортации#, которых у вас изначально #1000");
            p.Message("#2#. #1# очко равняется #10# блокам, для телепортации из других миров расстояние между координатами умножается на #8");
            p.Message("#3#. Очки можно #скрафтить#, все крафты показаны на спавне");
            p.Message("#4#. Для просмотра количества очков за телепортацию до #места#, используйте /#toPrice# <#место#> местом может быть любой #игрок# или место как #spawn# или #home");
            p.Message("#5#. Для перемещения на #спавн# используйте #/spawn");
            p.Message("#6#. Вы можете поставить одну #точку# для перемещения в мире, используйте /#sethome# в последствии вы сможете #переместиться# на эту точку использовав #/home");
            p.Message("#7#. Для перемещения к другому игроку используйте /#to# <#ник#>");
            p.Message("#8#. После #смерти# вам предложат телепортацию на место смерти в #2.5# раза дороже если бы вы телепортировались обычно, спустя #минуту# после смерти телепортация будет #недоступна#, так же как и после #перезахода# на сервер");
            p.Message("#9#. Команда #/rtp# бесплатная и работает раз в #30# секунд");

            return true;
        }
        if (args[0].equalsIgnoreCase("show") && p.permision == 10)
        {
            show_info();
            return true;
        }
        return true;
    }

    public static void show_info()
    {
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            show_info(pl);
        }
    }

    public static void show_info(Player pl)
    {
        ComponentBuilder str = rgb.sendMessageColor(chat.color[0],
                "Проголосуй за наш сервер пожалуйста ",
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageColor(chat.color[0],
                                "Жми и мы будем рады!",
                                null,
                                null
                        ).create()),
                new ClickEvent(ClickEvent.Action.OPEN_URL,"http://minecraftrating.ru/vote/97484/")
        ).append(chat.squareC.create());
        pl.spigot().sendMessage(str.create());
        pl.sendMessage("\n");

        str = rgb.sendMessageColor(chat.color[0],
                "Наш дискорд канал, Жмякай ",
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageColor(chat.color[0],
                                "Жми и присоединяйся к нам!",
                                null,
                                null
                        ).create()),
                new ClickEvent(ClickEvent.Action.OPEN_URL,"https://discord.gg/vxa2VQ6")
        ).append(chat.squareC.create());

        pl.spigot().sendMessage(str.create());
        pl.sendMessage("\n");

        str = rgb.sendMessageColor(chat.color[0],
                "https://discord.gg/vxa2VQ6 ",
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageColor(chat.color[0],
                                "Жми и присоединяйся к нам!",
                                null,
                                null
                        ).create()),
                new ClickEvent(ClickEvent.Action.OPEN_URL,"https://discord.gg/vxa2VQ6")
        ).append(chat.squareC.create());

        pl.spigot().sendMessage(str.create());
        pl.sendMessage("\n");

        str = rgb.sendMessageColor(chat.color[0],
                "Информация о возможностях ",
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageColor(chat.color[0],
                                "Будь в курсе фишек!",
                                null,
                                null
                        ).create()),
                new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/i")
        ).append(
                rgb.sendMessageColor(chat.color[1],
                        "/i ",
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                rgb.sendMessageColor(chat.color[0],
                                        "Будь в курсе фишек!",
                                        null,
                                        null
                                ).create()),
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/i")
                ).create()
        ).append(
                chat.squareC.create()
        );
        pl.spigot().sendMessage(str.create());
    }
}
