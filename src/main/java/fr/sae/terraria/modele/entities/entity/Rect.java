package fr.sae.terraria.modele.entities.entity;

import javafx.geometry.Rectangle2D;


public class Rect
{
    private Rectangle2D rect;

    // Taille du rectangle
    private int width;
    private int height;


    /**
     * Crée un Rectangle qui fonctionne avec Rectangle2D
     *
     * @param x Position en x du rectangle
     * @param y Position en y du Rectangle
     * @param width La largeur du Rectangle
     * @param height La longueur du Rectangle
     */
    public Rect(double x, double y, int width, int height)
    {
        this.rect = new Rectangle2D(x, y, width, height);

        this.width = width;
        this.height = height;
    }

    /**
     * Permet de mettre a jour les coordonnées en x et en y du rectangle
     *
     * @param x Les nouvelles coordonnées du rectangle en x
     * @param y Les nouvelles coordonnées du rectangle en y
     */
    public void update(double x, double y) { this.rect = new Rectangle2D(x, y, width, height); }

    /**
     * Regarde si ce Rectangle et un autre Rectangle mis en paramètre est en intersections
     *
     * @param rect Specifie le Rectangle de l'entité qu'on veut savoir s'ils ont en collision
     * @return Retourne null s'il n'y a pas de collision entre les deux Rectangles sinon retourne le Rectangle de l'entité
     */
    public Rectangle2D collideRect(Rect rect) { return (this.rect.intersects(rect.get())) ? rect.get() : null; }

    /** Obtient le rectangle crée par JavaFX */
    public Rectangle2D get() { return this.rect; }
}
