package com.example.test.controllers;

import com.example.test.entities.Request;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsPageController implements Initializable
{
    @FXML private TextField subjectTextField;
    @FXML private TextArea messageTextArea;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
       subjectTextField.textProperty().addListener((observable, oldValue, newValue) -> {
           successLabel.setVisible(false);
           errorLabel.setVisible(false);
       });

       messageTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
           successLabel.setVisible(false);
           errorLabel.setVisible(false);
       });
    }

    public void sendButtonOnAction()
    {
        String subject = subjectTextField.getText();
        String message = messageTextArea.getText();

        if (subject == null || subject.isEmpty() || subject.isBlank() ||
            message == null || message.isEmpty() || message.isBlank())
        {
            errorLabel.setVisible(true);
            return;
        }

        Request request = new Request(subject, message);
        // to database
        successLabel.setVisible(true);
    }
}
