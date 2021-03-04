package clans;

import Contest.GUIcontest;
import Item.Item;
import commands.message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.graf;
import own.player;
import own.reg;
import railway_system.road;
import text_processing.rgb;
import text_processing.rgb_map;
import text_processing.time;

import java.util.*;

public class GUI implements Listener
{
    private static String   info_clan = chat.color[0] + "Информация о фракциях";
    private static String   clans_list = chat.color[0] + "Список фракций";
    private static String   create_clan_but = chat.color[0] + "Создать фракцию";
    private static String   you_clan = chat.color[0] + "Ваша фракция";

    private static String   create_clan = chat.color[0] + "Создание фракции";
    private static String   new_name = chat.color[0] + "Придумать название";
    private static String   change_name = chat.color[0] + "Поменять название";
    private static String   create = chat.color[0] + "Подтвердить создание";

    private static String   new_color = chat.color[0] + "Выбрать цвет";
    private static String   change_color = chat.color[0] + "Поменять цвет";

    private static String   center = chat.color[0] + "Центер фракции";

    private static String   delete_clan = chat.color[1] + "Удалить Фракцию";
    private static String   leave_clan = chat.color[1] + "Покинуть Фракцию";
    private static String   infl = chat.color[0] + "Очки влияния";
    private static String   members = chat.color[0] + "Состав фракции";
    private static String   member = chat.color[0] + "Фракционер";
    private static String   invite_member = chat.color[0] + "Пригласить игрока";
    private static String   beacon = chat.color[0] + "Маяк";
    private static String   kazna = chat.color[0] + "Казна";
    private static String   contest = chat.color[0] + "Состязания";

    public String   name = "";
    public boolean  inter = false;
    public byte     color = 0;

    public static boolean infoClan(player p, String []args)
    {
        if (args.length != 0)
        {
            if (args.length == 2)
            {
                if (args[1].equals("yes"))
                {
                    if (p.getClan() != null)
                    {
                        p.sendMessageE(rgb.gradientLight(chat.color[0], "Вы уже состоите в фракции"));
                        return true;
                    }
                    if (Clan.getClan(args[0]) == null)
                    {
                        p.sendMessageE(rgb.gradientLight(chat.color[0], "Фракции не существует"));
                        return true;
                    }
                    for (String s : Clan.getClan(args[0]).getInvitePlayers())
                    {
                        p.sendMessageE(s);
                    }
                    if (Clan.getClan(args[0]) != null)
                    {
                        if (Clan.getClan(args[0]).getInvitePlayers().contains(p.p))
                        {
                            Clan.getClan(args[0]).addMember(p);
                        }
                        else
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вас #не приглашали# или время приглашения #истекло"));
                    }
                    return true;
                }
                if (args[1].equals("no"))
                {
                    return true;
                }
            }
            try {
                interColor(p, Byte.parseByte(args[0]));
            }catch(Exception ex){return true;}
            return true;
        }
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, info_clan);

        if (p.clan_uid == null)
            inv.setItem(2, Item.create(Material.LECTERN, create_clan_but, new String[]{
                  rgb.gradientLight(chat.color[1], "Создание стоит #" + Clan.price + "# " + chat.getNormFormSkit(Clan.price))
            }));
        else
            inv.setItem(2, Item.create(Material.LECTERN, you_clan + " " + p.getClan().getChatColor() + ChatColor.BOLD + p.getClan().getName(), new String[]{
                    rgb.gradientLight(chat.color[1], "#" + (p.getClan().getMembers().size() + 1) + "# " + chat.getNormForm(p.getClan().getMembers().size() + 1, "участник", "участника", "участников")),
                    rgb.gradientLight(chat.color[1], "Центер: X: #" + p.getClan().getCenter().getBlockX() + " #Z:# " + p.getClan().getCenter().getBlockZ()),
                    }, Enchantment.LURE, true));

