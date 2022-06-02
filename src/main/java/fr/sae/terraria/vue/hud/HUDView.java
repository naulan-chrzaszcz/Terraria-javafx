package fr.sae.terraria.vue.hud;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.scene.layout.Pane;


public class HUDView
{
    private final Clock gameTime;
    private final Player player;
    private final Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;


    /**
     * Affiche tout ce qui concerne l'HUD
     *
     * @param gameTime Pour afficher l'horloge
     * @param display Sur quel pane l'HUD doit s'afficher
     */
    public HUDView(Player player, Clock gameTime, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;
        this.gameTime = gameTime;
        this.display = display;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
    }

    public void display()
    {
        ItemSelectedView itemSelectedView = new ItemSelectedView(display, player, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        itemSelectedView.display();

        InventoryView inventoryView = new InventoryView(player.getInventory(), display, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        inventoryView.display();

        HealthBarView healthBarView = new HealthBarView(player, display, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        healthBarView.display(inventoryView.getX(), inventoryView.getY());

        ClockView clockView = new ClockView(gameTime, display, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        clockView.display(inventoryView.getInventoryBarImg(), inventoryView.getX(), inventoryView.getY());
    }
}
