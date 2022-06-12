package fr.sae.terraria.modele;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;


/**
 * <h1>Tile maps</h1>
 * <h2>Génère à l'écran la carte tuilé</h2>
 * <h3><u>Description:</u></h3>
 * <p>Cette classes permet à partir d'un fichier <code>.json</code> de chargé les données de la carte</p>
 * <p>Il est conservé dans la variable <code>maps</code></p>
 *
 * @author CHRZASZCZ Naulan
 */
public class TileMaps
{
    public static final int TILE_DEFAULT_SIZE = 16;

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

    public boolean isDirtTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.DIRT.ordinal(); }
    public boolean isSkyTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.SKY.ordinal(); }
    public boolean isRockTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.ROCK.ordinal(); }
    public boolean isFloorTopTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.FLOOR_TOP.ordinal(); }
    public boolean isFloorRightTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.FLOOR_RIGHT.ordinal(); }
    public boolean isFloorLeftTile(double x, double y) { return this.getTile((int) x, (int) y) == TileSet.FLOOR_LEFT.ordinal(); }


    public int getHeight() { return this.maps.length; }
    public int getWidth() { return this.maps[0].length; }
    public int getTile(int x, int y) { return this.maps[y][x]; }

    /**
     * Remplace grâce aux coordonnées entrées, un tile par celui qui est mis dans l'argument
     *
     * @param tileIndex Le tile qui doit remplacer une autre.
     * @param x la position horizontal de là où le tile doit être écrit
     * @param y la position vertical de là où le tile doit être écrit
     */
    public void setTile(int tileIndex, int y, int x) { this.maps[y][x] = tileIndex; }
}