        inv.setItem(0, Item.create(Material.WRITTEN_BOOK, clans_list));

        p.clickSound();
        p.pl.openInventory(inv);
        return true;
    }

    public static boolean Clans(player p, int list)
    {
        Inventory inv = Bukkit.createInventory(null, 54, clans_list + " страница " + list);

        inv.setItem(52, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Предыдущая страница")));

        inv.setItem(53, Item.create(Material.PAPER, rgb.gradientLight(chat.color[1], "Следующая страница")));

        List<Clan> clans = new ArrayList<>();
        for (String uid : graf.Clans.getKeys(false))
        {
            Clan clan = Clan.getClan(uid);
            if (clan.getOwner() != null)
                clans.add(clan);
        }
        clans.sort((a, b) -> (int) (b.getInfl() - a.getInfl()));

        for (int i = 0; i < (Math.min(52, clans.size())); i++) {
            Clan clan = clans.get(i);

            ItemStack head = Item.create(Material.PLAYER_HEAD,
                    clan.getName(),
                    new String[]{
                            rgb.gradientLight(chat.color[1], "Очков влияния #" + clan.getInfl()),
                            rgb.gradientLight(chat.color[1], "Глава #" + clan.getOwner().p),
                            rgb.gradientLight(chat.color[1], "Центер: X: #" + clan.getCenter().getBlockX() + " #Z:# " + clan.getCenter().getBlockZ()),
                            rgb.gradientLight(chat.color[1], "Создана #" + time.getTime(new Date().getTime(), clan.getTimeReg()) + "# назад"),
                    }
            );
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            List<String> lore = meta.getLore();

            String members = "";
            List<String> members_list = clan.getMembers();
            int list_lore = 0;

            for (int m = 0; m < clan.getMembers().size(); m++)
            {
                members += members_list.get(m) + ((m == members_list.size() - 1) ? "" : "#,# ");
                if (m % 4 == 3)
                {
                    if (list_lore++ == 1)
                        lore.add(rgb.gradientLight(chat.color[1], "Участники #" + members));
                    else
                        lore.add(rgb.gradientLight(chat.color[1], "#" + members));
                    members = "";
                }
            }
            if (!members.equalsIgnoreCase(""))
                lore.add(rgb.gradientLight(chat.color[1], "                #" + members));

            meta.setLore(lore);
            meta.setOwningPlayer(clan.getOwner().pl);
            head.setItemMeta(meta);

            inv.setItem(i, head);
        }

        p.pl.openInventory(inv);

        return true;
    }

    private static void createClan(player p)
    {
        if (!(p.clan_uid == null || p.clan_uid.equals("null")))
        {
            p.ErrorMessage("Вы уже состоите в #фракции");
            p.pl.closeInventory();
            return ;
        }
        reg region = reg.getReg(p.pl.getLocation());
        if (region == null || !region.isExist() || !region.owner.equals(p.p))
        {
            p.ErrorMessage("Вы должны владеть #чанком# в котором #находитесь");
            p.pl.closeInventory();
            return ;
        }
        if (!Clan.isCanCreateClan(
                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockX(),
                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockZ()
        ))
        {
            p.ErrorMessage("Фракция не может пересекать чужую фракцию\nОтойдите минимум на #12# блоков от границ другой фракции");
            p.pl.closeInventory();
            return ;
        }
        if (p.clan_gui == null)
            p.clan_gui = new GUI();

        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, create_clan);

        if (p.clan_gui.name.length() == 0 || p.clan_gui.color == 0 || p.money < Clan.price)
        {
            ItemStack item = Item.create(Material.RED_CONCRETE, create, new String[]{
                    (p.clan_gui.name.length() == 0 ? rgb.gradientLight(chat.color[1], "Вы не назвали #фракцию") : ""),
                    (p.clan_gui.color == 0 ? rgb.gradientLight(chat.color[1], "Вы не выбрали #цвет") : ""),
                    (p.money < Clan.price ? rgb.gradientLight(chat.color[1], "У вас #недостаточно# скитов") : "")
            });
            inv.setItem(0, item);
            inv.setItem(4, item);
        }
        else
        {
            ItemStack item = Item.create(Material.LIME_CONCRETE, create, new String[]{
                    rgb.gradientLight(chat.color[1], "Нажмите для #создания# фракции")
            }, Enchantment.LURE, true);
            inv.setItem(0, item);
            inv.setItem(4, item);
        }

        if (p.clan_gui.name.length() == 0)
            inv.setItem(1, Item.create(Material.CRIMSON_SIGN, new_name, new String[]{
                    rgb.gradientLight(chat.color[1], "После создания фракции"),
                    rgb.gradientLight(chat.color[1], "Название изменить #нельзя")
            }));
        else
            inv.setItem(1, Item.create(Material.WARPED_SIGN, change_name, new String[]{rgb.gradientLight(chat.color[1], "Название фракции #" + p.clan_gui.name)}, Enchantment.LURE, true));

        if (p.clan_gui.color == 0)
            inv.setItem(2, Item.create(Material.PAINTING, new_color, new String[]{
                    rgb.gradientLight(chat.color[1], "После создания фракции"),
                    rgb.gradientLight(chat.color[1], "Цвет изменить #нельзя")
            }));
        else
            inv.setItem(2, Item.create(Material.PAINTING, change_color, new String[]{
                    rgb.gradientLight(chat.color[1], "Цвет фракции #" + p.clan_gui.name),
                    rgb_map.byte_to_rgb.get(p.clan_gui.color) + "⬛⬛⬛⬛",
                    rgb_map.byte_to_rgb.get(p.clan_gui.color) + "⬛⬛⬛⬛",
                    rgb_map.byte_to_rgb.get(p.clan_gui.color) + "⬛⬛⬛⬛",
                    rgb_map.byte_to_rgb.get(p.clan_gui.color) + "⬛⬛⬛⬛",
            }, Enchantment.LURE, true));

        inv.setItem(3, Item.create(Material.NETHER_STAR, center, new String[]{
                rgb.gradientLight(chat.color[1], "X: #" + p.pl.getLocation().getChunk().getBlock(8, 1, 8).getLocation().getBlockX() + " #Z:# " + p.pl.getLocation().getChunk().getBlock(8, 1, 8).getLocation().getBlockZ()),
                rgb.gradientLight(chat.color[1], "Для изменения центра фракции"),
                rgb.gradientLight(chat.color[1], "#перейдите# в другой #чанк")
        }));

        p.clickSound();
        p.pl.openInventory(inv);
    }

    public static boolean youClan(player p, Clan clan)
    {
        Inventory inv = Bukkit.createInventory(null, 27, you_clan  + " " + p.getClan().getChatColor() + ChatColor.BOLD + p.getClan().getName());

        int i = ((inv.getSize() - 5) / 2);
        inv.setItem(i++, Item.create(Material.ENDER_EYE, infl, new String[]{
                rgb.gradientLight(chat.color[1], "У фракции #" + clan.getInfl() + "# очков влияния"),
                rgb.gradientLight(chat.color[1], "#Очки влияния# влияют на размер #фракции"),
                rgb.gradientLight(chat.color[1], "Радиус территории фракции #" + clan.getRadius())
        }));
        inv.setItem(i++, Item.create(Material.TOTEM_OF_UNDYING, members));

        if (p.getClan().isExist() && p.getClan().uid.equals(clan.uid) && p.clanCanInviteMember())
            inv.setItem(i++, Item.create(Material.PAPER, invite_member));

        inv.setItem(i++, Item.create(Material.BEACON, beacon, new String[]{
                rgb.gradientLight(chat.color[0], "Дает #эффекты# на всей территории")
        }));

        inv.setItem(i++, Item.create(Material.CHEST, kazna, new String[]{
                rgb.gradientLight(chat.color[0], "Состояние#: " + chat.int_s(clan.getKazna()) + "# " + chat.getNormFormSkit(clan.getKazna()))
        }));

        inv.setItem(i++, Item.create(Material.SPECTRAL_ARROW, contest, new String[]{
                rgb.gradientLight(chat.color[0], "Скоро#...")
        }));

        if (p.getClan().isExist() && p.getClan().uid.equals(clan.uid) && p.getClan().getOwner().equals(p))
            inv.setItem(inv.getSize() - 1, Item.create(Material.RED_CONCRETE, delete_clan));
        else if (p.getClan().isExist() && p.getClan().uid.equals(clan.uid))
            inv.setItem(inv.getSize() - 1, Item.create(Material.RED_CONCRETE, leave_clan));

        p.clickSound();
        p.pl.openInventory(inv);
        return true;
    }

    public static boolean ClanMembers(player p, int list)
    {
        Inventory inv = Bukkit.createInventory(null, 54, members + " страница " + list);

        inv.setItem(52, Item.create(Material.PAPER, rgb.gradientLight(chat.color[0], "Предыдущая страница")));

        inv.setItem(53, Item.create(Material.PAPER, rgb.gradientLight(chat.color[1], "Следующая страница")));

        List<String> members = p.getClan().getMembers();
        members.add(p.getClan().getOwner().p);
        members.sort((a, b) ->
                        (player.getPlayer(b).rank - player.getPlayer(a).rank)
        );

        for (int i = 0; i < (Math.min(52, members.size())); i++)
        {
            player member = player.getPlayer(members.get(i));
            ItemStack head = Item.create(Material.PLAYER_HEAD,
                    member.p + " " + (member.pl != null && member.pl.isOnline() ? (ChatColor.of("#22ff00") + " в игре") : (ChatColor.of("#929292") + " не в игре")),
                    new String[]{
                            rgb.gradientLight(Clan.getColorRank(member.rank), Clan.chat_rank + " " + member.getRank()),
                            rgb.gradientLight(chat.color[1], "Состоит в фракции #" + time.getTime(new Date().getTime(), member.time_clan_include)),
                            !p.clanCanModer(member) ? "" : rgb.gradientLight(chat.color[1], "Выгнать#/#Повысить игрока")
                    }
                    );
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(member.pl);
            head.setItemMeta(meta);

            inv.setItem(i, head);
        }

        p.pl.openInventory(inv);

        return true;
    }

    public static boolean Member(player p, player member)
    {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, GUI.member + " " + member.p);

        inv.setItem(0, Item.create(Material.SPRUCE_DOOR, "Выгнать игрока " + member.p));
        inv.setItem(1, Item.create(Material.PISTON, "Выдать роль игроку " + member.p));

        p.pl.openInventory(inv);

        return true;
    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(info_clan))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(create_clan_but))
                {
                    createClan(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].equalsIgnoreCase(you_clan.split(" ")[0]) &&
                        e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equalsIgnoreCase(you_clan.split(" ")[1]))
                {
                    youClan(player.getPlayer((Player) e.getWhoClicked()), player.getPlayer((Player) e.getWhoClicked()).getClan());
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(clans_list))
                {
                    Clans(player.getPlayer((Player) e.getWhoClicked()), 1);
                    return ;
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0].equalsIgnoreCase(clans_list.split(" ")[0]) &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[1].equalsIgnoreCase(clans_list.split(" ")[1]))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(create_clan_but))
                {
                    createClan(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(create_clan))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(new_name) || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(change_name))
                {
                    changeName(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(new_color) || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(change_color))
                {
                    changeColor(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(create) && e.getCurrentItem().getType().equals(Material.LIME_CONCRETE))
                {
                    player p = player.getPlayer((Player) e.getWhoClicked());
                    Clan.new_clan(p.clan_gui.name, p.clan_gui.color, p);
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0].equalsIgnoreCase(you_clan.split(" ")[0]) &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[1].equalsIgnoreCase(you_clan.split(" ")[1]))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(delete_clan))
                {
                    player.getPlayer((Player) e.getWhoClicked()).getClan().deleteClan();
                    e.getWhoClicked().closeInventory();
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(leave_clan))
                {
                    player.getPlayer((Player) e.getWhoClicked()).getClan().removeMember(player.getPlayer((Player) e.getWhoClicked()));
                    e.getWhoClicked().closeInventory();
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(members))
                {
                    ClanMembers(player.getPlayer((Player) e.getWhoClicked()), 1);
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(invite_member))
                {
                    inviteMember(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(beacon))
                {
                    GUIbeacon.Beacon(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(kazna))
                {
                    GUIKazna.Kazna(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(contest))
                {
                    GUIcontest.Contest(player.getPlayer((Player) e.getWhoClicked()));
                    return ;
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ").length > 2 &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0].equalsIgnoreCase(members.split(" ")[0]) &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[1].equalsIgnoreCase(members.split(" ")[1]))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                for (String mem : p.getClan().getMembers())
                {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].equalsIgnoreCase(mem))
                    {
                        player member = player.getPlayer(mem);
                        if (p.clanCanModer(member))
                        {
                            Member(p, member);
                        }
                    }
                }
            }
        }

        if (e.getWhoClicked().getOpenInventory().getTitle().split(" ").length == 2 &&
                e.getWhoClicked().getOpenInventory().getTitle().split(" ")[0].equalsIgnoreCase(member))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].equalsIgnoreCase("Выгнать"))
                {
                    player member = player.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[2]);
                    if (member != null)
                        p.getClan().removeMember(member);
                    p.pl.closeInventory();
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].equalsIgnoreCase("Выдать"))
                {
                    player member = player.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[3]);
                    GUInewRank.newRank(p, member);
                }
            }
        }
    }

    private static void changeName(player p)
    {
        p.clickSound();
        p.pl.closeInventory();
        p.clan_gui.inter = true;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Введите в чат #название #Фракции"), null, 0, 20, 20);
                if (p.clan_gui == null || !p.clan_gui.inter)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
    }

    private static void inviteMember(player p)
    {
        p.clickSound();
        p.pl.closeInventory();
        p.invite_in_clan = true;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Введите в чат #имя #игрока"), rgb.gradientLight(chat.color[0], "для отмены ввода отправьте слово #отмена"), 0, 20, 20);
                if (!p.invite_in_clan)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
    }

    private static void changeColor(player p)
    {
        p.clickSound();
        p.pl.closeInventory();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.pl.sendTitle(rgb.gradientLight(chat.color[0], "Выберите #цвет #фракции"), null, 0, 20, 20);
                if (p.clan_gui == null || p.clan_gui.color != 0)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
        ComponentBuilder str[] = new ComponentBuilder[]{new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),
                new ComponentBuilder(""),};
        ArrayList<ArrayList<Byte>> sort_color = new ArrayList<>();
        int i = 0;
        int j = 0;
        for (byte b : rgb_map.sort_byte)
        {
            ArrayList<Byte> list = new ArrayList<>();
            if (i++ % 2 == 0)
            {
                sort_color.add(list);
                list.add(b++);
                list.add(b++);
                list.add(b++);
                list.add(b);
            }
            else
            {
                b += 3;
                sort_color.get(j).add(b--);
                sort_color.get(j).add(b--);
                sort_color.get(j).add(b--);
                sort_color.get(j).add(b);
                j++;
            }
        }
        for (ArrayList<Byte> list : sort_color)
        {
            list.sort((a, b) -> {


                int[] col_a = rgb.get_rgb(rgb_map.byte_to_rgb.get(a).getName());
                int[] col_b = rgb.get_rgb(rgb_map.byte_to_rgb.get(b).getName());

                float hsv_a[] = new float[3];
                java.awt.Color.RGBtoHSB(col_a[0], col_a[1], col_a[2], hsv_a);

                float hsv_b[] = new float[3];
                java.awt.Color.RGBtoHSB(col_b[0], col_b[1], col_b[2], hsv_b);

                return (int) (hsv_b[2] * 50.0 - hsv_a[2] * 50.0);
            });
        }
        for (ArrayList<Byte> list : sort_color)
        {
            j = 0;
            for (byte b : list)
            {
                str[j++].append("⬛").color(rgb_map.byte_to_rgb.get(b)
                ).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("⬛⬛⬛⬛\n⬛⬛⬛⬛\n⬛⬛⬛⬛\n⬛⬛⬛⬛ " + b).color(rgb_map.byte_to_rgb.get(b)
                        ).create())
                ).event(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fraction " + (int)b)
                ).create();
            }
        }
        p.pl.spigot().sendMessage(rgb.sendMessageColor(chat.color[0], "Выберите #понравившийся #цвет для фракции #" + p.clan_gui.name, null, null).create());
        p.pl.spigot().sendMessage(rgb.sendMessageColor(chat.color[0], "Просто нажми на нужный #цвет", null, null).create());

        for (ComponentBuilder line : str)
            p.pl.spigot().sendMessage(line.create());
    }

    public static void invitePlayer(player p, String name)
    {
        if (name.split(" ")[0].equals("отмена"))
        {
            p.invite_in_clan = false;
            return ;
        }
        Clan clan = p.getClan();
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (pl.getName().equals(name.split(" ")[0]))
            {
                if (clan.getMembers().contains(pl.getName()) || clan.getOwner().p.equals(pl.getName()))
                {
                    p.sendMessageE(rgb.gradientLight(chat.color[0], "Игрок уже в фракции"));
                    return ;
                }
                if (clan.getInvitePlayers().contains(pl.getName()))
                {
                    p.sendMessageE(rgb.gradientLight(chat.color[0], "Этого #игрока# уже пригласили"));
                    return ;
                }
                p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы пригласили в клан игрока #" + name));
                if (!(clan.getInvitePlayers().contains(pl.getName())))
                {
                    List<String> inv_players = clan.getInvitePlayers();
                    inv_players.add(pl.getName());
                    clan.setInvitePlayers(inv_players);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if (Clan.getClan(clan.uid) != null)
                            {
                                List<String> inv_players = clan.getInvitePlayers();
                                inv_players.remove(pl.getName());
                                clan.setInvitePlayers(inv_players);
                            }
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 20 * 30);
                }

                p.invite_in_clan = false;

                ComponentBuilder m = new ComponentBuilder("Да");
                m.color(ChatColor.GREEN);
                m.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/fraction " + clan.uid + " yes"));
                m.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Нажми для подтверждения").color(ChatColor.GREEN).create()));
                pl.spigot().sendMessage(new ComponentBuilder("Вас пригласили в фракцию " + clan.getName() + ", принять приглашение? ").color(chat.color[0]).append(m.create()).append(chat.squareC.create()).create());

                return ;
            }
        }
        p.ErrorMessage("Этого #игрока# нет в игре, для отмены ввода отправьте слово #отмена");
    }

    public static void interName(player p, String name)
    {
        name = name.split(" ")[0];
        if (name.length() > 6)
        {
            p.ErrorMessage("Имя не может быть длинее #6# символов");
            return ;
        }
        if (!Clan.isFreeName(name))
        {
            p.ErrorMessage("Это имя занято");
            return ;
        }
        p.clan_gui.name = name;
        p.clan_gui.inter = false;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                createClan(p);
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 1);
    }

    public static void interColor(player p, byte color)
    {
        if (p.clan_gui == null)
            return ;
        p.clan_gui.color = color;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                createClan(p);
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 1);
    }
}
