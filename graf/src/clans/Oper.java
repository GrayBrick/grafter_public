package clans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import own.chat;
import own.graf;
import own.player;
import own.reg;
import text_processing.rgb;

import java.util.ArrayList;

public class Oper
{
    public static int tick_pay = 0;

    public static double ost(double r, double a, double b, double c, double d)
    {

        double 	S = Math.PI * r * r;

        double	segm_a = segment(r, a);
        double	segm_b = segment(r, b);
        double	segm_c = segment(r, c);
        double	segm_d = segment(r, d);

        double	atob_mini = (Math.sqrt((r * r) - (a * a)) - b);
        double	atod_mini = (Math.sqrt((r * r) - (a * a)) - d);

        double	btoa_mini = (Math.sqrt((r * r) - (b * b)) - a);
        double	btoc_mini = (Math.sqrt((r * r) - (b * b)) - c);

        double	ctob_mini = (Math.sqrt((r * r) - (c * c)) - b);
        double	ctod_mini = (Math.sqrt((r * r) - (c * c)) - d);

        double	dtoc_mini = (Math.sqrt((r * r) - (d * d)) - c);
        double	dtoa_mini = (Math.sqrt((r * r) - (d * d)) - a);

        double	ab_mini = Math.sqrt((atob_mini * atob_mini) + (btoa_mini * btoa_mini));
        double	ad_mini = Math.sqrt((atod_mini * atod_mini) + (dtoa_mini * dtoa_mini));

        double	cb_mini = Math.sqrt((ctob_mini * ctob_mini) + (btoc_mini * btoc_mini));
        double	cd_mini = Math.sqrt((ctod_mini * ctod_mini) + (dtoc_mini * dtoc_mini));

        double	h_ab = Math.sqrt((r * r) - (ab_mini / 2) * (ab_mini / 2));
        double	h_ad = Math.sqrt((r * r) - (ad_mini / 2) * (ad_mini / 2));
        double	h_cb = Math.sqrt((r * r) - (cb_mini / 2) * (cb_mini / 2));
        double	h_cd = Math.sqrt((r * r) - (cd_mini / 2) * (cd_mini / 2));

        double	S_tre_ab = segment(r, h_ab) + ((atob_mini * btoa_mini) / 2);
        double	S_tre_ad = segment(r, h_ad) + ((atod_mini * dtoa_mini) / 2);
        double	S_tre_cb = segment(r, h_cb) + ((ctob_mini * btoc_mini) / 2);
        double	S_tre_cd = segment(r, h_cd) + ((ctod_mini * dtoc_mini) / 2);

        double	S_segm_abcd = segm_b + segm_a + segm_c + segm_d -
                (
                        + (atob_mini > 0 ? S_tre_ab : 0)
                                + (btoc_mini > 0 ? S_tre_cb : 0)
                                + (ctod_mini > 0 ? S_tre_cd : 0)
                                + (dtoa_mini > 0 ? S_tre_ad : 0)
                );
        return S - S_segm_abcd;
    }

    public static double segment(double r, double a)
    {
        if (r >= a)
            return 0.5 * r * r * (2 * Math.acos(a / (r * 1.0)) - Math.sin(2 * Math.acos(a / (r * 1.0))));
        else
            return 0;
    }

//    public static int   getRadius(int area, Location loc)
//    {
//        int a = loc.getBlockX() + graf.size_world;
//        int b = loc.getBlockZ() + graf.size_world;
//
//        for (int i = 0; i < (graf.size_world * 2); i++)
//        {
//            if (ost(i, a, b, (graf.size_world * 2) - a, (graf.size_world * 2) - b) >= area)
//                return i;
//        }
//        return -1;
//    }

    public static void intersectionClans(Clan lose, Clan winer)
    {
        int x_lose = lose.getCenter().getBlockX();
        int z_lose = lose.getCenter().getBlockZ();

        int x_winer = winer.getCenter().getBlockX();
        int z_winer = winer.getCenter().getBlockZ();

        int r_lose = lose.getRadius();
        int r_winer = winer.getRadius();

        int distance = (int) Math.sqrt(
                Math.pow(Math.abs(x_lose - x_winer), 2) +
                        Math.pow(Math.abs(z_lose - z_winer), 2)
        );

        int peresech = 0;

        if (distance + r_lose < r_winer)
            peresech = lose.getInfl();
        else
        if (distance + r_winer < r_lose)
            peresech = winer.getInfl();
        else
            peresech = getSperesech(lose, winer);

        winer.setInfl((int) (winer.getInfl() + Math.max(peresech * 0.75, 1)));
        CLanWin(winer, (int) Math.max(peresech * 0.75, 1));
        lose.setInfl(Math.max(lose.getInfl() - peresech, 0));
        if (lose.getInfl() == 0)
            lose.deleteClan();
        else
            CLanLose(lose, Math.max(peresech, 0));
    }

