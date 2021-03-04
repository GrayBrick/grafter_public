package Listeners;

import commands.message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_16_R3.ItemItemFrame;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import own.chat;
import own.player;
import own.reg;
import Item.Item;
import railway_system.road;
import text_processing.rgb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ActionPrivate implements Listener
{
    @EventHandler
    public static void Blockbreak(BlockBreakEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        Location loc = e.getBlock().getLocation();
        reg region = reg.getReg(loc);
        if (!region.break_block)
        {
            e.setCancelled(true);
            p.ErrorMessage("Тут вообще никому нельзя ломать блоки");
            return ;
        }
        if (allowAction(p, loc))
        {
            X_ray_fix.break_block(loc);
            BonusBlockBreak(e, p);
            return ;
        }
        e.setCancelled(true);
        p.ErrorMessage("Не твоя собственность");
    }

    @EventHandler
    private static void BreakDoor(EntityBreakDoorEvent e)
    {
        if (e.getEntity() instanceof Zombie)
            e.setCancelled(true);
    }

    private static void BonusBlockBreak(BlockBreakEvent e, player p)
    {
        double coef = 0;
        coef = BonusEntityDeath.getCoef(p.BreakBlock);
        double coef_luck = 1 + (p.pl.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) / 10.0);
        coef *= coef_luck;
        e.setExpToDrop((int) (e.getExpToDrop() * coef));
        if (e.getExpToDrop() != 0)
            p.BreakBlock++;
        if ((p.typeBar.split(" ")[1].equals("all") || p.typeBar.split(" ")[1].equals("exp_block")) && (int) (e.getExpToDrop() * coef) != e.getExpToDrop())
        {
            p.pl.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new ComponentBuilder("Вы получили ").color(chat.color[1]
                    ).append((int) (e.getExpToDrop() * coef) + "").color(chat.color[0]
                    ).append(" очков опыта в место ").color(chat.color[1]
                    ).append(e.getExpToDrop() + "").color(chat.color[0]
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

    @EventHandler
    public static void place(BlockPlaceEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        Location loc = e.getBlock().getLocation();
        if (allowAction(p, loc))
            return ;
        e.setCancelled(true);
        p.ErrorMessage("Не оставля тут свои блоки");
    }

    @EventHandler
    public static void Armor(PlayerArmorStandManipulateEvent e)
    {
        Location loc = e.getRightClicked().getLocation();
        player p = player.getPlayer(e.getPlayer());
        if (allowAction(p, loc))
            return ;
        e.setCancelled(true);
        p.ErrorMessage("Запрещено тыкать (он живой)");
    }

    @EventHandler
    public static void Damage(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Arrow || e.getDamager() instanceof SpectralArrow || e.getDamager() instanceof Firework || e.getDamager() instanceof Trident || e.getDamager() instanceof Egg)
        {
            Projectile en = (Projectile) e.getDamager();
            if (en.getShooter() instanceof Player)
            {
                player p = player.getPlayer((Player) en.getShooter());
                Location loc = e.getEntity().getLocation();
                if (e.getEntity() instanceof Player)
                {
                    reg region = reg.getReg(loc);
                    if (region == null || !region.isExist() || region.pvp)
                    {
                        damage(e);
                        return ;
                    }
                    for (reg.arena ar : region.arenas)
                    {
                        if (ar.inArena(loc) && ar.inArena(p.pl.getLocation()))
                            return ;
                    }
                    e.setCancelled(true);
                    p.ErrorMessage("Не стреляй в него, предложи пойти выйти");
                }
                else if (e.getEntity() instanceof Animals)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Не твоя живность");
                }
                else if (e.getEntity() instanceof Villager)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Не бей поцана");
                }
                else if (e.getEntity() instanceof WanderingTrader)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Не бей поцана");
                }
                else if (e.getEntity() instanceof EnderCrystal)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("не выйдет");
                }
                else if (e.getEntity() instanceof IronGolem)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Он защитник");
                }
                else if (e.getEntity() instanceof ItemFrame)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Не ломай рамки");
                }
                else if (e.getEntity() instanceof ItemItemFrame)
                {
                    if (allowAction(p, loc))
                    {
                        damage(e);
                        return ;
                    }                    e.setCancelled(true);
                    p.ErrorMessage("Не ломай рамки2");
                }
            }

        }
        if (!(e.getDamager() instanceof Player))
            return ;
        player p = player.getPlayer((Player) e.getDamager());
        Location loc = e.getEntity().getLocation();
        if (e.getEntity() instanceof Player)
        {
            reg region = reg.getReg(loc);
            if (region == null || !region.isExist() || region.pvp)
            {
                damage(e);
                return ;
            }
            for (reg.arena ar : region.arenas)
            {
                if (ar.inArena(loc) && ar.inArena(p.pl.getLocation()))
                    return ;
            }
            e.setCancelled(true);
            p.ErrorMessage("Не бей его, предложи пойти выйти");
        }
        else if (e.getEntity() instanceof Animals)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }            e.setCancelled(true);
            p.ErrorMessage("Не твоя живность");
        }
        else if (e.getEntity() instanceof Villager)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }            e.setCancelled(true);
            p.ErrorMessage("Не бей поцана");
        }
        else if (e.getEntity() instanceof WanderingTrader)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }            e.setCancelled(true);
            p.ErrorMessage("Не бей поцана");
        }
        else if (e.getEntity() instanceof EnderCrystal)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }
            e.setCancelled(true);
            p.ErrorMessage("не выйдет");
        }
        else if (e.getEntity() instanceof IronGolem)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }
            e.setCancelled(true);
            p.ErrorMessage("Он защитник");
        }
        else if (e.getEntity() instanceof ItemFrame)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }
            e.setCancelled(true);
            p.ErrorMessage("Не ломай рамки");
        }
        else if (e.getEntity() instanceof ItemItemFrame)
        {
            if (allowAction(p, loc))
            {
                damage(e);
                return ;
            }
            e.setCancelled(true);
            p.ErrorMessage("Не ломай рамки2");
        }
    }

    private static void damage(EntityDamageByEntityEvent e)
    {
        Player_L.bit(e);
    }

    @EventHandler
    public static void BucketEmpty(PlayerBucketEmptyEvent e)
    {
        Location loc = e.getBlockClicked().getLocation();
        player p = player.getPlayer(e.getPlayer());
        if (allowAction(p, loc))
            return ;
        e.setCancelled(true);
        p.ErrorMessage("Не разливай, да не разлит будешь");
    }

    @EventHandler
    public static void EntityEvent(PlayerInteractEntityEvent e)
    {
        Location loc = e.getRightClicked().getLocation();
        player p = player.getPlayer(e.getPlayer());
        if (e.getRightClicked() instanceof WanderingTrader)
            return ;
        if (allowAction(p, loc))
            return ;
        e.setCancelled(true);
        p.ErrorMessage("Что ты задумал?");
    }

    @EventHandler
    public static void interact(PlayerInteractEvent e)
    {
        player p = player.getPlayer(e.getPlayer());
        if ((p.pl.getItemInHand().getType().equals(Material.SPLASH_POTION) || p.pl.getItemInHand().getType().equals(Material.LINGERING_POTION)))
        {
            Location loc = p.pl.getLocation();
            if (allowAction(p, loc))
                return ;
            e.setCancelled(true);
            p.ErrorMessage("Нельзя");
            return ;
        }
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getClickedBlock().getType().equals(Material.CRAFTING_TABLE) || e.getClickedBlock().getType().equals(Material.ENDER_CHEST))
            return ;
        Location loc = e.getClickedBlock().getLocation();
        if (allowAction(p, loc))
            return ;
        e.setCancelled(true);
        p.ErrorMessage("Пока нельзя");
    }

    private static ArrayList<Location> count_block(Location loc, ArrayList<Location> loc_set)
    {
        if (loc_set == null)
            loc_set = new ArrayList<>();

        loc_set.add(loc);

        Material block = loc.getBlock().getType();

        Location copy_loc = loc.clone();

        boolean f = false;
        copy_loc.setX(loc.getX() + 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);
        f = false;

        copy_loc = loc.clone();

        copy_loc.setX(loc.getX() - 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);
        f = false;

        copy_loc = loc.clone();

        copy_loc.setY(loc.getY() - 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);
        f = false;

        copy_loc = loc.clone();

        copy_loc.setY(loc.getY() + 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);
        f = false;

        copy_loc = loc.clone();

        copy_loc.setZ(loc.getZ() - 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);
        f = false;

        copy_loc = loc.clone();

        copy_loc.setZ(loc.getZ() + 1);
        for (Location l : loc_set)
        {
            if (l.getBlockX() == copy_loc.getBlockX() && l.getBlockY() == copy_loc.getBlockY() && l.getBlockZ() == copy_loc.getBlockZ())
            {
                f = true;
                break ;
            }
        }
        if (!f && copy_loc.getBlock().getType().equals(block))
            loc_set = count_block(copy_loc, loc_set);

        return loc_set;
    }

    @EventHandler
    public void entityEx(EntityExplodeEvent e)
    {
        e.blockList().clear();
    }

    @EventHandler
    public void join(BlockIgniteEvent e)
    {
        try
        {
            Location loc = e.getBlock().getLocation();
            player p = player.getPlayer(e.getPlayer());
            if (allowAction(p, loc))
                return ;
            e.setCancelled(true);
            p.ErrorMessage("Тут и так жарко");
        }catch(Exception ex){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Damage(EntityDamageEvent e)
    {
        reg region = reg.getReg(e.getEntity().getLocation());
        if (region == null || !region.isExist())
            return ;
        if (e.getEntity() instanceof Player)
        {
            if (region.player_damage)
                return;
            else
                e.setCancelled(true);
            return ;
        }
        if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
        {
            if (!(e.getEntity() instanceof Animals ||
                    e.getEntity() instanceof Villager ||
                    e.getEntity() instanceof WanderingTrader ||
                    e.getEntity() instanceof EnderCrystal ||
                    e.getEntity() instanceof IronGolem ||
                    e.getEntity() instanceof ItemFrame ||
                    e.getEntity() instanceof Item))
                return ;
            e.setCancelled(true);
            return ;
        }
    }

    @EventHandler
    public void BlockFrom(BlockFadeEvent e)
    {
        reg region = reg.getReg(e.getBlock().getLocation());
        if (region == null || !region.isExist())
            return ;
        if (e.getBlock().getType().equals(Material.FARMLAND) && !region.arable_dry)
            e.setCancelled(true);
    }

    @EventHandler
    public void Death(EntitySpawnEvent e)
    {
        if (e.getEntity() instanceof Monster || e.getEntity() instanceof Phantom)
        {
            reg region = reg.getReg(e.getLocation());
            if (region == null || !region.isExist())
                return ;
            if (!region.spawn_monster)
                e.setCancelled(true);
        }
    }

    public static boolean allowActionOwner(player p, Location loc)
    {
        if (!loc.getWorld().getName().equals("world"))
            return true;
        if (p.permision == 10)
            return true;
        reg region = reg.getReg(loc);
        if (region == null || region.isExist())
            return true;
        return region.owner.equals(p.p);
    }

    public static boolean allowActionOwner(player p, Location loc, reg region)
    {
        if (!region.isExist())
            return true;
        return region.owner.equals(p.p);
    }

    public static boolean allowAction(player p, Location loc)
    {
        if (!loc.getWorld().getName().equals("world"))
            return true;
        if (p.permision == 10)
            return true;
        reg region = reg.getReg(loc);
        if (!region.isExist())
            return true;
        for (String name : region.members)
            if (name.equals(p.p))
                return true;
        return allowActionOwner(p, loc, region);
    }
}
