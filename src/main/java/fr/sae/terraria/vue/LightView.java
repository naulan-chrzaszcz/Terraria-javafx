package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.TileMaps;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LightView {
    private static final int CIRCLE_RAY = 3;
    private static final double OPACITY_ITER = 0.0017;

    private Clock clock;
    private Pane filterPane;
    private SimpleDoubleProperty opacityNightAir;
    private SimpleDoubleProperty opacityNightFade;
    private Circle torchLight;
    private Shape air;
    private Shape fade;
    private Shape tunnel;

    private TileMaps tileMaps;

    public LightView(Clock clock, Pane filterPane, double scaleMultiplicatorHeight, double scaleMultiplicatorWidth, TileMaps tileMaps) {

        this.clock = clock;
        this.filterPane = filterPane;
        this.tileMaps= tileMaps;
        opacityNightAir = new SimpleDoubleProperty(0.0);
        opacityNightFade = new SimpleDoubleProperty(0.8143);

        torchLight = new Circle(scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*CIRCLE_RAY);

        air = new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth() ,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getHeight());
        fade =  new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth() ,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE+1);
        tunnel = new Rectangle(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth() ,scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE* tileMaps.getHeight() - scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*15);

        air.setFill(Color.web("#0d0d38"));

        Stop[] stops = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, Color.web("#0d0d38"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        fade.setFill(lg1);

        Stop[] stopsTorch = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, Color.web("#0d0d38"))};
        RadialGradient rg2 = new RadialGradient(0,0.1,0,0,scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*5,false,CycleMethod.NO_CYCLE,stopsTorch);
        torchLight.setFill(rg2);

        tunnel.setFill(Color.web("#0d0d38"));

        fade.setLayoutY(scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*14);
        tunnel.setLayoutY(scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE*15);

        tunnel.opacityProperty().bind(opacityNightFade);
        fade.opacityProperty().bind(opacityNightFade);
        air.opacityProperty().bind(opacityNightAir);
        filterPane.getChildren().addAll(air,fade,tunnel);
    }

    public void setLightElements() {
        clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity(newV.intValue())));
    }

    private void updateOpacity(int minutes) {
        if (minutes >= Clock.FOUR_PM_INGAME || minutes <= Clock.EIGHT_AM_INGAME) {
            if (minutes >= Clock.FOUR_PM_INGAME && minutes < Clock.ONE_DAY_INGAME) {
                opacityNightAir.setValue(opacityNightAir.getValue()+ OPACITY_ITER);
                opacityNightFade.setValue(opacityNightFade.getValue()- OPACITY_ITER);
            }
            else if (minutes > Clock.MIDNIGHT_INGAME && minutes <= Clock.EIGHT_AM_INGAME) {
                opacityNightAir.setValue(opacityNightAir.getValue()- OPACITY_ITER);
                opacityNightFade.setValue(opacityNightFade.getValue()+ OPACITY_ITER);
            }
        }
    }
}

/** Pour le futur listener:
 *
 * while (c.next){
 *     if(c.wasadded()){
 *          for( Torch t : c.getAdded(){
 *              torchLight.setLayoutY(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*t.getY());
 *              torchLight.setLayoutX(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*t.getX());
 *              air = Shape.substract(ait,torchLight);
 *              fade = Shape.substract(ait,torchLight);
 *              tunnel = Shape.substract(ait,torchLight);
 *          }
 *     }
 *
 *     if(c.wasRemoved()){
 *         filterpane.getchildren.clear();
 *
 *         for(Torch t : listTorch){
 *             torchLight.setLayoutY(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*t.getY());
 *             torchLight.setLayoutX(scaleMultiplicatorWidth* TileMaps.TILE_DEFAULT_SIZE*t.getX());
 *             air = Shape.substract(ait,torchLight);
 *             fade = Shape.substract(ait,torchLight);
 *             tunnel = Shape.substract(ait,torchLight);
 *         }
 *     }
 * }
 */
