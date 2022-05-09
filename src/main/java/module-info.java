module fr.sae.terraria {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.sae.terraria to javafx.fxml;
    exports fr.sae.terraria;
}