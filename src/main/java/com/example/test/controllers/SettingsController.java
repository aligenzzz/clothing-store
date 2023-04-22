package com.example.test.controllers;

import com.example.test.GlobalEntities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable
{
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private PasswordField passwordField;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameTextField.setPromptText(GlobalEntities.USER.getUsername());
        emailTextField.setPromptText(GlobalEntities.USER.getEmail());
        firstnameTextField.setPromptText(GlobalEntities.USER.getFirstName());
        lastnameTextField.setPromptText(GlobalEntities.USER.getLastName());
    }
}
