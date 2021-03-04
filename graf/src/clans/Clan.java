package clans;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.omg.CORBA.TRANSIENT;
import own.chat;
import own.graf;
import own.player;
import own.reg;
import text_processing.rgb;
import text_processing.rgb_map;

import java.io.File;
import java.util.*;

public class Clan
{
    public static int               price = 1000;
    public static String            chat_rank = "⚝";
    public static ArrayList<Clan>   clans = new ArrayList<>();
    public static final class       Effect
    {
        public static final byte SPEED = 0;
        public static final byte FAST_DIGGING = 1;
        public static final byte DAMAGE_RESISTANCE = 2;
        public static final byte INCREASE_DAMAGE = 3;
        public static final byte REGENERATION = 4;
        public static final byte JUMP = 5;
        public static final byte WATER_BREATHING = 6;

        public static byte[] valueOf()
        {
            return new byte[]{
                    SPEED,
                    FAST_DIGGING,
                    DAMAGE_RESISTANCE,
                    INCREASE_DAMAGE,
                    REGENERATION,
                    JUMP,
                    WATER_BREATHING
            };
        }
    }

    public String               uid;

    public Clan(String uid)
    {
        this.uid = uid;
    }

    public static Clan getClan(String uid)
    {
        //PotionEffectType.WATER_BREATHING
        if (graf.Clans.contains(uid))
            return new Clan(uid);
        return null;
    }

    public static Clan getClanName(String name)
    {
        for (String uid : graf.Clans.getKeys(false))
        {
            Clan clan = getClan(uid);
            if (clan.isExist() && clan.getName().equals(name))
                return clan;
        }
        return null;
    }

    public ArrayList<reg> getRegs()
    {
        ArrayList<reg> regs = new ArrayList<>();
        for (reg region : reg.Regs)
        {
            if (region.getClans().contains(this))
                regs.add(region);
        }
        return regs;
    }

    public boolean isExist()
    {
        if (getOwner() == null)
            return false;
        return true;
    }

    public static Clan new_clan(String name, byte color, player p)
    {
        p.pl.closeInventory();
        if (p.pl.getWorld().getName().equals("world"))
        if (!isFreeName(name))
        {
            p.ErrorMessage("Это имя занято");
            return null;
        }
        if (!(p.clan_uid == null || p.clan_uid.equals("null")))
        {
            p.ErrorMessage("Вы уже состоите в фракции");
            return null;
        }
        if (!isCanCreateClan(
                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockX(),
                p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation().getBlockZ()
        ))
        {
            p.ErrorMessage("Фракция не может пересекать чужую фракцию\nОтойдите минимум на #12# блоков от границ другой фракции");
            return null;
        }
        reg region = reg.getReg(p.pl.getLocation());
        if (region == null || !region.isExist() || !region.owner.equals(p.p))
        {
            p.ErrorMessage("Фракцию можно создавать только в регионе где вы главный");
            return null;
        }
        Clan clan = new Clan(getRandUid());
        clan.setName(name);
        clan.setColor(color);
        clan.setCenter(p.pl.getLocation().getChunk().getBlock(8, 0, 8).getLocation());
        clan.setTimeReg(new Date().getTime());
        clan.setInfl(20);
        p.clanInclude(clan, (byte) 50);
        clan.setOwner(p);
        p.money -= Clan.price;
        p.clan_gui = null;
        soundClanCreate(p);
        clans.add(clan);
        Clan.clans.sort((a, b) -> b.getInfl() - a.getInfl());
        return clan;
    }

    public static boolean isCanCreateClan(int x, int z)
    {
        for (Clan clan : clans)
        {
            if (clan.getDistance(x, z) <= (clan.getRadius() + 12))
                return false;
        }
        return true;
    }

    public void deleteClan()
    {
        for (String s : getMembers())
        {
            if (player.getPlayer(s).pl != null && player.getPlayer(s).pl.isOnline())
                soundClanDelete(player.getPlayer(s));
            player.getPlayer(s).clanLeave();
        }
        if (getOwner().pl != null && getOwner().pl.isOnline())
            soundClanDelete(getOwner());
        getOwner().clanLeave();
        graf.Clans.set(uid, null);
        clans.remove(this);
        Clan.clans.sort((a, b) -> b.getInfl() - a.getInfl());
    }

    public void set(String way, Object value)
    {
        graf.Clans.set(uid + way, value);
    }

    public Object get(String way)
    {
        return graf.Clans.get(uid + way);
    }

    public void setName(String name)
    {
        graf.Clans.set(uid + ".name", name);
    }

    public String getName()
    {
        return graf.Clans.getString(uid + ".name");
    }

    public void setColor(byte color)
    {
        graf.Clans.set(uid + ".color", color);
    }

    public byte getColor()
    {
        return (byte) graf.Clans.getInt(uid + ".color");
    }

    public ChatColor getChatColor()
    {
        return rgb_map.byte_to_rgb.get(getColor());
    }

