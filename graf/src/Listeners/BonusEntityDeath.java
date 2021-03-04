package Listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import own.chat;
import own.player;

import java.util.Date;
import java.util.Objects;

public class BonusEntityDeath implements Listener
{
    @EventHandler
    private static void death(EntityDeathEvent e)
    {
        if (e.getEntity().getKiller() == null)
            return ;
        if (e.getEntity() instanceof Piglin)
        {
            if (((Piglin) e.getEntity()).isBaby())
            {
                e.getDrops().clear();
            }
        }
        if (e.getEntity() instanceof Monster ||
                e.getEntity() instanceof Phantom ||
                e.getEntity() instanceof IronGolem ||
                e.getEntity() instanceof Animals ||
                e.getEntity() instanceof Ghast ||
                e.getEntity() instanceof Slime)
        {
            player p = player.getPlayer(e.getEntity().getKiller());
            p.DeathEntity++;
            double coef = getCoef(p.DeathEntity);
            double coef_mining = 1 + (p.pl.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) / 10.0);
            double coef_strong = 1;
            if (p.pl.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null)
                coef_strong = 1 + (p.pl.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() / 10.0);
            e.setDroppedExp((int) (e.getDroppedExp() * coef * coef_mining * coef_strong));
            if ((p.typeBar.split(" ")[1].equals("all") || p.typeBar.split(" ")[1].equals("exp_entity")) && (int) (e.getDroppedExp() * coef * coef_mining * coef_strong) != e.getDroppedExp())
            {
                p.pl.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder("Вы получили ").color(chat.color[1]
                        ).append((int) (e.getDroppedExp() * coef * coef_mining * coef_strong) + "").color(chat.color[0]
                        ).append(" очков опыта в место ").color(chat.color[1]
                        ).append(e.getDroppedExp() + "").color(chat.color[0]
                        ).append("").color(chat.color[0]).create());
                p.timerBar = true;
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.timerBar = false;
                    }
                }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 40);
            }
        }
    }

    public static double getCoef(int x)
    {
        double count = 5 * (Math.log(x) - Math.log(0.5));

        return 1 + (count / 100);
    }
}
