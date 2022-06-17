package fr.sae.terraria.vue.hud;

import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.View;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HealthBarView
{
    private final Image healthBarImg;
    private Player player;
    private Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;


    public HealthBarView(Player player, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;
        this.display = display;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.healthBarImg = View.loadAnImage("health.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
    }

    /** Affiche la barre de vie du joueur */
    private void displayHealthBar(double inventoryBarX, double inventoryBarY)
    {
        // Crée et positionne les cœurs dans un tableau de longueur qui correspond à la vie max du joueur.
        ImageView[] healths = new ImageView[(int) (player.getPvMax())];
        for (int i = 0; i < player.getPvMax(); i++) {
            ImageView healthView = new ImageView(healthBarImg);
            Rectangle2D viewPort = new Rectangle2D((healthBarImg.getWidth()/3)*2, 0, (healthBarImg.getWidth()/3), healthBarImg.getHeight());

            healthView.setViewport(viewPort);
            healthView.setX(inventoryBarX + ((healthView.getImage().getWidth()/3)*i));
            healthView.setY((inventoryBarY - healthView.getImage().getHeight()) - (2*scaleMultiplicatorHeight));
            display.getChildren().add(healthView);
            healths[i] = healthView;
        }

        // Modifie le cœur selon la vie du joueur
        player.pvProperty().addListener((obs, oldPv, newPv) -> {
            ImageView healthView;
            if (newPv.intValue() > oldPv.intValue()) {
                healthView = healths[newPv.intValue()-1];
                Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*2, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());
                healthView.setViewport(viewPort);
            }

            if (newPv.intValue() < oldPv.intValue()) {
                healthView = healths[oldPv.intValue()-1];
                Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*0, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());
                healthView.setViewport(viewPort);
            }
        });
    }

    public void display(double inventoryBarX, double inventoryBarY) { this.displayHealthBar(inventoryBarX, inventoryBarY); }
}
