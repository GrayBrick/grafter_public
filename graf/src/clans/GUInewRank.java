package clans;

import Item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import own.chat;
import own.player;

import java.util.List;

public class GUInewRank implements Listener
{

    final private static String new_rank = chat.color[0] + "Выдать ранг игроку";

    public static void newRank(player p, player member)
    {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, new_rank + " " + member.p);

        if (p.rank == 50 && member.rank != 50)
            inv.setItem(0, Item.create(Material.NETHERITE_HELMET, chat.color[0] + "Сделать" + Clan.getColorRank((byte) 50) + " Главой"));
        if (p.rank > 45 && member.rank != 45)
            inv.setItem(1, Item.create(Material.DIAMOND_HELMET, chat.color[0] + "Сделать" + Clan.getColorRank((byte) 45) + " Заместителем"));
        if (p.rank > 41 && member.rank != 41)
        inv.setItem(2, Item.create(Material.GOLDEN_HELMET, chat.color[0] + "Сделать" + Clan.getColorRank((byte) 41) + " Командиром"));
        if (p.rank > 40 && member.rank != 2)
            inv.setItem(3, Item.create(Material.IRON_HELMET, chat.color[0] + "Сделать" + Clan.getColorRank((byte) 2) + " Бойцом"));

        p.pl.closeInventory();
        p.pl.openInventory(inv);

        return;
    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0].equalsIgnoreCase(new_rank.split(" ")[0]) &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[1].equalsIgnoreCase(new_rank.split(" ")[1]))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ").length == 2)
                {
                    player member = player.getPlayer(e.getWhoClicked().getOpenInventory().getTitle().split(" ")[3]);
                    e.getWhoClicked().closeInventory();
                    if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equalsIgnoreCase("Главой"))
                    {
                        List<String> list = member.getClan().getMembers();
                        list.remove(member.p);
                        list.add(p.p);
                        p.getClan().setMembers(list);
                        p.getClan().setOwner(member);
                        p.newRank((byte) 45);
                        member.newRank((byte) 50);
                        return ;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equalsIgnoreCase("Заместителем"))
                    {
                        member.newRank((byte) 45);
                        return ;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equalsIgnoreCase("Командиром"))
                    {
                        member.newRank((byte) 41);
                        return ;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equalsIgnoreCase("Бойцом"))
                    {
                        member.newRank((byte) 2);
                        return ;
                    }
                }
            }
        }
    }
}
