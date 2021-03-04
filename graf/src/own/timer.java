package own;

import Listeners.NameTag;
import clans.Clan;
import clans.Oper;
import clans.SortClans;
import commands.message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import railway_system.road;
import text_processing.rgb;

import java.text.DecimalFormat;
import java.util.*;

public class timer
{
    public static String Trader_spawn =  rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), "Альберт");
    public static String Trader_spawn_lock = rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), "Продавец валюты");

    public static void timer()
    {
        timer_trader();
        timer_header();

        final int[] count = new int[]{0, 0};

        int i = 0;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (new Date().getMinutes() % 10 == 0 && new Date().getSeconds() > 5 && new Date().getSeconds() <= 10)
                {
                    message.show_info();
                    reloadPlayerRank();
                    reg.arendaOper();
                }
                if (count[0] % 4 == 0)
                {
                    Oper.GiveClanEffects();
                }

                timer_Scoreboard();

                for (String name : graf.config.getConfigurationSection("dont_pickup").getKeys(false))
                {
                    if (graf.config.get("dont_pickup." + name + ".world") != null)
                    {
                        Location loc = new Location(
                                Bukkit.getWorld(graf.config.getString("dont_pickup." + name + ".world")),
                                graf.config.getDouble("dont_pickup." + name + ".x"),
                                graf.config.getDouble("dont_pickup." + name + ".y"),
                                graf.config.getDouble("dont_pickup." + name + ".z")
                        );
                        if (loc.getChunk().isLoaded() && loc.getBlockY() > 10)
                        {
                            boolean f = false;
                            for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.01, 0.01, 0.01))
                            {
                                if (en instanceof Item && en.isInvulnerable())
                                {
                                    if (en.getPassengers().size() == 0 && graf.config.getInt("dont_pickup." + name + ".pass") != 0)
                                    {
                                        en.remove();
                                        break ;
                                    }
                                    f = true;
                                    break ;
                                }
                            }
                            for (Entity en : loc.getWorld().getNearbyEntities(loc, 1, 1, 1))
                            {
                                if (en instanceof ArmorStand && !en.hasGravity() && en.getVehicle() == null)
                                    en.remove();
                            }
                            if (!f)
                                NameTag.setNameTag(loc, name);
                        }

                    }
                    int last_id = graf.config.getInt("way_trader_spawn_last");
                    Location loc = new Location(
                            Bukkit.getWorld(graf.config.getString(("way_trader_spawn." + last_id + ".world"))),
                            graf.config.getDouble(("way_trader_spawn." + last_id + ".x")),
                            graf.config.getDouble(("way_trader_spawn." + last_id + ".y")),
                            graf.config.getDouble(("way_trader_spawn." + last_id + ".z")),
                            (float) graf.config.getDouble(("way_trader_spawn." + last_id + ".yaw")),
                            (float) graf.config.getDouble(("way_trader_spawn." + last_id + ".pitch"))
                    );

                    if (loc.getChunk().isLoaded())
                    {
                        boolean f = false;

                        for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.1, 0.1, 0.1))
                        {
                            if (en instanceof WanderingTrader && en.isInvulnerable())
                            {
                                ((WanderingTrader) en).addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1000000, 200, false, false));
                                f = true;
                                break ;
                            }
                        }

                        if (!f)
                            timer_trader();
                    }
                }
                if (count[0]++ > 1000)
                    count[0] = 0;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0,  20 * 5);
    }

    private static void timer_Scoreboard()
    {
        for(Player online : Bukkit.getOnlinePlayers())
        {
            player p = player.getPlayer(online);

            for (String bar : p.bar_list.keySet())
            {
                if (new Date().getTime() - p.bar_list.get(bar) > 3000)
                {
                    for (BossBar boss : player.bars)
                    {
                        if (boss.getTitle().equals(bar))
                            boss.removePlayer(online);
                    }
                    p.bar_list.remove(bar);
                }
            }

            reg region = reg.getReg(p.pl.getLocation());
            ArrayList<Clan> clans = Clan.getClanLocation(p.pl.getLocation().getBlockX(), p.pl.getLocation().getBlockZ());

            Scoreboard player_board = Bukkit.getScoreboardManager().getNewScoreboard();

            SortClans.sortAll(player_board);

            Objective objective = null;

            objective = getObjective(player_board, "Score", "s1", "s2", RenderType.INTEGER);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            ChatColor jast = ChatColor.WHITE;
            ChatColor red = ChatColor.RED;

            objective.setDisplayName(red + "" + ChatColor.BOLD + p.p);

            int i = 9;

            if (p.score.info_all)
            {
                if (p.score.info_reg && region != null && region.isExist())
                {
                    objective.getScore(jast + "Регион:").setScore(i--);
                    objective.getScore("    - " + red + region.name).setScore(i--);
                }

                if (p.score.info_clan && clans.size() != 0)
                {
                    objective.getScore(jast +
                            (clans.size() == 1 ? "Фракция:" : "Фракции:")
                    ).setScore(i--);
                    for (Clan clan : clans)
                        objective.getScore("    - " + red + clan.getName()).setScore(i--);
                }

                if (p.score.info_entity)
                    objective.getScore(jast + "Убито существ: " + red + p.DeathEntity).setScore(i--);

                if (p.score.info_block)
                    objective.getScore(jast + "Добыто опытной руды: " + red + p.BreakBlock).setScore(i--);

                if (p.score.info_money)
                    objective.getScore(jast + chat.getNormFormSkit(p.money) + ": " + red + chat.int_s(p.money)).setScore(i--);

                if (p.score.info)
                {
                    objective.getScore(ChatColor.GREEN + "Настройка информации:").setScore(i--);
                    objective.getScore("    - " + ChatColor.DARK_GREEN + "/me").setScore(i--);
                }
            }

            online.setScoreboard(player_board);
        }
    }

    private static Objective getObjective(String name, String s1, String s2, RenderType type)
    {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if (board.getObjective(name) != null)
            return board.getObjective(name);
        else
            return board.registerNewObjective(name, s1, s2, type);
    }

    private static Objective getObjective(Scoreboard board, String name, String s1, String s2, RenderType type)
    {
        if (board.getObjective(name) != null)
            return board.getObjective(name);
        else
            return board.registerNewObjective(name, s1, s2, type);
    }

    private static void timer_trader()
    {
        int last_id = graf.config.getInt("way_trader_spawn_last");
        Location loc = new Location(
                Bukkit.getWorld(graf.config.getString(("way_trader_spawn." + last_id + ".world"))),
                graf.config.getDouble(("way_trader_spawn." + last_id + ".x")),
                graf.config.getDouble(("way_trader_spawn." + last_id + ".y")),
                graf.config.getDouble(("way_trader_spawn." + last_id + ".z")),
                (float) graf.config.getDouble(("way_trader_spawn." + last_id + ".yaw")),
                (float) graf.config.getDouble(("way_trader_spawn." + last_id + ".pitch"))
        );

        boolean f = false;
        WanderingTrader trader = null;

        for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.1, 0.1, 0.1))
        {
            if (en instanceof WanderingTrader && en.isInvulnerable())
            {
                trader = (WanderingTrader) en;
                f = true;
                break ;
            }
        }
        if (!f) {
            trader = (WanderingTrader) loc.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
            trader.setAI(false);
            trader.setInvulnerable(true);
            trader.setGravity(false);
            trader.setCustomNameVisible(true);
            trader.setCustomName(Trader_spawn);
            trader.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1000000, 200, false, false));

            ItemStack item = new ItemStack(Material.COAL);
            item.setAmount(64);
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.DIAMOND), 10);
            recipe.addIngredient(item);
            recipe.addIngredient(item);

            List<MerchantRecipe> list = new ArrayList<>();
            list.add(recipe);

            trader.setRecipes(list);
        }

        final int[] count = new int[]{last_id, 0};

        WanderingTrader finalTrader = trader;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {

//                if (count[1] >= 24000)
//                    count[1] = 0;
//                count[1] += 10;
//                Bukkit.getWorld("world").setTime(count[1]);

                if (finalTrader.isDead())
                {
                    cancel();
                    return ;
                }
                if (!graf.config.contains(("way_trader_spawn." + ++count[0])))
                    count[0] = 1;
                Location loc = new Location(
                        Bukkit.getWorld(graf.config.getString(("way_trader_spawn." + count[0] + ".world"))),
                        graf.config.getDouble(("way_trader_spawn." + count[0] + ".x")),
                        graf.config.getDouble(("way_trader_spawn." + count[0] + ".y")),
                        graf.config.getDouble(("way_trader_spawn." + count[0] + ".z")),
                        (float) graf.config.getDouble(("way_trader_spawn." + count[0] + ".yaw")),
                        (float) graf.config.getDouble(("way_trader_spawn." + count[0] + ".pitch"))
                );

                graf.config.set("way_trader_spawn_last", count[0]);

                finalTrader.setCustomName(rgb.gradientShift("Альберт", chat.MessageColor, 10, count[0]));

                finalTrader.teleport(loc);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 1);
    }

    private static void timer_header()
    {
        final int []count = new int[]{0};

        DecimalFormat df = new DecimalFormat("###.#");

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                count[0]++;
                String []header = {
                        rgb.gradientShift(" ၁ mc.GrafTer.ru င ", rgb.rand_colors, 50, count[0]),
                        rgb.gradientShift(" ပ mc.GrafTer.ru ပ ", rgb.rand_colors, 50, count[0]),
                        rgb.gradientShift(" င mc.GrafTer.ru ၁ ", rgb.rand_colors, 50, count[0]),
                        rgb.gradientShift(" ဂ mc.GrafTer.ru ဂ ", rgb.rand_colors, 50, count[0])
                };
                String footer_0 = rgb.gradientShift("играют : ", rgb.rand_colors, 50, count[0]);
                String footer_1 = rgb.gradientShift("\nпинг    : ", rgb.rand_colors, 50, count[0]);
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    player plp = player.getPlayer(p);

                    if (plp.typeBar.split(" ")[0].equals("speed"))
                    {
                        if (plp.timerBar)
                            return ;
                        plp.speed_loc_2 = plp.speed_loc_1;
                        plp.speed_loc_1 = p.getLocation();
                        if (plp.speed_loc_2 != null)
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("Скорость: ").color(chat.color[0]).append(df.format((plp.speed_loc_1.distance(plp.speed_loc_2) / 0.35) * 3.6)).color(chat.color[1]
                            ).append(" км/ч").color(chat.color[0]).create());
                    }
                    else
                    if (plp.typeBar.split(" ")[0].equals("location"))
                    {
                        if (plp.timerBar)
                            return ;
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(
                                p.getLocation().getBlockX() + " " +
                                        p.getLocation().getBlockY() + " " +
                                        p.getLocation().getBlockZ() + " " +
                                        p.getLocation().getBlock().getBiome()
                        ).color(chat.color[1]).create());
                    }

                    String prefix;
                    String name;
                    String postfix;

                    if (plp.grad_prefix)
                    {
                        prefix = (p.getWorld().getName().equals("world") ? chat.MessageColor[1] : p.getWorld().getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˻" +
                                rgb.gradientShift(plp.getPrefix(), new ChatColor[]{ChatColor.of(plp.prefix_col0), ChatColor.of(plp.prefix_col1)}, 50, count[0])  +
                                (p.getWorld().getName().equals("world") ? chat.MessageColor[1] : p.getWorld().getName().equals("world_nether") ? chat.MessageColor[3] : chat.MessageColor[5]) + "˺ ";
                    }
                    else
                        prefix = plp.getPrefixFull();

                    if (plp.grad_name)
                    {
                        name = rgb.gradientShift(plp.getName(), new ChatColor[]{ChatColor.of(plp.name_col0), ChatColor.of(plp.name_col1)}, 50, count[0]);
                    }
                    else
                        name = plp.getName();

                    if (plp.grad_postfix)
                    {
                        postfix = rgb.gradientShiftRand(plp.getPostfix(), 5, count[0]);//rgb.gradientShift(plp.getPostfix(), new ChatColor[]{ChatColor.of(plp.postfix_col0), ChatColor.of(plp.postfix_col1)}, 20, count[0]);
                    }
                    else
                        postfix = plp.getPostfix();

                    p.setPlayerListName(prefix + ChatColor.of("#ffffff") + name + ChatColor.of("#ffffff") + postfix);

                    EntityPlayer ep = ((CraftPlayer) p).getHandle();
                    p.setPlayerListFooter(footer_0 + ChatColor.of("#00ffc4") + Bukkit.getOnlinePlayers().size() + footer_1 + ChatColor.of("#00ffc4") + ep.ping);
                    p.setPlayerListHeader(header[(count[0]) % 4]);
                }

            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 1, 7);
    }

    private static void reloadPlayerRank()
    {
        long now = new Date().getTime();
        long interval = 1000 * 60 * 60 * 12;
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            player p = player.getPlayer(pl);
            if (p.rank == 1 && (now - p.time_clan_include) >= (interval))
                p.newRank((byte) 2);
        }
    }
}
