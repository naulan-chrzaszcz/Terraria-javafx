module fr.sae.terraria {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens fr.sae.terraria to javafx.fxml;
    exports fr.sae.terraria;
    exports fr.sae.terraria.controller;
    opens fr.sae.terraria.controller to javafx.fxml;
    exports fr.sae.terraria.modele;
    opens fr.sae.terraria.modele to javafx.fxml;
    exports fr.sae.terraria.modele.entities;
    opens fr.sae.terraria.modele.entities to javafx.fxml;
    exports fr.sae.terraria.modele.entities.entity;
    opens fr.sae.terraria.modele.entities.entity to javafx.fxml;
}