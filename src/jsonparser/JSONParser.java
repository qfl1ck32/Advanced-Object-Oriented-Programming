package jsonparser;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.json.*;

import java.util.ArrayList;

public class JSONParser {
    private String filename;
    private JSONObject JSON;
    private Iterator <?> keys;

    {
        filename = null;
    }

    private boolean openFile(String filename) {
        this.filename = filename;

        try {
            Path path = Paths.get(filename);
            String JSON = Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.joining(System.lineSeparator()));
            this.JSON = new JSONObject(JSON);
            this.keys = this.JSON.keys();
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    
    public JSONParser(JSONObject JSON) {
        this.JSON = JSON;
        this.keys = this.JSON.keys();
    }

    public JSONParser(Object o) {
        this.JSON = (JSONObject) o;
        this.keys = this.JSON.keys();
    }
    
    public JSONParser(String filename) {
        this.filename = filename;

        this.openFile(this.filename);
    }

    public boolean saveFile() {
        if (filename == null)
            return false;

        try {
            FileWriter file = new FileWriter(filename);

            file.write(JSON.toString(4));

            file.close();
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }


    public Object getObject(String key) {
        try {
            return JSON.get(key);
        }

        catch (Exception e) {
            return null;
        }
    }
  
    public String getString(String key) {
        try {
            return JSON.getString(key);
        }

        catch (Exception e) {
            return null;
        }
    }

    public Integer getInteger(String key) {
        try {
            return JSON.getInt(key);
        }

        catch (Exception e) {
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        try {
            return JSON.getBoolean(key);
        }

        catch (Exception e) {
            return null;
        }
    }

    public ArrayList <String> getStringArray(String key) {
        try {
            ArrayList <String> ans = new ArrayList <> ();

            JSONArray arr = JSON.getJSONArray(key);

            for (int i = 0; i < arr.length(); ++i)
                ans.add((String) arr.get(i));

            return ans;
        }

        catch (Exception e) {
            return null;
        }
    }

    public JSONObject getJSON(String key) {
        try {
            return JSON.getJSONObject(key);
        }

        catch (Exception e) {
            return null;
        }
    }

    public JSONObject getJSON() {
        return this.JSON;
    }

    public boolean setObject(String key, Object value) {
        try {
            JSON.put(key, value);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }

    public Object createJSON() {
        return (Object) (new JSONObject());
    }

    public String nextKey() {
        try {
            return (String) keys.next();
        }
        
        catch (Exception e) {
            return null;
        }
    }

    public Boolean hasKey(String key) {
        return JSON.has(key);
    }
}