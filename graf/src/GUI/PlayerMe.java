package GUI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import own.chat;
import own.graf;
import own.player;

import Item.*;
import own.reg;
import railway_system.road;
import text_processing.rgb;
import text_processing.time;

import java.util.ArrayList;
import java.util.Date;

public class PlayerMe implements Listener
{
    public static String name_inv_me = chat.color[0] + "Ваши данные";
    public static String name_inv_rg = chat.color[0] + "Ваши регионы";
    public static String name_inv_bar = chat.color[0] + "Настройка информации";
    public static String name_inv_score = chat.color[0] + "Настройка Scorebord";

    public static boolean me(player pl, String []args)
    {
        player p = null;
        if (args.length == 0)
            p = pl;
        else
        {
            if (pl.permision == 10)
            {
                p = player.getPlayer(Bukkit.getPlayer(args[0]));
            }
        }

        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, name_inv_me);

        int count = 0;
        for (reg region : reg.Regs)
        {
            if (region.owner != null && region.owner.equals(p.p))
            {
                count += region.getSize();
            }
        }

        inv.setItem(0, Item.create(Material.GRASS_BLOCK, chat.color[0] + "Ваши приваты", new String[]{
                rgb.gradientLight(chat.color[1], "Ваши приваты"),
                "",
                rgb.gradientLight(chat.color[1], p.blockRegs == 0 ?
                        "Вам #разрешено# приватить, так как у вас нет #заблокированных# чанков" :
                        "У вас #" + p.blockRegs + "# " + chat.getNormForm(p.blockRegs, "заблокированный чанк", "заблокированных чанка", "заблокированных чанков")),
                "",
                rgb.gradientLight(chat.color[1], "Если вы #просрочите# аренду региона"),
                rgb.gradientLight(chat.color[1], "То вам заблокируют чанки из которых состоял регион"),
                "",
                rgb.gradientLight(chat.color[1], "Если у вас есть #хоть один# заблокированный чанк"),
                rgb.gradientLight(chat.color[1], "То вам #запретят# приватить"),
                "",
                rgb.gradientLight(chat.color[1], "Разблокировать чанки можно у #Альберта"),
                "",
                rgb.gradientLight(chat.color[1], "У вас #" + (p.allowRegs - count) + "# свободных чанков для привата из #" + p.allowRegs),
                "",
                rgb.gradientLight(chat.color[1], "Хотите больше чанков? Обратитесь к #Альберту# на спавне"),
                "",
                rgb.gradientLight(chat.MessageColor[0], "Жми для просмотра информации")
        }));

        inv.setItem(1, Item.create(Material.ENDER_PEARL, rgb.gradientLightInt(chat.color[0], p.tp_points + chat.getNormForm(p.tp_points, " очко", " очка", " очков") + " телепортации"), new String[]{
                rgb.gradientLight(chat.color[1], "За эти очки вы можете совершать телепортацию"),
                rgb.gradientLight(chat.color[1], "к другим игрокам #(/to ник)#, на #spawn# и в другие места"),
                rgb.gradientLight(chat.color[1], "для просмотра стоимости введите команду #/toprice #<#место#>"),
        }));

        inv.setItem(2, Item.create(Material.BIRCH_SIGN, chat.color[0] + "Настройка информации справа", new String[]{
                rgb.gradientLight(chat.color[1], "Настройка #Scoreboard")
        }));

        inv.setItem(3, Item.create(Material.REPEATER, chat.color[0] + "Настройка строки над слотами", new String[]{
                rgb.gradientLight(chat.color[1], "По базовым настройкам там показываются координаты")
        }));

        inv.setItem(4, Item.create(Material.SUNFLOWER, rgb.gradientLightInt(chat.color[0], chat.int_s(p.money) + chat.getNormForm(p.money, " скит", " скита", " скитов")), new String[]{
                rgb.gradientLight(chat.color[1], "Скит это внутриигровая валюта"),
                rgb.gradientLight(chat.color[1], "Приобрести их можно на спавне у #Альберта"),
                rgb.gradientLight(chat.color[1], "За нее вы можете приобритать разные #разрешения# и #продукты#"),
                rgb.gradientLight(chat.color[1], "на пример дополнительные #чанки привата"),
        }, Enchantment.LURE, true));

        pl.pl.openInventory(inv);

