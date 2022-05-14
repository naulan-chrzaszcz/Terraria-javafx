package fr.sae.terraria.modele;

import java.io.FileReader;

import java.util.Arrays;

import com.google.gson.stream.JsonReader;


public class TileMaps
{
    // Constantes
    public static final int TILE_DEFAULT_SIZE = 16;
    public static final int SKY = 0;
    public static final int STONE = 1;
    public static final int DIRT = 2;
    public static final int FLOOR_TOP = 3;
    public static final int FLOOR_LEFT = 4;
    public static final int FLOOR_RIGHT = 5;

    // Qui concerne la carte
    private int[][] maps;
    private int w;
    private int h;


    /**
     * Compte la taille de la carte et ensuite la charge dans
     *  la variable maps
     *
     *  @param path Le chemin depuis le root (src)
     */
    public void load(String path)
    {
        boolean widthSave = false;
        int i = 0;
        int j = 0;

        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader))
        {
            jsonReader.beginObject();
            // Déduit la taille de la carte.
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
            maps = new int[h][w];
            // Ecrit la carte dans la mémoire.
            jsonReader.beginObject();
            while (jsonReader.hasNext())
            {
                jsonReader.nextName();

                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    maps[i][j] = jsonReader.nextInt();
                    j++;
                }
                jsonReader.endArray();

                j = 0;
                i++;
            }
            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Crée une variable de la même taille que la carte couramment utilisé
     *  La copie et renvoie la variable qui a copié la carte du jeu
     *
     * @return Un tableau 2D qui contient la carte qui est utilisé dans le jeu.
     */
    public int[][] copy()
    {
        int[][] mapCopy = new int[maps.length][maps[0].length];

        for (int i = 0; i < this.maps.length; i++)
            mapCopy[i] = Arrays.copyOf(maps[i], maps[i].length);

        return mapCopy;
    }

    public void clear()
    {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                maps[y][x] = SKY;
    }

    public void fill(int tileIndex)
    {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                maps[y][x] = tileIndex;
    }


    public int getHeight() { return maps.length; }
    public int getWidth() { return maps[0].length; }
    public int getTile(int x, int y) { return maps[y][x]; }

    public void setTile(int tileIndex, int y, int x) { maps[y][x] = tileIndex; }
}
