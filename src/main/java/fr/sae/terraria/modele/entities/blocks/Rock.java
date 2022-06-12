package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileSet;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Tool;

import java.util.Objects;


public class Rock extends Block implements StowableObjectType, CollideObjectType, PlaceableObjectType
{
    public static final int BREAK_RESISTANCE = 5;
    public static final int STONE_LOOTS = 3;

    private final Environment environment;

    private final double xOrigin;
    private final double yOrigin;


    public Rock(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;

        this.pv.set(Rock.BREAK_RESISTANCE);
    }

    public Rock(Environment environment) { this(environment, 0, 0); }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* NE RIEN REMPLIR */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        this.breakAnimation(this.environment, this, this.xOrigin, this.yOrigin);

        // Une fois le bloc cassé, de la pierre est ramassée et le bloc se supprime du Pane
        Player player = this.environment.getPlayer();
        if (this.getPv() <= 0) {
            for (int i = 0; i < 3; i++)
                player.pickup(Item.STONE);

            int yIndexTile = (int) (getY()/this.environment.heightTile);
            int xIndexTile = (int) (getX()/this.environment.widthTile);
            this.environment.getTileMaps().setTile(TileSet.SKY.ordinal(), yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
            this.environment.getBlocks().remove(this);
        }

        // Peu importe l'outil qu'utilise le joueur, si c'est un outil, il perdra de la durabilité, mais le bloc ne peut que se casser avec une pioche
        Stack stack = player.getStackSelected();
        if (!Objects.isNull(stack) && stack.getItem() instanceof Tool) {
            if (Tool.isPickaxe(((Tool) stack.getItem()).getTypeOfTool()))
                this.setPv(this.getPv() - 1);
            ((Tool) stack.getItem()).use();
        }
    }

    @Override public void place(int x, int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        int widthTile = this.environment.widthTile;
        int heightTile = this.environment.heightTile;

        Block entity = new Rock(this.environment, x*widthTile, y*heightTile);
        entity.setRect(widthTile, heightTile);

        Player player = this.environment.getPlayer();
        Inventory inventory = player.getInventory();
        if (!Objects.isNull(player.getStackSelected()))
            inventory.get().get(inventory.getPosCursor()).remove();

        this.environment.getTileMaps().setTile(TileSet.ROCK.ordinal(), y, x);
        this.environment.getEntities().add(entity);
        this.environment.getBlocks().add(entity);
    }
}
