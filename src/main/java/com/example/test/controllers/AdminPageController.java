package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GridAnimation;
import com.example.test.entities.OrderItem;
import com.example.test.entities.Request;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
    @FXML private Button ordersButton;
    @FXML private Button requestsButton;
    @FXML private Button settingsButton;
    @FXML private StackPane stackPane;
    @FXML private AnchorPane tableAnchorPane;
    @FXML private TableView<User> tableView;
    @FXML private ProgressBar progressBar;
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    public ObservableList<User> users = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try { this.profileButtonOnAction(); }
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

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(false);
        scrollPane.setVisible(false);

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

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(true);
        scrollPane.setVisible(false);

        fillTable();

        usersButton.setTextFill(Constants.ACTIVECOLOR);
    }

    GridAnimation animation;
    public void ordersButtonOnAction() throws IOException
    {
        if (ordersButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(false);
        scrollPane.setVisible(true);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        List<OrderItem> orders = DatabaseConnector.getInstance().getOrderItems();
        animation = new GridAnimation(orders, gridPane, scrollPane, null, 2);
        animation.start();

        ordersButton.setTextFill(Constants.ACTIVECOLOR);
    }
    public void requestsButtonOnAction() throws IOException
    {
        if (requestsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(false);
        scrollPane.setVisible(true);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        List<Request> requests = DatabaseConnector.getInstance().getRequests();
        animation = new GridAnimation(requests, gridPane, scrollPane, null, 1);
        animation.start();

        requestsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void settingsButtonOnAction() throws IOException
    {
        if (settingsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(false);
        scrollPane.setVisible(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.SETTINGS).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        stackPane.getChildren().add(anchorPane);

        settingsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.close();
    }

    public void logoutMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
        stage.centerOnScreen();
    }

    private void disactiveButtons()
    {
        profileButton.setTextFill(Constants.DISACTIVECOLOR);
        usersButton.setTextFill(Constants.DISACTIVECOLOR);
        ordersButton.setTextFill(Constants.DISACTIVECOLOR);
        requestsButton.setTextFill(Constants.DISACTIVECOLOR);
        settingsButton.setTextFill(Constants.DISACTIVECOLOR);
    }

    private void fillTable()
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
        ordersButton.disableProperty().bind(task.runningProperty());
        requestsButton.disableProperty().bind(task.runningProperty());
        settingsButton.disableProperty().bind(task.runningProperty());

        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.visibleProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
