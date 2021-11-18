package controller;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import utils.Utils;

import java.util.Optional;
import java.util.function.Predicate;

public class Customers {

    /**
     * Customers table
     */
    @FXML
    public TableView<Customer> customersTable;

    /**
     * Customers table ID column
     */
    @FXML
    public TableColumn<Customer, Integer> idCol;

    /**
     * Customers table name column
     */
    @FXML
    public TableColumn<Customer, String> nameCol;

    /**
     * Customers table address column
     */
    @FXML
    public TableColumn<Customer, String> addressCol;

    /**
     * Customers table post code column
     */
    @FXML
    public TableColumn<Customer, String> postCodeCol;

    /**
     * Customers table phone column
     */
    @FXML
    public TableColumn<Customer, String> phoneCol;

    /**
     * Customers table country column
     */
    @FXML
    public TableColumn<Customer, String> countryCol;

    /**
     * Customers table division column
     */
    @FXML
    public TableColumn<Customer, String> divisionCol;

    /**
     * Customers error label
     */
    @FXML
    public Label customersErrorLabel;

    /**
     * The part search text field
     */
    @FXML
    private TextField customerSearch;

    /**
     * The currently selected customer
     */
    private static Customer customer;


    private final Utils utils = Utils.getInstance();

    /**
     * Initializes the customers view and populates the customers table
     */
    @FXML
    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postCodeCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));


        FilteredList<Customer> filteredCustomers = new FilteredList<>(Customer.getAll(), b -> true);

        customerSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredCustomers.setPredicate(customerPredicate(newValue));

            if (filteredCustomers.size() == 0) {
                customersError("No matching customers found!");
            } else {
                clearErrors();
            }
        });

        customersTable.setItems(filteredCustomers);
    }

    /**
     * Part predicate
     *
     * @param string The search string
     * @return boolean
     */
    private Predicate<Customer> customerPredicate(String string) {

        return part -> {

            if (string == null || string.isEmpty()) {
                return true;
            }

            return searchCustomers(part, string);
        };
    }

    /**
     * Part search method
     *
     * @param customer the customer to compare
     * @param string The search string
     * @return boolean
     */
    private boolean searchCustomers(Customer customer, String string) {

        String searchString = string.toLowerCase();

        return (customer.getName().toLowerCase().contains(searchString)) ||
                (customer.getAddress().toLowerCase().contains(searchString)) ||
                (customer.getPostCode().toLowerCase().contains(searchString)) ||
                (customer.getPhone().toLowerCase().contains(searchString)) ||
                (customer.getDivision().getCountry().getName().toLowerCase().contains(searchString)) ||
                (customer.getDivision().getName().toLowerCase().contains(searchString)) ||
                Integer.valueOf(customer.getId()).toString().equals(searchString);
    }

    public void onAppointmentsButtonClick(ActionEvent event) {
        utils.switchScenes(event, "appointments", "Appointments");
    }

    /**
     * Opens the add/update customer form window
     * @param event
     */
    public void onNewButtonClick(ActionEvent event) {
        utils.openWindow(event, "addUpdateCustomer", "Add Customer");
    }

    /**
     * Opens the add/update customer form window
     * @param event
     */
    public void onEditButtonClick(ActionEvent event) {

        clearErrors();

        customer = customersTable.getSelectionModel().getSelectedItem();

        if (customer == null) {
            customersError("You must select a customer");
            return;
        }

        utils.openWindow(event, "addUpdateCustomer", "Edit Customer");
    }

    /**
     * Deletes the selected customer record
     * <p>
     * <b>Lambda Expression Justification:</b> A lambda is used in this method to handle deleting
     * all of a customers appointments.
     * <pre>{@code
     * customer.getAppointments().forEach( (a) -> { a.delete(); } );
     * }</pre>
     *
     * @param event
     */
    public void onDeleteButtonClick(ActionEvent event) {

        clearErrors();

        customer = customersTable.getSelectionModel().getSelectedItem();

        if (customer == null) {
            customersError("You must select a customer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setContentText("The selected customer and any appointments will be deleted. Are you sure?");
        Optional<ButtonType> confirmation = alert.showAndWait();

        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {

            // Lambda expression to delete all customer appointments
            customer.getAppointments().forEach( (a) -> { a.delete(); } );
            customer.delete();
        }
    }

    /**
     * Returns the currently selected customer
     *
     * @return customer
     */
    public static Customer getSelectedCustomer() {
        return customer;
    }

    private void customersError(String msg) {
        clearErrors();
        customersErrorLabel.setText("Error: " + msg);
    }

    private void clearErrors() {
        customersErrorLabel.setText("");
    }
}
