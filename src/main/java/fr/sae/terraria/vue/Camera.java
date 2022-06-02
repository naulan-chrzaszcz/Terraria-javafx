package fr.sae.terraria.vue;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Camera
{


    public Camera(Player player, StackPane paneHadCamera, TileMaps tileMaps)
    {
        Rectangle clip = new Rectangle(0, 0, 1280, 720);
        clip.xProperty().bind(
                Bindings.createDoubleBinding(() -> clampRange(player.getX() - 1280 / 2, 0, 90000 - 1280),
                                             player.getXProperty(), paneHadCamera.widthProperty())
        );
        clip.yProperty().bind(
                Bindings.createDoubleBinding(() -> clampRange(player.getY() - 720 / 2, 0, 90000 - 1280),
                                             player.getYProperty(), paneHadCamera.widthProperty())
        );
        paneHadCamera.setClip(clip);
        paneHadCamera.translateXProperty().bind(clip.xProperty().multiply(-1));
        paneHadCamera.translateYProperty().bind(clip.yProperty().multiply(-1));
    }

    private double clampRange(double value, double min, double max) {
        return (value < min) ? min : (value > max) ? max : value;
    }
}
