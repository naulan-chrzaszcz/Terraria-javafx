package fr.sae.terraria.modele.entities.entity;

import javafx.geometry.Rectangle2D;


public class Rect
{
    private Rectangle2D rect;


    public Rect(double x, double y, int width, int height)
    {
        this.rect = new Rectangle2D(x, y, width, height);
    }

    public void update(double x, double y)
    {
        double width = this.rect.getWidth();
        double height = this.rect.getHeight();

        this.rect = new Rectangle2D(x, y, (int) width, (int) height);
    }

    public boolean collideRect(Rect rect) { return this.rect.intersects(rect.get()); }


    public Rectangle2D get() { return this.rect; }
}
