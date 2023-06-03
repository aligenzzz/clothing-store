package com.example.test.controllers;

import com.example.test.Constants;
import com.example.test.GlobalEntities;
import com.example.test.entities.Customer;
import com.example.test.enums.OrderState;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable
{
    @FXML private TextField nameOnCardTextField;
    @FXML private TextField creditCardNumberTextField;
    @FXML private PasswordField cvvPasswordField;
    @FXML private TextField expDateTextField;
    @FXML private TextField countryTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField zipCodeTextField;
    @FXML private Label successLabel;
    @FXML private Label errorLabel;
    @FXML private Button proceedButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (GlobalEntities.ORDER.getState() == OrderState.booked)
        {
            nameOnCardTextField.setDisable(true);
            creditCardNumberTextField.setDisable(true);
            expDateTextField.setDisable(true);
            cvvPasswordField.setDisable(true);
        }

        nameOnCardTextField.textProperty().addListener(event ->
        {
            nameOnCardTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !nameOnCardTextField.getText().isEmpty() &&
                            !nameOnCardTextField.getText().matches(Constants.CARDNAME_PATTERN));
        });
        creditCardNumberTextField.textProperty().addListener(event ->
        {
            creditCardNumberTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !creditCardNumberTextField.getText().isEmpty() &&
                            !creditCardNumberTextField.getText().matches(Constants.CARDNUMBER_PATTERN));
        });
        expDateTextField.textProperty().addListener(event ->
        {
            expDateTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !expDateTextField.getText().isEmpty() &&
                            !expDateTextField.getText().matches(Constants.EXPDATE_PATTERN));
        });
        cvvPasswordField.textProperty().addListener(event ->
        {
            cvvPasswordField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !cvvPasswordField.getText().isEmpty() &&
                            !cvvPasswordField.getText().matches(Constants.CVV_PATTERN));
        });
        countryTextField.textProperty().addListener(event ->
        {
            countryTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !countryTextField.getText().isEmpty() &&
                            !countryTextField.getText().matches(Constants.COUNTRY_PATTERN));
        });
        cityTextField.textProperty().addListener(event ->
        {
            cityTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !cityTextField.getText().isEmpty() &&
                            !cityTextField.getText().matches(Constants.CITY_PATTERN));
        });
        zipCodeTextField.textProperty().addListener(event ->
        {
            zipCodeTextField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"),
                    !zipCodeTextField.getText().isEmpty() &&
                            !zipCodeTextField.getText().matches(Constants.ZIPCODE_PATTERN));
        });

        nameOnCardTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        creditCardNumberTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        expDateTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        cvvPasswordField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        countryTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        cityTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        streetTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
        zipCodeTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            successLabel.setVisible(false);
            errorLabel.setVisible(false);
        }));
    }

    public void proceedButtonOnAction() throws IOException
    {
        if (GlobalEntities.ORDER == null) return;

        boolean error = false;

        String name = nameOnCardTextField.getText();
        String number = creditCardNumberTextField.getText();
        String expDate = expDateTextField.getText();
        String cvv = cvvPasswordField.getText();
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String street = streetTextField.getText();
        String zipCode = zipCodeTextField.getText();

        if (GlobalEntities.ORDER.getState() == OrderState.paid)
        {
            if (name == null || name.isEmpty() || name.isBlank())
                error = true;
            if (number == null || number.isEmpty() || number.isBlank())
                error = true;
            if (expDate == null || expDate.isEmpty() || expDate.isBlank())
                error = true;
            if (cvv == null || cvv.isEmpty() || cvv.isBlank())
                error = true;
            if (nameOnCardTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                creditCardNumberTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                expDateTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
                cvvPasswordField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")))
                error = true;
        }

        if (country == null || country.isEmpty() || country.isBlank())
            error = true;
        if (city == null || city.isEmpty() || city.isBlank())
            error = true;
        if (street == null || street.isEmpty() || street.isBlank())
            error = true;
        if (zipCode == null || zipCode.isEmpty() || zipCode.isBlank())
            error = true;
        if (countryTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
            cityTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
            streetTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")) ||
            zipCodeTextField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error")))
            error = true;

        if (error)
        {
            errorLabel.setVisible(true);
            return;
        }

        Customer customer = (Customer) GlobalEntities.USER;

        Task<Void> task = new Task<>()
        {
            @Override
            protected @Nullable Void call() throws IOException
            {
                if (GlobalEntities.ORDER.getId() == 0)
                {
                    customer.deleteAllShoppingItems();
                    customer.addOrder(GlobalEntities.ORDER);
                }
                else
                    GlobalEntities.ORDER.changeState(OrderState.paid);

                return null;
            }
        };
        task.setOnSucceeded(event ->
        {
            successLabel.setVisible(true);
            GlobalEntities.ORDER = null;
        });
        proceedButton.disableProperty().bind(task.runningProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    public void cancelButtonOnAction()
    {
        GlobalEntities.ORDER = null;

        Stage stage = (Stage) expDateTextField.getScene().getWindow();
        stage.close();
    }
}
