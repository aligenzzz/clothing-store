package com.example.test.controllers;

import com.example.test.*;
import com.example.test.entities.Item;
import com.example.test.entities.Shop;
import com.example.test.enums.AccessType;
import com.example.test.enums.CustomerChoice;
import com.example.test.interfaces.IListener;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainPageController implements Initializable
{
    @FXML private GridPane gridPane;
    @FXML private ScrollPane scrollPane;
    private GridAnimation animation;
    private List<Item> itemList = new ArrayList<>();
    private IListener listener;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private Map<Double, String> shops = new HashMap<>();
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


        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call() throws IOException
            {
                shops = DatabaseConnector.getInstance().getShops();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        listener = this::chooseItemCard;
        disactiveButtons();
        homeButton.setTextFill(Constants.ACTIVECOLOR);

        try { this.itemList = DatabaseConnector.getInstance().getItems(); }
        catch (IOException exception) { throw new RuntimeException(exception); }
    }
    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    public void profileMenuButtonOnAction() throws IOException
    {
        GlobalEntities.CHOICE = CustomerChoice.PROFILE;
        this.loadCustomerPage();
    }

    public void favouriteMenuButtonOnAction() throws IOException
    {
        GlobalEntities.CHOICE = CustomerChoice.FAVOURITE;
        this.loadCustomerPage();
    }
    public void shoppingMenuButtonOnAction() throws IOException
    {
        GlobalEntities.CHOICE = CustomerChoice.SHOPPING;
        this.loadCustomerPage();
    }

    private void loadCustomerPage() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Parent root = FXMLLoader.load(new File(Constants.CUSTOMERPAGE).toURI().toURL());
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 700));
        stage.centerOnScreen();
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
        itemPriceLabel.setText(Constants.PRICE_FORMAT.format(item.getPrice()) + " $");
        itemShopLabel.setText(shops.get(shopId));
        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }
    @FXML private AnchorPane homePage;
    @FXML private Button homeButton;
    public void homeButtonOnAction()
    {
        if (homeButton.getTextFill() == Constants.ACTIVECOLOR && homePage.isVisible()) return;
        disactiveButtons();

        scrollPane.setVisible(false);
        homePage.setVisible(true);

        homeButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML private Button allItemsButton;
    public void allItemsButtonOnAction() { this.itemsButtonOnAction(allItemsButton, "none", "none"); }
    @FXML private Button blousesButton;
    public void blousesButtonOnAction() { this.itemsButtonOnAction(blousesButton, "blouse", "sweatshirt"); }
    @FXML private Button topsButton;
    public void topsButtonOnAction() { this.itemsButtonOnAction(topsButton, "top", "t-shirt"); }
    @FXML private Button shirtsButton;
    public void shirtsButtonOnAction() { this.itemsButtonOnAction(shirtsButton, "shirt", "none"); }
    @FXML private Button pantsButton;
    public void pantsButtonOnAction() { this.itemsButtonOnAction(pantsButton, "pants", "jeans"); }
    @FXML private Button shortsButton;
    public void shortsButtonOnAction() { this.itemsButtonOnAction(shortsButton, "shorts", "none"); }
    @FXML private Button skirtsButton;
    public void skirtsButtonOnAction() { this.itemsButtonOnAction(skirtsButton, "skirt", "none"); }
    @FXML private Button dressesButton;
    public void dressesButtonOnAction() { this.itemsButtonOnAction(dressesButton, "dress", "none"); }
    @FXML private Button jacketsButton;
    public void jacketsButtonOnAction() { this.itemsButtonOnAction(jacketsButton, "jacket", "none"); }
    @FXML private Button shoesButton;
    public void shoesButtonOnAction() { this.itemsButtonOnAction(shoesButton, "shoes", "none"); }

    public void modifyCollectionButtonOnAction() throws IOException
    {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < Constants.MODIFYCOLLECTION.length; i++)
            items.add(itemList.get((int) Constants.MODIFYCOLLECTION[i] - 1));

        if (animation != null) animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation(items, gridPane, scrollPane, listener, 4);

        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
    }
    public void dazedCollectionButtonOnAction() throws IOException
    {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < Constants.DAZEDCOLLECTION.length; i++)
            items.add(itemList.get((int) Constants.DAZEDCOLLECTION[i] - 1));

        if (animation != null) animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation(items, gridPane, scrollPane, listener, 4);

        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
    }

    public void toFavouriteButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().addFavouriteItem(GlobalEntities.USER.getId(), itemId);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void toShoppingButtonOnAction() throws IOException
    {
        if (GlobalEntities.USER.getAccessType() == AccessType.nonuser)
        {
            alert.showAndWait();
            return;
        }

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                DatabaseConnector.getInstance().addShoppingItem(GlobalEntities.USER.getId(), itemId);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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
        itemShopLabel.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void disactiveButtons()
    {
        homeButton.setTextFill(Color.WHITE);
        allItemsButton.setTextFill(Color.WHITE);
        blousesButton.setTextFill(Color.WHITE);
        topsButton.setTextFill(Color.WHITE);
        shirtsButton.setTextFill(Color.WHITE);
        pantsButton.setTextFill(Color.WHITE);
        shortsButton.setTextFill(Color.WHITE);
        skirtsButton.setTextFill(Color.WHITE);
        dressesButton.setTextFill(Color.WHITE);
        jacketsButton.setTextFill(Color.WHITE);
        shoesButton.setTextFill(Color.WHITE);
    }
    private void itemsButtonOnAction(@NotNull Button button, String parameter, String additional)
    {
        anchorPane.setVisible(false);
        scrollPane.setDisable(false);

        if (button.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        if (animation != null) animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation(this.getItemList(parameter, additional), gridPane, scrollPane, listener, 4);

        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        button.setTextFill(Constants.ACTIVECOLOR);
    }
    private @NotNull List<Item> getItemList(String parameter, String additional)
    {
        List<Item> result = new ArrayList<>();

        for (Item item: itemList)
        {
            if ((parameter.equals("none") && additional.equals("none")) || (item.getName().equals(parameter) && additional.equals("none")) ||
                    item.getName().equals(parameter) || item.getName().equals(additional))
                result.add(item);
        }

        return result;
    }

    public void logoutMenuButtonOnAction() throws IOException
    {
        Parent root = FXMLLoader.load(new File(Constants.LOGIN).toURI().toURL());
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setScene(new Scene(root, 520, 400));
        stage.centerOnScreen();
    }
}
