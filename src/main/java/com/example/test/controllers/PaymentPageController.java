package com.example.test.controllers;

import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.enums.OrderState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable
{
    @FXML private TextField creditCardNumberTextField;
    @FXML private PasswordField cvvPasswordField;
    @FXML private TextField expDateTextField;
    @FXML private TextField nameOnCardTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (GlobalEntities.ORDER.getState() == OrderState.booked)
        {
            creditCardNumberTextField.setDisable(true);
            nameOnCardTextField.setDisable(true);
            expDateTextField.setDisable(true);
            cvvPasswordField.setDisable(true);
        }
    }

    public void proceedButtonOnAction() throws IOException
    {
        Customer customer = (Customer) GlobalEntities.USER;
        if (!customer.isFoundOrder(GlobalEntities.ORDER.getId()))
        {
            customer.addOrder(GlobalEntities.ORDER);
            customer.deleteAllShoppingItems();
        }
        else
        {

        }

        this.cancelButtonOnAction();
    }
    public void cancelButtonOnAction()
    {

        Stage stage = (Stage) expDateTextField.getScene().getWindow();
        stage.close();
    }
}
