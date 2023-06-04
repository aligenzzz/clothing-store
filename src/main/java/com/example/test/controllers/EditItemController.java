package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.DatabaseConnector;
import com.example.test.GlobalEntities;
import com.example.test.entities.Item;
import com.example.test.entities.Vendor;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditItemController implements Initializable
{
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button editButton;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;
    @FXML private TextField imageTextField;
    @FXML private TextField priceTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        choiceBox.setValue("Select name...");
        choiceBox.getItems().add("blouse");
        choiceBox.getItems().add("sweatshirt");
        choiceBox.getItems().add("top");
        choiceBox.getItems().add("t-shirt");
        choiceBox.getItems().add("shirt");
        choiceBox.getItems().add("pants");
        choiceBox.getItems().add("jeans");
        choiceBox.getItems().add("shorts");
        choiceBox.getItems().add("skirt");
        choiceBox.getItems().add("dress");
        choiceBox.getItems().add("jacket");
        choiceBox.getItems().add("shoes");

        choiceBox.setOnMouseClicked(event -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        });
        imageTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        priceTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));

        imageTextField.textProperty().addListener(event ->
        {
            imageTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !imageTextField.getText().isEmpty() && !imageTextField.getText().matches(Constants.URL_PATTERN)
            );
        });
        priceTextField.textProperty().addListener(event ->
        {
            priceTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !priceTextField.getText().isEmpty() && !priceTextField.getText().matches(Constants.PRICE_PATTERN)
            );
        });
    }

    private Item item = null;

    public void setData(@NotNull Item item)
    {
        this.item = item;

        choiceBox.setValue(item.getName());
        imageTextField.setPromptText(item.getImageSource());
        priceTextField.setPromptText(Constants.PRICE_FORMAT.format(item.getPrice()));
    }

    public void editButtonOnAction()
    {
        if (this.item == null)
            this.addNewItem();
        else
            this.onlyEditItem();
    }

    private void onlyEditItem()
    {
        Item editedItem = new Item(item.getId(), item.getName(), item.getImageSource(),
                item.getPrice(), item.getShop());

        editedItem.setName(choiceBox.getValue());
        String image = imageTextField.getText();
        String price = priceTextField.getText();

        boolean allIsBlank = true;

        if (!Objects.equals(editedItem.getName(), item.getName()))
            allIsBlank = false;
        if (image != null && !image.isBlank() && !image.isEmpty())
        {
            editedItem.setImageSource(image);
            allIsBlank = false;
        }
        if (price != null && !price.isBlank() && !price.isEmpty())
        {
            try { editedItem.setPrice(Double.parseDouble(price.replace(',', '.'))); }
            catch(Exception ignored) {}

            allIsBlank = false;
        }

        boolean isError = imageTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                priceTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error"));
        if (isError || allIsBlank)
            errorLabel.setVisible(true);
        else
        {
            Task<Void> task = new Task<>()
            {
                @Override
                protected @Nullable Void call() throws IOException
                {
                    DatabaseConnector.getInstance().editItem(editedItem);
                    return null;
                }
            };
            task.setOnSucceeded(event ->
            {
                Vendor vendor = (Vendor) GlobalEntities.USER;
                vendor.editItem(editedItem);

                successLabel.setVisible(true);

                imageTextField.setPromptText(editedItem.getImageSource());
                priceTextField.setPromptText(String.valueOf(editedItem.getPrice()));
            });
            editButton.disableProperty().bind(task.runningProperty());

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            imageTextField.clear();
            priceTextField.clear();
        }
    }

    private void addNewItem()
    {
        String image = imageTextField.getText();
        String price = priceTextField.getText();

        if (image == null || image.isEmpty() || image.isBlank() ||
                price == null || price.isEmpty() || price.isBlank() ||
                choiceBox.getValue() == null || Objects.equals(choiceBox.getValue(), "Select name..."))
        {
            errorLabel.setVisible(true);
            return;
        }

        boolean isError = imageTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                priceTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error"));
        if (isError)
            errorLabel.setVisible(true);
        else
        {
            Item newItem = new Item(0, choiceBox.getValue(), image, Double.parseDouble(price.replace(',', '.')),
                    GlobalEntities.SHOP.getId());

            Task<Double> task = new Task<>()
            {
                @Override
                protected @NotNull Double call() throws IOException
                {
                    return DatabaseConnector.getInstance().addItem(newItem);
                }
            };
            task.setOnSucceeded(event ->
            {
                Vendor vendor = (Vendor) GlobalEntities.USER;
                newItem.setId(task.getValue());
                vendor.addItem(newItem);

                successLabel.setVisible(true);
            });
            editButton.disableProperty().bind(task.runningProperty());

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            choiceBox.setValue("Select name...");
            imageTextField.clear();
            priceTextField.clear();
        }
    }

    public void cancelButtonOnAction()
    {
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
