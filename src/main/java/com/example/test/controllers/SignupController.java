package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable
{
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label unsuccessfulMessageLabel;
    @FXML
    private Label successfulMessageLabel;
    @FXML
    private Button gobackButton;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public void signupButtonOnAction() throws IOException
    {
        if (!isFine())
        {
            successfulMessageLabel.setVisible(false);
            unsuccessfulMessageLabel.setVisible(true);
        }
        else
        {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.AddUser(usernameTextField.getText(), emailTextField.getText(), firstnameTextField.getText(),
                    lastnameTextField.getText(), passwordField.getText());
            unsuccessfulMessageLabel.setVisible(false);
            successfulMessageLabel.setVisible(true);
        }
    }
    public void gobackButtonOnAction()
    {
        try
        {
            Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
            Stage stage = (Stage) gobackButton.getScene().getWindow();
            stage.setScene(new Scene(root, 520, 400));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        DatabaseConnector databaseConnector = new DatabaseConnector();

        usernameTextField.textProperty().addListener(event -> {
            try { usernameTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), databaseConnector.isFoundUser(usernameTextField.getText(), 1)); }
            catch (IOException e) { throw new RuntimeException(e); }
        });

        emailTextField.textProperty().addListener(event ->
        {
            try
            {
                emailTextField.pseudoClassStateChanged(
                        PseudoClass.getPseudoClass("error"),
                        (!emailTextField.getText().isEmpty() && !emailTextField.getText().matches(EMAIL_PATTERN)) ||
                                databaseConnector.isFoundUser(emailTextField.getText(), 3)
                );
            }
            catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    private boolean isFine() throws IOException
    {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        if (usernameTextField.getText().isBlank() || emailTextField.getText().isBlank() || firstnameTextField.getText().isBlank() ||
                lastnameTextField.getText().isBlank() || passwordField.getText().isBlank())
            return false;
        else return !databaseConnector.isFoundUser(usernameTextField.getText(), 1) && !databaseConnector.isFoundUser(emailTextField.getText(), 3) &&
                emailTextField.getText().matches(EMAIL_PATTERN);
    }
}
