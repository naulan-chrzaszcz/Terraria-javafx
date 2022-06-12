module fr.sae.terraria
{
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;

    opens fr.sae.terraria to javafx.graphics;
    exports fr.sae.terraria;
    opens fr.sae.terraria.controller to javafx.fxml;
    exports fr.sae.terraria.modele;
    opens fr.sae.terraria.modele to javafx.fxml;
    exports fr.sae.terraria.modele.entities;
    opens fr.sae.terraria.modele.entities to javafx.fxml;
    exports fr.sae.terraria.modele.entities.entity;
    opens fr.sae.terraria.modele.entities.entity to javafx.fxml;
    exports fr.sae.terraria.modele.entities.player;
    opens fr.sae.terraria.modele.entities.player to javafx.fxml;
    exports fr.sae.terraria.modele.entities.player.inventory;
    opens fr.sae.terraria.modele.entities.player.inventory to javafx.fxml;
}