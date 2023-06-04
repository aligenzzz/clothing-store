package com.example.test.controllers;

import com.example.test.*;
import com.example.test.entities.Customer;
import com.example.test.entities.Item;
import com.example.test.entities.NonUser;
import com.example.test.entities.Vendor;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IListener;
import com.example.test.interfaces.IObserver;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

public class ShopPageController implements Initializable, IObserver
{
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane gridPane;
    @FXML private HBox customerButtons;
    @FXML private HBox vendorButtons;
    @FXML private ImageView bannerImageView;
    @FXML private Label shopNameLabel;
    @FXML private Menu returnMenuItem;
    @FXML private Menu shopToFavouriteMenuItem;
    private final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private IListener listener;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        alert.setHeaderText("You have no rights to this action!");
        alert.setContentText("Do yo want signup or login???");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setBorder(Border.stroke(Color.web("#b56d8c")));
        alert.setOnCloseRequest(event ->
        {
            ButtonType result = alert.getResult();
            if (result == ButtonType.OK)
            {
                try
                {
                    Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
                    Stage stage = (Stage) gridPane.getScene().getWindow();
                    stage.setScene(new Scene(root, 520, 400));
                    stage.centerOnScreen();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    exception.getCause();
                }
            }
        });

        listener = this::chooseItemCard;

        if (GlobalEntities.USER instanceof Customer || GlobalEntities.USER instanceof NonUser)
        {
            customerButtons.setVisible(true);
            shopToFavouriteMenuItem.setVisible(true);
        }
        if (GlobalEntities.USER instanceof Vendor)
        {
            vendorButtons.setVisible(true);
            returnMenuItem.setVisible(true);
        }

        String imagePath = "";
        if (GlobalEntities.SHOP.getImageSource().startsWith("https://"))
            imagePath = GlobalEntities.SHOP.getImageSource();
        else
            imagePath = Constants.SHOPSIMAGEPATH + GlobalEntities.SHOP.getImageSource();

        Image image = new Image(imagePath);
        bannerImageView.setImage(image);
        shopNameLabel.setText(GlobalEntities.SHOP.getName().toUpperCase());
        shopNameLabel.setTextFill(GlobalEntities.SHOP.getTextColor());
        List<Item> itemList = GlobalEntities.SHOP.getItems();

        GridAnimation animation = new GridAnimation(itemList, gridPane, scrollPane, listener, 5);
        animation.start();

        Vendor.addObserver(this);
    }
    @FXML private ImageView imageView;
    @FXML private Label itemNameLabel;
    @FXML private Label itemPriceLabel;
    @FXML private AnchorPane anchorPane;
    private double itemId;
    private Item item;
    public void chooseItemCard(@NotNull Item item)
    {
        this.itemId = item.getId();
        this.item = item;

        String imagePath = "";
        if (item.getImageSource().startsWith("https://"))
            imagePath = item.getImageSource();
        else
            imagePath = Constants.ITEMSIMAGEPATH + item.getImageSource();

        imageView.setImage(new Image(imagePath));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(Constants.PRICE_FORMAT.format(item.getPrice()) + " $");

        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }
    public void toFavouriteButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;
        customer.addFavouriteItem(itemId);
    }
    public void toShoppingButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;
        customer.addShoppingItem(itemId);
    }

    public void shopToFavouriteMenuButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;
        customer.addFavouriteShop(GlobalEntities.SHOP.getId());
    }
    public void editButtonOnAction() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(new File(Constants.EDITITEM).toURI().toURL());
        Parent root = loader.load();
        EditItemController itemController = loader.getController();
        itemController.setData(item);

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 350, 300));
        stage.setTitle("Edit item");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(Constants.ICONPATH))));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.centerOnScreen();
    }
    @FXML private Button deleteButton;
    public void deleteButtonOnButton()
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().deleteItem(item);
                return null;
            }
        };
        deleteButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        Vendor vendor = (Vendor) GlobalEntities.USER;
        vendor.deleteItem(item);
    }
    public void returnMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.VENDORPAGE).toURI().toURL());
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
    }
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update()
    {
        this.closeItemCardButtonOnAction();

        gridPane.getChildren().clear();
        List<Item> itemList = GlobalEntities.SHOP.getItems();
        GridAnimation animation = new GridAnimation(itemList, gridPane, scrollPane, listener, 5);
        animation.start();
    }
}
