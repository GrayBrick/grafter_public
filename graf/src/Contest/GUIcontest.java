package Contest;

import Item.Item;
import clans.Clan;
import clans.ClanEffects;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import own.chat;
import own.player;
import text_processing.rgb;

public class GUIcontest implements Listener
{
    final private static String contest = chat.color[0] + "Состязания";
    final private static String pvp_1_1 = chat.color[0] + "Дуэль";

    public static void Contest(player p)
    {
        Inventory inv = update_inv(p, Bukkit.createInventory(null, 27, contest));

        if (inv == null)
            return ;

        p.pl.openInventory(inv);
    }

//    public static void BeaconEffects(player p)
//    {
//        Inventory inv = update_inv_effects(p, Bukkit.createInventory(null, 27, effects));
//
//        if (inv == null)
//            return ;
//
//        p.pl.openInventory(inv);
//    }

    public static Inventory update_inv(player p, Inventory inv)
    {
        Clan clan = p.getClan();
        if (clan == null)
            return null;

        inv.clear();

        inv.setItem(0, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Принцип #работы"), new String[]{
                rgb.gradientLight(chat.color[0], "Скоро#...")
        }));

        inv.setItem(10, Item.create(Material.IRON_SWORD, pvp_1_1, new String[]{
                rgb.gradientLight(chat.color[0], "Сражение #1# на #1"),
                rgb.gradientLight(chat.color[0], "Вы будите сражаться в #равных# условиях или в #своих вещах"),
                "",
                rgb.gradientLight(chat.color[0], "Если хоть один участник выберет сражение в #равных условиях"),
                rgb.gradientLight(chat.color[0], "Сражение будет происходить с #одинаковым# снаряжением"),
                rgb.gradientLight(chat.color[0], "Вещи из #текущего# инвентаря будут #сохранены"),
                "",
                rgb.gradientLight(chat.color[0], "Если #оба# участника выберут сражение в #своих вещах"),
                rgb.gradientLight(chat.color[0], "То на арену вы выйдите в #своем снаряжении"),
                rgb.gradientLight(chat.color[0], "и после смерти #потеряете его"),
                "",
                rgb.gradientLight(chat.color[0], "Если сражение происходит против участника из #другой# фракции"),
                rgb.gradientLight(chat.color[0], "То за победу в сражении вы заработаете"),
                rgb.gradientLight(chat.color[0], "#очки влияния# для своей фракции"),
                rgb.gradientLight(chat.color[0], "При #проигрыше# потери очков не будет"),
                "",
                rgb.gradientLight(chat.color[0], "Так же вы можете сделать ставку на свою победу"),
                rgb.gradientLight(chat.color[0], "Если оба участника сделали ставку то за победу"),
                rgb.gradientLight(chat.color[0], "Вы получите #выигрыш"),
        }, true));

        return inv;
    }

//    public static Inventory update_inv_effects(player p, Inventory inv)
//    {
//        Clan clan = p.getClan();
//        if (clan == null)
//            return null;
//
//        inv.clear();
//
//        inv.setItem(0, Item.create(Material.LIME_CONCRETE, ChatColor.of("#00FF00") + "Включить все"));
//        inv.setItem(inv.getSize() - 1, Item.create(Material.RED_CONCRETE, ChatColor.of("#FF0000") + "Выключить все"));
//
//        int i = ((inv.getSize() - ClanEffects.Effects.length) / 2);
//        for (ClanEffects effect : ClanEffects.Effects)
//            inv.setItem(i++, Buttom(clan, effect));
//
//        return inv;
//    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(contest))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(pvp_1_1))
                {
                    Contest_pvp_1_1.add_player_que(p);
                    p.pl.closeInventory();
                    return ;
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(contest))
        {

        }
    }
}