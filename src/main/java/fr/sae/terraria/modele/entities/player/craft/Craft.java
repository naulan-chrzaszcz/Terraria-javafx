package fr.sae.terraria.modele.entities.player.craft;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.BlockSet;
import fr.sae.terraria.modele.entities.items.Item;

import fr.sae.terraria.modele.entities.player.craft.recipes.RockRecipe;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.*;


public class Craft
{

    public boolean possibleToCreateTool(int id) {
        boolean firstGood = false;
        boolean secundGood = false;
        int quantityNeeded = 0;
        for (int i = 0; i < Inventory.NB_BOXES_MAX; i++) {
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
        if (id == 1)
            inventory.getPlayer().pickup(new Tool(ToolSet.PICKAXE, MaterialSet.IRON));
        else if (id == 3)
            inventory.getPlayer().pickup(new Tool(ToolSet.PICKAXE, MaterialSet.WOOD));
        else if (id == 4)
            inventory.getPlayer().pickup(new Tool(ToolSet.AXE, MaterialSet.IRON));
        else if (id == 6)
            inventory.getPlayer().pickup(new Tool(ToolSet.AXE, MaterialSet.WOOD));
        else if (id == 7)
            inventory.getPlayer().pickup(new Tool(ToolSet.SWORD, MaterialSet.IRON));
        else if(id == 9)
            inventory.getPlayer().pickup(new Tool(ToolSet.SWORD, MaterialSet.WOOD));
        if (id == 10)
            inventory.getPlayer().pickup(new Block(BlockSet.ROCK, null));

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
                if (id % 3 == 0) {
                    if (Item.isWood(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    }
                }
                if (id % 3 == 2) {
                    if (Item.isStone(this.inventory.get().get(i).getItem())) {
                        this.inventory.get().get(i).removeQuantity(quantityNeeded);
                    }
                }
                if (id % 3 == 1) {
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


    public static Block rock(final Environment environment)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        for (int i = 0; i < inventory.get().size(); i++) {
            Stack stack = inventory.get().get(i);
            if (Item.isStone(stack.getItem()) && stack.removeQuantity(RockRecipe.NB_STONES))
                return new Block(BlockSet.ROCK, environment);
        }
        return null;
    }

    public static Block pickaxe(final Environment environment, final MaterialSet material)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        for (int i = 0; i < inventory.get().size(); i++) {
            Stack stack = inventory.get().get(i);


        }
        return null;
    }
}
