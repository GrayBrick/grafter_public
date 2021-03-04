package Listeners;

import commands.message;
import commands.teleport;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;
import own.reg;
import railway_system.road;
import text_processing.rgb;

import java.util.Date;
import java.util.Objects;

public class Player_L implements Listener {

    @EventHandler
    public static void join(PlayerJoinEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                rgb.gradientTitle(e.getPlayer(), new String[]{"GrafTer", "Добро пожаловать"}, chat.title_inv, 3, 3500, 0.9, 0.9);
            }
        }.runTaskLaterAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 20);
        player p = player.getPlayer(e.getPlayer());
        p.pl = e.getPlayer();
        e.setJoinMessage(rgb.gradientLight(chat.color[0], p.p + " #присоединился"));
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                message.show_info(p.pl);
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 1);
    }

    @EventHandler
    private static void leave(PlayerQuitEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        p.clan_gui = null;
        p.invite_in_clan = false;
        p.kazna_give = false;
        p.kazna_take = false;
        if (p.go_to_station)
        {
            if (p.pl.getVehicle() != null)
            {
                p.pl.getVehicle().remove();
                p.go_to_station = false;
                road r = road.getRoad(p.station_from);
                Chunk ch = p.pl.getWorld().getChunkAt(r.x, r.z);

                switch (r.type.split("_")[1])
                {
                    case "e":
                    {
                        Location loc_tp = ch.getBlock(4, 2, 6).getLocation();

                        loc_tp.setX(loc_tp.getX() + 0.5);
                        loc_tp.setZ(loc_tp.getZ() + 0.5);

                        p.pl.teleport(loc_tp);
                        break ;
                    }

                    case "w":
                    {
                        Location loc_tp = ch.getBlock(11, 2, 9).getLocation();

                        loc_tp.setX(loc_tp.getX() + 0.5);
                        loc_tp.setZ(loc_tp.getZ() + 0.5);

                        p.pl.teleport(loc_tp);
                        break ;
                    }

                    case "n":
                    {
                        Location loc_tp = ch.getBlock(6, 2, 11).getLocation();

                        loc_tp.setX(loc_tp.getX() + 0.5);
                        loc_tp.setZ(loc_tp.getZ() + 0.5);

                        p.pl.teleport(loc_tp);
                        break ;
                    }

                    case "s":
                    {
                        Location loc_tp = ch.getBlock(9, 2, 4).getLocation();

                        loc_tp.setX(loc_tp.getX() + 0.5);
                        loc_tp.setZ(loc_tp.getZ() + 0.5);

                        p.pl.teleport(loc_tp);
                        break ;
                    }
                }
            }
        }
        e.setQuitMessage(rgb.gradientLight(chat.color[0], p.p + " #вышел"));
    }

    @EventHandler
    private static void death(PlayerDeathEvent e)
    {
        player p = player.getPlayer(e.getEntity());
        p.death = e.getEntity().getLocation();
        p.death_time = new Date().getTime();
        e.setDeathMessage(rgb.gradientLight(chat.color[0], p.p + " #умер"));
    }

    @EventHandler
    private static void death(PlayerRespawnEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        if (p.home != null)
            e.setRespawnLocation(p.home);
        int price = (int) (teleport.getPrice(e.getRespawnLocation(), p.death) * 2.5);
        ComponentBuilder mess = rgb.sendMessageColor(chat.color[0],
                "Вы можете вернуться на место смерти за " + price + chat.getNormForm(price, " очко телепортации "," очка телепортации ", " очков телепортации "),
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        rgb.sendMessageColor(chat.color[1],
                                "жми",
                                null,
                                null
                        ).create()
                ),
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/to death")
        ).append(chat.squareC.create());
        p.pl.spigot().sendMessage(mess.create());
    }

    @EventHandler
    private static void move(PlayerMoveEvent e)
    {
        Long d0 = new Date().getTime();
        player p = player.getPlayer(e.getPlayer());
        if (p.permision == 10)
            return ;
        if (p.prison)
        {
            e.setCancelled(true);
            return ;
        }
        reg region = reg.getReg(e.getTo());
        if (region != null)
        {
            if (!p.inArena)
            {
                for (reg.arena ar : region.arenas)
                {
                    if (ar.inArena(p.pl.getLocation()))
                    {
                        p.inArena = true;
                        p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Вы вошли в зону #PVP"), rgb.gradientLight(chat.color[0], "Тут вас могут #убить"), 20 ,40, 20);
                        break ;
                    }
                }
            }
            else
            {
                boolean f = false;
                for (reg.arena ar : region.arenas)
                {
                    if (ar.inArena(p.pl.getLocation()))
                    {
                        f = true;
                        break ;
                    }
                }
                if (!f)
                {
                    p.inArena = false;
                    p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Вы вышли из зоны #PVP"), rgb.gradientLight(chat.color[0], "Дыши спокойнее"), 20 ,40, 20);
                }
            }

            if (ActionPrivate.allowAction(p, e.getTo()))
            {
                if (p.pl.getGameMode().equals(GameMode.ADVENTURE))
                    p.pl.setGameMode(GameMode.SURVIVAL);
            }
            else
            if (!p.pl.getGameMode().equals(GameMode.ADVENTURE))
                p.pl.setGameMode(GameMode.ADVENTURE);
        }
        else
        if (!p.pl.getGameMode().equals(GameMode.SURVIVAL))
            p.pl.setGameMode(GameMode.SURVIVAL);
        if (e.getTo().getY() < 7)
        {
            p = player.getPlayer(e.getPlayer());
            if (p.permision == 10)
                return ;
            if (p.pl.getVehicle() != null)
                return ;
            Chunk ch = e.getTo().getChunk();
            road r = road.getRoad(ch.getX(), ch.getZ());
            if (!r.isExist())
                return ;
            if (!r.type.split("_")[0].equals("station"))
            {
                e.setCancelled(true);
                return ;
            }

            //запрет передвижения на станциях
            switch (r.type.split("_")[1])
            {
                case "e":
                {
                    Location loc_tp = ch.getBlock(6, 2, 6).getLocation();
                    if (e.getTo().getX() > loc_tp.getX())
                        e.setCancelled(true);
                    break ;
                }

                case "w":
                {
                    Location loc_tp = ch.getBlock(10, 2, 9).getLocation();
                    if (e.getTo().getX() < loc_tp.getX())
                        e.setCancelled(true);
                    break ;
                }

                case "n":
                {
                    Location loc_tp = ch.getBlock(6, 2, 10).getLocation();
                    if (e.getTo().getZ() < loc_tp.getZ())
                        e.setCancelled(true);
                    break ;
                }

                case "s":
                {
                    Location loc_tp = ch.getBlock(9, 2, 6).getLocation();
                    if (e.getTo().getZ() > loc_tp.getZ())
                        e.setCancelled(true);
                    break ;
                }
            }
        }
        Long d1 = new Date().getTime();
        //p.sendMessageE(d1 - d0 + "");
    }

    @EventHandler
    private static void tp(PlayerTeleportEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        reg region = reg.getReg(e.getTo());
        if (region != null)
        {
            if (ActionPrivate.allowAction(p, e.getTo()))
                return ;
            if (!p.pl.getGameMode().equals(GameMode.ADVENTURE))
                p.pl.setGameMode(GameMode.ADVENTURE);
        }
        else
            if (!p.pl.getGameMode().equals(GameMode.SURVIVAL))
                p.pl.setGameMode(GameMode.SURVIVAL);
        if (e.getTo().getY() < 7)
        {
            if (p.permision == 10)
                return ;
            Chunk ch = e.getTo().getChunk();
            road r = road.getRoad(ch.getX(), ch.getZ());
            if (!r.isExist())
                return ;
            if (!r.type.split("_")[0].equals("station"))
            {
                e.setCancelled(true);
                return ;
            }

            switch (r.type.split("_")[1])
            {
                case "e":
                {
                    Location loc_tp = ch.getBlock(6, 2, 6).getLocation();
                    if (e.getTo().getX() > loc_tp.getX())
                        e.setCancelled(true);
                    break ;
                }

                case "w":
                {
                    Location loc_tp = ch.getBlock(10, 2, 9).getLocation();
                    if (e.getTo().getX() < loc_tp.getX())
                        e.setCancelled(true);
                    break ;
                }

                case "n":
                {
                    Location loc_tp = ch.getBlock(6, 2, 10).getLocation();
                    if (e.getTo().getZ() < loc_tp.getZ())
                        e.setCancelled(true);
                    break ;
                }

                case "s":
                {
                    Location loc_tp = ch.getBlock(9, 2, 6).getLocation();
                    if (e.getTo().getZ() > loc_tp.getZ())
                        e.setCancelled(true);
                    break ;
                }
            }
        }
    }

    public static void bit(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
        {
            player p = player.getPlayer((Player) e.getDamager());
            player en = player.getPlayer((Player) e.getEntity());

            if (en.bar_hp == null)
                en.bar_hp = Bukkit.createBossBar(e.getEntity().getName(), BarColor.RED, BarStyle.SEGMENTED_6);

            if (!player.bars.contains(en.bar_hp))
                player.bars.add(en.bar_hp);

            en.bar_hp.setProgress(en.pl.getHealth() / en.pl.getMaxHealth() * 1.0);
            boolean f = false;
            for (String bar : p.bar_list.keySet())
            {
                if (bar.equals(en.bar_hp.getTitle()))
                    f = true;
            }
            if (!f)
            {
                p.bar_list.put(en.bar_hp.getTitle(), new Date().getTime());
                en.bar_hp.addPlayer(p.pl);
            }
            else
                p.bar_list.put(en.bar_hp.getTitle(), new Date().getTime());


            p.pl.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new ComponentBuilder("Вы нанесли ").color(chat.color[1]
                    ).append((int) e.getDamage() + "").color(chat.color[0]
                    ).append(" урона").color(chat.color[1]).create());
            p.timerBar = true;
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    p.timerBar = false;
                }
            }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 40);
        }
    }
}
