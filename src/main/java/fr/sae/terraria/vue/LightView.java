package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Torch;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    private Shape actualAir;
    private Shape actualFade;
    private Shape actualTunnel;
    private Environment environment;

    private TileMaps tileMaps;

    public LightView(Clock clock, Pane filterPane, Environment env) {
        this.environment = env;
        this.clock = clock;
        this.filterPane = filterPane;
        this.tileMaps= env.getTileMaps();
        int tileSize =(int) (env.scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        int widthMap =(int) (env.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth());
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
        clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity(newV.intValue())));

        init();
        initTochListener(env.getTorches());
    }

    public void init(){
        actualAir = Shape.union(baseAir,baseAir);
        actualFade = Shape.union(baseFade,baseFade);
        actualTunnel = Shape.union(baseTunnel,baseTunnel);

        Stop[] stops = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        actualFade.setFill(lg1);
        actualAir.setFill(NIGHT_COLOR);
        actualTunnel.setFill(NIGHT_COLOR);

        actualTunnel.opacityProperty().bind(opacityNightFade);
        actualFade.opacityProperty().bind(opacityNightFade);
        actualAir.opacityProperty().bind(opacityNightAir);

        filterPane.getChildren().addAll(actualTunnel,actualFade,actualAir);
    }

    private void initTochListener(ObservableList<Torch> torches) {
        torches.addListener((ListChangeListener) c -> {
            while(c.next()){
                filterPane.getChildren().clear();

                if (c.wasRemoved()){
                    actualTunnel = Shape.union(baseTunnel,baseTunnel);
                    actualFade = Shape.union(baseFade,baseFade);
                    actualAir = Shape.union(baseAir,baseAir);

                    for (int i = 0; i < environment.getTorches().size(); i++){
                        torchLight.setLayoutX(environment.getTorches().get(i).getX());
                        torchLight.setLayoutY(environment.getTorches().get(i).getY());

                        actualAir = Shape.subtract(actualAir,torchLight);
                        actualFade = Shape.subtract(actualFade,torchLight);
                        actualTunnel = Shape.subtract(actualTunnel, torchLight);
                    }

                }
                else if (c.wasAdded()){
                    Shape newAir = Shape.union(actualAir,actualAir);
                    Shape newFade = Shape.union(actualFade,actualFade);
                    Shape newTunnel = Shape.union(actualTunnel,actualTunnel);

                    for (int i = 0; i < c.getAddedSubList().size(); i++){
                        torchLight.setLayoutX(((Torch)c.getAddedSubList().get(i)).getX());
                        torchLight.setLayoutY(((Torch)c.getAddedSubList().get(i)).getY());

                        newAir = Shape.subtract(newAir,torchLight);
                        newFade = Shape.subtract(newFade,torchLight);
                        newTunnel = Shape.subtract(newTunnel, torchLight);
                    }

                    actualAir = newAir;
                    actualFade = newFade;
                    actualTunnel = newTunnel;
                }
                Stop[] stops = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
                LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
                actualFade.setFill(lg1);
                actualAir.setFill(NIGHT_COLOR);
                actualTunnel.setFill(NIGHT_COLOR);

                actualTunnel.opacityProperty().bind(opacityNightFade);
                actualFade.opacityProperty().bind(opacityNightFade);
                actualAir.opacityProperty().bind(opacityNightAir);

                filterPane.getChildren().addAll(actualTunnel,actualAir,actualFade);
            }});
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
