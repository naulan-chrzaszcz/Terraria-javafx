package fr.sae.terraria.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class RabbitView
{
    private final ImageView rabbitImgView;

    private final Image rabbitImg;

    private final Pane display;


    public RabbitView(Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.display = display;

        this.rabbitImg = View.loadAnImage("rabbit/rabbit_left.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.rabbitImgView = new ImageView(rabbitImg);
    }
}
