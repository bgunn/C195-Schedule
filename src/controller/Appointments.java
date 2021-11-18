package controller;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Appointment;
import model.Customer;
import utils.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.function.Predicate;

public class Appointments {

    /**
     * Label to store messages about upcoming appointments
     */
    public Label upcomingApptMessage;

    /**
     * Label to store the local date/time
     */
    public Label currentDateTimeLabel;

    /**
     * Label to store the office date/time
     */
    public Label officeDateTimeLabel;

    /**
     * Label to store the UTC date/time
     */
    public Label utcDateTimeLabel;

    /**
     * The appointments table
     */
    public TableView<Appointment> appointmentsTable;

    /**
     * The appointments table ID column
     */
    public TableColumn<Appointment, Integer> idCol;

    /**
     * The appointments table title column
     */
    public TableColumn<Appointment, String> titleCol;

    /**
     * The appointments table description column
     */
    public TableColumn<Appointment, String> descriptionCol;

    /**
     * The appointments table location column
     */
    public TableColumn<Appointment, String> locationCol;

    /**
     * The appointments table contact name column
     */
    public TableColumn<Appointment, String> contactCol;

    /**
     * The appointments table type column
     */
    public TableColumn<Appointment, String> typeCol;

    /**
     * The appointments table start column
     */
    public TableColumn<Appointment, LocalDateTime> startCol;

    /**
     * The appointments table end column
     */
    public TableColumn<Appointment, LocalDateTime> endCol;

    /**
     * The appointments table customer ID column
     */
    public TableColumn<Appointment, Integer> customerIdCol;

    /**
     * The appointments table user ID column
     */
    public TableColumn<Appointment, Integer> userIdCol;

    /**
     * The appointments search text field
     */
    public TextField appointmentSearch;

    /**
     * A label to hold error messages
     */
    public Label appointmentsErrorLabel;

    /**
     * The currently selected appointment
     */
    private static Appointment appointment;

    /**
     * Store the Utils singleton
     */
    private final Utils utils = Utils.getInstance();

    /**
     * Initializes the customers view and populates the customers table
     */
    @FXML
    public void initialize() {

        currentDateTimeLabel.setText(utils.getLocalDateTimeString());
        officeDateTimeLabel.setText(utils.getOfficeDateTimeString());
        utcDateTimeLabel.setText(utils.getUTCDateTimeString());

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        FilteredList<Appointment> filteredAppointments = new FilteredList<>(Appointment.getAll(), b -> true);

        appointmentSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredAppointments.setPredicate(appointmentPredicate(newValue));

            if (filteredAppointments.size() == 0) {
                appointmentsError("No matching appointments found!");
            } else {
                clearErrors();
            }
        });

        appointmentsTable.setItems(filteredAppointments);

        // Update the format of the start and end date time columns
        startCol.setCellFactory(getDateCell(utils.getDateTimeFormatter()));
        endCol.setCellFactory(getDateCell(utils.getDateTimeFormatter()));
    }

    /**
     * <pre>
     * Generates a cellFactory for LocalDateTime columns.
     *
     * This method keeps the LocalDateTime type on the column and just updates the display
     * </pre>
     *
     * <b>Lambda Expression Justification:</b> A lambda is used in this method to generate a TableCell
     * with the specified date/time format.
     *
     * @param format The DateTimeFormatter to determine how to display the date/time
     */
    public static <ROW,T extends Temporal> Callback<TableColumn<ROW, T>, TableCell<ROW, T>> getDateCell (DateTimeFormatter format) {

        return column -> new TableCell<>() {

            @Override
            protected void updateItem(T item, boolean empty) {

                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        };
    }

    /**
     * Part predicate
     *
     * @param string The search string
     * @return boolean
     */
    private Predicate<Appointment> appointmentPredicate(String string) {

        return part -> {

            if (string == null || string.isEmpty()) {
                return true;
            }

            return searchAppointments(part, string);
        };
    }

    /**
     * Part search method
     *
     * @param appointment the appointment to compare
     * @param string The search string
     * @return boolean
     */
    private boolean searchAppointments(Appointment appointment, String string) {

        String searchString = string.toLowerCase();

        return (appointment.getTitle().toLowerCase().contains(searchString)) ||
                (appointment.getDescription().toLowerCase().contains(searchString)) ||
                (appointment.getType().toLowerCase().contains(searchString)) ||
                Integer.valueOf(appointment.getId()).toString().equals(searchString);
    }

    /**
     * Switches the scene to the customers view
     *
     * @param event The button click event
     */
    public void onCustomersButtonClick(ActionEvent event) {
        utils.switchScenes(event, "customers", "Customers");
    }

    /**
     * Launches the new appointment window
     *
     * @param event The button click event
     */
    public void onNewButtonClick(ActionEvent event) {
        utils.openWindow(event, "addUpdateAppointment", "Add Appointment");
    }

    /**
     * Launches the edit appointment window
     *
     * @param event The button click event
     */
    public void onEditButtonClick(ActionEvent event) {

        clearErrors();

        appointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            appointmentsError("You must select an appointment");
            return;
        }

        utils.openWindow(event, "addUpdateAppointment", "Edit Appointment");
    }

    /**
     * Deletes the selected appointment
     *
     * @param event The button click event
     */
    public void onDeleteButtonClick(ActionEvent event) {
    }

    /**
     * Set the provided error message on the appointments error label
     *
     * @param msg The error message to display
     */
    private void appointmentsError(String msg) {
        clearErrors();
        appointmentsErrorLabel.setText("Error: " + msg);
    }

    /**
     * Convenience method to remove test from the error label
     */
    private void clearErrors() {
        appointmentsErrorLabel.setText("");
    }

    /**
     * Returns the currently selected appointment
     *
     * @return appointment
     */
    public static Appointment getSelectedAppointment() {
        return appointment;
    }
}
