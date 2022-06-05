package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.TileMaps;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LightView
{
    private static final Color NIGHT_COLOR = Color.web("#0d0d38");
    private static final double OPACITY_ITER = .0017;
    private static final int CIRCLE_RAY = 3;

    private double tileSize;
    private double widthMap;
    private int delimitationDirtStone;

    private Clock clock;
    private SimpleDoubleProperty opacityNightAir;
    private SimpleDoubleProperty opacityNightFade;
    private Circle torchLight;
    private Shape air;
    private Shape fade;
    private Shape tunnel;

    private TileMaps tileMaps;


    public LightView(Clock clock, Pane filterPane, double scaleMultiplicatorHeight, double scaleMultiplicatorWidth, TileMaps tileMaps)
    {
        super();
        this.tileSize = (int) (scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        this.widthMap = (int) (scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth());

        this.clock = clock;
        this.tileMaps= tileMaps;
        this.delimitationDirtStone = fullStoneArea();

        this.opacityNightAir = new SimpleDoubleProperty(.0);
        this.opacityNightFade = new SimpleDoubleProperty(.8143);

        this.torchLight = new Circle(this.tileSize*LightView.CIRCLE_RAY);
        this.air = new Rectangle(this.widthMap, this.tileSize*tileMaps.getHeight());
        this.fade =  new Rectangle(this.widthMap, this.tileSize);
        this.tunnel = new Rectangle(this.widthMap, this.tileSize*tileMaps.getHeight() - this.tileSize*this.delimitationDirtStone);

        this.air.setFill(NIGHT_COLOR);

        final Stop[] stops = new Stop[] { new Stop(0,new Color(0, 0, 0, 0) ), new Stop(1, LightView.NIGHT_COLOR)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        this.fade.setFill(lg1);

        final Stop[] stopsTorch = new Stop[] { new Stop(0, new Color(0, 0, 0, 0)), new Stop(1, LightView.NIGHT_COLOR)};
        RadialGradient rg2 = new RadialGradient(0, .1, 0, 0, this.tileSize*5, false, CycleMethod.NO_CYCLE, stopsTorch);
        this.torchLight.setFill(rg2);

        this.tunnel.setFill(LightView.NIGHT_COLOR);
        this.tunnel.setLayoutY(this.tileSize*(this.delimitationDirtStone+1));
        this.fade.setLayoutY(this.tunnel.getLayoutY()-this.tileSize);

        this.tunnel.opacityProperty().bind(this.opacityNightFade);
        this.fade.opacityProperty().bind(this.opacityNightFade);
        this.air.opacityProperty().bind(this.opacityNightAir);
        filterPane.getChildren().addAll(this.air, this.fade, this.tunnel);
    }

    public void setLightElements() { this.clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity(newV.intValue()))); }

    private void updateOpacity(int minutes)
    {
        if (minutes >= Clock.FOUR_PM_INGAME || minutes <= Clock.EIGHT_AM_INGAME) {
            if (minutes >= Clock.FOUR_PM_INGAME && minutes < Clock.ONE_DAY_INGAME) {
                this.opacityNightAir.setValue(this.opacityNightAir.getValue()+LightView.OPACITY_ITER);
                this.opacityNightFade.setValue(this.opacityNightFade.getValue()-LightView.OPACITY_ITER);
            } else if (minutes > Clock.MIDNIGHT_INGAME && minutes <= Clock.EIGHT_AM_INGAME) {
                this.opacityNightAir.setValue(this.opacityNightAir.getValue()-LightView.OPACITY_ITER);
                this.opacityNightFade.setValue(this.opacityNightFade.getValue()+LightView.OPACITY_ITER);
            }
        }
    }


    private int fullStoneArea()
    {
        int line = 0;
        int column = 0;
        boolean found = false;
        boolean wrongLine = false;

        while (line < this.tileMaps.getHeight() && !found) {

            wrongLine = false;
            column = 0;

            while (column < this.tileMaps.getWidth() && !found && !wrongLine){

                if (this.tileMaps.getTile(column,line) !=  TileMaps.STONE)
                    wrongLine = true;
                else if (column == this.tileMaps.getWidth() - 1)
                    found = true;

                column++;
            }
            line++;
        }
        return line-1;
    }
}

/* Pour le futur listener:
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
