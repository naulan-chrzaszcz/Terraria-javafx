package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileSet;
import fr.sae.terraria.modele.entities.entity.BreakableObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.items.Vodka;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Tool;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Objects;


public class Block extends Entity implements BreakableObjectType, PlaceableObjectType, StowableObjectType
{
    public static final int ROCK_NB_LOOTS = 3;
    public static final int DEFAULT_RESISTANCE = 2;

    private final Environment environment;
    private BlockSet typeOfBlock;

    private final double xOrigin;
    private final double yOrigin;


    public Block(final BlockSet typeOfBlock, final Environment environment, int x, int y)
    {
        super(x, y);
        this.typeOfBlock = typeOfBlock;
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;

        this.pv.set(Block.DEFAULT_RESISTANCE);
    }

    public Block(final BlockSet typeOfBlock, final Environment environment) { this(typeOfBlock, environment, 0, 0); }

    private void breakAnimation()
    {
        Timeline timeline = new Timeline();
        int animationCycleCount = 5;
        int[] time = new int[1];
        // Animation de cassure du bloc
        timeline.setCycleCount(animationCycleCount);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.setX(xOrigin + (Math.cos(time[0])*environment.scaleMultiplicatorWidth));
            this.setY(yOrigin + (-Math.sin(time[0]*environment.scaleMultiplicatorHeight)));

            time[0]++;
        }));
        timeline.getKeyFrames().add(keyFrame);
        // Faire revenir le bloc à sa position initiale lorsque l'animation est terminée
        timeline.statusProperty().addListener(c -> {
            this.setX(xOrigin);
            this.setY(yOrigin);
        });
        timeline.play();
    }

    private void loots()
    {
        Player player = this.environment.getPlayer();

        if (this.typeOfBlock == BlockSet.FLOOR_TOP || this.typeOfBlock == BlockSet.FLOOR_LEFT || this.typeOfBlock == BlockSet.FLOOR_RIGHT || this.typeOfBlock == BlockSet.DIRT) {
            player.pickup(new Block(BlockSet.FLOOR_TOP, this.environment));
        } else if (this.typeOfBlock == BlockSet.ROCK) {
            for (int loot = 0; loot < Block.ROCK_NB_LOOTS; loot++)
                player.pickup(Item.STONE);
        } else if (this.typeOfBlock == BlockSet.TALL_GRASS) {
            for (int loot = (int) (Math.random()*3)+1; loot < TallGrass.LOOTS_FIBRE_MAX; loot++)
                player.pickup(Item.FIBER);

            boolean mustDropVodka = Math.random() < Vodka.DROP_RATE;
            if (mustDropVodka)
                player.pickup(new Vodka(this.environment));
        } else if (this.typeOfBlock == BlockSet.TORCH) {
            player.pickup(new Block(BlockSet.TORCH, this.environment));
        } else if (this.typeOfBlock == BlockSet.TREE) {
            player.pickup(new Block(BlockSet.TREE, this.environment));
        }
    }

    @Override public void updates() { }

    @Override public void breaks()
    {
        if (this.typeOfBlock == BlockSet.ROCK)
            Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        else if (this.typeOfBlock == BlockSet.TALL_GRASS)
            Environment.playSound("sound/cut.wav", false);
        else Environment.playSound("sound/grassyStep.wav", false);
        this.breakAnimation();

        if (this.getPv() <= 0) {
            this.loots();

            int yIndexTile = (int) (getY()/environment.heightTile);
            int xIndexTile = (int) (getX()/environment.widthTile);
            this.environment.getTileMaps().setTile(TileSet.SKY.ordinal(), yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
            this.environment.getBlocks().remove(this);
        }

        if (isRock(this)) {
            Stack item = this.environment.getPlayer().getStackSelected();
            if (!Objects.isNull(item) && item.getItem() instanceof Tool) {
                Tool tool = (Tool) item.getItem();
                if (Tool.isPickaxe(tool))
                    this.setPv(this.getPv() - 1);
                tool.use();
            }
        } else this.setPv(this.getPv() - 1);
    }

    @Override public void place(final int x, final int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        int widthTile = this.environment.widthTile;
        int heightTile = this.environment.heightTile;

        Player player = this.environment.getPlayer();
        Inventory inventory = player.getInventory();
        if (!Objects.isNull(player.getStackSelected()))
            inventory.get().get(inventory.getPosCursor()).remove();
        Block block = new Block(BlockSet.FLOOR_TOP, this.environment, x*widthTile, y*heightTile);
        block.setRect(widthTile, heightTile);

        TileSet tile = TileSet.getTileSet(this.typeOfBlock);
        if (!Objects.isNull(tile))
            this.environment.getTileMaps().setTile(tile.ordinal(), y, x);
        this.environment.getEntities().add(block);
        this.environment.getBlocks().add(block);

        if (this.typeOfBlock == BlockSet.TREE)
            this.environment.getTrees().add((Tree) block);
        if (this.typeOfBlock == BlockSet.TORCH)
            this.environment.getTorches().add(block);

    }

    public static boolean isFloorTop(final Block block) { return block.getTypeOfBlock() == BlockSet.FLOOR_TOP; }
    public static boolean isFloorLeft(final Block block) { return block.getTypeOfBlock() == BlockSet.FLOOR_LEFT; }
    public static boolean isFloorRight(final Block block) { return block.getTypeOfBlock() == BlockSet.FLOOR_RIGHT; }
    public static boolean isDirt(final Block block) { return block.getTypeOfBlock() == BlockSet.DIRT; }
    public static boolean isRock(final Block block) { return block.getTypeOfBlock() == BlockSet.ROCK; }
    public static boolean isTallGrass(final Block block) { return block.getTypeOfBlock() == BlockSet.TALL_GRASS; }
    public static boolean isTorch(final Block block) { return block.getTypeOfBlock() == BlockSet.TORCH; }


    public BlockSet getTypeOfBlock() { return this.typeOfBlock; }
}
