package fr.sae.terraria.vue;

import fr.sae.terraria.modele.TileMaps;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class MouseCursorView
{
    private Rectangle mouseCursorRect;


    public MouseCursorView(Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth);
        int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight);
        mouseCursorRect = new Rectangle(tileWidth, tileHeight);

        mouseCursorRect.setFill(Color.TRANSPARENT);
        mouseCursorRect.setStroke(Color.RED);
        mouseCursorRect.setStrokeWidth(2*scaleMultiplicatorWidth);

        display.addEventFilter(MouseEvent.MOUSE_MOVED, mouse -> {
            int xCursor = (int) (mouse.getX()/tileWidth) * tileWidth;
            int yCursor = (int) (mouse.getY()/tileHeight) * tileHeight;

            mouseCursorRect.setX(xCursor);
            mouseCursorRect.setY(yCursor);

        });
        display.getChildren().add(mouseCursorRect);
    }
}
