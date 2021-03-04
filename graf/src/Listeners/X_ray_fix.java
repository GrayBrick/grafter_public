package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;

public class X_ray_fix implements Listener
{
    public static int                       max_y = 100;
    public static ArrayList<Chunk_bloks>    chunk_list = new ArrayList<>();

    public static Chunk_bloks get_Chunk(int x, int z)
    {
        for (Chunk_bloks chb : chunk_list)
            if (chb.chunk.getX() == x && chb.chunk.getZ() == z)
                return chb;
            return null;
    }

    public static void unload_Chunk(Chunk ch)
    {
        Chunk_bloks chb = get_Chunk(ch.getX(), ch.getZ());
        if (chb == null)
            return ;
        chb.show();
        chunk_list.remove(chb);
    }

    public static void break_block(Location loc)
    {
        Chunk ch = loc.getChunk();
        if (loc.getY() > max_y)
            return ;
        Chunk_bloks chb = get_Chunk(ch.getX(), ch.getZ());
        if (chb == null)
            return ;

        int bx = loc.getBlockX();
        int by = loc.getBlockY();
        int bz = loc.getBlockZ();

        for (Block_change bch : chb.block_list)
        {
            if (Math.abs(bch.x - bx) <= 1 && Math.abs(bch.z - bz) <= 1 && Math.abs(bch.y - by) <= 1)
            {
                bch.show();
                chb.block_list.remove(bch);
                return ;
            }
        }
    }

    public static class Chunk_bloks
    {
        public boolean                  ishide = false;
        public ArrayList<Block_change>  block_list = new ArrayList<>();
        private Chunk                   chunk;

        public Chunk_bloks(Chunk chunk)
        {
            this.chunk = chunk;
            block_list = new ArrayList<>();
            hide();
        }

        public void hide()
        {
            if (chunk.isLoaded())
            {
                ishide = true;
                for (byte x = 0; x < 16; x++)
                {
                    for (byte y = 0; y < max_y; y++)
                    {
                        for (byte z = 0; z < 16; z++)
                        {
                            Block block = chunk.getBlock(x, y, z);

                            if (block.getType().equals(Material.COAL_ORE))
                            {
                                add_block(block, (byte) 0);
                                continue ;
                            }
                            if (block.getType().equals(Material.IRON_ORE))
                            {
                                add_block(block, (byte) 1);
                                continue ;
                            }
                            if (block.getType().equals(Material.REDSTONE_ORE))
                            {
                                add_block(block, (byte) 2);
                                continue ;
                            }
                            if (block.getType().equals(Material.LAPIS_ORE))
                            {
                                add_block(block, (byte) 3);
                                continue ;
                            }
                            if (block.getType().equals(Material.GOLD_ORE))
                            {
                                add_block(block, (byte) 4);
                                continue ;
                            }
                            if (block.getType().equals(Material.DIAMOND_ORE))
                            {
                                add_block(block, (byte) 5);
                                continue ;
                            }
                            if (block.getType().equals(Material.EMERALD_ORE))
                            {
                                add_block(block, (byte) 6);
                            }
                        }
                    }
                }
            }
        }

        public void add_block(Block block, byte id)
        {
            if (!can_hide(block.getLocation()))
                return ;

            Block_change block_ch = new Block_change(id, block.getLocation());
            block_ch.hide();
            block_list.add(block_ch);
        }

        public static boolean can_hide(Location loc_block)
        {
            Location loc = loc_block.clone();
            Chunk ch = loc.getChunk();
            loc.add(1, 0, 0);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false;

            loc.add(-2, 0, 0);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false ;

            loc.add(1, 1, 0);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false ;

            loc.add(0, -2, 0);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false ;

            loc.add(0, 1, 1);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false ;

            loc.add(0, 0, -2);
            if (loc.getChunk().isLoaded() && (loc.getBlock().isEmpty() || loc.getBlock().isLiquid() || loc.getBlock().isPassable()))
                return false ;
            return true;
        }

        public void show()
        {
            ishide = false;
            for (Block_change block : block_list)
                block.show();
            block_list.clear();
        }
    }

    private static class Block_change
    {
        private byte    id;
        private String  world;
        private int     x;
        private int     y;
        private int     z;

        private Block_change(byte id, Location loc)
        {
            this.id = id;
            world = loc.getWorld().getName();
            x = loc.getBlockX();
            y = loc.getBlockY();
            z = loc.getBlockZ();
        }

        public void hide()
        {
            if (world.equals("world"))
                new Location(Bukkit.getWorld(world), x, y, z).getBlock().setType(Material.STONE);
        }

        public void show()
        {
            Block block = new Location(Bukkit.getWorld(world), x, y, z).getBlock();
            switch (id)
            {
                case 0 :
                    block.setType(Material.COAL_ORE);
                    break ;
                case 1 :
                    block.setType(Material.IRON_ORE);
                    break ;
                case 2 :
                    block.setType(Material.REDSTONE_ORE);
                    break ;
                case 3 :
                    block.setType(Material.LAPIS_ORE);
                    break ;
                case 4 :
                    block.setType(Material.GOLD_ORE);
                    break ;
                case 5 :
                    block.setType(Material.DIAMOND_ORE);
                    break ;
                case 6 :
                    block.setType(Material.EMERALD_ORE);
                    break ;
            }
        }
    }
}
