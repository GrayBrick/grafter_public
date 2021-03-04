package clans;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClanEffects {
    public static ClanEffects[] Effects = new ClanEffects[]{
            new ClanEffects(
                    new int[]{100, 5000},
                    "Скорость",
                    (byte) 2,
                    Material.SUGAR,
                    Clan.Effect.SPEED,
                    PotionEffectType.SPEED,
                    0.5
            ),
            new ClanEffects(
                    new int[]{10000, 50000},
                    "Спешка",
                    (byte) 2,
                    Material.IRON_PICKAXE,
                    Clan.Effect.FAST_DIGGING,
                    PotionEffectType.FAST_DIGGING,
                    2
            ),

            new ClanEffects(
                    new int[]{1000, 10000},
                    "Сопротивление",
                    (byte) 2,
                    Material.TURTLE_HELMET,
                    Clan.Effect.DAMAGE_RESISTANCE,
                    PotionEffectType.DAMAGE_RESISTANCE,
                    1.5
            ),

            new ClanEffects(
                    new int[]{5000, 25000},
                    "Сила",
                    (byte) 2,
                    Material.BLAZE_POWDER,
                    Clan.Effect.INCREASE_DAMAGE,
                    PotionEffectType.INCREASE_DAMAGE,
                    2
            ),

            new ClanEffects(
                    new int[]{10000, 50000},
                    "Регенерация",
                    (byte) 2,
                    Material.GLISTERING_MELON_SLICE,
                    Clan.Effect.REGENERATION,
                    PotionEffectType.REGENERATION,
                    1.5
            ),

            new ClanEffects(
                    new int[]{500, 5000},
                    "Прыгучесть",
                    (byte) 2,
                    Material.RABBIT_FOOT,
                    Clan.Effect.JUMP,
                    PotionEffectType.JUMP,
                    1
            ),

            new ClanEffects(
                    new int[]{50000},
                    "Подводное дыхание",
                    (byte) 1,
                    Material.PUFFERFISH,
                    Clan.Effect.WATER_BREATHING,
                    PotionEffectType.WATER_BREATHING,
                    3
            )
    };

    public int              price[];
    public String           name;
    public byte             max_lvl;
    public Material         item;
    public byte             effect_id;
    public PotionEffectType type;
    public double           price_tick;

    public ClanEffects(int[] price, String name, byte max_lvl, Material item, byte effect_id, PotionEffectType type, double price_tick) {
        //Clan.Effect.REGENERATION
        this.price = price;
        this.name = name;
        this.max_lvl = max_lvl;
        this.item = item;
        this.effect_id = effect_id;
        this.type = type;
        this.price_tick = price_tick;
    }
}
