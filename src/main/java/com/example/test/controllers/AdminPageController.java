package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.entities.Item;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable
{
    @FXML
    private Button profileButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button settingsButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane tableAnchorPane;
    @FXML
    private TableView<User> tableView;

    ObservableList<User> users = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            this.profileButtonOnAction();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tableView.setEditable(true);
        tableView.setItems(users);
        TableColumn<User, Double> tableColumn1 = new TableColumn<>("Id");
        TableColumn<User, String> tableColumn2 = new TableColumn<>("Username");
        TableColumn<User, String> tableColumn3 = new TableColumn<>("Password");
        TableColumn<User, String> tableColumn4 = new TableColumn<>("E-mail");
        TableColumn<User, String> tableColumn5 = new TableColumn<>("First name");
        TableColumn<User, String> tableColumn6 = new TableColumn<>("Last name");
        TableColumn<User, AccessType> tableColumn7 = new TableColumn<>("Access type");
        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        // tableColumn1.setEditable(false);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("username"));
        // tableColumn2.setEditable(false);
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>("password"));
        // tableColumn3.setEditable(false);
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>("email"));
        // tableColumn4.setEditable(false);
        tableColumn5.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        // tableColumn5.setEditable(false);
        tableColumn6.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        // tableColumn6.setEditable(false);
        tableColumn7.setCellValueFactory(new PropertyValueFactory<>("accessType"));
        // tableColumn7.setEditable(true);
        tableView.getColumns().setAll(tableColumn1, tableColumn2, tableColumn3, tableColumn4, tableColumn5, tableColumn6, tableColumn7);

        List<User> users = DatabaseConnector.getInstance().getUsers();
        this.users.addAll(users);


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
}
