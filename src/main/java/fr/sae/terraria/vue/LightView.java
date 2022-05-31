package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Torch;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LightView {
    private static final double opacityIter = 0.0017;

    private ObservableList<Torch> torches;
    private Clock clock;
    private Pane filterPane;
    private double opacityNight;
    private Shape air;
    private Shape fade;
    private Shape tunnel;


    public LightView(Clock clock, Pane filterPane, double scaleMultiplicatorHeight, double scaleMultiplicatorWidth) {

        this.clock = clock;
        this.filterPane = filterPane;
        opacityNight = .0;

        air = new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*30 ,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*14);
        fade =  new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*30,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        tunnel = new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*30,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);

        air.setFill(Color.web("#0d0d38"));
        fade.setFill(Color.YELLOW);
        tunnel.setFill(Color.BLACK);

        fade.setLayoutY(scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*14);
        tunnel.setLayoutY(scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*15);

        filterPane.getChildren().addAll(air,fade,tunnel);
    }

    public void setLightElements() {
        clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity(newV.intValue())));
    }

    private void updateOpacity(int minutes) {
        if (minutes >= Clock.FOUR_PM_INGAME || minutes <= Clock.EIGHT_AM_INGAME) {

            if (minutes >= Clock.FOUR_PM_INGAME && minutes < Clock.ONE_DAY_INGAME) { opacityNight += opacityIter ; }
            else if (minutes > Clock.MIDNIGHT_INGAME && minutes <= Clock.EIGHT_AM_INGAME) { opacityNight -= opacityIter ; }

            opacityNight = (double) Math.round(opacityNight * 10000) / 10000;
            filterPane.setStyle("-fx-opacity: " + opacityNight);
        }
    }
}
