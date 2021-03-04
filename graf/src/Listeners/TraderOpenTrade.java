package Listeners;

import Item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import own.chat;
import own.player;
import own.reg;
import own.timer;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TraderOpenTrade implements Listener
{

    private static List<Integer> ampList = Arrays.asList(200, 201, 202);

    @EventHandler
    private static void trade(InventoryOpenEvent e)
    {
        if (e.getInventory().getHolder() instanceof WanderingTrader && ((WanderingTrader) e.getInventory().getHolder()).isInvulnerable())
        {
            if (((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.DOLPHINS_GRACE) == null || ((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.DOLPHINS_GRACE).getAmplifier() != 200)
                return ;
            e.setCancelled(true);
            player p = player.getPlayer((Player) e.getPlayer());

            Location loc = e.getPlayer().getLocation().clone();
            loc.setY(loc.getY() - 50);

            WanderingTrader trader = getTrader(loc, "Валюта", 200);

            List<MerchantRecipe> list = new ArrayList<>();

            ItemStack item = Item.create(Material.SUNFLOWER, rgb.gradientLight(chat.color[0], "Покупка чанков"), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня")}, Enchantment.LURE, true);
            ItemStack result = Item.create(Material.GRASS_BLOCK, rgb.gradientLight(chat.color[0], "Покупка чанков"), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня")});

            MerchantRecipe recipe = new MerchantRecipe(result, 10);
            recipe.addIngredient(item);

            list.add(recipe);

            item = Item.create(Material.DIAMOND, rgb.gradientLight(chat.color[0], "Покупка скитов"), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня")});
            result = Item.create(Material.SUNFLOWER, rgb.gradientLight(chat.color[0], "Покупка скитов"), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня")}, Enchantment.LURE, true);

            recipe = new MerchantRecipe(result, 10);
            recipe.addIngredient(item);

            list.add(recipe);

            int price = p.blockRegs * reg.getPriceUnblockChunk(3);
            item = Item.create(Material.SUNFLOWER, rgb.gradientLight(chat.color[0], "Разблокировка чанков"), new String[]{rgb.gradientLight(chat.color[1], "Стоимость #" + price + "# " + chat.getNormFormSkit(price))});
            result = Item.create(Material.BARRIER, rgb.gradientLight(chat.color[0], "Разблокировка чанков"), new String[]{rgb.gradientLight(chat.color[1], "Стоимость #" + price + "# " + chat.getNormFormSkit(price))}, Enchantment.LURE, true);

            recipe = new MerchantRecipe(result, 10);
            recipe.addIngredient(item);

            list.add(recipe);

            trader.setRecipes(list);

            e.getPlayer().openMerchant(trader, true);
        }
    }

    @EventHandler
    private static void deleteTrader(InventoryCloseEvent e)
    {
        if (e.getInventory().getHolder() instanceof WanderingTrader)
        {
            if (((WanderingTrader) e.getInventory().getHolder()).isInvulnerable())
            {
                if (((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.INVISIBILITY) != null && ampList.contains(((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.INVISIBILITY).getAmplifier()))
                {
                    e.getPlayer().getInventory().addItem(e.getPlayer().getItemOnCursor());
                    e.getPlayer().getItemOnCursor().setAmount(0);
                    ((WanderingTrader) e.getInventory().getHolder()).remove();
                }
            }
        }
    }

    @EventHandler
    private static void select(TradeSelectEvent e)
    {
        if (e.getInventory().getHolder() instanceof WanderingTrader)
        {
            if (((WanderingTrader) e.getInventory().getHolder()).isInvulnerable() && ((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.INVISIBILITY) != null)
            {
                int ampInt = ((WanderingTrader) e.getInventory().getHolder()).getPotionEffect(PotionEffectType.INVISIBILITY).getAmplifier();
                player p = player.getPlayer((Player) e.getWhoClicked());
                if (ampInt == 200)
                {
                    if (e.getIndex() == 1)
                        sale_item(p);
                    if (e.getIndex() == 0)
                        pay_chunks(p);
                    if (e.getIndex() == 2)
                        unlock_chunks(p);
                } else if (ampInt == 201)
                {
                    if (e.getIndex() == 0)
                        deal(Material.NETHERITE_INGOT, 1, 60, p);
                    if (e.getIndex() == 1)
                        deal(Material.EMERALD, 1, 10, p);
                    if (e.getIndex() == 2)
                        deal(Material.DIAMOND, 1, 15, p);
                    if (e.getIndex() == 3)
                        deal(Material.IRON_INGOT, 1, 1, p);
                    if (e.getIndex() == 4)
                        deal(Material.GUNPOWDER, 1, 3, p);
                } else if (ampInt == 202)
                {
                    if (e.getIndex() == 0 && p.allowRegs < player.MaxRegsAllow)
                    {
                        int price = getNPrice(p, 1);
                        if (p.money < price)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает скитов для покупки чанка"));
                            return ;
                        }
                        p.allowRegs += 1;
                        p.money -= price;
                        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы потратили " + price + " скитов на покупку чанка\n") +
                                rgb.gradientLight(chat.color[0], "Теперь у вас " + p.allowRegs + " чанков и " + p.money + " скитов"));
                        pay_chunks(p);
                    }
                    if (e.getIndex() == 1 && p.allowRegs < player.MaxRegsAllow - 5)
                    {
                        int price = getNPrice(p, 5);
                        if (p.money < price)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает скитов для покупки чанков"));
                            return ;
                        }
                        p.allowRegs += 5;
                        p.money -= price;
                        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы потратили " + price + " скитов на покупку чанков\n") +
                                rgb.gradientLight(chat.color[0], "Теперь у вас " + p.allowRegs + " чанков и " + p.money + " скитов"));
                        pay_chunks(p);
                    }
                    if (e.getIndex() == 2 && p.allowRegs < player.MaxRegsAllow - 10)
                    {
                        int price = getNPrice(p, 10);
                        if (p.money < price)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает скитов для покупки чанков"));
                            return ;
                        }
                        p.allowRegs += 10;
                        p.money -= price;
                        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы потратили " + price + " скитов на покупку чанков\n") +
                                rgb.gradientLight(chat.color[0], "Теперь у вас " + p.allowRegs + " чанков и " + p.money + " скитов"));
                        pay_chunks(p);
                    }
                    if (e.getIndex() == 3 && p.allowRegs < player.MaxRegsAllow - 20)
                    {
                        int price = getNPrice(p, 20);
                        if (p.money < price)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает скитов для покупки чанков"));
                            return ;
                        }
                        p.allowRegs += 20;
                        p.money -= price;
                        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы потратили " + price + " скитов на покупку чанков\n") +
                                rgb.gradientLight(chat.color[0], "Теперь у вас " + p.allowRegs + " чанков и " + p.money + " скитов"));
                        pay_chunks(p);
                    }
                    if (e.getIndex() == 4 && p.allowRegs < player.MaxRegsAllow)
                    {
                        int price = getNPrice(p, player.MaxRegsAllow - p.allowRegs);
                        if (p.money < price)
                        {
                            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает скитов для покупки чанков"));
                            return ;
                        }
                        p.allowRegs = player.MaxRegsAllow;
                        p.money -= price;
                        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы потратили " + price + " скитов на покупку чанков\n") +
                                rgb.gradientLight(chat.color[0], "Теперь у вас " + p.allowRegs + " чанков и " + p.money + " скитов"));
                        pay_chunks(p);
                    }
                }
            }
        }
    }

    private static void unlock_chunks(player p)
    {
        if (p.blockRegs == 0)
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас нет #заблокированных# чанков"));
            return;
        }
        if (p.money < (p.blockRegs * reg.getPriceUnblockChunk(3)))
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вам не хватает #" +
                    ((p.blockRegs * reg.getPriceUnblockChunk(3)) - p.money) + "# " + chat.getNormFormSkit((p.blockRegs * reg.getPriceUnblockChunk(3)) - p.money)));
            return;
        }
        p.money -= (p.blockRegs * reg.getPriceUnblockChunk(3));
        p.blockRegs = 0;
        p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы разблокировали чанки"));
        p.pl.closeInventory();
    }

    private static void sale_item(player p)
    {
        Location loc = p.pl.getLocation().clone();
        loc.setY(loc.getY() - 50);

        WanderingTrader trader = getTrader(loc, "Продажа предметов", 201);

        List<MerchantRecipe> list = new ArrayList<>();

        list.add(new Entitys.trader("Незеритовый слиток", 1, 60, Material.NETHERITE_INGOT).getRecipe());
        list.add(new Entitys.trader("Изумруд", 1, 10, Material.EMERALD).getRecipe());
        list.add(new Entitys.trader("Алмаз", 1, 15, Material.DIAMOND).getRecipe());
        list.add(new Entitys.trader("Железо", 1, 1, Material.IRON_INGOT).getRecipe());
        list.add(new Entitys.trader("Порох", 1, 3, Material.GUNPOWDER).getRecipe());

        trader.setRecipes(list);

        p.pl.openMerchant(trader, true);
    }

    private static void pay_chunks(player p)
    {
        Location loc = p.pl.getLocation().clone();
        loc.setY(loc.getY() - 50);

        WanderingTrader trader = getTrader(loc, "Покупка чанков", 202);

        List<MerchantRecipe> list = new ArrayList<>();

        if (p.allowRegs < player.MaxRegsAllow)
            list.add(new Entitys.trader("чанк", 1, getNPrice(p, 1), Material.GRASS_BLOCK).getRecipeChunk());
        else
            list.add(new Entitys.trader("чанк", 1, 1, Material.BARRIER).getRecipeClose());

        if (p.allowRegs < player.MaxRegsAllow - 5)
            list.add(new Entitys.trader("чанков", 5, getNPrice(p, 5), Material.GRASS_BLOCK).getRecipeChunk());
        else
            list.add(new Entitys.trader("чанк", 1, 1, Material.BARRIER).getRecipeClose());

        if (p.allowRegs < player.MaxRegsAllow - 10)
            list.add(new Entitys.trader("чанков", 10, getNPrice(p, 10), Material.GRASS_BLOCK).getRecipeChunk());
        else
            list.add(new Entitys.trader("чанк", 1, 1, Material.BARRIER).getRecipeClose());

        if (p.allowRegs < player.MaxRegsAllow - 20)
            list.add(new Entitys.trader("чанков", 20, getNPrice(p, 20), Material.GRASS_BLOCK).getRecipeChunk());
        else
            list.add(new Entitys.trader("чанк", 1, 1, Material.BARRIER).getRecipeClose());

        if (p.allowRegs < player.MaxRegsAllow)
            list.add(new Entitys.trader("чанков", player.MaxRegsAllow - p.allowRegs, getNPrice(p, player.MaxRegsAllow - p.allowRegs), Material.GRASS_BLOCK).getRecipeChunk());
        else
            list.add(new Entitys.trader("чанк", 1, 1, Material.BARRIER).getRecipeClose());

        trader.setRecipes(list);

        p.pl.openMerchant(trader, true);
    }

    private static int getNPrice(player p,int x)
    {
        int count = 0;
        for (int i = 0; i < x; i++)
        {
            count += (int) (20 * (Math.pow(1.03630993, p.allowRegs + i - 9)));
        }
        return count;
    }

    private static WanderingTrader getTrader(Location loc, String name, int amtInt)
    {
        WanderingTrader trader = (WanderingTrader) loc.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
        trader.setAI(false);
        trader.setInvulnerable(true);
        trader.setGravity(false);
        trader.setCustomName(rgb.gradient(chat.color[0].getName(), chat.color[1].getName(), name));
        trader.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, amtInt, false, false));
        trader.setSilent(true);

        return trader;
    }

    public static String getNameSale(int items, String item, int price)
    {
        String str = rgb.gradientLight(chat.color[1], items + " ");
        str += rgb.gradientLight(chat.color[0], item);
        str += rgb.gradientLight(chat.color[1], " = " + price);
        str += rgb.gradientLight(chat.color[0], price % 10 == 1 ? " скит" : ((price % 10 == 2 || price % 10 == 3 || price % 10 == 4) ? " скита" : " скитов"));
        return str;
    }

    public static String getNamePay(int items, String item, int price)
    {
        String str = rgb.gradientLight(chat.color[1], price + "");
        str += rgb.gradientLight(chat.color[0], price % 10 == 1 ? " скит" : ((price % 10 == 2 || price % 10 == 3 || price % 10 == 4) ? " скита" : " скитов"));
        str += rgb.gradientLight(chat.color[1], " = " + items);
        str += rgb.gradientLight(chat.color[0], " " + item);
        return str;
    }

    public static void deal(Material material, int item_count, int price, player p)
    {
        if (p.pl.getItemOnCursor().getType().equals(material) && p.pl.getItemOnCursor().getAmount() >= item_count)
        {
            p.money += (p.pl.getItemOnCursor().getAmount() - (p.pl.getItemOnCursor().getAmount() % item_count)) * (price / item_count);
            p.sendMessage(rgb.gradientLight(chat.color[0], "Вы продали " + (p.pl.getItemOnCursor().getAmount() - (p.pl.getItemOnCursor().getAmount() % item_count)) + " предметов за " + (p.pl.getItemOnCursor().getAmount() - (p.pl.getItemOnCursor().getAmount() % item_count)) * (price / item_count) + " скитов"));
            p.pl.getItemOnCursor().setAmount(p.pl.getItemOnCursor().getAmount() % item_count);
            sale_item(p);
            return ;
        }
        int count = 0;
        for (ItemStack item : p.pl.getInventory())
        {
            if (item != null && item.getType().equals(material))
                count += item.getAmount();
        }
        if (count < item_count)
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "У вас не хватает предметов"));
            return ;
        }
        for (ItemStack item : p.pl.getInventory())
        {
            if (item != null && item.getType().equals(material))
            {
                if (item.getAmount() >= item_count)
                {
                    item.setAmount(item.getAmount() - item_count);
                    p.money += price;
                    p.sendMessage(rgb.gradientLight(chat.color[0], "Вы продали " + item_count + " предметов за " + price + " скитов"));
                    return ;
                }
            }
        }
        int item_count_copy = item_count;
        for (ItemStack item : p.pl.getInventory())
        {
            if (item_count != 0 && item != null && item.getType().equals(material))
            {
                if (item.getAmount() >= item_count)
                {
                    item.setAmount(item.getAmount() - item_count);
                    item_count = 0;
                }
                else
                {
                    item_count -= item.getAmount();
                    item.setAmount(0);
                }
            }
        }
        p.money += price;
        p.sendMessage(rgb.gradientLight(chat.color[0], "Вы продали " + item_count_copy + " предметов за " + price + " скитов"));
    }
}
