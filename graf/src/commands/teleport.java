package commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import own.chat;
import own.graf;
import own.player;
import text_processing.rgb;
import text_processing.time;

import java.util.Date;
import java.util.Objects;

public class teleport
{
    public static int  rtp_interval = 30 * 1000;

    public static int getPrice(Location loc, Location loc1)
    {
        int price = 0;
        if (loc.getWorld().getName().equals(loc1.getWorld().getName()))
        {
            int distance = (int) loc.distance(loc1);
            price = distance / 10;
            return price;
        }
        else
        {
            int distance = 0;
            if (loc1.getWorld().getName().equals("world"))
            {
                distance = (int) loc1.distance(new Location(loc1.getWorld(), loc.getX() * 8,loc.getY() * 8, loc.getZ() * 8));
            }
            else
                distance = (int) loc.distance(new Location(loc.getWorld(), loc1.getX() * 8,loc1.getY() * 8, loc1.getZ() * 8));
            price = distance / 10;
            return price;
        }
    }
    public static boolean tp_spawn(player p, String args[])
    {
        if (args.length != 0)
        {
            if (graf.config.contains("spawn." + args[0] + ".world"))
            {
                Location loc = new Location(Bukkit.getWorld(graf.config.getString("spawn." + args[0] + ".world")),
                        graf.config.getDouble("spawn." + args[0] + ".x"),
                        graf.config.getDouble("spawn." + args[0] + ".y"),
                        graf.config.getDouble("spawn." + args[0] + ".z"),
                        (float)graf.config.getDouble("spawn." + args[0] + ".yaw"),
                        (float)graf.config.getDouble("spawn." + args[0] + ".pitch")
                        );
                p.pl.teleport(loc);
            }
            return true;
        }
        int price = getPrice(p.pl.getLocation(), graf.spawn);
        if (price > p.tp_points)
        {
            p.sendMessageE(rgb.gradientLightInt(chat.color[0], "Вам не хватает " + (price - p.tp_points) + " очков телепортации, подробнее ") + rgb.gradientLight(chat.color[1], "/info tp"));
            return true;
        }
        p.tp_points -= price;
        p.sendMessageG(rgb.gradientLightInt(chat.color[0], "Телепортация стоила " + price + " очков телепортации, у вас осталось " + p.tp_points + " очков"));
        p.pl.teleport(graf.spawn);
        return true;
    }

    public static boolean setHome(player p)
    {
        p.setHome();
        return true;
    }

    public static boolean Home(player p)
    {
        if (p.home == null)
        {
            p.sendMessageE(chat.strCommand("У вас нет точки дома, поставьте ее: ", "/sethome", null));
            return true;
        }
        int price = getPrice(p.pl.getLocation(), p.home);
        if (price > p.tp_points)
        {
            p.sendMessageE(rgb.gradientLightInt(chat.color[0], "Вам не хватает " + (price - p.tp_points) + " очков телепортации, подробнее") + rgb.gradientLight(chat.color[1], " /info tp"));
            return true;
        }
        p.tp_points -= price;
        p.toHome();
        p.sendMessageG(rgb.gradientLightInt(chat.color[0], "Телепортация стоила " + price + " очков телепортации, у вас осталось " + p.tp_points + " очков"));
        return true;
    }

