package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.entities.Request;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class RequestController
{
    @FXML private Label idLabel;
    @FXML private TextField subjectTextField;
    @FXML private TextArea messageTextArea;
    @FXML private Button approveButton;

    private Request request;

    public void setData(@NotNull Request request)
    {
        this.request = request;

        idLabel.setText(Constants.ID_FORMAT.format(request.getId()));
        subjectTextField.setText(request.getSubject());
        messageTextArea.setText(request.getMessage());
    }

    public void approveButtonOnAction()
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                request.Delete();
                return null;
            }
        };
        approveButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
