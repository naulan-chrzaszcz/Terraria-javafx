package fr.sae.terraria.modele.entities.entity;

import javafx.geometry.Rectangle2D;


public class Rect
{
    private Rectangle2D rect;

    private int width;
    private int height;


    public Rect(double x, double y, int width, int height)
    {
        this.rect = new Rectangle2D(x, y, width, height);

        this.width = width;
        this.height = height;
    }

    public void update(double x, double y) { this.rect = new Rectangle2D(x, y, width, height); }

    public Rectangle2D collideRect(Rect rect) { return (this.rect.intersects(rect.get())) ? rect.get() : null; }

    public Rectangle2D get() { return this.rect; }
}