        return true;
    }

    public static boolean rg(player pl, player p, int list)
    {
        Inventory inv = Bukkit.createInventory(null, 54, name_inv_rg + " страница " + list);

        inv.setItem(52, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Предыдущая страница")));

        inv.setItem(53, Item.create(Material.PAPER, rgb.gradientLight(chat.color[1], "Следующая страница")));

        ArrayList<reg> regs = p.getRegsCountAll();
        regs.sort((a, b) -> a.owner.equals(p) && !b.owner.equals(p) ? 1 : 0);

        for (int i = 0; i < (Math.min(52, regs.size())); i++)
        {
            String members = "";
            for (int m = 0; m < regs.get(i).members.size(); m++)
                members += regs.get(i).members.get(m) + ((m == regs.get(i).members.size() - 1) ? "" : ", ");

            road ro = null;
            int distance = -1;

            Location reg_loc = regs.get(i).getCenter();

            for (road r : road.roads)
            {
                if (r.type.split("_")[0].equals("station"))
                {
                    Location loc = p.pl.getWorld().getChunkAt(r.x, r.z).getBlock(8, 3, 8).getLocation();
                    if (distance > loc.distance(reg_loc) || distance == -1)
                    {
                        ro = r;
                        distance = (int) loc.distance(reg_loc);
                    }
                }
            }

            inv.setItem(i, Item.create(regs.get(i).owner.equals(p.p) ? Material.GRASS_BLOCK : Material.DIRT, rgb.gradientLight(chat.color[0], regs.get(i).name), new String[]{
                    rgb.gradientLight(chat.color[1], "Владелец #" + regs.get(i).owner),
                    "",
                    rgb.gradientLight(chat.color[1], "Участники #" + members),
                    "",
                    rgb.gradientLight(chat.color[1], "Размер #" + (regs.get(i).x1 + 1) + " #на #" + (regs.get(i).z1 + 1) + chat.getNormForm((regs.get(i).z1 + 1), " #чанк", " #чанка", " #чанков")),
                    rgb.gradientLight(chat.color[1], "Координаты " + "x: #" + (regs.get(i).x * 16) + "# z: #" + (regs.get(i).z * 16)),
                    "",
                    rgb.gradientLight(chat.color[1], "Ближайшее метро #" + ro.name + " #в #" + distance + " #" + chat.getNormForm(distance, "метре", "метрах", "метрах") + " от центра региона"),
                    "",
                    rgb.gradientLight(chat.color[1], "Осталось времени аренды " + (regs.get(i).arenda ? time.getTime(regs.get(i).time_keep, new Date().getTime()) : "пару лет уж точно"))
            }));
        }

        pl.pl.openInventory(inv);

        return true;
    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        player p = player.getPlayer((Player) e.getWhoClicked());
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(name_inv_me))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Ваши приваты"))
                rg(p, p, 1);
            else
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Настройка строки над слотами"))
                bar(p);
            else
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Настройка информации справа"))
                Scoreboard(p);
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ").length >= 2 && (e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0] + " " + e.getWhoClicked().getOpenInventory().getTitle().split(" ")[1]).equalsIgnoreCase(name_inv_rg))
            e.setCancelled(true);

        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(name_inv_bar))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить показ координат"))
                {
                    p.typeBar = "default " + p.typeBar.split(" ")[1];
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить показ координат"))
                {
                    p.typeBar = "location " + p.typeBar.split(" ")[1];
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить показ скорости"))
                {
                    p.typeBar = "default " + p.typeBar.split(" ")[1];
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить показ скорости"))
                {
                    p.typeBar = "speed " + p.typeBar.split(" ")[1];
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию об опыте с существ"))
                {
                    if (p.typeBar.split(" ")[1].equals("all"))
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "exp_block";
                    else
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "none";
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию об опыте с существ"))
                {
                    if (p.typeBar.split(" ")[1].equals("exp_block"))
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "all";
                    else
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "exp_entity";
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию об опыте с руды"))
                {
                    if (p.typeBar.split(" ")[1].equals("all"))
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "exp_entity";
                    else
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "none";
                    bar(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию об опыте с руды"))
                {
                    if (p.typeBar.split(" ")[1].equals("exp_entity"))
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "all";
                    else
                        p.typeBar = p.typeBar.split(" ")[0] + " " + "exp_block";
                    bar(p);
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(name_inv_score))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить поностью показ информации"))
                {
                    p.score.info_all = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить показ информации"))
                {
                    p.score = new player.ScoreInfo();
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить надпись /me"))
                {
                    p.score.info = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить надпись /me"))
                {
                    p.score.info = true;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию о регионе в котором находишься"))
                {
                    p.score.info_reg = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию о регионе в котором находишься"))
                {
                    p.score.info_reg = true;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию о фракции в которой находишься"))
                {
                    p.score.info_clan = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию о фракции в которой находишься"))
                {
                    p.score.info_clan = true;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию о убитых существах"))
                {
                    p.score.info_entity = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию о убитых существах"))
                {
                    p.score.info_entity = true;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить информацию о добытой оптыной руде"))
                {
                    p.score.info_block = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить информацию о добытой оптыной руде"))
                {
                    p.score.info_block = true;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[1] + "Выключить баланс"))
                {
                    p.score.info_money = false;
                    Scoreboard(p);
                }
                else
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(chat.color[0] + "Включить баланс"))
                {
                    p.score.info_money = true;
                    Scoreboard(p);
                }
            }
        }
    }

    private static boolean bar(player p)
    {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, name_inv_bar);

        if (p.typeBar.split(" ")[0].equals("location"))
            inv.setItem(0, Item.create(Material.END_ROD, chat.color[1] + "Выключить показ координат", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(0, Item.create(Material.END_ROD, chat.color[0] + "Включить показ координат"));

        if (p.typeBar.split(" ")[0].equals("speed"))
            inv.setItem(1, Item.create(Material.SUGAR, chat.color[1] + "Выключить показ скорости", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(1, Item.create(Material.SUGAR, chat.color[0] + "Включить показ скорости"));

        if (p.typeBar.split(" ")[1].equals("all") || p.typeBar.split(" ")[1].equals("exp_entity"))
            inv.setItem(2, Item.create(Material.BAT_SPAWN_EGG, chat.color[1] + "Выключить информацию об опыте с существ", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(2, Item.create(Material.BAT_SPAWN_EGG, chat.color[0] + "Включить информацию об опыте с существ"));

        if (p.typeBar.split(" ")[1].equals("all") || p.typeBar.split(" ")[1].equals("exp_block"))
            inv.setItem(3, Item.create(Material.DIAMOND_ORE, chat.color[1] + "Выключить информацию об опыте с руды", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(3, Item.create(Material.DIAMOND_ORE, chat.color[0] + "Включить информацию об опыте с руды"));

        p.pl.openInventory(inv);

        return true;
    }

    private static boolean Scoreboard(player p)
    {
        Inventory inv = Bukkit.createInventory(null, 9, name_inv_score);

        if (p.score.info_all)
            inv.setItem(0, Item.create(Material.BLACK_BANNER, chat.color[1] + "Выключить поностью показ информации", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(0, Item.create(Material.BLACK_BANNER, chat.color[0] + "Включить показ информации"));

        if (p.score.info)
            inv.setItem(1, Item.create(Material.WRITTEN_BOOK, chat.color[1] + "Выключить надпись /me", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(1, Item.create(Material.WRITTEN_BOOK, chat.color[0] + "Включить надпись /me"));

        if (p.score.info_reg)
            inv.setItem(2, Item.create(Material.GRASS_BLOCK, chat.color[1] + "Выключить информацию о регионе в котором находишься", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(2, Item.create(Material.GRASS_BLOCK, chat.color[0] + "Включить информацию о регионе в котором находишься"));

        if (p.score.info_clan)
            inv.setItem(3, Item.create(Material.LECTERN, chat.color[1] + "Выключить информацию о фракции в которой находишься", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(3, Item.create(Material.LECTERN, chat.color[0] + "Включить информацию о фракции в которой находишься"));

        if (p.score.info_entity)
            inv.setItem(4, Item.create(Material.ROTTEN_FLESH, chat.color[1] + "Выключить информацию о убитых существах", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(4, Item.create(Material.ROTTEN_FLESH, chat.color[0] + "Включить информацию о убитых существах"));

        if (p.score.info_block)
            inv.setItem(5, Item.create(Material.COAL_ORE, chat.color[1] + "Выключить информацию о добытой оптыной руде", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(5, Item.create(Material.COAL_ORE, chat.color[0] + "Включить информацию о добытой оптыной руде"));

        if (p.score.info_money)
            inv.setItem(6, Item.create(Material.SUNFLOWER, chat.color[1] + "Выключить баланс", Enchantment.DAMAGE_ALL, true));
        else
            inv.setItem(6, Item.create(Material.SUNFLOWER, chat.color[0] + "Включить баланс"));

        p.pl.openInventory(inv);

        return true;
    }
}
