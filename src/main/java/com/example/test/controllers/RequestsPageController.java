package com.example.test.controllers;

import com.example.test.entities.Request;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RequestsPageController implements Initializable
{
    @FXML private TextField subjectTextField;
    @FXML private TextArea messageTextArea;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;
    @FXML private Button sendButton;

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

       TextFormatter<String> subjectTextFormatter = new TextFormatter<>(
                change -> change.getControlNewText().length() <= 100 ? change : null);
       TextFormatter<String> messageTextFormatter = new TextFormatter<>(
                change -> change.getControlNewText().length() <= 1000 ? change : null);

       subjectTextField.setTextFormatter(subjectTextFormatter);
       messageTextArea.setTextFormatter(messageTextFormatter);
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

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                Request request = new Request(subject, message);
                request.Send();

                return null;
            }
        };
        task.setOnSucceeded(event -> successLabel.setVisible(true));

        sendButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
