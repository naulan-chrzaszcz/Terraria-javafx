package fr.sae.terraria.modele.blocks;

import fr.sae.terraria.modele.entities.entity.Entity;

public class TallGrass extends Entity
{
    /*
        TODO: Donne de la fibre au joueur de manière aléatoire
              Peut ne pas drop ou drop juste 1 (Pas plus)
              (Optionnel) Faire que l'herbe au passage du joueur provoque un bruitage
              (Optionnel) Faire une animation lorsque le joueur passe dedans
     */
    public TallGrass(int x, int y){
        super(x,y);

    }

    @Override
    public void updates() {

    }
}
