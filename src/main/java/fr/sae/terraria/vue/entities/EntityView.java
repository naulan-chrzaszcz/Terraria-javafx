package fr.sae.terraria.vue.entities;

import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public abstract class EntityView
{
    protected final ImageView imgView;
    protected final Entity entity;


    protected EntityView(final Entity entity)
    {
        this.entity = entity;

        this.imgView = new ImageView();
        this.imgView.translateXProperty().bind(entity.getXProperty());
        this.imgView.translateYProperty().bind(entity.getYProperty());
    }

    protected abstract void animation(int frame);

    private void startAnimation()
    {
        int widthEntity = this.entity.getRect().getWidth();
        // int heightEntity = this.entity.getRect().getHeight();

        // Change la limite de frame de l'animation selon le sprite sheet chargé
        this.imgView.imageProperty().addListener((obs, oldImg, newImg) -> {
            int newEndFrame = (int) (newImg.getWidth() / widthEntity);
            if (this.entity.getAnimation().getFrame() >= newEndFrame)
                this.entity.getAnimation().reset();
            this.entity.getAnimation().setEndFrame(newEndFrame);
        });

        this.entity.getAnimation().getFrameProperty().addListener((obs, oldFrame, newFrame) -> this.animation(newFrame.intValue()));
    }

    public void display(final Pane display)
    {
        display.getChildren().add(this.imgView);
        this.startAnimation();
    }


    public ImageView getImgView() { return this.imgView; }
}
