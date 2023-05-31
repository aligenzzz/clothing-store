package com.example.test.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConnectionErrorController
{
    @FXML private Button okButton;
    public void okButtonOnAction()
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
