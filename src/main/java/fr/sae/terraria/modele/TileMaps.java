package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;


public class TileMaps
{
    public final static int tileDefaultSize = 16;
    public final static int SKY = 0;
    public final static int STONE = 1;
    public final static int DIRT = 2;
    public final static int FLOOR_TOP = 3;
    public final static int FLOOR_LEFT = 4;
    public final static int FLOOR_RIGHT = 5;

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
            for (int j = 0; j < this.maps[i].length; j++)
                mapCopy[i][j] = this.maps[i][j];

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
    public int getTile(int y, int x) { return maps[y][x]; }

    public void setTile(int tileIndex, int y, int x) { maps[y][x] = tileIndex; }
}
