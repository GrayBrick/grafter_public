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
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;
import text_processing.rgb;

import java.util.Objects;

public class GUIKazna implements Listener
{
    final private static String kazna = chat.color[0] + "Казна";

    final private static String give = chat.color[0] + "Внести скиты";
    final private static String take = chat.color[0] + "Забрать скиты";

    public static void Kazna(player p)
    {
        Inventory inv = update_inv(p, Bukkit.createInventory(null, InventoryType.HOPPER, kazna));

        if (inv == null)
            return ;

        p.pl.openInventory(inv);
    }

    public static Inventory update_inv(player p, Inventory inv)
    {
        Clan clan = p.getClan();
        if (clan == null)
            return null;

        inv.clear();

        inv.setItem(0, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Принцип #работы"), new String[]{
                rgb.gradientLight(chat.color[0], "Казна тратится на работу #маяка"),
                rgb.gradientLight(chat.color[0], "Так же скиты можно выдать из казны"),
                rgb.gradientLight(chat.color[0], "участникам фракции")
        }));
        inv.setItem(1, Item.create(Material.DROPPER, give, new String[]{
                rgb.gradientLight(chat.color[0], p.rank < 45 ? "Вы не можете вносить #скиты" : ""),
                rgb.gradientLight(chat.color[0], "После #нажатия# напишите сумму в чат"),
                rgb.gradientLight(chat.color[0], "Ваши #скиты# начислятся в казну"),
        }));
        inv.setItem(2, Item.create(Material.HOPPER, take, new String[]{
                rgb.gradientLight(chat.color[0], p.rank < 45 ? "Вы не можете забрать #скиты" : ""),
                rgb.gradientLight(chat.color[0], "После #нажатия# напишите сумму в чат"),
                rgb.gradientLight(chat.color[0], "#Cкиты# из казны выдадут вам"),
        }));
        inv.setItem(3, Item.create(Material.SUNFLOWER, rgb.gradientLight(chat.color[0], "Состояние#: " + chat.int_s(clan.getKazna()) + "# " + chat.getNormFormSkit(clan.getKazna()))));

        return inv;
    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(kazna))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(give))
                {
                    kazna_give(p);
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(take))
                {
                    kazna_take(p);
                    return ;
                }
            }
        }
    }

    private static void kazna_give(player p)
    {
        p.clickSound();
        p.pl.closeInventory();
        p.kazna_give = true;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Введите в чат #количество# скитов"), rgb.gradientLight(chat.color[0], "для отмены ввода отправьте слово #отмена"), 0, 20, 20);
                if (!p.kazna_give || p.getClan() == null)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
    }

    private static void kazna_take(player p)
    {
        p.clickSound();
        p.pl.closeInventory();
        p.kazna_take = true;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Введите в чат #количество# скитов"), rgb.gradientLight(chat.color[0], "для отмены ввода отправьте слово #отмена"), 0, 20, 20);
                if (!p.kazna_take || p.getClan() == null)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
    }

    public static void interSkits(player p, String message)
    {
        if (message.split(" ")[0].equals("отмена") || p.getClan() == null || p.rank < 45)
        {
            p.invite_in_clan = false;
            p.kazna_give = false;
            p.kazna_take = false;
            return ;
        }
        int summ = 0;
        try{
            summ = Integer.parseInt(message.split(" ")[0]);
        } catch(Exception ex){
            p.ErrorMessage("Это не число");
        }
        if (summ < 1)
        {
            p.ErrorMessage("Вы не можете совершить #операцию# с числом менее #1");
            return ;
        }
        if (p.kazna_give)
        {
            if (p.money < summ)
            {
                p.ErrorMessage("У вас #недостаточно# скитов#,# баланс можно посмотреть в #/me");
                return ;
            }
            p.Message("Вы перевели в казну клана #" + p.getClan().getName() + " #" + summ + "# " + chat.getNormFormSkit(summ));
            p.getClan().setKazna(p.getClan().getKazna() + summ);
            p.money -= summ;
            p.kazna_give = false;
            return ;
        }
        if (p.kazna_take)
        {
            if (p.getClan().getKazna() < summ)
            {
                p.ErrorMessage("В #казне# нет столько #скитов");
                return ;
            }
            p.Message("Вы взяли из казны фракции #" + p.getClan().getName() + " #" + summ + "# " + chat.getNormFormSkit(summ));
            p.getClan().setKazna(p.getClan().getKazna() - summ);
            p.money += summ;
            p.kazna_take = false;
            return ;
        }
    }
}
