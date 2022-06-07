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
    private static final Color NIGHT_COLOR = Color.web("#0d0d38");
    private static final Stop[] STOPS_FADE = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
    private static final LinearGradient GRADIENT_FADE = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, STOPS_FADE);
    private static final Stop[] STOPS_TORCH = new Stop[] { new Stop(0, Color.web("#FAC539")), new Stop(1,new Color(0,0,0,0))};
    private static final RadialGradient GRADIENT_TORCH = new RadialGradient(0,0.1,0,0,200,false,CycleMethod.NO_CYCLE, STOPS_TORCH);
    private static int delimitationDirtStone;
    private static int widthMap;
    private static int tileSize;
    private Pane filterPane;
    private SimpleDoubleProperty opacityNightAir;
    private SimpleDoubleProperty opacityNightFade;
    private Circle torchLight;
    private Shape actualAir;
    private Shape actualFade;
    private Shape actualTunnel;
    private Environment environment;

    private TileMaps tileMaps;

    public LightView(Clock clock, Pane filterPane, Environment env) {
        this.environment = env;
        this.filterPane = filterPane;
        this.tileMaps= env.getTileMaps();
        tileSize =(int) (env.scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        widthMap =(int) (env.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth());
        delimitationDirtStone = fullStoneArea();

        opacityNightAir = new SimpleDoubleProperty(0.0);
        opacityNightFade = new SimpleDoubleProperty(0.8143);

        torchLight = new Circle(tileSize*CIRCLE_RAY);

        resetShapes();
        addEffects();

        clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity(newV.intValue())));

        filterPane.getChildren().addAll(actualTunnel,actualFade,actualAir);
        initTochListener(env.getTorches());
    }

    private void initTochListener(ObservableList<Torch> torches) {
        torches.addListener((ListChangeListener) c -> {
            while(c.next()){
                filterPane.getChildren().clear();
                addTochLights();
                if (c.wasRemoved()){
                    resetShapes();

                    for (int i = 0; i < environment.getTorches().size(); i++){
                        torchLight.setLayoutX(environment.getTorches().get(i).getX() + (tileSize/2));
                        torchLight.setLayoutY(environment.getTorches().get(i).getY() + (tileSize/8));

                        actualAir = Shape.subtract(actualAir,torchLight);
                        actualFade = Shape.subtract(actualFade,torchLight);
                        actualTunnel = Shape.subtract(actualTunnel, torchLight);
                    }

                }
                else if (c.wasAdded()){

                    for (int i = 0; i < c.getAddedSubList().size(); i++) {
                        torchLight.setLayoutX(((Torch) c.getAddedSubList().get(i)).getX() + (tileSize/2));
                        torchLight.setLayoutY(((Torch) c.getAddedSubList().get(i)).getY()+ (tileSize/8));

                        actualAir = Shape.subtract(actualAir, torchLight);
                        actualFade = Shape.subtract(actualFade, torchLight);
                        actualTunnel = Shape.subtract(actualTunnel, torchLight);
                    }
                }
                addEffects();

//                addTorchLight();
                filterPane.getChildren().addAll(actualTunnel,actualAir,actualFade);
            }});
    }

//    private void addTorchLight() {
//        for (int i = 0; i < environment.getTorches().size(); i ++){
//            Shape torchLight = Shape.union(this.torchLight,this.torchLight);
//            Stop[] stopsTorch = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
//            RadialGradient rg2 = new RadialGradient(0,0.1,0,0,tileSize*5,false,CycleMethod.NO_CYCLE,stopsTorch);
//            torchLight.setFill(rg2);
//            torchLight.setOpacity(0.5);
//            filterPane.getChildren().add(torchLight);
//        }
//    }


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
        int column;
        boolean found = false;
        boolean wrongLine;

        while (line < tileMaps.getHeight() && !found) {

            wrongLine = false;
            column = 0;

            while (column < tileMaps.getWidth() && !found && !wrongLine) {

                if (tileMaps.getTile(column, line) != TileMaps.STONE)
                    wrongLine = true;
                else if (column == tileMaps.getWidth() - 1)
                    found = true;

                column++;
            }
            line++;
        }
        return line - 1;
    }

    private void resetShapes(){
        actualAir = new Rectangle(widthMap,tileSize*tileMaps.getHeight());
        actualFade =  new Rectangle(widthMap,tileSize);
        actualTunnel = new Rectangle(widthMap,tileSize* tileMaps.getHeight() - tileSize*delimitationDirtStone);


        actualTunnel.setLayoutY(tileSize*(delimitationDirtStone+1));
        actualFade.setLayoutY(actualTunnel.getLayoutY()-tileSize);
    }
    private void addEffects(){
        actualFade.setFill(GRADIENT_FADE);
        actualAir.setFill(NIGHT_COLOR);
        actualTunnel.setFill(NIGHT_COLOR);

        actualTunnel.opacityProperty().bind(opacityNightFade);
        actualFade.opacityProperty().bind(opacityNightFade);
        actualAir.opacityProperty().bind(opacityNightAir);
    }
    private void addTochLights() {
        for (int i = 0; i < environment.getTorches().size() ; i ++){
            Circle torchLight = new Circle(tileSize*CIRCLE_RAY);

            torchLight.setLayoutX(environment.getTorches().get(i).getX() + (tileSize/2));
            torchLight.setLayoutY(environment.getTorches().get(i).getY() + (tileSize/8));

            torchLight.setFill(GRADIENT_TORCH);
            torchLight.setOpacity(0.5);

            filterPane.getChildren().add(torchLight);
        }
    }
}
