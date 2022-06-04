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
    private static  final Color NIGHT_COLOR = Color.web("#0d0d38");
    private Clock clock;
    private Pane filterPane;
    private SimpleDoubleProperty opacityNightAir;
    private SimpleDoubleProperty opacityNightFade;
    private Circle torchLight;
    private Shape baseAir;
    private Shape baseFade;
    private Shape baseTunnel;

    private TileMaps tileMaps;

    public LightView(Clock clock, Pane filterPane, double scaleMultiplicatorHeight, double scaleMultiplicatorWidth, TileMaps tileMaps) {

        this.clock = clock;
        this.filterPane = filterPane;
        this.tileMaps= tileMaps;
        int tileSize =(int) (scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        int widthMap =(int) (scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth());
        int delimitationDirtStone = fullStoneArea();

        opacityNightAir = new SimpleDoubleProperty(0.0);
        opacityNightFade = new SimpleDoubleProperty(0.8143);

        torchLight = new Circle(tileSize*CIRCLE_RAY);
        baseAir = new Rectangle(widthMap,tileSize*tileMaps.getHeight());
        baseFade =  new Rectangle(widthMap,tileSize);
        baseTunnel = new Rectangle(widthMap,tileSize* tileMaps.getHeight() - tileSize*delimitationDirtStone);


        Stop[] stopsTorch = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
        RadialGradient rg2 = new RadialGradient(0,0.1,0,0,tileSize*5,false,CycleMethod.NO_CYCLE,stopsTorch);
        torchLight.setFill(rg2);


        baseTunnel.setLayoutY(tileSize*(delimitationDirtStone+1));
        baseFade.setLayoutY(baseTunnel.getLayoutY()-tileSize);

        update();
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

    public void update(){
        Shape air = Shape.union(baseAir,baseAir);
        Shape fade = Shape.union(baseFade,baseFade);
        Shape tunnel = Shape.union(baseTunnel,baseTunnel);

        Stop[] stops = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        fade.setFill(lg1);
        air.setFill(NIGHT_COLOR);
        tunnel.setFill(NIGHT_COLOR);

        tunnel.opacityProperty().bind(opacityNightFade);
        fade.opacityProperty().bind(opacityNightFade);
        air.opacityProperty().bind(opacityNightAir);

        filterPane.getChildren().addAll(tunnel,fade,air);
    }


    private int fullStoneArea() {
        int line = 0;
        int column = 0;
        boolean found = false;
        boolean wrongLine = false;

        while (line < tileMaps.getHeight() && !found) {

            wrongLine = false;
            column = 0;

            while (column < tileMaps.getWidth() && !found && !wrongLine){

                if (tileMaps.getTile(column,line) !=  TileMaps.STONE)
                    wrongLine = true;
                else if (column == tileMaps.getWidth() - 1)
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
