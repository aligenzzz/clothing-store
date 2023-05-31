package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Order;
import com.example.test.entities.OrderItem;
import com.example.test.enums.OrderState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
    @FXML private Button payButton;
    @FXML private Label orderStateLabel;
    @FXML private VBox vBox;
    @FXML private ComboBox<AnchorPane> comboBox;

    private Order order;
    public void setData(@NotNull Order order) throws IOException
    {
        this.order = order;

        idLabel.setText(Constants.ID_FORMAT.format(order.getId()));
        itemsLabel.setText(order.getItems().size() + " items");
        priceLabel.setText(Constants.PRICE_FORMAT.format(order.getPrice()) + " $");

        orderStateLabel.setText(order.getState().toString());

        if (order.getState() == OrderState.paid) payButton.setDisable(true);

        comboBox.setMaxWidth(180);

        for (OrderItem i : order.getItems())
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.ORDERITEM).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();
            OrderItemController orderItemController = fxmlLoader.getController();
            orderItemController.setData(i);

            anchorPane.setOnMouseClicked(event -> System.out.print('9'));

            comboBox.getItems().add(anchorPane);
        }
        comboBox.setEditable(false);
        comboBox.setCellFactory(param -> new AnchorPaneListCell());
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
