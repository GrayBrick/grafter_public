package Json;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class J
{
    public String       file_name;
    public JSONObject   json;

    public J(String file_name)
    {
        this.file_name = file_name;
        File file = new File(file_name);
        try
        {
            file.createNewFile();
        } catch (Exception ex){
            System.out.println("Не удалось создать файл " + file_name);
        }
        try
        {
            openJson();
        } catch(Exception ignored){}
    }

    public void openJson()
    {
        try
        {
            Object obj = new JSONParser().parse(new FileReader(file_name));
            json = (JSONObject) obj;
        } catch(Exception ex){
            json = new JSONObject();
        }
    }

    public void saveJson()
    {
        try
        {
            try (PrintStream out = new PrintStream(new FileOutputStream(file_name))) {
                out.print(json.toJSONString());
            }
        } catch(Exception ex){
            System.out.println("Не удалось сохранить файл " + file_name);
        }
    }
}
