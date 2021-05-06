package JSONParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
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
        try {
            this.filename = filename;

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

    public JSONParser() {
        this.JSON = new JSONObject();
        this.keys = this.JSON.keys();
    }



    public boolean put(String key, JSONParser value)  {
        try {
            this.JSON.put(key, value.JSON);
            return true;
        }

        catch (JSONException e) {
            return false;
        }
    }

    public boolean put(String key, String value) {
        try {
            this.JSON.put(key, value);
            return true;
        }

        catch (JSONException e) {
            return false;
        }
    }

    public boolean put(String key, List <?> value) {
        try {
            this.JSON.put(key, value);
            return true;
        }

        catch (JSONException e) {
            return false;
        }
    }

    public boolean put(String key, boolean value) {
        try {
            this.JSON.put(key, value);
            return true;
        }

        catch (JSONException e) {
            return false;
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

    public JSONParser getJSONParser(String key) {
        try {
            return new JSONParser(JSON.get(key));
        }

        catch (Exception ignored) {
            return null;
        }
    }

    public boolean saveModifications() {
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