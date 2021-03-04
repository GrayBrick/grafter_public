package clans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import own.graf;
import own.player;

import java.util.ArrayList;

public class SortClans
{
    final static private String list_char = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    public static void sortAll(Scoreboard board)
    {
        ArrayList<Clan> clans = new ArrayList<>();

        for (String uid : graf.Clans.getKeys(false))
        {
            Clan clan = Clan.getClan(uid);
            if (clan.isExist())
                clans.add(clan);
        }
        clans.sort((a, b) -> (b.getInfl() - a.getInfl()));

        board.getTeams().forEach(Team::unregister);

        int position = 1;

        for (Clan clan : clans)
        {
            Team Glava = board.registerNewTeam(position + " " + list_char.charAt(0) + " " + clan.getName());
            setTeam(Glava, clan, Clan.getColorRank((byte) 50) + Clan.chat_rank);

            Team Zam = board.registerNewTeam(position + " " + list_char.charAt(1) + " " + clan.getName());
            setTeam(Zam, clan, Clan.getColorRank((byte) 45) + Clan.chat_rank);

            Team Com = board.registerNewTeam(position + " " + list_char.charAt(2) + " " + clan.getName());
            setTeam(Com, clan, Clan.getColorRank((byte) 41) + Clan.chat_rank);

            Team Boy = board.registerNewTeam(position + " " + list_char.charAt(3) + " " + clan.getName());
            setTeam(Boy, clan, Clan.getColorRank((byte) 2) + Clan.chat_rank);

            Team newBoy = board.registerNewTeam(position + " " + list_char.charAt(4) + " " + clan.getName());
            setTeam(newBoy, clan, Clan.getColorRank((byte) 1) + Clan.chat_rank);

            Glava.addEntry(clan.getOwner().p);
            for (String pl : clan.getMembers())
            {
                player member = player.getPlayer(pl);
                switch (member.rank)
                {
                    case (45) :
                    {
                        Zam.addEntry(pl);
                        break ;
                    }
                    case (41) :
                    {
                        Com.addEntry(pl);
                        break ;
                    }
                    case (2) :
                    {
                        Boy.addEntry(pl);
                        break ;
                    }
                    case (1) :
                    {
                        newBoy.addEntry(pl);
                        break ;
                    }
                }
            }
            position++;
        }
        Team all = board.registerNewTeam(position + " " + list_char.charAt(0) + " " + "jast");
        for (Player pl : Bukkit.getOnlinePlayers())
        {
            if (player.getPlayer(pl).clan_uid == null)
                all.addEntry(pl.getName());
        }
    }

    public static void setTeam(Team team, Clan clan, String suffix)
    {
        team.setPrefix(player.getPrefixFull(Bukkit.getWorld("world"), clan.getChatColor() + clan.getName()));
        team.setSuffix(" " + suffix);
    }
}
