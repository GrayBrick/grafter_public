package Listeners;

import com.google.common.collect.ArrayListMultimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class craftTpPoints implements Listener
{
    private static final String points = chat.color[0] + "очки телепортации";
    private static final String points_5 = chat.color[1] + " 5 очков телепортации";
    private static final String points_15 = chat.color[1] + " 15 очков телепортации";
    private static final String points_300 = chat.color[1] + " 300 очков телепортации";
    private static final String points_65 = chat.color[1] + " 65 очков телепортации";
    private static final String points_100 = chat.color[1] + " 100 очков телепортации";

    public static ArrayList<ShapelessRecipe> recipes = new ArrayList<>();

    public static void addRecipe()
    {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(points);
        List list = new ArrayList<>();
        list.add(points_15);
        meta.setLore(list);
        item.setItemMeta(meta);

        ShapelessRecipe recipe = new ShapelessRecipe(item);

        recipe.addIngredient(Material.ENDER_PEARL);
        recipe.addIngredient(Material.PHANTOM_MEMBRANE);

        Bukkit.addRecipe(recipe);
        recipes.add(recipe);

        item = new ItemStack(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(points);
        list = new ArrayList<>();
        list.add(points_5);
        meta.setLore(list);
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(item);

        recipe.addIngredient(Material.ENDER_PEARL);

        //Bukkit.addRecipe(recipe);
        //recipes.add(recipe);

        item = new ItemStack(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(points);
        list = new ArrayList<>();
        list.add(points_300);
        meta.setLore(list);
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(item);

        recipe.addIngredient(1, Material.ENDER_PEARL);
        recipe.addIngredient(1, Material.PHANTOM_MEMBRANE);
        recipe.addIngredient(1, Material.FIREWORK_ROCKET);
        recipe.addIngredient(1, Material.TNT);

        Bukkit.addRecipe(recipe);
        recipes.add(recipe);

        item = new ItemStack(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(points);
        list = new ArrayList<>();
        list.add(points_65);
        meta.setLore(list);
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(item);

        recipe.addIngredient(1, Material.ENDER_PEARL);
        recipe.addIngredient(1, Material.PHANTOM_MEMBRANE);
        recipe.addIngredient(1, Material.FIREWORK_ROCKET);

        Bukkit.addRecipe(recipe);
        recipes.add(recipe);

        item = new ItemStack(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(points);
        list = new ArrayList<>();
        list.add(points_100);
        meta.setLore(list);
        item.setItemMeta(meta);

        recipe = new ShapelessRecipe(item);

        recipe.addIngredient(1, Material.ENDER_PEARL);
        recipe.addIngredient(1, Material.PHANTOM_MEMBRANE);
        recipe.addIngredient(1, Material.FIREWORK_ROCKET);
        recipe.addIngredient(1, Material.ARROW);

        Bukkit.addRecipe(recipe);
        recipes.add(recipe);
    }

    public static void addPoints(player p, int points)
    {
        p.tp_points += points;
        p.sendMessageG(rgb.gradientLight(chat.color[0], "Вы получили #" + points + " #очков телепортации, теперь у вас их #" + p.tp_points));
    }

    @EventHandler
    public static void click(InventoryClickEvent e)
    {
         if (e.getCurrentItem() != null &&
                 e.getCurrentItem().getItemMeta() != null &&
                 e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(points))
         {
             final int[] count = new int[]{0};
             new BukkitRunnable()
             {
                 @Override
                 public void run()
                 {
                     InventoryView inv = e.getWhoClicked().getOpenInventory();
                     if (inv.getCursor().getAmount() != 0 && inv.getCursor().getItemMeta().getDisplayName().equalsIgnoreCase(points))
                     {
                         addPoints(player.getPlayer((Player) e.getWhoClicked()), inv.getCursor().getAmount() * Integer.parseInt(
                                 e.getCursor().getItemMeta().getLore().get(0).split(" ")[1]
                         ));
                         inv.getCursor().setAmount(0);
                         InventoryView inve = e.getWhoClicked().getOpenInventory();
                         e.getWhoClicked().closeInventory();
                         e.getWhoClicked().openInventory(inve);
                         return ;
                     }
                     for (ItemStack item :e.getWhoClicked().getInventory().getContents())
                     {
                         if (item != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(points))
                         {
                             addPoints(player.getPlayer((Player) e.getWhoClicked()), item.getAmount() * Integer.parseInt(
                                     item.getItemMeta().getLore().get(0).split(" ")[1]
                             ));
                             item.setAmount(0);
                         }
                     }
                     InventoryView inve = (InventoryView) e.getWhoClicked().getOpenInventory();
                     ((Player) e.getWhoClicked()).closeInventory();
                     ((Player) e.getWhoClicked()).openInventory(inve);
                 }
             }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 1);
         }
    }
}
