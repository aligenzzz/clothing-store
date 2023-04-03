package com.example.test.controllers;

import com.example.test.DatabaseConnector;
import com.example.test.controllers.ItemController;
import com.example.test.entities.Item;
import com.example.test.interfaces.IListener;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable
{
    @FXML
    GridPane gridPane;
    @FXML
    MenuItem closeMenuItem;
    @FXML
    ScrollPane scrollPane;

    Color color = Color.web("#484c58");

    public void closeMenuButtonOnAction()
    {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();
    }

    private final List<Item> itemList = new ArrayList<>();

    private IListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        listener = this::chooseItemCard;
        disactiveButtons();
        homeButton.setTextFill(color);

        DatabaseConnector databaseConnector = new DatabaseConnector();
        try { databaseConnector.getItems(itemList); }
        catch (IOException exception) { throw new RuntimeException(exception); }
    }

    @FXML
    ImageView imageView;
    @FXML
    Label itemNameLabel;
    @FXML
    Label itemPriceLabel;
    @FXML
    Label itemShopLabel;
    @FXML
    AnchorPane anchorPane;
    public void chooseItemCard(Item item)
    {
        imageView.setImage(new Image("D:\\JavaIDE\\test\\src\\main\\resources\\images\\items\\" + item.getImageSource()));
        itemNameLabel.setText("☆ " + item.getName().toUpperCase() + " ☆");
        itemPriceLabel.setText(item.getPrice());
        itemShopLabel.setText(item.getShop());
        scrollPane.setDisable(true);
        anchorPane.setVisible(true);
    }

    @FXML
    Button closeItemCardButton;
    public void closeItemCardButtonOnAction()
    {
        scrollPane.setDisable(false);
        anchorPane.setVisible(false);
    }

    @FXML
    AnchorPane homePage;
    @FXML
    Button homeButton;
    public void homeButtonOnAction()
    {
        if (homeButton.getTextFill() == color) return;
        disactiveButtons();

        scrollPane.setVisible(false);
        homePage.setVisible(true);

        homeButton.setTextFill(color);
    }
    @FXML
    Button allItemsButton;
    public void allItemsButtonOnAction() throws IOException {
        if (allItemsButton.getTextFill() == color) return;
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
        allItemsButton.setTextFill(color);
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
            fxmlLoader.setLocation(getClass().getResource("item.fxml"));
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

    GridAnimation animation = new GridAnimation("none", "none");
    @FXML
    Button blousesButton;
    public void blousesButtonOnAction() throws IOException
    {
        if (blousesButton.getTextFill() == color) return;
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
        blousesButton.setTextFill(color);
    }
    @FXML
    Button topsButton;
    public void topsButtonOnAction() throws IOException {
        if (topsButton.getTextFill() == color) return;
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
        topsButton.setTextFill(color);
    }
    @FXML
    Button shirtsButton;
    public void shirtsButtonOnAction() throws IOException {
        if (shirtsButton.getTextFill() == color) return;
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
        shirtsButton.setTextFill(color);
    }
    @FXML
    Button pantsButton;
    public void pantsButtonOnAction() throws IOException {
        if (pantsButton.getTextFill() == color) return;
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
        pantsButton.setTextFill(color);
    }
    @FXML
    Button shortsButton;
    public void shortsButtonOnAction() throws IOException {
        if (shortsButton.getTextFill() == color) return;
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
        shortsButton.setTextFill(color);
    }
    @FXML
    Button skirtsButton;
    public void skirtsButtonOnAction() throws IOException {
        if (skirtsButton.getTextFill() == color) return;
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
        skirtsButton.setTextFill(color);
    }
    @FXML
    Button dressesButton;
    public void dressesButtonOnAction() throws IOException {
        if (dressesButton.getTextFill() == color) return;
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
        dressesButton.setTextFill(color);
    }
    @FXML
    Button jacketsButton;
    public void jacketsButtonOnAction() throws IOException {
        if (jacketsButton.getTextFill() == color) return;
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
        jacketsButton.setTextFill(color);
    }
    @FXML
    Button shoesButton;
    public void shoesButtonOnAction() throws IOException {
        if (shoesButton.getTextFill() == color) return;
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
        shoesButton.setTextFill(color);
    }

    public void disactiveButtons()
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
