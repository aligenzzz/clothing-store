package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.Shop;
import com.example.test.interfaces.IListener;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable
{
    @FXML private Button profileButton;
    @FXML private Button favouriteItemsButton;
    @FXML private Button shoppingItemsButton;
    @FXML private Button ordersButton;
    @FXML private Button purchasedItemsButton;
    @FXML private Button shopsButton;
    @FXML private Button settingsButton;
    @FXML private Button returnButton;
    @FXML private ScrollPane scrollPane;
    @FXML private StackPane stackPane;
    @FXML private GridPane gridPane;
    @FXML private VBox vBox;
    private IListener listener;
    private final Customer customer = (Customer) GlobalEntities.USER;
    private GridAnimation animation;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;
        disactiveButtons();
        try { this.profileButtonOnAction(); }
        catch (IOException exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

                customer.setFavouriteItems(databaseConnector.getFavouriteItems(customer.getId()));
                customer.setShoppingItems(databaseConnector.getShoppingItems(customer.getId()));
                customer.setPurchasedItems(databaseConnector.getPurchasedItems(customer.getId()));
                customer.setFavouriteShops(databaseConnector.getFavouriteShops(customer.getId()));
                customer.setOrders(databaseConnector.getOrders(customer.getId()));

                return null;
            }
        };

        profileButton.disableProperty().bind(task.runningProperty());
        favouriteItemsButton.disableProperty().bind(task.runningProperty());
        shoppingItemsButton.disableProperty().bind(task.runningProperty());
        ordersButton.disableProperty().bind(task.runningProperty());
        purchasedItemsButton.disableProperty().bind(task.runningProperty());
        shopsButton.disableProperty().bind(task.runningProperty());
        settingsButton.disableProperty().bind(task.runningProperty());
        returnButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    @FXML private ImageView imageView;
    @FXML private Label itemNameLabel;
    @FXML private Label itemPriceLabel;
    @FXML private Label itemShopLabel;
    @FXML private AnchorPane anchorPane;
    private double itemId;
    private double shopId;
    public void chooseItemCard(@NotNull Item item) throws IOException
    {
        this.itemId = item.getId();
        this.shopId = item.getShop();

        imageView.setImage(new Image(Constants.ITEMSIMAGEPATH + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(Constants.FORMAT.format(item.getPrice()) + " $");
        itemShopLabel.setText(DatabaseConnector.getInstance().getShop(item.getShop()).getName());
        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    @FXML Button closeItemCardButton;
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }
    public void profileButtonOnAction() throws IOException
    {
        if (profileButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.PROFILE).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.setData();
        stackPane.getChildren().add(anchorPane);

        profileButton.setTextFill(Constants.ACTIVECOLOR);
    }
    public void favouriteItemsButtonOnAction() throws IOException { this.itemsButtonOnAction(favouriteItemsButton, customer.getFavouriteItems(), 4, false); }
    public void shoppingItemsButtonOnAction() throws IOException  { this.itemsButtonOnAction(shoppingItemsButton, customer.getShoppingItems(), 4, false); }
    public void ordersButtonOnAction() throws IOException { this.itemsButtonOnAction(ordersButton, customer.getOrders(), 1, false); }
    public void purchasedItemsButtonOnAction() throws IOException { this.itemsButtonOnAction(purchasedItemsButton, customer.getPurchasedItems(), 4, false); }
    public void shopsButtonOnAction() throws IOException { this.itemsButtonOnAction(shopsButton, customer.getFavouriteShops(), 1, false); }
    public void settingsButtonOnAction() throws IOException
    {
        if (settingsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);

        if (vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(new File(Constants.SETTINGS).toURI().toURL());
        AnchorPane anchorPane = fxmlLoader.load();
        stackPane.getChildren().add(anchorPane);

        settingsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    public void returnButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.MAINPAGE).toURI().toURL());
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.close();
    }
    @FXML private Button toFavouriteButton;
    public void toFavouriteButtonOnAction() throws IOException { customer.addFavouriteItem(this.itemId); }
    @FXML private Button toShoppingButton;
    public void toShoppingButtonOnAction() throws IOException { customer.addShoppingItem(this.itemId); }

    public void deleteButtonOnAction() throws IOException
    {
        if (favouriteItemsButton.getTextFill() == Constants.ACTIVECOLOR)
        {
            customer.deleteFavouriteItem(this.itemId);
            this.itemsButtonOnAction(favouriteItemsButton, customer.getFavouriteItems(), 4, true);
        }
        if (shoppingItemsButton.getTextFill() == Constants.ACTIVECOLOR)
        {
            customer.deleteShoppingItem(this.itemId);
            this.itemsButtonOnAction(shoppingItemsButton, customer.getShoppingItems(), 4, true);
        }
    }
    private void itemsButtonOnAction(@NotNull Button button, List<?> list, int maxColumn, boolean update) throws IOException
    {
        anchorPane.setVisible(false);
        scrollPane.setDisable(false);

        if (button.getTextFill() == Constants.ACTIVECOLOR && !update) return;
        disactiveButtons();

        scrollPane.setVisible(true);
        if (button == favouriteItemsButton)
        {
            toFavouriteButton.setVisible(false);
            toShoppingButton.setVisible(true);
        }
        if (button == shoppingItemsButton)
        {
            toFavouriteButton.setVisible(true);
            toShoppingButton.setVisible(false);
        }

        if (button != shoppingItemsButton && vBox.getChildren().size() == 2) vBox.getChildren().remove(1);
        if (stackPane.getChildren().size() == 3) stackPane.getChildren().remove(2);

        if (button == shoppingItemsButton)
        {
            if (update) vBox.getChildren().remove(1);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.PAYMENT).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();
            PaymentController paymentController = fxmlLoader.getController();
            paymentController.setData();
            vBox.getChildren().add(anchorPane);
        }

        if (animation != null) animation.stop();
        gridPane.getChildren().clear();
        animation = new GridAnimation(list, gridPane, scrollPane, listener, maxColumn);
        animation.start();

        button.setTextFill(Constants.ACTIVECOLOR);
    }

    public void shopButtonOnAction()
    {
        GlobalEntities.SHOP = new Shop();
        GlobalEntities.SHOP.setId(this.shopId);

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                GlobalEntities.SHOP = databaseConnector.getShop(GlobalEntities.SHOP.getId());
                return null;
            }
        };
        task.setOnSucceeded(event ->
        {
            try
            {
                Parent root = FXMLLoader.load(new File(Constants.SHOPPAGE).toURI().toURL());
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 900, 700));
                stage.setTitle("Shop");
                stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                stage.centerOnScreen();

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                exception.getCause();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void disactiveButtons()
    {
        profileButton.setTextFill(Color.WHITE);
        favouriteItemsButton.setTextFill(Color.WHITE);
        shoppingItemsButton.setTextFill(Color.WHITE);
        ordersButton.setTextFill(Color.WHITE);
        purchasedItemsButton.setTextFill(Color.WHITE);
        shopsButton.setTextFill(Color.WHITE);
        settingsButton.setTextFill(Color.WHITE);
        returnButton.setTextFill(Color.WHITE);
    }

    public void logoutMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
        stage.centerOnScreen();
    }
}