    public static boolean to(player p, String[] args)
    {
        if (args.length == 11)
        {
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                player play = player.getPlayer(pl);
                if (pl.getName().equalsIgnoreCase(args[10]))
                {
                    if (args[0].equals("2"))
                    {
                        if (play.tp())
                            play.sendMessageE(rgb.gradientLight(chat.color[0], "Ваш телепорт отменили"));
                        play.tp(false);
                        return true;
                    }
                    if (!play.tp() || new Date().getTime() - play.tptime() > 10000)
                    {
                        play.tptime(new Date().getTime());
                        p.sendMessageE(rgb.gradientLight(chat.color[0], "Он больше не хочет к вам телепортироваться"));
                        return true;
                    }
                    play.sendMessageE(rgb.gradientLight(chat.color[0], "Не двигайтесь 1 секунду для телепортации"));
                    p.sendMessageE(rgb.gradientLight(chat.color[0], "Вы подтвердили запрос игрока " + pl.getName()));
                    tptimer(play, p);
                    play.tp(false);
                    return true;
                }
            }
            p.sendMessageE(rgb.gradientLight(chat.color[0],"Игрок не в сети"));
            return true;
        }
        if (args.length != 1)
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0],"/to <ник игрока>"));
            return true;
        }
        if (args[0].equalsIgnoreCase("death"))
        {
            if (new Date().getTime() - p.death_time > (1000 * 60 * 60))
            {
                p.sendMessageE(rgb.gradientLightInt(chat.color[0], "Прошла минута с момента смерти"));
                return true;
            }
            if (p.death == null)
            {
                p.sendMessageE(rgb.gradientLight(chat.color[0], "Не можем найти места смерти, может ты перезашел"));
                return true;
            }
            int price = (int) (getPrice(p.pl.getLocation(), p.death) * 2.5);
            if (p.tp_points < price)
            {
                p.sendMessageE(rgb.gradientLightInt(chat.color[0], "Вам не хватает " + (price - p.tp_points) + " очков телепортации"));
                return true;
            }
            p.tp_points -= price;
            p.pl.teleport(p.death);
            p.sendMessageG(rgb.gradientLightInt(chat.color[0], "Телепортация стоила " + price + " очков телепортации, у вас осталось " + p.tp_points + " очков"));
            p.death = null;
            return true;
        }
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (pl.getName().equalsIgnoreCase(args[0]))
            {
                p.sendMessage(rgb.gradientLight(chat.color[0], "Вы отправили запрос игроку " + pl.getName() + "\nждите подтверждения...\nТелепортация будет стоить " + getPrice(p.pl.getLocation(), pl.getLocation())));
                p.tp(true);
                p.tptime(new Date().getTime());
                ComponentBuilder m = new ComponentBuilder("Да");
                m.color(ChatColor.GREEN);
                m.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/to 1 1 1 1 1 1 1 1 1 1 " + p.p));
                m.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Нажми для подтверждения").color(ChatColor.GREEN).create()));
                ComponentBuilder m1 = new ComponentBuilder(" Нет ");
                m1.color(ChatColor.RED);
                m1.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/to 2 1 1 1 1 1 1 1 1 1 " + p.p));
                m1.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Нажми для отказа").color(ChatColor.RED).create()));
                pl.spigot().sendMessage(new ComponentBuilder("Принять игрока " + p.p + " ? ").color(chat.color[0]).append(m.create()).append(m1.create()).append(chat.squareC.create()).create());
                return true;
            }
        }
        p.sendMessageE(rgb.gradientLight(chat.color[0], "Игрок не в сети"));
        return true;
    }

    public static void tptimer(player p, player to)
    {
        p.tploc(p.pl.getLocation());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Location loc = p.tploc();
                Location loc1 = p.pl.getLocation();
                if (loc.getX() == loc1.getX() && loc.getY() == loc1.getY() && loc.getZ() == loc1.getZ())
                {
                    int price = getPrice(p.pl.getLocation(), to.pl.getLocation());
                    if (price > p.tp_points)
                    {
                        p.sendMessageE(rgb.gradientLightInt(chat.color[0], "Вам не хватает " + (price - p.tp_points) + " очков телепортации, подробнее") + rgb.gradientLight(chat.color[1], " /info tp"));
                        return ;
                    }
                    p.tp_points -= price;
                    p.sendMessageG(rgb.gradientLightInt(chat.color[0], "Телепортация стоила " + price + " очков телепортации, у вас осталось " + p.tp_points + " очков"));
                    p.pl.teleport(to.pl.getLocation());
                }
                else
                    p.sendMessageE(rgb.gradientLight(chat.color[0], "Телепорт отменен"));
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 20);
    }

    public static boolean toPrice(player p, String[] args)
    {
        if (args.length != 1)
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "Используй: ") + rgb.gradientLight(chat.color[1], "/toPrice <место>\n")
                    + rgb.gradientLight(chat.color[0], "Пример: ") + rgb.gradientLight(chat.color[1], "/toPrice home/<ник игрока>"));
            return true;
        }
        if (args[0].equalsIgnoreCase("spawn"))
        {
            p.sendMessage(rgb.gradientLight(chat.color[0], "Телепортация будет стоить " + getPrice(p.pl.getLocation(), graf.spawn) + chat.getNormForm(getPrice(p.pl.getLocation(), graf.spawn), " очко", " очка", " очков")));
            return true;
        }
        if (args[0].equalsIgnoreCase("home"))
        {
            p.sendMessage(rgb.gradientLight(chat.color[0], "Телепортация будет стоить " + getPrice(p.pl.getLocation(), p.home) + chat.getNormForm(getPrice(p.pl.getLocation(), p.home), " очко", " очка", " очков")));
            return true;
        }
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (pl.getName().equals(args[0]))
            {
                p.sendMessage(rgb.gradientLightInt(chat.color[0], "Телепортация будет стоить " + getPrice(p.pl.getLocation(), pl.getLocation()) + chat.getNormForm(getPrice(p.pl.getLocation(), pl.getLocation()), " очко", " очка", " очков")));
                return true;
            }
        }
        p.sendMessageE(rgb.gradientLight(chat.color[0], "Нет такого места в мире"));
        return true;
    }

    public static boolean rtp(player p, String[] args)
    {
        if ((new Date().getTime() - p.tp_time) < rtp_interval && p.permision != 10)
        {
            p.ErrorMessage("До следующего использования подожди " + time.getTime(p.tp_time + rtp_interval, new Date().getTime()));
            return true;
        }
        for (int i = 0; i < 1000; i++)
        {
            int x = (int) (Math.random() * (graf.size_world * 2)) - graf.size_world;
            int z = (int) (Math.random() * (graf.size_world * 2)) - graf.size_world;

            Location loc = new Location(Bukkit.getWorld("world"), x, 256, z);
            for (int y = 256; y > 30; y--)
            {
                loc.setY(y);
                if (loc.getBlock().isEmpty())
                    continue ;
                if (loc.getBlock().isLiquid() || loc.getBlock().isPassable())
                    break ;
                if (loc.getBlock().getRelative(0, 1, 0).isEmpty() && loc.getBlock().getRelative(0, 2, 0).isEmpty())
                {
                    loc.setY(y + 1);
                    loc.setX(loc.getX() + 0.5);
                    loc.setZ(loc.getZ() + 0.5);
                    p.pl.teleport(loc);
                    p.tp_time = new Date().getTime();
                    return true;
                }
            }
        }
        return true;
    }
}