    public void setCenter(Location loc)
    {
        graf.Clans.set(uid + ".center.x", loc.getBlockX());
        graf.Clans.set(uid + ".center.z", loc.getBlockZ());
    }
    public Location getCenter()
    {
        return new Location(Bukkit.getWorld("world"), graf.Clans.getInt(uid + ".center.x"), 1, graf.Clans.getInt(uid + ".center.z"));
    }
    public player getOwner()
    {
        if (graf.Clans.getString(uid + ".owner") == null)
            return null;
        return player.getPlayer(graf.Clans.getString(uid + ".owner"));
    }

    public void setOwner(player p)
    {
        graf.Clans.set(uid + ".owner", p.p);
    }

    public void setTimeReg(long x)
    {
        graf.Clans.set(uid + ".time_reg", x);
    }

    public long getTimeReg()
    {
        return graf.Clans.getLong(uid + ".time_reg");
    }

    public void setInfl(int x)
    {
        graf.Clans.set(uid + ".infl", x);
    }

    public int getInfl()
    {
        return graf.Clans.getInt(uid + ".infl");
    }

    public void setMembers(List<String> list)
    {
        graf.Clans.set(uid + ".members", list);
    }

    public List<String> getMembers()
    {
        List<String> members = new ArrayList<>();
        members.addAll(graf.Clans.getStringList(uid + ".members"));
        return members;
    }

    public List<player> getAll()
    {
        List<String> members = getMembers();
        members.add(getOwner().p);
        List<player> ret = new ArrayList<>();
        for (String member : members)
        {
            player p = player.getPlayer(member);
            if (p.pl != null)
                ret.add(p);
        }
        return ret;
    }

    public void setInvitePlayers(List<String> list)
    {
        graf.Clans.set(uid + ".invite_players", list);
    }

    public List<String> getInvitePlayers()
    {
        List<String> members = new ArrayList<>();
        members.addAll(graf.Clans.getStringList(uid + ".invite_players"));
        return members;
    }

    public void setBeakonStay(Boolean x)
    {
        graf.Clans.set(uid + ".beacon.stay", x);
    }

    public boolean getBeakonStay()
    {
        return graf.Clans.getBoolean(uid + ".beacon.stay");
    }

    public void setKazna(int x)
    {
        graf.Clans.set(uid + ".kazna", x);
    }

    public int getKazna()
    {
        return graf.Clans.getInt(uid + ".kazna");
    }

    public void setEffectsLvl(byte eff, byte lvl)
    {
        graf.Clans.set(uid + ".effects." + eff, lvl);
    }

    public byte getEffectsLvl(byte eff)
    {
        return (byte) graf.Clans.getInt(uid + ".effects." + eff);
    }

    public void setEffectIsOn(byte eff, boolean ison)
    {
        graf.Clans.set(uid + ".effects.ison." + eff, ison);
    }

    public boolean getEffectsIsOn(byte eff)
    {
        return graf.Clans.getBoolean(uid + ".effects.ison." + eff);
    }

    public static boolean isFreeName(String name)
    {
        if (graf.Clans != null)
        for (String uid : graf.Clans.getKeys(false))
        {
            if (graf.Clans.getString(uid + ".name") != null && graf.Clans.getString(uid + ".name").equalsIgnoreCase(name))
                return false;
        }
        return true;
    }

    public static ChatColor getColorRank(byte rank)
    {
        if (rank == 50)
            return ChatColor.of("#ff0000");
        if (rank == 45)
            return ChatColor.of("#BA00C7");
        if (rank == 41)
            return ChatColor.of("#0022CB");
        if (rank == 2)
            return ChatColor.of("#00C0C7");
        return ChatColor.of("#ffffff");
    }

