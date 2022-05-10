package fr.sae.terraria.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable
{
    @FXML
    TilePane gamePane;
    @FXML
    HBox title;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}