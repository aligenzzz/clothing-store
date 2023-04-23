package com.example.test.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable
{
    @FXML private Button cancelButton;
    @FXML private TextField creditCardNumberTextField;
    @FXML private PasswordField cvvPasswordField;
    @FXML private TextField expDateTextField;
    @FXML private TextField nameOnCardTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

}
