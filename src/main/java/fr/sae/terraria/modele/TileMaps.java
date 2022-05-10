package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;


public class TileMaps
{
    private int[][] map;
    private int w;
    private int h;


    public void load(String path)
    {
        int i = 0;
        int j = 0;

        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader)) {

            jsonReader.beginObject();
            // Deduit la taille de la carte.
            while (jsonReader.hasNext())
            {
                h++;
                jsonReader.beginArray();
                while (jsonReader.hasNext())
                    w++;
                jsonReader.endArray();
            }
            jsonReader.endObject();

            map = new int[h][w];
            // Ecrit la carte dans la mémoire.
            jsonReader.beginObject();
            while (jsonReader.hasNext())
            {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    map[i][j] = jsonReader.nextInt();
                    j++;
                }
                jsonReader.endArray();

                j = 0;
                i++;
            }
            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void clear()
    {
        for(int y = 0; y < getHeight(); y++)
            continue;
    }

    public int getHeight() { return map.length; }
    public int getWidth() { return map[0].length; }
    public int getTile(int y, int x) { return map[y][x]; }
    public void setTile(int tileIndex, int y, int x) { map[y][x] = tileIndex; }
}
