package fr.sae.terraria.modele.blocks;


public class Stone extends Block
{
    /*
        TODO: Mettre en place un drop aléatoire de charbon
             Si le joueur craft la Stone, il ne drop pas de charbon
             Mettre une durabilité
             Le nombre de pierre qui est drop est entre 1 et 4
             (Optionnel) Faire un bruitage lorsque le joueur le casse
             (Optionnel) Faire des particules de pierre lorsque on est en train de le casser
    */
    public Stone(int x, int y){
        super(x, y);

    }

    @Override
    public void updates()
    {
    }
}
