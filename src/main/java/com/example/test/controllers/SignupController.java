package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.enums.UserAccount;
import com.example.test.interfaces.User;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Label unsuccessfulMessageLabel;
    @FXML private Label successfulMessageLabel;
    @FXML private Button gobackButton;
    @FXML private Button signupButton;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public void signupButtonOnAction() throws IOException
    {
        Task<Boolean> task = new Task<>()
        {
            @Override
            protected @NotNull Boolean call() throws IOException
            {
                return isFine();
            }
        };
        task.setOnSucceeded(event ->
        {
            if (!task.getValue())
            {
                unsuccessfulMessageLabel.setVisible(true);
            }
            else
            {
                try
                {
                    databaseConnector.addUser(new User(0, usernameTextField.getText(),  passwordField.getText(),
                            emailTextField.getText(), firstnameTextField.getText(),
                            lastnameTextField.getText(), AccessType.customer));
                } catch (IOException e) { throw new RuntimeException(e); }

                usernameTextField.clear();
                emailTextField.clear();
                firstnameTextField.clear();
                lastnameTextField.clear();
                passwordField.clear();

                successfulMessageLabel.setVisible(true);
            }
        });
        signupButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void gobackButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
        Stage stage = (Stage) gobackButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        emailTextField.textProperty().addListener(event ->
        {
            emailTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !emailTextField.getText().isEmpty() && !emailTextField.getText().matches(EMAIL_PATTERN)
            );
        });
        passwordField.textProperty().addListener(event ->
        {
            passwordField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    (!passwordField.getText().isEmpty() && passwordField.getText().length() < 8)
            );
        });

        usernameTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(false);
        }));
        emailTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(false);
        }));
        firstnameTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(false);
        }));
        lastnameTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(false);
        }));
        passwordField.textProperty().addListener(((observable, oldValue, newValue) -> {
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(false);
        }));
    }

    private boolean isFine() throws IOException
    {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || username.isEmpty() ||
            email == null || email.isBlank() || email.isEmpty() ||
            firstname == null || firstname.isBlank() || firstname.isEmpty() ||
            lastname == null || lastname.isBlank() || lastname.isEmpty() ||
            password == null || password.isBlank() || password.isEmpty())
            return false;
        else return !(databaseConnector.isFoundUser(usernameTextField.getText(), UserAccount.USERNAME) ||
                    databaseConnector.isFoundUser(emailTextField.getText(), UserAccount.EMAIL) ||
                    emailTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                    passwordField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")));
    }
}
