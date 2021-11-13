package controller;

import dao.CountryDaoImpl;
import dao.DivisionDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.Country;
import model.Customer;
import model.Division;
import utils.Utils;

public class AddCustomer {

    /**
     * Customer name label
     */
    @FXML
    public Label nameLabel;

    /**
     * Customer name field
     */
    @FXML
    private TextField nameField;

    /**
     * Customer phone label
     */
    @FXML
    public Label phoneLabel;

    /**
     * Customer phone field
     */
    @FXML
    private TextField phoneField;

    /**
     * Customer address label
     */
    @FXML
    public Label addressLabel;

    /**
     * Customer address field
     */
    @FXML
    private TextField addressField;

    /**
     * Customer post code label
     */
    @FXML
    public Label postCodeLabel;

    /**
     * Customer post code field
     */
    @FXML
    private TextField postCodeField;

    /**
     * Customer country label
     */
    @FXML
    public Label countryLabel;

    /**
     * Customer country select
     */
    @FXML
    private ComboBox countrySelect;

    /**
     * Customer division label
     */
    @FXML
    public Label divisionLabel;

    /**
     * Customer division select
     */
    @FXML
    private ComboBox divisionSelect;

    /**
     * label for displaying error messages
     */
    @FXML
    public Label addCustomerErrorLabel;

    private Utils utils = Utils.getInstance();

    /**
     * Initializes the addCustomer view and populates the dropdown selectors
     * with country and division data.
     */
    @FXML
    public void initialize() {

        // Division dropdown is disabled until a country is selected
        divisionSelect.setDisable(true);

        ObservableList<String> countrySelectList = FXCollections.observableArrayList();

        try {
            ObservableList<Country> countries = new CountryDaoImpl().getAll();

            if (countries != null) {
                for (Country country: countries) {
                    countrySelectList.add(country.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        countrySelect.setItems(countrySelectList);
    }

    /**
     * Saves the part in the inventory parts collection
     *
     * @param event Save button clicked event
     */
    @FXML
    public void onSaveButtonClick(ActionEvent event) {

        doValidate();

        try {
            new Customer(
                    nameField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    postCodeField.getText(),
                    (String) divisionSelect.getSelectionModel().getSelectedItem()
            ).save();

            utils.closeWindow(event);

        } catch (Exception e) {
            e.printStackTrace();
            utils.doError("Add Customer", "There was an unexpected error adding the customer");
        }
    }

    /**
     * Checks that all fields have been provided
     *
     * @return Boolean
     */
    private Boolean doValidate() {

        clearErrors();

        String message = "Customer %s is required!";

        if (nameField.getText().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "name"));
            nameLabel.setTextFill(Color.RED);
            return false;
        } else if (phoneField.getText().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "phone"));
            phoneLabel.setTextFill(Color.RED);
            return false;
        } else if (addressField.getText().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "address"));
            addressLabel.setTextFill(Color.RED);
            return false;
        } else if (postCodeField.getText().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "post code"));
            postCodeLabel.setTextFill(Color.RED);
            return false;
        } else if (countrySelect.getSelectionModel().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "country"));
            countryLabel.setTextFill(Color.RED);
            return false;
        } else if (divisionSelect.getSelectionModel().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "division"));
            divisionLabel.setTextFill(Color.RED);
            return false;
        }

        return true;
    }

    /**
     * Clear all errors
     *
     * @return void
     */
    private void clearErrors() {
        addCustomerErrorLabel.setText("");
        nameLabel.setTextFill(Color.BLACK);
        phoneLabel.setTextFill(Color.BLACK);
        addressLabel.setTextFill(Color.BLACK);
        postCodeLabel.setTextFill(Color.BLACK);
        countryLabel.setTextFill(Color.BLACK);
        divisionLabel.setTextFill(Color.BLACK);
    }

    /**
     * Initializes and enables the division selector after a country has been selected
     *
     * @param event combobox select event
     */
    @FXML
    public void onCountrySelect(ActionEvent event) {

        ObservableList<String> divisionSelectList = FXCollections.observableArrayList();

        try {

            ObservableList<Division> divisions = new DivisionDaoImpl().getByCountry((String) countrySelect.getSelectionModel().getSelectedItem());

            if (divisions != null) {
                for (Division division: divisions) {
                    divisionSelectList.add(division.getName());
                }
            }

            divisionSelect.setItems(divisionSelectList);

            divisionSelect.setDisable(false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Closes the add customer form window
     *
     * @param event Cancel button clicked event
     */
    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        utils.closeWindow(event);
    }
}
