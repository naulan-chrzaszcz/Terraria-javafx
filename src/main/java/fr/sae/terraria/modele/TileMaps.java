package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URI;

import static fr.sae.terraria.modele.TilesIndex.SKY;


public class TileMaps
{
    private int[][] map;
    private int w;
    private int h;


    public void load(String path)
    {
        boolean widthSave = false;
        int i = 0;
        int j = 0;

        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader))
        {
            jsonReader.beginObject();
            // Deduit la taille de la carte.
            while (jsonReader.hasNext())
            {
                h++;

                jsonReader.nextName();

                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.nextInt();
                    if (!widthSave) w++;
                }
                jsonReader.endArray();

                widthSave = true;
            }
            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }


        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader))
        {
            map = new int[h][w];
            // Ecrit la carte dans la mémoire.
            jsonReader.beginObject();
            while (jsonReader.hasNext())
            {
                jsonReader.nextName();

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

    public int[][] copy()
    {
        int[][] mapCopy = new int[map.length][map[0].length];

        for (int i = 0; i < this.map.length; i++)
            for (int j = 0; j < this.map[i].length; j++)
                mapCopy[i][j] = this.map[i][j];

        return mapCopy;
    }

    public void clear()
    {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                map[y][x] = SKY;
    }

    public void fill(int tileIndex)
    {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                map[y][x] = tileIndex;
    }

    public int getHeight() { return map.length; }
    public int getWidth() { return map[0].length; }
    public int getTile(int y, int x) { return map[y][x]; }
    public void setTile(int tileIndex, int y, int x) { map[y][x] = tileIndex; }
}
