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
    private static final Color NIGHT_COLOR = Color.web("#0d0d38");
    private static final Stop[] STOPS_FADE = new Stop[] { new Stop(0,new Color(0,0,0,0) ), new Stop(1, NIGHT_COLOR)};
    private static final LinearGradient GRADIENT_FADE = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, STOPS_FADE);
    private static final Stop[] STOPS_TORCH = new Stop[] {  new Stop(0, Color.RED),new Stop(0.1, Color.web("#fbff00")), new Stop(1,new Color(0.9843,1,0,0.05))};
    private static final RadialGradient GRADIENT_TORCH = new RadialGradient(0,0,.5,.5,0.3,true,CycleMethod.NO_CYCLE, STOPS_TORCH);

    private static int delimitationDirtStone;
    private static int widthMap;
    private static int tileSize;

    private final SimpleDoubleProperty opacityNightAir;
    private final SimpleDoubleProperty opacityNightFade;
    private final Environment environment;
    private final TileMaps tileMaps;
    private final Circle torchLight;
    private final Pane filterPane;
    private final Clock clock;

    private Shape actualAir;
    private Shape actualFade;
    private Shape actualTunnel;


    public LightView(Clock clock, Pane filterPane, Environment env) {
        this.clock = clock;
        this.environment = env;
        this.filterPane = filterPane;
        this.tileMaps= env.getTileMaps();
        tileSize = (int) (env.scaleMultiplicatorHeight*TileMaps.TILE_DEFAULT_SIZE);
        widthMap = (int) (env.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE*tileMaps.getWidth());
        delimitationDirtStone = fullStoneArea();

        this.opacityNightAir = new SimpleDoubleProperty(.0);
        this.opacityNightFade = new SimpleDoubleProperty(.8143);

        this.torchLight = new Circle(this.tileSize*CIRCLE_RAY);

        this.resetShapes();
        this.addEffects();

        this.clock.minutesProperty().addListener(((obs, oldV, newV) -> updateOpacity()));

        this.filterPane.getChildren().addAll(this.actualTunnel, this.actualFade, this.actualAir);
        this.initTochListener(env.getTorches());
    }

    private void initTochListener(ObservableList<Torch> torches) {
        torches.addListener((ListChangeListener<? super Torch>) c -> {
            while(c.next()){
                this.filterPane.getChildren().clear();
                addTochLights();
                if (c.wasRemoved()){
                    resetShapes();

                    for (int i = 0; i < this.environment.getTorches().size(); i++){
                        this.torchLight.setLayoutX(this.environment.getTorches().get(i).getX() + (tileSize/2));
                        this.torchLight.setLayoutY(this.environment.getTorches().get(i).getY() + (tileSize/3));

                        this.actualAir = Shape.subtract(this.actualAir, this.torchLight);
                        this.actualFade = Shape.subtract(this.actualFade, this.torchLight);
                        this.actualTunnel = Shape.subtract(this.actualTunnel, this.torchLight);
                    }

                }
                else if (c.wasAdded()){

                    for (int i = 0; i < c.getAddedSubList().size(); i++) {
                        this.torchLight.setLayoutX(c.getAddedSubList().get(i).getX() + (tileSize/2));
                        this.torchLight.setLayoutY(c.getAddedSubList().get(i).getY()+ (tileSize/3));

                        this.actualAir = Shape.subtract(this.actualAir, this.torchLight);
                        this.actualFade = Shape.subtract(this.actualFade, this.torchLight);
                        this.actualTunnel = Shape.subtract(this.actualTunnel, this.torchLight);
                    }
                }
                addEffects();

                this.filterPane.getChildren().addAll(this.actualTunnel, this.actualAir, this.actualFade);
            }});
    }

    private void updateOpacity()
    {
        if (this.clock.getMinutes() > Clock.MINUTES_IN_A_DAY/2)
            this.opacityNightAir.set(((this.clock.getMinutes()*(2. /* Compensation temps */))/Clock.MINUTES_IN_A_DAY) - (1.1 /* Décallage */));
        else this.opacityNightAir.set(((Clock.MINUTES_IN_A_DAY - (this.clock.getMinutes()*(4. /* Compensation temps */)))/Clock.MINUTES_IN_A_DAY) - (.1 /* Décallage */));
    }


    private int fullStoneArea()
    {
        int line = 0;
        int column;
        boolean found = false;
        boolean wrongLine;

        while (line < this.tileMaps.getHeight() && !found) {
            wrongLine = false;
            column = 0;

            while (column < this.tileMaps.getWidth() && !found && !wrongLine) {
                if (this.tileMaps.getTile(column, line) != TileMaps.STONE)
                    wrongLine = true;
                else if (column == this.tileMaps.getWidth() - 1)
                    found = true;
                column++;
            }
            line++;
        }
        return line - 1;
    }

    private void resetShapes()
    {
        this.actualAir = new Rectangle(widthMap,tileSize*this.tileMaps.getHeight());
        this.actualFade =  new Rectangle(widthMap,tileSize);
        this.actualTunnel = new Rectangle(widthMap,tileSize* this.tileMaps.getHeight() - tileSize*delimitationDirtStone);

        this.actualTunnel.setLayoutY(tileSize*(delimitationDirtStone+1));
        this.actualFade.setLayoutY(this.actualTunnel.getLayoutY()-tileSize);
    }

    private void addEffects()
    {
        this.actualFade.setFill(GRADIENT_FADE);
        this.actualAir.setFill(NIGHT_COLOR);
        this.actualTunnel.setFill(NIGHT_COLOR);

        this.actualTunnel.opacityProperty().bind(this.opacityNightFade);
        this.actualFade.opacityProperty().bind(this.opacityNightFade);
        this.actualAir.opacityProperty().bind(this.opacityNightAir);
    }

    private void addTochLights()
    {
        for (int i = 0; i < this.environment.getTorches().size() ; i ++){
            Circle torchLight = new Circle(tileSize*CIRCLE_RAY);

            torchLight.setLayoutX(this.environment.getTorches().get(i).getX() + (tileSize/2));
            torchLight.setLayoutY(this.environment.getTorches().get(i).getY() + (tileSize/3));

            torchLight.setFill(GRADIENT_TORCH);
            torchLight.setOpacity(0.5);

            this.filterPane.getChildren().add(torchLight);
        }
    }
}
