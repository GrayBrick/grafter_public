package Contest;

import clans.Clan;
import commands.message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.Objects;

public class Contest_pvp_1_1 extends Contest
{
    final public static String choseParam = chat.color[0] + "Настройка схватки";

    public static ArrayList<player> que_players = new ArrayList<>();

    public static void add_player_que(player p)
    {
        if (p.pl == null)
            return ;
        if (p.inArena)
        {
            p.ErrorMessage("Ты на #арене");
            return ;
        }
        if (p.queueArena)
        {
            p.ErrorMessage("Мы уже #ищем# тебе соперника");
            return ;
        }
        que_players.add(p);
        p.queueArena = true;
        if (que_players.size() == 0)
        {
            int []count = new int[]{0};
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!p.queueArena)
                        cancel();
                    count[0]++;
                    if (count[0] % 4 == 0)
                        p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Мы ищем вам соперника"), rgb.gradientLight(chat.color[0], "Для отмены поиска напиши #отмена"), 0, 20, 20);
                    if (count[0] % 4 == 1)
                        p.pl.sendTitle(rgb.gradientLight(chat.color[0], ".Мы ищем вам соперника."), rgb.gradientLight(chat.color[0], "Для отмены поиска напиши #отмена"), 0, 20, 20);
                    if (count[0] % 4 == 2)
                        p.pl.sendTitle(rgb.gradientLight(chat.color[0], "..Мы ищем вам соперника.."), rgb.gradientLight(chat.color[0], "Для отмены поиска напиши #отмена"), 0, 20, 20);
                    if (count[0] % 4 == 3)
                        p.pl.sendTitle(rgb.gradientLight(chat.color[0], "...Мы ищем вам соперника..."), rgb.gradientLight(chat.color[0], "Для отмены поиска напиши #отмена"), 0, 20, 20);
                }
            }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
            return ;
        }
    }

//    public static void ChuseParam(player p1, player p2)
//    {
//        Inventory inv = update_inv(p1, p2, Bukkit.createInventory(null, 27, choseParam));
//
//        if (inv == null)
//            return ;
//
//        p1.pl.openInventory(inv);
//        p2.pl.openInventory(inv);
//    }

//    public static Inventory update_inv(player p1, player p2, Inventory inv)
//    {
//        inv.clear();
//
//        inv.setItem(0, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Принцип #работы"), new String[]{
//                rgb.gradientLight(chat.color[0], "Скоро#...")
//        }));
//
//        inv.setItem(10, Item.create(Material.IRON_SWORD, pvp_1_1, new String[]{
//                rgb.gradientLight(chat.color[0], "Сражение #1# на #1"),
//                rgb.gradientLight(chat.color[0], "Вы будите сражаться в #равных# условиях или в #своих вещах"),
//                "",
//                rgb.gradientLight(chat.color[0], "Если хоть один участник выберет сражение в #равных условиях"),
//                rgb.gradientLight(chat.color[0], "Сражение будет происходить с #одинаковым# снаряжением"),
//                rgb.gradientLight(chat.color[0], "Вещи из #текущего# инвентаря будут #сохранены"),
//                "",
//                rgb.gradientLight(chat.color[0], "Если #оба# участника выберут сражение в #своих вещах"),
//                rgb.gradientLight(chat.color[0], "То на арену вы выйдите в #своем снаряжении"),
//                rgb.gradientLight(chat.color[0], "и после смерти #потеряете его"),
//                "",
//                rgb.gradientLight(chat.color[0], "Если сражение происходит против участника из #другой# фракции"),
//                rgb.gradientLight(chat.color[0], "То за победу в сражении вы заработаете"),
//                rgb.gradientLight(chat.color[0], "#очки влияния# для своей фракции"),
//                rgb.gradientLight(chat.color[0], "При #проигрыше# потери очков не будет"),
//                "",
//                rgb.gradientLight(chat.color[0], "Так же вы можете сделать ставку на свою победу"),
//                rgb.gradientLight(chat.color[0], "Если оба участника сделали ставку то за победу"),
//                rgb.gradientLight(chat.color[0], "Вы получите #выигрыш"),
//        }, true));
//
//        return inv;
//    }
}
