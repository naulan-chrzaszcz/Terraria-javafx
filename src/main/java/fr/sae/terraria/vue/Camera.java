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
        double midCamera = (this.clip.getHeight()/2);
        double[] centerCameraY = new double[1];
        player.getYProperty().addListener((obs, oldX, newX) -> {
            if (player.offset[1] == Entity.IS_JUMPING) {
                if (player.getGravity().timer > player.getGravity().flightTime*2 /* Légère bidouille */) {
                    centerCameraY[0] = player.getY() - midCamera;
                } else centerCameraY[0] = player.getGravity().yInit - midCamera;
            } else centerCameraY[0] = player.getY() - midCamera;
        });

        // Suit le joueur en 'y'
        this.clip.yProperty().bind(
                Bindings.createDoubleBinding(
                        () -> clampRange(centerCameraY[0], minScrollHeightCamera, maxScrollHeightCamera),
                        player.getYProperty(), paneHadCamera.heightProperty()
                )
        );

        paneHadCamera.setClip(this.clip);
        paneHadCamera.translateXProperty().bind(this.clip.xProperty().multiply(-1));
        paneHadCamera.translateYProperty().bind(this.clip.yProperty().multiply(-1));
    }

    private double clampRange(double value, double min, double max) { return (value < min) ? min : (value > max) ? max : value; }
}