    private static void CLanWin(Clan clan, int infl)
    {
        for (String pl : clan.getMembers())
        {
            player p = player.getPlayer(pl);
            if (p.pl != null)
                p.pl.sendMessage(rgb.gradientLight(chat.color[0], "Ваша фракция выиграла #" + infl + "# " + chat.getNormForm(infl, "очко", "очка", "очков") + " влияния"));
        }
        if (clan.getOwner().pl != null)
            clan.getOwner().pl.sendMessage(rgb.gradientLight(chat.color[0], "Ваша фракция выиграла #" + infl + "# " + chat.getNormForm(infl, "очко", "очка", "очков") + " влияния"));
    }

    private static void CLanLose(Clan clan, int infl)
    {
        for (String pl : clan.getMembers())
        {
            player p = player.getPlayer(pl);
            if (p.pl != null)
                p.pl.sendMessage(rgb.gradientLight(chat.color[0], "Ваша фракция потеряла #" + infl + "# " + chat.getNormForm(infl, "очко", "очка", "очков") + " влияния"));
        }
        if (clan.getOwner().pl != null)
            clan.getOwner().pl.sendMessage(rgb.gradientLight(chat.color[0], "Ваша фракция потерял #" + infl + "# " + chat.getNormForm(infl, "очко", "очка", "очков") + " влияния"));
    }

    public static int getSperesech(Clan a, Clan b)
    {
        int x_lose = a.getCenter().getBlockX();
        int z_lose = a.getCenter().getBlockZ();

        int x_winer = b.getCenter().getBlockX();
        int z_winer = b.getCenter().getBlockZ();

        int r_lose = a.getRadius();
        int r_winer = b.getRadius();

        int distance = (int) Math.sqrt(
                Math.pow(Math.abs(x_lose - x_winer), 2) +
                        Math.pow(Math.abs(z_lose - z_winer), 2)
        );

        double a_lose = (Math.pow(distance, 2) + Math.pow(r_lose, 2) - Math.pow(r_winer, 2)) / (2 * distance);
        double a_winer = (Math.pow(distance, 2) - Math.pow(r_lose, 2) + Math.pow(r_winer, 2)) / (2 * distance);

        double s_lose = segment(r_lose, a_lose);
        double s_winer = segment(r_winer, a_winer);

        return (int) (s_lose + s_winer);
    }

    public static void GiveClanEffects()
    {
        if (tick_pay++ == 10)
            tick_pay = 0;
        for (Player pl : Bukkit.getWorld("world").getPlayers())
        {
            player p = player.getPlayer(pl);
            ArrayList<Clan> clans = Clan.getClanLocation(p.pl.getLocation().getBlockX(), p.pl.getLocation().getBlockZ());
            for (Clan clan : clans)
            {
                boolean give = false;
                if (!clan.getBeakonStay())
                    continue ;
                if (p.getClan() != null && p.getClan().getName().equals(clan.getName()))
                    give = true;
                else
                {
                    for (reg region : p.getRegs())
                    {
                        if (region.getClans().contains(clan))
                        {
                            give = true;
                            break ;
                        }
                    }
                }
                if (give)
                {
                    for (ClanEffects effect : ClanEffects.Effects)
                    {
                        if (clan.getEffectsIsOn(effect.effect_id))
                        {
                            if (tick_pay == 10)
                            {
                                if (clan.getKazna() > (effect.price_tick * clan.getEffectsLvl(effect.effect_id)))
                                {
                                    clan.setKazna((int) (clan.getKazna() - (effect.price_tick * clan.getEffectsLvl(effect.effect_id))));
                                    p.pl.addPotionEffect(
                                            new PotionEffect(effect.type, 20 * 25, clan.getEffectsLvl(effect.effect_id) - 1, true, true)
                                    );
                                }
                            }
                            else
                            {
                                if (clan.getKazna() > (effect.price_tick * clan.getEffectsLvl(effect.effect_id)))
                                {
                                    p.pl.addPotionEffect(
                                            new PotionEffect(effect.type, 20 * 25, clan.getEffectsLvl(effect.effect_id) - 1, true, true)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
