package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.Main;
import com.example.test.entities.Item;
import com.example.test.entities.Shop;
import com.example.test.enums.AccessType;
import com.example.test.interfaces.IListener;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainPageController implements Initializable
{
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;

    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    public void profileMenuButtonOnAction()
    {
        if (GlobalEntities.USER.accessType == AccessType.nonuser) return;

        try
        {
            Parent root = FXMLLoader.load(new File(Constants.CUSTOMERPAGE).toURI().toURL());
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.centerOnScreen();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    private final List<Item> itemList = new ArrayList<>();

    private IListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;
        disactiveButtons();
        homeButton.setTextFill(Constants.ACTIVECOLOR);

        DatabaseConnector databaseConnector = new DatabaseConnector();
        try { databaseConnector.getItems(itemList); }
        catch (IOException exception) { throw new RuntimeException(exception); }
    }

    @FXML
    private ImageView imageView;
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label itemPriceLabel;
    @FXML
    private Label itemShopLabel;
    @FXML
    private AnchorPane anchorPane;

    private double itemId;
    private double shopId;
    public void chooseItemCard(Item item)
    {
        this.itemId = item.getId();
        this.shopId = item.getShop();

        imageView.setImage(new Image(Constants.ITEMSIMAGEPATH + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(String.valueOf(item.getPrice()));
        DatabaseConnector databaseConnector = new DatabaseConnector();
        itemShopLabel.setText(databaseConnector.getShop(shopId).getName());
        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }

    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }

    @FXML
    private AnchorPane homePage;
    @FXML
    private Button homeButton;
    public void homeButtonOnAction()
    {
        if (homeButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        scrollPane.setVisible(false);
        homePage.setVisible(true);

        homeButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button allItemsButton;
    public void allItemsButtonOnAction()
    {
        if (allItemsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("none", "none");
        if (homePage.isVisible())
        {
            scrollPane.setVisible(true);
            homePage.setVisible(false);
        }
        animation.start();
        allItemsButton.setTextFill(Constants.ACTIVECOLOR);
    }

    private class GridAnimation extends AnimationTimer
    {
        private final int size = itemList.size();
        private int i = 0;
        String parameter;
        String additional;
        int column = 0;
        int row = 0;
        public GridAnimation(String parameter, String additional)
        {
            this.parameter = parameter;
            this.additional = additional;
        }

        @Override
        public void handle(long now)
        {
            scrollPane.setHvalue(0);
            scrollPane.setVvalue(0);

            try { doHandle(); }
            catch (IOException e) { throw new RuntimeException(e); }

            gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        }

        private void doHandle() throws IOException
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(Constants.ITEM).toURI().toURL());
            AnchorPane anchorPane = fxmlLoader.load();

            Item item = itemList.get(i);
            if (column == 4)
            {
                column = 0;
                row++;
            }

            if ((parameter.equals("none") && additional.equals("none")) || (item.getName().equals(parameter) && additional.equals("none")) ||
                    item.getName().equals(parameter) || item.getName().equals(additional))
            {
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(item, listener);
                gridPane.add(anchorPane, column++, row);
            }

            GridPane.setMargin(anchorPane, new Insets(10));

            i++;
            if (i >= size) stop();
        }
    }

    private GridAnimation animation = new GridAnimation("none", "none");
    @FXML
    private Button blousesButton;
    public void blousesButtonOnAction()
    {
        if (blousesButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("blouse", "sweatshirt");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        blousesButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button topsButton;
    public void topsButtonOnAction()
    {
        if (topsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("top", "t-shirt");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        topsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button shirtsButton;
    public void shirtsButtonOnAction()
    {
        if (shirtsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("shirt", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        shirtsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button pantsButton;
    public void pantsButtonOnAction()
    {
        if (pantsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("pants", "jeans");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        pantsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button shortsButton;
    public void shortsButtonOnAction()
    {
        if (shortsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("shorts", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        shortsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button skirtsButton;
    public void skirtsButtonOnAction()
    {
        if (skirtsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("skirt", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        skirtsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button dressesButton;
    public void dressesButtonOnAction()
    {
        if (dressesButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("dress", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        dressesButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button jacketsButton;
    public void jacketsButtonOnAction()
    {
        if (jacketsButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("jacket", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        jacketsButton.setTextFill(Constants.ACTIVECOLOR);
    }
    @FXML
    private Button shoesButton;
    public void shoesButtonOnAction()
    {
        if (shoesButton.getTextFill() == Constants.ACTIVECOLOR) return;
        disactiveButtons();

        animation.stop();

        gridPane.getChildren().clear();
        animation = new GridAnimation("shoes", "none");
        if (homePage.isVisible())
        {
            homePage.setVisible(false);
            scrollPane.setVisible(true);
        }
        animation.start();
        shoesButton.setTextFill(Constants.ACTIVECOLOR);
    }

    public void toFavouriteButtonOnAction()
    {
        if (GlobalEntities.USER.accessType == AccessType.nonuser) return;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.addFavouriteItem(GlobalEntities.USER.id, this.itemId);
    }
    public void toShoppingButtonOnAction()
    {
        if (GlobalEntities.USER.accessType == AccessType.nonuser) return;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.addShoppingItem(GlobalEntities.USER.id, this.itemId);
    }
    public void shopButtonOnAction()
    {
        GlobalEntities.SHOP = new Shop();
        GlobalEntities.SHOP.setId(this.shopId);

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                DatabaseConnector databaseConnector = new DatabaseConnector();
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
}
