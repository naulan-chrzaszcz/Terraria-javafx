package fr.sae.terraria.vue.hud;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.GenerateEntity;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Rect;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
import fr.sae.terraria.modele.entities.items.Meat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class MouseCursorView
{
    private Rectangle mouseCursorRect;
    private double scaleMultiplicatorWidth;


    /**
     * Un rectangle rouge qui suit la souris
     *   Permet de savoir où nous cliquons sur l'écran plus précisément sur quel tile
     *
     * @param display L'afficheur qui se gère du HUD
     * @param scaleMultiplicatorWidth Scaling en largeur
     * @param scaleMultiplicatorHeight Scaling en hauteur
     */
    public MouseCursorView(Pane HUD, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;

        int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth);
        int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight);

        mouseCursorRect = new Rectangle(tileWidth, tileHeight);
        mouseCursorRect.setFill(Color.TRANSPARENT);
        mouseCursorRect.setStroke(Color.RED);
        mouseCursorRect.setStrokeWidth(scaleMultiplicatorWidth);
        this.setCursorAnimation();

        HUD.addEventFilter(MouseEvent.MOUSE_MOVED, mouse -> {
            int xCursor = (int) ((mouse.getX()+((Rectangle) display.getParent().getClip()).getX())/tileWidth) * tileWidth;
            int yCursor = (int) ((mouse.getY()+((Rectangle) display.getParent().getClip()).getY())/tileHeight) * tileHeight;

            mouseCursorRect.setX(xCursor);
            mouseCursorRect.setY(yCursor);
        });
        display.getChildren().add(mouseCursorRect);
    }

    private void setCursorAnimation()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);
        double[] time = new double[] {1};
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            mouseCursorRect.setStrokeWidth((Math.cos(time[0]) * scaleMultiplicatorWidth)+scaleMultiplicatorWidth);

            time[0] += .05;
        }));
        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }
}
