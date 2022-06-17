package fr.sae.terraria.vue.entities;

import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 * <h1>Entity View</h1>
 * <h2>Une classe qui permet d'affiché des entités</h2>
 * <h3><u>Description:</u></h3>
 * <p>Cette classe permet simplement grâce à la fonction <code>animation()</code> de codé une animations pour une entité en particulier</p>
 * <p>Cette classe gère les choses suivantes:</p>
 * <ul>
 *     <li>Crée une ImageView, pas besoin dans recrée une autre dans les classes qui hériterais cette classes.</li>
 *     <li>Bindings des positions de l'entité à l'ImageView.</li>
 *     <li>Change dynamiquement la limite de frame selon l'image utilisé.</li>
 * </ul>
 *
 * @author CHRZASZCZ Naulan
 */
public abstract class EntityView
{
    protected final ImageView imgView;
    protected final Entity entity;


    protected EntityView(final Entity entity)
    {
        this.entity = entity;

        this.imgView = new ImageView();
        this.imgView.translateXProperty().bind(entity.xProperty());
        this.imgView.translateYProperty().bind(entity.yProperty());
    }

    /** l'endroit où il va avoir les animations qui vont êtres programmer. */
    protected abstract void animation(int frame);

    /** La fonction qui permet de démarrer les animations des entités */
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

        this.entity.getAnimation().getFrameProperty().addListener((obs, oldFrame, newFrame) -> {
            if(newFrame.intValue() >= this.entity.getAnimation().getEndFrame())
                this.animation(oldFrame.intValue());
            else this.animation(newFrame.intValue());
        });
    }

    /** Affiche l'entité à l'écran et démarre l'animation */
    public void display(final Pane display)
    {
        display.getChildren().add(this.imgView);
        this.startAnimation();
    }


    public ImageView getImgView() { return this.imgView; }
}
