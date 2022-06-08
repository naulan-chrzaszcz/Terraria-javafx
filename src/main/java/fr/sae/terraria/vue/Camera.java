package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;


public class Camera
{
    private final Environment environment;
    private final Rectangle clip;


    public Camera(final Environment environment, final StackPane paneHadCamera)
    {
        super();
        this.environment = environment;

        final TileMaps tileMaps = this.environment.getTileMaps();
        final Player player = this.environment.getPlayer();

        int widthCamera = (int) (this.environment.scaleMultiplicatorWidth*Terraria.DISPLAY_RENDERING_WIDTH);
        int heightCamera = (int) (this.environment.scaleMultiplicatorHeight*Terraria.DISPLAY_RENDERING_HEIGHT);
        this.clip = new Rectangle(0, 0, widthCamera, heightCamera);

        int tileSize = (int) (environment.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE);
        int minScrollWidthCamera = 0;
        int minScrollHeightCamera = 0;
        int maxScrollWidthCamera = (tileMaps.getWidth()*tileSize) - widthCamera;
        int scrollToleranceHeight = this.environment.heightTile*5;
        int maxScrollHeightCamera = ((tileMaps.getHeight()*tileSize) - heightCamera) - scrollToleranceHeight;

        // Suit le joueur en 'x'
        this.clip.xProperty().bind(
                Bindings.createDoubleBinding(
                        () -> clampRange(player.getX() - this.clip.getWidth()/2, minScrollWidthCamera, maxScrollWidthCamera),
                        player.getXProperty(), paneHadCamera.widthProperty()
                )
        );

        // Evite que la caméra suit le joueur quand il saute sauf en cas de besoin
        double midHeightCamera = (this.clip.getHeight()/2);
        double[] centerPlayerOnYIntoCamera = new double[1];
        double[] gap = new double[1];
        player.getYProperty().addListener((obs, oldX, newX) -> {
            // Suit le joueur
            centerPlayerOnYIntoCamera[0] = player.getY() - midHeightCamera;
            if (player.offset[1] == Entity.IS_JUMPING) {
                gap[0] = player.getGravity().yInit - player.getY();
                centerPlayerOnYIntoCamera[0] = player.getGravity().yInit - midHeightCamera;
                if (player.getGravity().timer > player.getGravity().flightTime*2 /* Légère bidouille */)
                    centerPlayerOnYIntoCamera[0] = player.getY() - midHeightCamera;
            }

            // décale "proprement" la caméra vers le haut
            if (player.offset[1] == Entity.IDLE && gap[0] > 0) {
                gap[0] /= 2;
                centerPlayerOnYIntoCamera[0] = (player.getY() + gap[0]) - midHeightCamera;
            }
        });

        // Suit le joueur en 'y'
        this.clip.yProperty().bind(
                Bindings.createDoubleBinding(
                        () -> clampRange(centerPlayerOnYIntoCamera[0], minScrollHeightCamera, maxScrollHeightCamera),
                        player.getYProperty(), paneHadCamera.heightProperty()
                )
        );

        paneHadCamera.setClip(this.clip);
        paneHadCamera.translateXProperty().bind(this.clip.xProperty().multiply(-1));
        paneHadCamera.translateYProperty().bind(this.clip.yProperty().multiply(-1));
    }

    private double clampRange(double value, double min, double max) { return (value < min) ? min : (value > max) ? max : value; }
}
