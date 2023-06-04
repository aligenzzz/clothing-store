package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Admin;
import com.example.test.entities.Customer;
import com.example.test.entities.Vendor;
import com.example.test.enums.AccessType;
import com.example.test.enums.UserAccount;
import com.example.test.interfaces.User;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button changeButton;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameTextField.setPromptText(GlobalEntities.USER.getUsername());
        emailTextField.setPromptText(GlobalEntities.USER.getEmail());
        firstnameTextField.setPromptText(GlobalEntities.USER.getFirstName());
        lastnameTextField.setPromptText(GlobalEntities.USER.getLastName());

        usernameTextField.textProperty().addListener(event ->
        {
            try { usernameTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    databaseConnector.isFoundUser(usernameTextField.getText(), UserAccount.USERNAME)); }
            catch (IOException e) { throw new RuntimeException(e); }
        });
        emailTextField.textProperty().addListener(event ->
        {
            try
            {
                emailTextField.pseudoClassStateChanged(
                        PseudoClass.getPseudoClass("error"),
                        (!emailTextField.getText().isEmpty() && !emailTextField.getText().matches(Constants.EMAIL_PATTERN)) ||
                                databaseConnector.isFoundUser(emailTextField.getText(), UserAccount.EMAIL)
                );
            }
            catch (IOException e) { throw new RuntimeException(e); }
        });
        passwordField.textProperty().addListener(event ->
        {
            passwordField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !passwordField.getText().isEmpty() && passwordField.getText().length() < 8
            );
        });

        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        firstnameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        lastnameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
    }

    public void changeButtonOnAction()
    {
        User editedUser = new User(GlobalEntities.USER);

        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String password = passwordField.getText();

        boolean allIsBlank = true;

        if (username != null && !username.isBlank() && !username.isEmpty())
        {
            editedUser.setUsername(username);
            allIsBlank = false;
        }
        if (email != null && !email.isBlank() && !email.isEmpty())
        {
            editedUser.setEmail(email);
            allIsBlank = false;
        }
        if (firstname != null && !firstname.isBlank() && !firstname.isEmpty())
        {
            editedUser.setFirstName(firstname);
            allIsBlank = false;
        }
        if (lastname != null && !lastname.isBlank() && !lastname.isEmpty())
        {
            editedUser.setLastName(lastname);
            allIsBlank = false;
        }
        if (password != null && !password.isBlank() && !password.isEmpty())
        {
            editedUser.setPassword(password);
            allIsBlank = false;
        }

        boolean isError = usernameTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                          emailTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                          passwordField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error"));

        if (isError || allIsBlank)
            errorLabel.setVisible(true);
        else
        {
            Task<Void> task = new Task<>()
            {
                @Override
                protected @Nullable Void call() throws IOException
                {
                    DatabaseConnector.getInstance().editUser(editedUser);

                    if (editedUser.getAccessType() == AccessType.customer)
                        GlobalEntities.USER = new Customer(editedUser);
                    else if (editedUser.getAccessType() == AccessType.vendor)
                        GlobalEntities.USER = new Vendor(editedUser);
                    else if (editedUser.getAccessType() == AccessType.admin)
                        GlobalEntities.USER = new Admin(editedUser);

                    return null;
                }
            };
            task.setOnSucceeded(event ->
            {
                successLabel.setVisible(true);

                usernameTextField.setPromptText(GlobalEntities.USER.getUsername());
                emailTextField.setPromptText(GlobalEntities.USER.getEmail());
                firstnameTextField.setPromptText(GlobalEntities.USER.getFirstName());
                lastnameTextField.setPromptText(GlobalEntities.USER.getLastName());
            });

            changeButton.disableProperty().bind(task.runningProperty());

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            usernameTextField.clear();
            emailTextField.clear();
            firstnameTextField.clear();
            lastnameTextField.clear();
            passwordField.clear();
        }
    }
}
