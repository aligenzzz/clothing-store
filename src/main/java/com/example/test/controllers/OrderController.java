package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Customer;
import com.example.test.entities.Order;
import com.example.test.entities.OrderItem;
import com.example.test.entities.Vendor;
import com.example.test.enums.OrderState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class OrderController
{
    @FXML private Label idLabel;
    @FXML private Label itemsLabel;
    @FXML private Label priceLabel;
    @FXML private AnchorPane customerAnchorPane;
    @FXML private Button payButton;
    @FXML private AnchorPane vendorAnchorPane;
    @FXML private Label orderStateLabel;
    @FXML private ChoiceBox<String> stateChoiceBox;
    @FXML private Label expandButton;
    @FXML private VBox vBox;

    @FXML private ComboBox<AnchorPane> comboBox;


    private Order order;
    public void setData(@NotNull Order order) throws IOException {
        this.order = order;

        idLabel.setText(String.valueOf(Math.floor(order.getId())));
        itemsLabel.setText(order.getItems().size() + " items");
        priceLabel.setText(Constants.FORMAT.format(order.getPrice()) + " $");

        if (GlobalEntities.USER instanceof Customer)
        {
            customerAnchorPane.setVisible(true);
            orderStateLabel.setText(order.getState().toString());

            if (order.getState() == OrderState.paid) payButton.setDisable(true);

            comboBox.setMaxWidth(180);

            for (OrderItem i : order.getItems())
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File(Constants.ORDERITEM).toURI().toURL());
                AnchorPane anchorPane = fxmlLoader.load();
                OrderItemController orderItemController = fxmlLoader.getController();
                orderItemController.setData(i.getItem(), i.getState());

                anchorPane.setOnMouseClicked(event -> System.out.print('9'));

                comboBox.getItems().add(anchorPane);
            }
            comboBox.setEditable(false);
            comboBox.setCellFactory(param -> new AnchorPaneListCell());
        }
        else if (GlobalEntities.USER instanceof Vendor)
        {
            vendorAnchorPane.setVisible(true);

            ObservableList<String> choices = FXCollections.observableArrayList();
            choices.add(OrderState.booked.toString());
            choices.add(OrderState.paid.toString());
            choices.add(OrderState.confirmed.toString());
            choices.add(OrderState.collected.toString());
            choices.add(OrderState.sent.toString());
            choices.add(OrderState.approved.toString());
            choices.add(OrderState.finished.toString());
            stateChoiceBox.setItems(choices);
        }
    }

    public void payButtonOnAction() throws IOException
    {
        order.setState(OrderState.paid);
        GlobalEntities.ORDER = order;

        Parent root = FXMLLoader.load(new File(Constants.PAYMENTPAGE).toURI().toURL());
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Payment");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.centerOnScreen();
    }

    private static class AnchorPaneListCell extends ListCell<AnchorPane>
    {
        @Override
        protected void updateItem(AnchorPane item, boolean empty)
        {

            if (empty || item == null)
            {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(item);

            }
        }
    }
}
