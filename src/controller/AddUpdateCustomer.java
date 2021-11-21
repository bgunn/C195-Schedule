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


public class AddUpdateCustomer {

    /**
     * Customer ID field
     */
    @FXML
    private TextField idField;

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
    private ComboBox<String> countrySelect;

    /**
     * Customer division label
     */
    @FXML
    public Label divisionLabel;

    /**
     * Customer division select
     */
    @FXML
    private ComboBox<String> divisionSelect;

    /**
     * label for displaying error messages
     */
    @FXML
    public Label addCustomerErrorLabel;

    private final Utils utils = Utils.getInstance();

    /**
     * Initializes the addCustomer view and populates the dropdown selectors
     * with country and division data.
     */
    @FXML
    public void initialize() {

        // Get the selected customer record or null if this is a new customer
        Customer customer = Customers.getSelectedCustomer();

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

        if (customer == null) {
            // Disable the division selector until a country is selected
            divisionSelect.setDisable(true);
        } else {

            String countryValue = customer.getDivision().getCountry().getId() + " - " +
                                  customer.getDivision().getCountry().getName();

            idField.setText(String.valueOf(customer.getId()));
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhone());
            addressField.setText(customer.getAddress());
            postCodeField.setText(customer.getPostCode());
            countrySelect.setValue(countryValue);

            initDivisionSelector();
            divisionSelect.setValue(customer.getDivisionId() + " - " + customer.getDivision().getName());
        }
    }

    /**
     * Saves the customer
     *
     * @param event Save button clicked event
     */
    @FXML
    public void onSaveButtonClick(ActionEvent event) {

        if (!doValidate()) { return; }

        Boolean status = null;

        if (idField.getText().isEmpty()) {
            status = createCustomer();
        } else {
            status = updateCustomer();
        }

        if (Boolean.TRUE.equals(status)) {
            utils.closeWindow(event);
        }
    }

    /**
     * Creates a new customer
     */
    private Boolean createCustomer() {

        try {

            new Customer(
                    nameField.getText(),
                    addressField.getText(),
                    postCodeField.getText(),
                    phoneField.getText(),
                    utils.getIdFromComboString((String) divisionSelect.getSelectionModel().getSelectedItem())
            ).save();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            utils.doError("Add Customer", "There was an unexpected error adding the customer");
        }

        return false;
    }

    /**
     * Update an existing customer
     *
     * @return returns true success or false on failure
     */
    private Boolean updateCustomer() {

        try {

            Customer c = Customer.get(Integer.parseInt(idField.getText()));

            c.setName(nameField.getText());
            c.setAddress(addressField.getText());
            c.setPostCode(postCodeField.getText());
            c.setPhone(phoneField.getText());
            c.setDivisionId(utils.getIdFromComboString((String) divisionSelect.getSelectionModel().getSelectedItem()));
            c.save();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            utils.doError("Update Customer", "There was an unexpected error updating the customer");
        }

        return false;
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
        } else if (countrySelect.getSelectionModel().getSelectedItem().isEmpty()) {
            addCustomerErrorLabel.setText(String.format(message, "country"));
            countryLabel.setTextFill(Color.RED);
            return false;
        } else if (divisionSelect.getSelectionModel().getSelectedItem().isEmpty()) {
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
        initDivisionSelector();
    }

    private void initDivisionSelector() {

        divisionSelect.setValue("");

        ObservableList<String> divisionSelectList = FXCollections.observableArrayList();

        try {

            ObservableList<Division> divisions = new DivisionDaoImpl().getByCountry((String) countrySelect.getSelectionModel().getSelectedItem());

            if (divisions != null) {
                for (Division division: divisions) {
                    divisionSelectList.add(division.getId() + " - " + division.getName());
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
