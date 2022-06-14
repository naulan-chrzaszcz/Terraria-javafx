package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.BlockSet;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Item;

import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.tools.*;

import java.util.Objects;

public class Craft {
    private Inventory inventory;
    private Environment environment;
    public Craft(Inventory inventory, Environment environment) {
        this.inventory = inventory;
    }

    public boolean possibleToCreateTool(int id) {
        boolean firstGood = false;
        boolean secundGood = false;
        int quantityNeeded = 0;
        for (int i = 0; i < this.inventory.NB_BOXES_MAX; i++) {
            if (id <= 9) {
                if (id <= 6) {
                    quantityNeeded = 2;
                    //TODO a changé le bois en stick
                    if (Item.isWood(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        firstGood = true;
                        quantityNeeded = 3;
                    }

                } else {
                    quantityNeeded = 1;
                    //TODO a changé le bois en stick
                    if (Item.isWood(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        firstGood = true;
                        quantityNeeded = 2;
                    }
                }
                if (id % 3 == 1) {
                    if (Item.isWood(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        secundGood = true;
                    }
                }
                if (id % 3 == 2) {
                    if (Item.isStone(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        secundGood = true;
                    }
                }
                if (id % 3 == 0) {
                    if (Item.isIron(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        secundGood = true;
                    }
                }
            }
            else if (id == 10) {
                quantityNeeded = 3;
                if (Item.isStone(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                    firstGood = true;
                    secundGood = true;
                }
            } else if (id == 11) {
                quantityNeeded = 3;
                //TODO a changé le bois en stick
                if (Item.isWood(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                    firstGood = true;
                }
                if (Item.isFiber(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded)
                    secundGood = true;
            }
            if (firstGood && secundGood)
                return true;
        }
        return false;
    }

    public void createTool(int id) {
        int quantityNeeded = 0;
        if (id == 10)
            inventory.getPlayer().pickup(new Block(BlockSet.ROCK, this.environment));

        for (int i = 0; i < this.inventory.nbStacksIntoInventory(); i++) {
            if (id <= 9) {
                if (id <= 6) {
                    quantityNeeded = 2;
                    //TODO a changé le bois en stick
                    if (Item.isWood(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                        quantityNeeded = 3;
                    }

                } else {
                    quantityNeeded = 1;
                    if (Item.isWood(this.inventory.get().get(i).getItem()) && this.inventory.get().get(i).getNbItems() >= quantityNeeded) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                        quantityNeeded = 2;
                    }
                }
                if (id % 3 == 1) {
                    if (Item.isWood(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    }
                }
                if (id % 3 == 2) {
                    if (Item.isStone(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    }
                }
                if (id % 3 == 0) {
                    if (Item.isIron(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    }
                }
            } else if (id == 10) {
                quantityNeeded = 3;
                if (Item.isStone(this.inventory.get().get(i).getItem())) {
                    this.inventory.get().get(i).removeQuantity(quantityNeeded);
                }
            } else if (id == 11) {
                quantityNeeded = 3;
                if (Item.isWood(this.inventory.get().get(i).getItem())) {
                    this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    quantityNeeded = 3;
                }
                if (Item.isFiber(this.inventory.get().get(i).getItem()))
                    this.inventory.get().get(i).removeQuantity(quantityNeeded);
            }

        }
    }
}
