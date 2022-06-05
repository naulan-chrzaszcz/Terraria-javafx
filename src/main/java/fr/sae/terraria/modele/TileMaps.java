package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;


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


    public TileMaps() { super(); }

    /**
     * Compte la taille de la carte et ensuite la charge dans
     *  la variable maps
     *
     *  @param path Le chemin depuis le root (src)
     */
    public void load(final String path)
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
                this.h++;

                jsonReader.nextName();

                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.nextInt();
                    if (!widthSave) this.w++;
                }
                jsonReader.endArray();

                widthSave = true;
            }
            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }


        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = new JsonReader(fileReader))
        {
            this.maps = new int[this.h][this.w];
            // Ecrit la carte dans la mémoire.
            jsonReader.beginObject();
            while (jsonReader.hasNext())
            {
                jsonReader.nextName();

                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    this.maps[i][j] = jsonReader.nextInt();
                    j++;
                }
                jsonReader.endArray();

                j = 0;
                i++;
            }
            jsonReader.endObject();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public int getHeight() { return this.maps.length; }
    public int getWidth() { return this.maps[0].length; }
    public int getTile(int x, int y) { return this.maps[y][x]; }

    public void setTile(int tileIndex, int y, int x) { this.maps[y][x] = tileIndex; }
}
