package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LoginController
{
    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button skipButton;
    public void loginButtonOnAction(ActionEvent event) throws IOException
    {
        if(!usernameTextField.getText().isBlank() && !passwordField.getText().isBlank()) validateLogin();
        else messageLabel.setVisible(true);
    }

    @FXML
    ProgressIndicator progressIndicator;
    private void validateLogin() throws IOException
    {
        if (messageLabel.isVisible()) messageLabel.setVisible(false);
        loginButton.setDisable(true);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                DatabaseConnector databaseConnector = new DatabaseConnector();
                return databaseConnector.isFoundUser(usernameTextField.getText(), passwordField.getText());
            }
        };
        task.setOnSucceeded(event -> {
            boolean result = task.getValue();
            if (result)
            {
                Parent root = null;
                try { root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL()); }
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
    public void signupButtonOnAction(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(new File(Constants.SIGNUP).toURI().toURL());
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root, 520, 400));
            stage.centerOnScreen();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }
    public void skipButtonOnAction()
    {
        try
        {
            Parent root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
            Stage stage = (Stage) skipButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.centerOnScreen();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }
    public void cancelButtonOnAction(ActionEvent event)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}