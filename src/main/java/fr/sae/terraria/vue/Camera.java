package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;


public class Camera
{


    public Camera(Environment environment, StackPane paneHadCamera)
    {
        TileMaps tileMaps = environment.getTileMaps();
        Player player = environment.getPlayer();
        Rectangle clip = new Rectangle(0, 0, environment.scaleMultiplicatorWidth*Terraria.DISPLAY_RENDERING_WIDTH, environment.scaleMultiplicatorHeight*Terraria.DISPLAY_RENDERING_HEIGHT);

        int tileSize = (int) (environment.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE);
        int minScrollWidthCamera = 0; int minScrollHeightCamera = 0;
        int maxScrollWidthCamera = (int) ((tileMaps.getWidth()*tileSize) - clip.getWidth());
        int maxScrollHeightCamera = (int) ((tileMaps.getHeight()*tileSize) - clip.getHeight()) - environment.heightTile*5;

        clip.xProperty()
                .bind(Bindings.createDoubleBinding(
                        () -> clampRange(player.getX() - clip.getWidth()/2, minScrollWidthCamera, maxScrollWidthCamera),
                        player.getXProperty(), paneHadCamera.widthProperty()
                )
        );

        clip.yProperty()
                .bind(Bindings.createDoubleBinding(
                        () -> clampRange(player.getY() - (clip.getHeight()/2), minScrollHeightCamera, maxScrollHeightCamera),
                        player.getYProperty(), paneHadCamera.heightProperty()
                )
        );

        paneHadCamera.setClip(clip);
        paneHadCamera.translateXProperty().bind(clip.xProperty().multiply(-1));
        paneHadCamera.translateYProperty().bind(clip.yProperty().multiply(-1));
    }

    private double clampRange(double value, double min, double max) { return (value < min) ? min : (value > max) ? max : value; }
}
