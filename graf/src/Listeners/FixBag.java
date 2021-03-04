package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class FixBag implements Listener
{
    private static ArrayList<Location>  block_list = new ArrayList<>();
    private static int                  time = 20;

    @EventHandler
    private static void BlockEx(BlockPistonExtendEvent e)
    {
        if (block_list.contains(e.getBlock().getLocation()))
            e.setCancelled(true);
        else
        {
            for (Block b : e.getBlocks())
            {
                if (b.getType().equals(Material.BLACK_CARPET) ||
                        b.getType().equals(Material.BLUE_CARPET) ||
                        b.getType().equals(Material.BROWN_CARPET) ||
                        b.getType().equals(Material.CYAN_CARPET) ||
                        b.getType().equals(Material.GRAY_CARPET) ||
                        b.getType().equals(Material.GREEN_CARPET) ||
                        b.getType().equals(Material.LIGHT_BLUE_CARPET) ||
                        b.getType().equals(Material.LIGHT_GRAY_CARPET) ||
                        b.getType().equals(Material.LIME_CARPET) ||
                        b.getType().equals(Material.MAGENTA_CARPET) ||
                        b.getType().equals(Material.ORANGE_CARPET) ||
                        b.getType().equals(Material.PINK_CARPET) ||
                        b.getType().equals(Material.PURPLE_CARPET) ||
                        b.getType().equals(Material.RED_CARPET) ||
                        b.getType().equals(Material.WHITE_CARPET) ||
                        b.getType().equals(Material.YELLOW_CARPET)
                )
                    e.setCancelled(true);
                if (b.getType().equals(Material.SLIME_BLOCK) || b.getType().equals(Material.HONEY_BLOCK))
                {
                    block_list.add(e.getBlock().getLocation());
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            block_list.remove(e.getBlock().getLocation());
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                }
            }
        }
    }

    @EventHandler
    private static void BlockEx(BlockPistonRetractEvent e)
    {
        if (block_list.contains(e.getBlock().getLocation()))
            e.setCancelled(true);
        else
        {
            for (Block b : e.getBlocks())
            {
                if (b.getType().equals(Material.BLACK_CARPET) ||
                        b.getType().equals(Material.BLUE_CARPET) ||
                        b.getType().equals(Material.BROWN_CARPET) ||
                        b.getType().equals(Material.CYAN_CARPET) ||
                        b.getType().equals(Material.GRAY_CARPET) ||
                        b.getType().equals(Material.GREEN_CARPET) ||
                        b.getType().equals(Material.LIGHT_BLUE_CARPET) ||
                        b.getType().equals(Material.LIGHT_GRAY_CARPET) ||
                        b.getType().equals(Material.LIME_CARPET) ||
                        b.getType().equals(Material.MAGENTA_CARPET) ||
                        b.getType().equals(Material.ORANGE_CARPET) ||
                        b.getType().equals(Material.PINK_CARPET) ||
                        b.getType().equals(Material.PURPLE_CARPET) ||
                        b.getType().equals(Material.RED_CARPET) ||
                        b.getType().equals(Material.WHITE_CARPET) ||
                        b.getType().equals(Material.YELLOW_CARPET)
                )
                    e.setCancelled(true);
                if (b.getType().equals(Material.SLIME_BLOCK) || b.getType().equals(Material.HONEY_BLOCK))
                {
                    block_list.add(e.getBlock().getLocation());
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            block_list.remove(e.getBlock().getLocation());
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), time);
                }
            }
        }
    }
}
