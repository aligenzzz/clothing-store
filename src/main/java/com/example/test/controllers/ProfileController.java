package com.example.test.controllers;

import com.example.test.GlobalEntities;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController
{
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label accessTypeLabel;

    public void setData()
    {
        usernameLabel.setText(GlobalEntities.USER.getUsername());
        emailLabel.setText(GlobalEntities.USER.getEmail());
        firstNameLabel.setText(GlobalEntities.USER.getFirstName());
        lastNameLabel.setText(GlobalEntities.USER.getLastName());
        accessTypeLabel.setText(String.valueOf(GlobalEntities.USER.getAccessType()));
    }
}
