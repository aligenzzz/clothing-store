package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.NonUser;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginController
{
    @FXML private Label messageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Button cancelButton;
    @FXML private Button skipButton;
    @FXML private ProgressIndicator progressIndicator;
    public void loginButtonOnAction()
    {
        if(!usernameTextField.getText().isBlank() && !passwordField.getText().isBlank()) validateLogin();
        else messageLabel.setVisible(true);
    }
    private void validateLogin()
    {
        if (messageLabel.isVisible()) messageLabel.setVisible(false);
        loginButton.setDisable(true);

        Task<User> task = new Task<>()
        {
            @Override
            protected User call() throws Exception
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                return databaseConnector.getUser(usernameTextField.getText(), passwordField.getText());
            }
        };
        task.setOnSucceeded(event ->
        {
            GlobalEntities.USER = task.getValue();
            if (GlobalEntities.USER != null)
            {
                Parent root = null;
                try
                {
                    if (GlobalEntities.USER.getAccessType() == AccessType.customer) root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
                    else if (GlobalEntities.USER.getAccessType() == AccessType.vendor) root = FXMLLoader.load(new File(Constants.VENDORPAGE).toURI().toURL());
                    else if (GlobalEntities.USER.getAccessType() == AccessType.admin) root = FXMLLoader.load(new File(Constants.ADMINPAGE).toURI().toURL());
                }
                catch (IOException exception) { throw new RuntimeException(exception); }
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 700));
                stage.centerOnScreen();
            }
            else messageLabel.setVisible(true);
            loginButton.setDisable(false);
        });

        progressIndicator.progressProperty().bind(task.progressProperty());
        progressIndicator.visibleProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void signupButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.SIGNUP).toURI().toURL());
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
        stage.centerOnScreen();
    }
    public void skipButtonOnAction() throws IOException
    {
        GlobalEntities.USER = new NonUser(AccessType.nonuser);

        Parent root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
        Stage stage = (Stage) skipButton.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }
    public void cancelButtonOnAction()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

        GlobalEntities.USER = new User();
    }
}