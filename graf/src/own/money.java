package own;

import text_processing.rgb;

public class money
{
    public static boolean getMoney(player p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(rgb.gradientLight(chat.color[0], "У вас " + p.money + " скитов"));
            return true;
        }
        return true;
    }
}
