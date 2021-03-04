package Listeners;

import commands.set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import own.chat;
import own.graf;
import own.player;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static commands.set.armor_new;

public class NameTag implements Listener
{
    @EventHandler
    public void pickup(EntityPickupItemEvent e)
    {
        if (e.getItem().isInvulnerable())
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void stack(ItemMergeEvent e)
    {
        if (e.getEntity().isInvulnerable() || e.getTarget().isInvulnerable())
        {
            e.setCancelled(true);
        }
    }

    public static void setNameTag(Location loc, String name)
    {
        if (!loc.getChunk().isLoaded())
            return ;
        ItemStack drop_item = graf.config.getItemStack("dont_pickup." + name + ".item");
        Item item = null;
        if (!drop_item.getType().equals(Material.AIR))
            item = Objects.requireNonNull(loc.getWorld()).dropItem(loc, drop_item);
        else
            item = Objects.requireNonNull(loc.getWorld()).dropItem(loc, new ItemStack(Material.ENDER_PEARL, 1));
        item.setGravity(false);
        item.setCustomNameVisible(true);
        item.setInvulnerable(true);
        item.teleport(loc);
        item.setVelocity(new Vector(0,0,0));
        if (graf.config.getBoolean("dont_pickup." + name + ".glowing"))
            item.setGlowing(true);

        if (graf.config.getInt("dont_pickup." + name + ".pass") == 0)
            item.setCustomName(graf.config.getString("dont_pickup." + name + ".string"));
        else
        {
            if (graf.config.getInt("dont_pickup." + name + ".pass") == 3)
            {
                item.addPassenger(armor_new(graf.config.getString("dont_pickup." + name + ".string"), loc, 0));
                item.addPassenger(armor_new(graf.config.getString("dont_pickup." + name + ".string1"), loc, 1));
                return ;
            }
            if (graf.config.getInt("dont_pickup." + name + ".pass") == 4)
            {
                item.addPassenger(armor_new(graf.config.getString("dont_pickup." + name + ".string"), loc, 0));
                item.addPassenger(armor_new(graf.config.getString("dont_pickup." + name + ".string1"), loc, 1));
                item.setCustomName(graf.config.getString("dont_pickup." + name + ".string2"));
                return ;
            }
            item.addPassenger(armor_new(graf.config.getString("dont_pickup." + name + ".string"), loc, graf.config.getInt("dont_pickup." + name + ".pass") == 2 ? 0 : 1));
        }
    }

    @EventHandler
    private void recipe(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            player p = player.getPlayer(e.getPlayer());
            if (p.permision == 10 && p.pl.isSneaking() && p.pl.getItemInHand().getType().equals(Material.BEDROCK))
            {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                {
                    if (e.getClickedBlock().getType().equals(Material.DROPPER))
                    {
                        Dropper block = (Dropper) e.getClickedBlock().getState();
                        Inventory inv = Bukkit.createInventory(block, InventoryType.WORKBENCH, "Создание рецепта помощи");
                        p.pl.openInventory(inv);
                    }
                }
                else
                {
                    String name = e.getClickedBlock().getLocation().getWorld().getName()
                            + "+" + e.getClickedBlock().getLocation().getBlockX()
                            + "+" + e.getClickedBlock().getLocation().getBlockY()
                            + "+" + e.getClickedBlock().getLocation().getBlockZ();
                    if (graf.config.contains("craft_help." + name))
                    {
                        graf.config.set("craft_help." + name, null);
                    }
                }
            }
            else
            {
                String name = e.getClickedBlock().getLocation().getWorld().getName()
                        + "+" + e.getClickedBlock().getLocation().getBlockX()
                        + "+" + e.getClickedBlock().getLocation().getBlockY()
                        + "+" + e.getClickedBlock().getLocation().getBlockZ();
                if (graf.config.contains("craft_help." + name))
                {
                    e.setCancelled(true);
                    final int []count = new int[]{0};
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            count[0]++;
                            if (count[0] % 5 == 0 || count[0] == 1)
                            {
                                Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, "Пример крафта");

                                List<ItemStack> list_items = new ArrayList<>();

                                for (String item : graf.config.getConfigurationSection("craft_help." + name).getKeys(false))
                                    list_items.add(graf.config.getItemStack("craft_help." + name + "." + item));

                                for (ShapelessRecipe recipe : craftTpPoints.recipes)
                                {
                                    if (list_items.containsAll(recipe.getIngredientList()) && list_items.size() == recipe.getIngredientList().size())
                                    {
                                        inv.setItem(0, recipe.getResult());
                                        break ;
                                    }
                                }

                                for (ItemStack item : list_items)
                                {
                                    while (true)
                                    {
                                        int rand = (int) (Math.random() * 8) + 1;
                                        if (inv.getItem(rand) == null)
                                        {
                                            inv.setItem(rand, item);
                                            break ;
                                        }
                                    }
                                }
                                p.pl.openInventory(inv);
                            }
                            if (count[0] != 0 && !p.pl.getOpenInventory().getTitle().equals("Пример крафта"))
                            {
                                cancel();
                                return ;
                            }
                        }
                    }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0,  4);
                }
            }
        }
    }

    @EventHandler
    private void recipe(InventoryCloseEvent e)
    {
        if (e.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase("Создание рецепта помощи"))
        {
            player p = player.getPlayer((Player) e.getPlayer());
            Location loc = ((Dropper) p.pl.getOpenInventory().getTopInventory().getHolder()).getLocation();
            ItemStack[] list = p.pl.getOpenInventory().getTopInventory().getContents();
            String name = loc.getWorld().getName() + "+" + loc.getBlockX() + "+" + loc.getBlockY() + "+" + loc.getBlockZ();
            for (int i = 0; i < list.length; i++)
                graf.config.set("craft_help." + name + "." + i, list[i]);
            loc.getBlock().setType(Material.CRAFTING_TABLE);
        }
    }

    @EventHandler
    private void recipe(InventoryClickEvent e)
    {
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Пример крафта"))
        {
            e.setCancelled(true);
        }
    }

    public static void createNameTag(Location location, String name, String[] args, ItemStack item_name)
    {
        if (item_name == null)
            return ;
        if (graf.config.get("dont_pickup." + name + ".world") != null)
        {
            Location loc = new Location(
                    Bukkit.getWorld(graf.config.getString("dont_pickup." + name + ".world")),
                    graf.config.getDouble("dont_pickup." + name + ".x"),
                    graf.config.getDouble("dont_pickup." + name + ".y"),
                    graf.config.getDouble("dont_pickup." + name + ".z")
            );
            for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.01, 0.01, 0.01))
            {
                if (en instanceof Item)
                {
                    for (Entity ent : en.getPassengers())
                        ent.remove();
                    en.remove();
                }
            }
        }

        ChatColor col1 = null;
        ChatColor col2 = null;

        boolean f = false;
        boolean glow = false;
        int pass = 0;
        int	string_i = 0;

        String []string = {" "," "," "};

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("next"))
            {
                string_i++;
            }
            else
            if (args[i].split(":")[0].equals("pass"))
            {
                if (args[i].split(":")[1].equals("big"))
                    pass = 1;
                if (args[i].split(":")[1].equals("small"))
                    pass = 2;
                if (args[i].split(":")[1].equals("all"))
                    pass = 3;
                if (args[i].split(":")[1].equals("full"))
                    pass = 4;
            }
            else
            if (args[i].equals("glow"))
            {
                glow = true;
            }
            else
            if (args[i].split("")[0].equals("#") && !f)
            {
                if (args[i].split("-").length == 1)
                    string[string_i] += ChatColor.of(args[i]);
                else
                {
                    col1 = ChatColor.of(args[i].split("-")[0]);
                    col2 = ChatColor.of(args[i].split("-")[1]);
                    f = true;
                }
            }
            else
            if (args[i].split("")[0].equals("&") && !f)
            {
                if (args[i].split("-").length == 1)
                {
                    string[string_i] += chat.color[Integer.parseInt(args[i].split("")[1])];
                }
                else
                {
                    col1 = chat.color[Integer.parseInt(args[i].split("-")[0].split("")[1])];
                    col2 = chat.color[Integer.parseInt(args[i].split("-")[1].split("")[1])];
                    f = true;
                }
            }
            else
            {
                string[string_i] += args[i] + (i == args.length - 1 ? "" : " ");
            }
        }
        if (f)
        {
            string[0] = rgb.gradient(col1.getName(), col2.getName(), string[0]);
            string[1] = rgb.gradient(col1.getName(), col2.getName(), string[1]);
            string[2] = rgb.gradient(col1.getName(), col2.getName(), string[2]);
        }

        Item item = location.getWorld().dropItem(location, item_name);
        item.setGravity(false);
        item.setCustomNameVisible(true);
        item.setInvulnerable(true);
        item.teleport(location);
        item.setVelocity(new Vector(0,0,0));
        if (glow)
            item.setGlowing(true);

        Location loc = location;

        graf.config.set("dont_pickup." + name + ".world",loc.getWorld().getName());
        graf.config.set("dont_pickup." + name + ".x",loc.getX());
        graf.config.set("dont_pickup." + name + ".y",loc.getY());
        graf.config.set("dont_pickup." + name + ".z",loc.getZ());
        graf.config.set("dont_pickup." + name + ".item", item_name);
        graf.config.set("dont_pickup." + name + ".glowing", glow);
        graf.config.set("dont_pickup." + name + ".pass", pass);
        graf.config.set("dont_pickup." + name + ".string", string[0]);
        graf.config.set("dont_pickup." + name + ".string1", string[1]);
        graf.config.set("dont_pickup." + name + ".string2", string[2]);

        if (pass == 0)
            item.setCustomName(string[0]);
        else
        {
            if (pass == 3)
            {
                item.addPassenger(armor_new(string[0], loc, 0));
                item.addPassenger(armor_new(string[1], loc, 1));
                return ;
            }
            if (pass == 4)
            {
                item.addPassenger(armor_new(string[0], loc, 0));
                item.addPassenger(armor_new(string[1], loc, 1));
                item.setCustomName(string[2]);
                return ;
            }
            item.addPassenger(armor_new(string[0], loc, pass == 2 ? 1 : 0));
        }
    }

    public static void deleteNameTag(String name)
    {
        if (!graf.config.contains("dont_pickup." + name + ".world"))
            return ;
        Location loc = new Location(
                Bukkit.getWorld(graf.config.getString("dont_pickup." + name + ".world")),
                graf.config.getDouble("dont_pickup." + name + ".x"),
                graf.config.getDouble("dont_pickup." + name + ".y"),
                graf.config.getDouble("dont_pickup." + name + ".z")
        );
        for (Entity en : loc.getWorld().getNearbyEntities(loc, 0.01, 0.01, 0.01))
        {
            if (en instanceof Item)
            {
                for (Entity ent : en.getPassengers())
                    ent.remove();
                en.remove();
            }
        }
        graf.config.set("dont_pickup." + name, "");
    }
}
