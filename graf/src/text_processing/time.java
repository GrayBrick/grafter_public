package text_processing;

import org.bukkit.Bukkit;
import own.chat;

import java.util.Date;

public class time
{
    public static String getNowTime()
    {
        Date    date = new Date();
        String  str = "";

        if (date.getHours() < 10)
            str += "0" + date.getHours();
        else
            str += date.getHours();
        str += ":";
        if (date.getMinutes() < 10)
            str += "0" + date.getMinutes();
        else
            str += date.getMinutes();
        return str;
    }

    public static String getTime(Date d1, Date d2)
    {
        long date = d1.getTime() - d2.getTime();
        String  str = "";

        int sec = (int) ((date / 1000) % 60);
        int min = (int) ((date / (1000 * 60)) % 60);
        int hours = (int) ((date / (1000 * 60 * 60)) % 24);
        int day = (int) ((date / (1000 * 60 * 60 * 24)));

        if (day != 0)
            str += day + chat.getNormForm(day, " день ", " дня ", " дней ");

        if (hours != 0)
            str += hours + chat.getNormForm(hours, " час ", " часа ", " часов ");

        if (min != 0)
            str += min + chat.getNormForm(min, " минута ", " минуты ", " минут ");

        return str;
    }

    public static String getTime(long d1, long d2)
    {
        long date = d1 - d2;
        String  str = "";

        int sec = (int) ((date / 1000) % 60);
        int min = (int) ((date / (1000 * 60)) % 60);
        int hours = (int) ((date / (1000 * 60 * 60)) % 24);
        int day = (int) ((date / (1000 * 60 * 60 * 24)));

        if (day != 0)
            str += day + chat.getNormForm(day, " день ", " дня ", " дней ");

        if (hours != 0)
            str += hours + chat.getNormForm(hours, " час ", " часа ", " часов ");

        if (min != 0)
            str += min + chat.getNormForm(min, " минута ", " минуты ", " минут ");

        if (sec != 0)
            str += sec + chat.getNormForm(sec, " секунда ", " секунды ", " секунд ");

        return str;
    }
}
