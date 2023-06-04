package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.GridAnimation;
import com.example.test.entities.Admin;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IObserver;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable, IObserver
{
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    private final Admin admin = (Admin) GlobalEntities.USER;
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
    @FXML private ComboBox<String> comboBox;
    @FXML private TextField userIdLabel;
    @FXML private Button editRoleButton;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try { this.profileButtonOnAction(); }
        catch (IOException exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        Task<Void> task1 = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                admin.setRequests(databaseConnector.getRequests());
                return null;
            }
        };
        requestsButton.disableProperty().bind(task1.runningProperty());

        Task<Void> task2 = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                admin.setOrders(databaseConnector.getSpecificOrders("approved"));
                return null;
            }
        };
        ordersButton.disableProperty().bind(task2.runningProperty());

        Thread thread1 = new Thread(task1);
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.setDaemon(true);
        thread2.start();

        Admin.addObserver(this);

        comboBox.getItems().add(AccessType.customer.toString());
        comboBox.getItems().add(AccessType.vendor.toString());
        comboBox.getItems().add(AccessType.admin.toString());

        userIdLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        comboBox.setOnMouseClicked(event -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
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
    public void ordersButtonOnAction() { this.itemsButtonOnAction(ordersButton, admin.getOrders(), 1, false); }
    public void requestsButtonOnAction() { this.itemsButtonOnAction(requestsButton, admin.getRequests(), 1, false); }

    public void itemsButtonOnAction(@NotNull Button button, List<?> list, int maxColumn, boolean update)
    {
        if (button.getTextFill() == Constants.ACTIVECOLOR && !update) return;
        disactiveButtons();

        if(stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);
        tableAnchorPane.setVisible(false);
        scrollPane.setVisible(true);

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(list, gridPane, scrollPane, null, maxColumn);
        animation.start();

        button.setTextFill(Constants.ACTIVECOLOR);
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
        tableView.getItems().clear();
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

    @Override
    public void update()
    {
        try
        {
            if (ordersButton.getTextFill() == Constants.ACTIVECOLOR)
                this.itemsButtonOnAction(ordersButton, admin.getOrders(), 1, true);
            if (requestsButton.getTextFill() == Constants.ACTIVECOLOR)
                this.itemsButtonOnAction(requestsButton, admin.getRequests(), 1, true);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public void editRoleButtonOnAction()
    {
        if (!userIdLabel.getText().matches("^\\d+") ||
            comboBox.getValue() == null)
        {
            errorLabel.setVisible(true);
            return;
        }

        Task<Boolean> task = new Task<>()
        {
            @Override
            protected @NotNull Boolean call() throws IOException
            {
                return admin.editRole(Double.parseDouble(userIdLabel.getText()),
                    AccessType.valueOf(comboBox.getValue()));
            }
        };
        task.setOnSucceeded(event ->
        {
            if (task.getValue())
            {
                successLabel.setVisible(true);
                fillTable();
            }
            else
                errorLabel.setVisible(true);
        });
        editRoleButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
