package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.items.Vodka;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class DrunkView {

    private static final int WIDTH_COS = 50;
    private static final int HEIGHT_COS = 100;

    private FloatMap floatMap;
    private ObjectProperty<DisplacementMap> displacementMap;
    private int startTick;
    Timeline drunkTimeline;
    private double timer;
    Environment environment;

    public DrunkView(Environment environment, StackPane cameraView) {
        this.environment = environment;
        this.drunkTimeline = new Timeline();
        this.timer = 0;
        this.startTick = 0;
        this.floatMap = new FloatMap();
        this.displacementMap = new SimpleObjectProperty<>();


        drunkTimeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05), (ev -> {
            updateEffect();
        }));
        drunkTimeline.getKeyFrames().add(keyFrame);

        floatMap.setWidth(WIDTH_COS);
        floatMap.setHeight(HEIGHT_COS);

        cameraView.effectProperty().bind(displacementMap);

        environment.getPlayer().drunkProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                displacementMap.set(new DisplacementMap());
                displacementMap.getValue().setMapData(floatMap);
                startTick = environment.getTicks();
                drunkTimeline.play();
            } else {dissasambleEffect();}
        });
    }


    private void updateEffect() {

        for (int i = 0; i < WIDTH_COS; i++) {
            double v = (Math.cos(timer + i / 20.0 * Math.PI) - 0.5) / 40.0;
            for (int j = 0; j < HEIGHT_COS; j++) {
                floatMap.setSamples(i, j, 0.0f, (float) v);
            }
        }
        timer += 0.03;

        if (startTick+ Vodka.DRUNK_EFFECT_TIME <= environment.getTicks()){
            environment.getPlayer().drunkProperty().set(false);
        }

    }

    private void dissasambleEffect() {
        drunkTimeline.stop();
        displacementMap.set(null);
    }

}