    public void addMember(player p)
    {
        if (getMembers().contains(p.p) || getOwner().p.equals(p.p))
        {
            p.sendMessageE(rgb.gradientLight(chat.color[0], "Вы уже состоите в данной фракции"));
            return ;
        }
        List<String> inv_players = getInvitePlayers();
        inv_players.remove(p.p);
        setInvitePlayers(inv_players);
        p.clanInclude(this, (byte) 1);
        p.pl.sendTitle(chat.color[0] + "Добро пожаловать в " + rgb_map.byte_to_rgb.get(p.getClan().getColor()) + p.getClan().getName(), null, 20, 60, 20);
        List<String> list = getMembers();
        list.add(p.p);
        setMembers(list);
        getOwner().sendMessageG(rgb.gradientLight(chat.color[0], "В #" + p.getClan().getName() + "# новый игрок #" + p.p));
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (list.contains(pl.getName()))
            {
                player.getPlayer(pl).sendMessageG(rgb.gradientLight(chat.color[0], "В #" + p.getClan().getName() + "# новый игрок #" + p.p));
            }
        }
    }

    public void removeMember(player p)
    {
        if (getMembers().contains(p.p))
        {
            if (p.isOnline() && p.getClan() != null)
                p.pl.sendTitle(chat.color[0] + "Вы покинули фракцию " + rgb_map.byte_to_rgb.get(p.getClan().getColor()) + p.getClan().getName(), null, 20, 60, 20);
            List<String> list = getMembers();
            list.remove(p.p);
            setMembers(list);
            p.clanLeave();
            getOwner().sendMessageG(rgb.gradientLight(chat.color[0], "Игрок #" + p.p + "# покинул фракцию #" + getOwner().getClan().getName()));
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                if (list.contains(pl.getName()))
                {
                    player.getPlayer(pl).sendMessageG(rgb.gradientLight(chat.color[0], "Игрок #" + p.p + "# покинул фракцию #" + getOwner().getClan().getName()));
                }
            }
        }
    }

    public static String getRandUid()
    {
        while (true)
        {
            boolean f = false;
            String name = Integer.toHexString((int) (Math.random() * 20000000));
            if (graf.Clans != null)
            {
                for (String uid : graf.Clans.getKeys(false))
                {
                    if (uid.equals(name))
                    {
                        f = true;
                        break ;
                    }
                }
            }
            if (!f)
                return name;
        }
    }

    public int  getDistance(int x, int z)
    {
        int cx = graf.Clans.getInt(uid + ".center.x");
        int cz = graf.Clans.getInt(uid + ".center.z");

        int ax = Math.abs(cx - x);
        int az = Math.abs(cz - z);

        return (int) Math.sqrt((ax * ax) + (az * az));
    }

    public static ArrayList<Clan> getClanLocation(int x, int z)
    {
        ArrayList<Clan> clans = new ArrayList<>();
        for (Clan clan : Clan.clans)
        {
            if (clan.getDistance(x, z) <= clan.getRadius())
                clans.add(clan);
        }
        return clans;
    }

    public int  getRadius()
    {
        return (int) Math.sqrt(getInfl() / Math.PI);
    }

    public static void soundClanCreate(player p)
    {
        p.pl.sendTitle(chat.color[0] + "Фракция " + rgb_map.byte_to_rgb.get(p.getClan().getColor()) + p.getClan().getName() + chat.color[0] + " созадана", "", 20, 60, 20);
        int[] count = new int[]{0};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (count[0]++ >= 3)
                    cancel();
                Fireworkrun(p.pl.getLocation(), new ChatColor[]{rgb_map.byte_to_rgb.get(p.getClan().getColor())}, new ChatColor[]{rgb_map.byte_to_rgb.get(p.getClan().getColor())}, 0);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("graf")), 0, 20);
        p.pl.playSound(p.pl.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, (float) 1);
        p.pl.playSound(p.pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, (float) 1);
        p.pl.playSound(p.pl.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, (float) 1.5);
    }

    public static void soundClanDelete(player p)
    {
        try{
            p.pl.sendTitle(chat.color[0] + "Фракция " + rgb_map.byte_to_rgb.get(p.getClan().getColor()) + p.getClan().getName() + chat.color[0] + " больше не существует", "", 20, 60, 20);

            p.pl.playSound(p.pl.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, (float) 1);
            p.pl.playSound(p.pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, (float) 0.6);
        }catch(Exception ex){}
    }

    public static void Fireworkrun(Location loc, ChatColor[]colors, ChatColor []fade, int power)
    {
        loc.setY(loc.getY() + 1.7);

        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);

        FireworkMeta fwmeta = fw.getFireworkMeta();

        FireworkEffect.Builder ef = FireworkEffect.builder();
        ef.flicker(true);
        ef.trail(true);
        ef.with(FireworkEffect.Type.BALL_LARGE);
        for (ChatColor color : colors)
        {
            ef.withColor(Color.fromRGB(rgb.get_rgb(color.getName())[0], rgb.get_rgb(color.getName())[1],rgb.get_rgb(color.getName())[2]));
        }
        for (ChatColor color : fade)
        {
            ef.withFade(Color.fromRGB(rgb.get_rgb(color.getName())[0], rgb.get_rgb(color.getName())[1],rgb.get_rgb(color.getName())[2]));
        }
        FireworkEffect efk = ef.build();

        fwmeta.addEffect(efk);
        fwmeta.setPower(power);

        fw.setFireworkMeta(fwmeta);
    }

    public  synchronized static void getconClan()
    {
        File file = new File("plugins/Clans.yml");
        graf.Clans =  YamlConfiguration.loadConfiguration(file);

        for (String uid : graf.Clans.getKeys(false))
        {
            Clan clan = Clan.getClan(uid);
            if (clan != null && clan.isExist())
                Clan.clans.add(clan);
        }

        Clan.clans.sort((a, b) -> b.getInfl() - a.getInfl());
    }

    public static void saveconClan()
    {
        File file = new File("plugins/Clans.yml");
        try {
            graf.Clans.save(file);
        } catch (Exception ignored) {}
    }
}
