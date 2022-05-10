package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TileMaps {
    private Map<Integer, List<Integer>> map;


    public TileMaps(int w, int h) {
        this.map = new HashMap<>();
    }

    public void load(String path)
    {
        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader)) {
            // Demarrage de la lecture du fichier.
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                map.put(Integer.parseInt(name), new ArrayList<>());

                // Commence à lire la liste contenue dans la key
                jsonReader.beginArray();
                while (jsonReader.hasNext())
                    map.get(Integer.parseInt(name)).add(jsonReader.nextInt());
                jsonReader.endArray();
            }

            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public int getMaxHeight() { return map.size(); }
    public int getMaxWidth() { return map.get(0).size(); }
    public int getTile(int y, int x) { return map.get(y).get(x); }
}
