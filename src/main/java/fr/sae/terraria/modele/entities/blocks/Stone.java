package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Pickaxe;
import fr.sae.terraria.modele.entities.tools.Tool;

import java.util.Objects;


public class Stone extends Block implements StowableObjectType, CollideObjectType, PlaceableObjectType
{
    public static final int BREAK_RESISTANCE = 5;

    private final Environment environment;

    private final double xOrigin;
    private final double yOrigin;


    public Stone(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;

        this.pv.set(Stone.BREAK_RESISTANCE);
    }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* NE RIEN REMPLIR */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        this.breakAnimation(this.environment, this, this.xOrigin, this.yOrigin);

        // Une fois le bloc détruit, il donne la pierre et le supprime de la TileMaps
        Player player = this.environment.getPlayer();
        if (this.getPv() <= 0) {
            player.pickup(this);

            int yIndexTile = (int) (getY()/this.environment.heightTile);
            int xIndexTile = (int) (getX()/this.environment.widthTile);
            this.environment.getTileMaps().setTile(TileMaps.SKY, yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
            this.environment.getBlocks().remove(this);
        }

        // S'il utilise le bon outil, il commencera à casser le bloc sinon use l'outil sans casser le bloc.
        Stack stack = player.getStackSelected();
        if (!Objects.isNull(stack.getItem())) {
            if (stack.getItem() instanceof Pickaxe)
                this.setPv(this.getPv() - 1);
            if (stack.getItem() instanceof Tool)
                ((Tool) stack.getItem()).use();
        }
    }

    @Override public void place(int x, int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        int widthTile = this.environment.widthTile;
        int heightTile = this.environment.heightTile;

        Entity entity = new Stone(this.environment, x*widthTile, y*heightTile);
        entity.setRect(widthTile, heightTile);

        Player player = this.environment.getPlayer();
        Inventory inventory = player.getInventory();
        if (!Objects.isNull(player.getStackSelected()))
            inventory.get().get(inventory.getPosCursor()).remove();

        this.environment.getTileMaps().setTile(TileMaps.STONE, y, x);
        this.environment.getEntities().add(entity);
        this.environment.getBlocks().add((Block) entity);
    }
}
