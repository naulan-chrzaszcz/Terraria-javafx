package fr.sae.terraria.modele.entities.entity;

import javafx.geometry.Rectangle2D;


public class Rect
{
    private Rectangle2D value;


    public Rect(double x, double y, int width, int height) { this.value = new Rectangle2D(x, y, width, height); }

    /**
     * Mets à jour la position du rectangle géré par JavaFX
     * @param x La position du Rectangle sur l'axe des abscisses
     * @param y La position du Rectangle sur l'axe des ordonnées
     */
    public void updates(double x, double y)
    {
        double width = this.value.getWidth();
        double height = this.value.getHeight();

        this.value = new Rectangle2D(x, y, (int) width, (int) height);
    }

    /** Permet de savoir si deux rectangles sont en collision */
    public boolean collideRect(Rect rect) { return this.value.intersects(rect.get()) || this.value.contains(rect.get()); }
    /** Permet de savoir si deux rectangles sont en collision */
    public boolean collideRect(Rectangle2D rect) { return this.value.intersects(rect) || this.value.contains(rect); }

    /** Permet d'avoir le Rectangle qui est géré par JavaFX */
    public Rectangle2D get() { return this.value; }
    public int getWidth() { return (int) (this.value.getWidth()); }
    public int getHeight() { return (int) (this.value.getHeight()); }
}
