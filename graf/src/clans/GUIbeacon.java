package clans;

import Item.Item;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import own.chat;
import own.player;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.List;

public class GUIbeacon implements Listener
{
    final private static String beacon = chat.color[0] + "Настройка маяка";
    final private static String effects = chat.color[0] + "Настройка " + chat.color[1] + "эффектов";

    public static void Beacon(player p)
    {
        Inventory inv = update_inv(p, Bukkit.createInventory(null, 9, beacon));

        if (inv == null)
            return ;

        p.pl.openInventory(inv);
    }

    public static void BeaconEffects(player p)
    {
        Inventory inv = update_inv_effects(p, Bukkit.createInventory(null, 27, effects));

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
                rgb.gradientLight(chat.color[0], "Маяк действует на #всей# территории фракции"),
                rgb.gradientLight(chat.color[0], "#Эффект# наложится только на тех"),
                rgb.gradientLight(chat.color[0], "кто #состоит# в фракции или"),
                rgb.gradientLight(chat.color[0], "чей #регион# находится на #территории# фракции")
        }));
        if (clan.getBeakonStay())
        {
            inv.setItem(1, Item.create(Material.BLAZE_POWDER, effects, new String[]{
                    rgb.gradientLight(chat.color[0], "Тут #заместители# и #глава# могут"),
                    rgb.gradientLight(chat.color[0], "покупать, включать и выключать #эффекты")
            }));
            inv.setItem(4, Item.create(Material.BEACON, rgb.gradientLight(chat.color[0], "#Забрать# маяк")));
        }
        else
        {
            inv.setItem(4, Item.create(Material.BIRCH_SIGN, rgb.gradientLight(chat.color[0], "Установи маяк"), new String[]{
                    rgb.gradientLight(chat.color[0], "Для #установки# маяка нажми на"),
                    rgb.gradientLight(chat.color[0], "#один# маяк в своем инвентаре")
            }));
        }

        return inv;
    }

    public static Inventory update_inv_effects(player p, Inventory inv)
    {
        Clan clan = p.getClan();
        if (clan == null)
            return null;

        inv.clear();

        inv.setItem(0, Item.create(Material.LIME_CONCRETE, ChatColor.of("#00FF00") + "Включить все"));
        inv.setItem(inv.getSize() - 1, Item.create(Material.RED_CONCRETE, ChatColor.of("#FF0000") + "Выключить все"));

        int i = ((inv.getSize() - ClanEffects.Effects.length) / 2);
        for (ClanEffects effect : ClanEffects.Effects)
            inv.setItem(i++, Buttom(clan, effect));

        return inv;
    }

    private static ItemStack Buttom(Clan clan, ClanEffects patern)
    {
        if (clan.getEffectsIsOn(patern.effect_id))
        {
            return Item.create(patern.item, patern.name, new String[]{
                    "",
                    rgb.gradientLight(ChatColor.of("#00FF00"), "Включен"),
                    "",
                    rgb.gradientLight(chat.color[0], "Уровень: #" + clan.getEffectsLvl(patern.effect_id)),
                    "",
                    rgb.gradientLight(chat.color[0], "Цена работы в час: #" + patern.price_tick * 14.4 * clan.getEffectsLvl(patern.effect_id) + "# скитов за #1# игрока"),
                    rgb.gradientLight(chat.color[0], "если он получает эффект"),
                    "",
                    clan.getEffectsLvl(patern.effect_id) == patern.max_lvl
                            ?
                            rgb.gradientLight(chat.color[0], "Максимальный #уровень")
                            :
                            rgb.gradientLight(chat.color[0], "Поднять уровень: #" + chat.int_s(patern.price[clan.getEffectsLvl(patern.effect_id)]) + "# " + chat.getNormFormSkit(patern.price[clan.getEffectsLvl(patern.effect_id)])),
                    "",
                    rgb.gradientLight(chat.color[0], "#ЛКМ#: #-# Выключить"),
                    clan.getEffectsLvl(patern.effect_id) == patern.max_lvl
                            ?
                            ""
                            :
                            rgb.gradientLight(chat.color[0], "#ПКМ#: #-# Повысить уровень"),
            }, Enchantment.LURE,true);
        }
        else
        {
            return Item.create(patern.item, patern.name, new String[]{
                    "",
                    rgb.gradientLight(ChatColor.of("#FF0000"), "Выключен"),
                    "",
                    rgb.gradientLight(chat.color[0], "Уровень: #" + clan.getEffectsLvl(patern.effect_id)),
                    "",
                    rgb.gradientLight(chat.color[0], "Цена работы в час: #" + patern.price_tick * 14.4 * clan.getEffectsLvl(patern.effect_id) + "# скитов за #1# игрока"),
                    rgb.gradientLight(chat.color[0], "если он получает эффект"),
                    "",
                    clan.getEffectsLvl(patern.effect_id) == patern.max_lvl
                            ?
                            rgb.gradientLight(chat.color[0], "Максимальный #уровень")
                            :
                            rgb.gradientLight(chat.color[0], "Поднять уровень: #" + chat.int_s(patern.price[clan.getEffectsLvl(patern.effect_id)]) + "# " + chat.getNormFormSkit(patern.price[clan.getEffectsLvl(patern.effect_id)])),
                    "",
                    rgb.gradientLight(chat.color[0], "#ЛКМ#: #-# Включить"),
                    clan.getEffectsLvl(patern.effect_id) == patern.max_lvl
                            ?
                            ""
                            :
                            rgb.gradientLight(chat.color[0], "#ПКМ#: #-# Повысить уровень"),
            }, true);
        }
    }

    @EventHandler
    private static void click(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(beacon))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType().equals(Material.BEACON) && p.getClan().getOwner().p.equals(p.p) && e.getCurrentItem().getAmount() == 1 && e.getCurrentItem().getItemMeta().getLore() == null)
                {
                    if (e.getClickedInventory().getHolder() != null)
                    {
                        if (p.getClan().getBeakonStay())
                            return ;
                        p.getClan().setBeakonStay(true);
                        update_inv(p, p.pl.getOpenInventory().getTopInventory());
                        e.getCurrentItem().setAmount(0);
                    }
                    else
                    {
                        if (!p.getClan().getBeakonStay())
                            return ;
                        p.getClan().setBeakonStay(false);
                        update_inv(p, p.pl.getOpenInventory().getTopInventory());
                        p.pl.getOpenInventory().getBottomInventory().addItem(Item.create(Material.BEACON, 1));
                    }
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(effects))
                {
                    BeaconEffects(p);
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(effects))
        {
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getWhoClicked());
            if (e.getCurrentItem() != null)
            {
                Clan clan = p.getClan();
                if (clan == null || p.rank < 45)
                    return ;
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#00FF00") + "Включить все"))
                {
                    for (ClanEffects effect : ClanEffects.Effects)
                    {
                        if (clan.getEffectsLvl(effect.effect_id) != 0)
                            clan.setEffectIsOn(effect.effect_id, true);
                    }
                    for (player member : clan.getAll())
                    {
                        if (member.pl.getOpenInventory().getTitle().equalsIgnoreCase(effects))
                            update_inv_effects(member, member.pl.getOpenInventory().getTopInventory());
                    }
                    return ;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#FF0000") + "Выключить все"))
                {
                    for (ClanEffects effect : ClanEffects.Effects)
                    {
                        if (clan.getEffectsLvl(effect.effect_id) != 0)
                            clan.setEffectIsOn(effect.effect_id, false);
                    }
                    for (player member : clan.getAll())
                    {
                        if (member.pl.getOpenInventory().getTitle().equalsIgnoreCase(effects))
                            update_inv_effects(member, member.pl.getOpenInventory().getTopInventory());
                    }
                    return ;
                }
                for (ClanEffects effect : ClanEffects.Effects)
                    click_effect(e, clan, p, effect);
            }
        }
    }

    public static void click_effect(InventoryClickEvent e, Clan clan, player p, ClanEffects patern)
    {
        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(patern.name))
        {
            if (e.getAction().equals(InventoryAction.PICKUP_ALL))
            {
                p.clickSound();
                if (clan.getEffectsIsOn(patern.effect_id))
                {
                    clan.setEffectIsOn(patern.effect_id, false);
                    for (player member : clan.getAll())
                    {
                        if (member.pl.getOpenInventory().getTitle().equalsIgnoreCase(effects))
                            update_inv_effects(member, member.pl.getOpenInventory().getTopInventory());
                    }
                    return ;
                }
                else
                {
                    if (clan.getEffectsLvl(patern.effect_id) == 0)
                    {
                        p.ErrorMessage("Эффект #недоступен");
                        return ;
                    }
                    clan.setEffectIsOn(patern.effect_id, true);
                    for (player member : clan.getAll())
                    {
                        if (member.pl.getOpenInventory().getTitle().equalsIgnoreCase(effects))
                            update_inv_effects(member, member.pl.getOpenInventory().getTopInventory());
                    }
                    return ;
                }
            }
            if (e.getAction().equals(InventoryAction.PICKUP_HALF))
            {
                p.clickSound();
                if (patern.max_lvl == clan.getEffectsLvl(patern.effect_id))
                {
                    p.ErrorMessage("Уже #максимальный# уровень");
                    return ;
                }
                int price = patern.price[clan.getEffectsLvl(patern.effect_id)];
                if (p.money < price)
                {
                    p.ErrorMessage("У вас недостаточно скитов для покупки");
                    return ;
                }
                p.money -= price;
                clan.setEffectsLvl(patern.effect_id, (byte) (clan.getEffectsLvl(patern.effect_id) + 1));
                for (player member : clan.getAll())
                {
                    if (member.pl.getOpenInventory().getTitle().equalsIgnoreCase(effects))
                        update_inv_effects(member, member.pl.getOpenInventory().getTopInventory());
                }
                p.Message("Вы улучшели эффект# " + patern.name);
            }
        }
    }
}