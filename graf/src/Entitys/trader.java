package Entitys;

import Item.*;
import Listeners.TraderOpenTrade;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import own.chat;
import own.player;
import text_processing.rgb;

import java.util.ArrayList;

public class trader
{
    public String       item_name;
    public int          item_count;
    public int          price;
    public Material     material;

    public trader(String item_name, int item_count, int price, Material material)
    {
        this.item_name = item_name;
        this.item_count = item_count;
        this.price = price;
        this.material = material;
    }

    public MerchantRecipe getRecipe()
    {
        ItemStack item = Item.create(material, item_count, TraderOpenTrade.getNameSale(item_count, item_name, price), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня для совершения сделки")});
        ItemStack skit = Item.create(Material.SUNFLOWER, price, TraderOpenTrade.getNameSale(item_count, item_name, price), new String[]{rgb.gradientLight(chat.color[1], "Жми на меня для совершения сделки")}, Enchantment.LURE, true);

        MerchantRecipe recipe = new MerchantRecipe(skit, 10);
        recipe.addIngredient(item);

        return recipe;
    }

    public MerchantRecipe getRecipeChunk()
    {
        ItemStack item = Item.create(material, item_count < 65 ? item_count : 1, TraderOpenTrade.getNamePay(item_count, item_name, price), new String[]{
                rgb.gradientLight(chat.color[1], item_count < 65 ? "Жми на меня для покупки" : "Купить все недостающие чанки"),
                rgb.gradientLight(chat.color[1], item_count < 65 ? item_count == 1 ? "дополнительного чанка привата" : "дополнительных чанков привата" : "Максимум может быть " + player.MaxRegsAllow + " чанка")
        });
        ItemStack skit = Item.create(Material.SUNFLOWER, TraderOpenTrade.getNamePay(item_count, item_name, price), new String[]{
                rgb.gradientLight(chat.color[1], "Жми на меня для покупки"),
                rgb.gradientLight(chat.color[1], item_count == 1 ? "дополнительного чанка привата" : "дополнительных чанков привата")
        }, Enchantment.LURE, true);

        MerchantRecipe recipe = new MerchantRecipe(item, 10);
        recipe.addIngredient(skit);

        return recipe;
    }

    public MerchantRecipe getRecipeClose()
    {
        ItemStack item = Item.create(material, rgb.gradientLight(chat.color[0], "Вы купили все доступные чанки"), new String[]{
                rgb.gradientLight(chat.color[1], "У вас максимальное количество чанков"),
                rgb.gradientLight(chat.color[1], "144 чанка")
        });
        ItemStack skit = Item.create(material, rgb.gradientLight(chat.color[0], "Вы купили все доступные чанки"), new String[]{
                rgb.gradientLight(chat.color[1], "У вас максимальное количество чанков"),
                rgb.gradientLight(chat.color[1], "144 чанка")
        }, Enchantment.LURE, true);

        MerchantRecipe recipe = new MerchantRecipe(item, 10);
        recipe.addIngredient(skit);

        return recipe;
    }
}
