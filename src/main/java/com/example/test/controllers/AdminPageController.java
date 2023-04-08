package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable
{
    @FXML private Button profileButton;
    @FXML private Button usersButton;
    @FXML private Button settingsButton;
    @FXML private StackPane stackPane;
    @FXML private AnchorPane tableAnchorPane;
    @FXML private TableView<User> tableView;
    public ObservableList<User> users = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            this.profileButtonOnAction();
            this.fillTable();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public void profileButtonOnAction() throws IOException
    {
        if (profileButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        tableAnchorPane.setVisible(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.PROFILE).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.setData();
        stackPane.getChildren().add(anchorPane);

        profileButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void usersButtonOnAction()
    {
        if (usersButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if(stackPane.getChildren().size() == 2) stackPane.getChildren().remove(1);
        tableAnchorPane.setVisible(true);

        usersButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.close();
    }

    private void disactiveButtons()
    {
        profileButton.setTextFill(Color.WHITE);
        usersButton.setTextFill(Color.WHITE);
        settingsButton.setTextFill(Color.WHITE);
    }

    private void fillTable() throws IOException
    {
        tableView.setItems(users);

        TableColumn<User, Double> tableColumn1 = new TableColumn<>("Id");
        TableColumn<User, String> tableColumn2 = new TableColumn<>("Username");
        TableColumn<User, String> tableColumn3 = new TableColumn<>("Password");
        TableColumn<User, String> tableColumn4 = new TableColumn<>("E-mail");
        TableColumn<User, String> tableColumn5 = new TableColumn<>("First name");
        TableColumn<User, String> tableColumn6 = new TableColumn<>("Last name");
        TableColumn<User, AccessType> tableColumn7 = new TableColumn<>("Access type");

        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>("password"));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumn5.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumn6.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumn7.setCellValueFactory(new PropertyValueFactory<>("accessType"));

        List<TableColumn<User, ?>> columns = new ArrayList<>();
        columns.add(tableColumn1);
        columns.add(tableColumn2);
        columns.add(tableColumn3);
        columns.add(tableColumn4);
        columns.add(tableColumn5);
        columns.add(tableColumn6);
        columns.add(tableColumn7);
        tableView.getColumns().setAll(columns);

        Task<List<User>> task = new Task<>()
        {
            @Override
            protected List<User> call() throws IOException
            { return DatabaseConnector.getInstance().getUsers(); }
        };
        task.setOnSucceeded(event -> this.users.addAll(task.getValue()));

        profileButton.disableProperty().bind(task.runningProperty());
        usersButton.disableProperty().bind(task.runningProperty());
        settingsButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
