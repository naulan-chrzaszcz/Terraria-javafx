package fr.sae.terraria.vue.hud;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.vue.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

public class ClockView
{
    private final Image clockCursorImg;
    private final Image clockImg;
    private final Clock gameTime;
    private final Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;


    public ClockView(Clock gameTime, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.gameTime = gameTime;
        this.display = display;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.clockImg = View.loadAnImage("clock.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);
        this.clockCursorImg = View.loadAnImage("clock-cursor.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);
    }

    /** Affiche une horloge à aiguille à l'écran. */
    private void displayClock(Image inventoryBarImg, double inventoryBarX, double inventoryBarY)
    {
        ImageView clockCursorView = new ImageView(clockCursorImg);
        ImageView clockView = new ImageView(clockImg);
        Rotate rotate = new Rotate();
        double yClock = inventoryBarY - clockImg.getHeight();
        double xClock = inventoryBarX + (inventoryBarImg.getWidth()/2 - clockImg.getWidth()/2);

        clockCursorView.setX(xClock + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2));
        clockCursorView.setY(yClock);
        clockView.setX(xClock);
        clockView.setY(yClock);

        rotate.setPivotX(xClock + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2) + (clockCursorImg.getWidth()/2));
        rotate.setPivotY(yClock + clockCursorImg.getHeight());
        gameTime.minutesProperty().addListener(((obs, oldV, newV) -> rotate.setAngle((newV.intValue()/8) - 90)));
        clockCursorView.getTransforms().add(rotate);

        display.getChildren().add(clockView);
        display.getChildren().add(clockCursorView);
    }

    public void display(Image inventoryBarImg, double inventoryBarX, double inventoryBarY) { this.displayClock(inventoryBarImg, inventoryBarX, inventoryBarY); }
}
