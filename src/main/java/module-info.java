module fr.sae.terraria {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.sae.terraria to javafx.fxml;
    exports fr.sae.terraria;
    exports fr.sae.terraria.controller;
    opens fr.sae.terraria.controller to javafx.fxml;
    exports fr.sae.terraria.modele;
    opens fr.sae.terraria.modele to javafx.fxml;
}