package controller;

import dao.ContactDaoImpl;
import dao.CustomerDaoImpl;
import dao.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AddUpdateAppointment {

    /**
     * Appointment title label
     */
    @FXML
    public Label titleLabel;

    /**
     * Appointment description label
     */
    @FXML
    public Label descriptionLabel;

    /**
     * Appointment location label
     */
    @FXML
    public Label locationLabel;

    /**
     * Appointment type label
     */
    @FXML
    public Label typeLabel;

    /**
     * Appointment start label
     */
    @FXML
    public Label startLabel;

    /**
     * Appointment end label
     */
    @FXML
    public Label endLabel;

    /**
     * Appointment customer label
     */
    @FXML
    public Label customerLabel;

    /**
     * Appointment contact label
     */
    @FXML
    public Label contactLabel;

    /**
     * Appointment user label
     */
    @FXML
    public Label userLabel;

    /**
     * Appointment ID field
     */
    @FXML
    public TextField idField;

    /**
     * Appointment title field
     */
    @FXML
    public TextField titleField;

    /**
     * Appointment description field
     */
    @FXML
    public TextField descriptionField;

    /**
     * Appointment location field
     */
    @FXML
    public TextField locationField;

    /**
     * Appointment type field
     */
    @FXML
    public TextField typeField;

    /**
     * Appointment start date picker
     */
    @FXML
    public DatePicker startDatePicker;

    /**
     * Appointment start time field
     */
    @FXML
    public TextField startTimeField;

    /**
     * Appointment enda date picker
     */
    @FXML
    public DatePicker endDatePicker;

    /**
     * Appointment end time field
     */
    @FXML
    public TextField endTimeField;

    /**
     * Appointment customer selector
     */
    @FXML
    public ComboBox<String> customerSelect;

    /**
     * Appointment contact selector
     */
    @FXML
    public ComboBox<String> contactSelect;

    /**
     * Appointment user selector
     */
    @FXML
    public ComboBox<String> userSelect;

    /**
     * Appointment errors label
     */
    @FXML
    public Label addAppointmentErrorLabel;

    /**
     * Store the Utils singleton
     */
    private final Utils utils = Utils.getInstance();

    /**
     * Initializes the addAppointment view
     */
    @FXML
    public void initialize() {

        // Get the selected appointment record or null if this is a new appointment
        Appointment appointment = Appointments.getSelectedAppointment();

        startDatePicker.setConverter(getConverter());
        endDatePicker.setConverter(getConverter());

        // populate the contacts, users, and customers combo boxes
        ObservableList<String> contactSelectList = FXCollections.observableArrayList();
        ObservableList<String> userSelectList = FXCollections.observableArrayList();
        ObservableList<String> customerSelectList = FXCollections.observableArrayList();

        try {

            ObservableList<Contact> contacts = new ContactDaoImpl().getAll();
            ObservableList<User> users = new UserDaoImpl().getAll();
            ObservableList<Customer> customers = new CustomerDaoImpl().getAll();

            if (contacts != null) {
                for (Contact contact: contacts) {
                    contactSelectList.add(contact.getId() + " - " + contact.getName());
                }
            }

            if (users != null) {
                for (User user: users) {
                    userSelectList.add(user.getId() + " - " + user.getUsername());
                }
            }

            if (customers != null) {
                for (Customer customer: customers) {
                    customerSelectList.add(customer.getId() + " - " + customer.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        contactSelect.setItems(contactSelectList);
        userSelect.setItems(userSelectList);
        customerSelect.setItems(customerSelectList);

        if (appointment != null) {
            idField.setText(String.valueOf(appointment.getId()));
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());
            locationField.setText(appointment.getLocation());
            typeField.setText(appointment.getType());
            startDatePicker.setValue(appointment.getStart().toLocalDate());
            startTimeField.setText(appointment.getStart().format(DateTimeFormatter.ofPattern("HH:mm")));
            endDatePicker.setValue(appointment.getEnd().toLocalDate());
            endTimeField.setText(appointment.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
            customerSelect.setValue(appointment.getCustomerId() + " - " + appointment.getCustomer().getName());
            contactSelect.setValue(appointment.getContactId() + " - " + appointment.getContact().getName());
            userSelect.setValue(appointment.getUserId() + " - " + appointment.getUser().getUsername());
        }
    }

    /**
     * Saves the appointment
     *
     * @param event Save button clicked event
     */
    @FXML
    public void onSaveButtonClick(ActionEvent event) {

        if (!doValidate()) { return; }

        Boolean status = null;

        if (idField.getText().isEmpty()) {
            status = createAppointment();
        } else {
            status = updateAppointment();
        }

        if (Boolean.TRUE.equals(status)) {

            // Update the customers observable list
            Appointment.getAll();

            // Close the add/update window
            utils.closeWindow(event);
        }

    }

    /**
     * Creates a new appointment
     *
     * @return true on success or false on failure
     */
    private Boolean createAppointment() {

        // Combines the start and end date strings with the time strings
        String startString = startDatePicker.getValue() + " " + startTimeField.getText();
        String endString = endDatePicker.getValue() + " " + endTimeField.getText();

        int contactId = utils.getIdFromComboString((String) contactSelect.getSelectionModel().getSelectedItem());
        int userId = utils.getIdFromComboString((String) userSelect.getSelectionModel().getSelectedItem());
        int customerId = utils.getIdFromComboString((String) customerSelect.getSelectionModel().getSelectedItem());

        try {

            new Appointment(
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeField.getText(),
                    LocalDateTime.parse(startString, utils.getDateTimeFormatter()),
                    LocalDateTime.parse(endString, utils.getDateTimeFormatter()),
                    contactId,
                    userId,
                    customerId
            ).save();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            utils.doError("Add Appointment", "There was an unexpected error adding the appointment");
        }

        return false;
    }

    /**
     * Update an existing appointment
     *
     * @return true on success or false on failure
     */
    private Boolean updateAppointment() {

        // Combines the start and end date strings with the time strings
        String startString = startDatePicker.getValue() + " " + startTimeField.getText();
        String endString = endDatePicker.getValue() + " " + endTimeField.getText();

        int contactId = utils.getIdFromComboString((String) contactSelect.getSelectionModel().getSelectedItem());
        int userId = utils.getIdFromComboString((String) userSelect.getSelectionModel().getSelectedItem());
        int customerId = utils.getIdFromComboString((String) customerSelect.getSelectionModel().getSelectedItem());

        try {

            Appointment a = Appointment.get(Integer.parseInt(idField.getText()));

            a.setTitle(titleField.getText());
            a.setDescription(descriptionField.getText());
            a.setLocation(locationField.getText());
            a.setType(typeField.getText());
            a.setStart(LocalDateTime.parse(startString, utils.getDateTimeFormatter()));
            a.setEnd(LocalDateTime.parse(endString, utils.getDateTimeFormatter()));
            a.setContactId(contactId);
            a.setUserId(userId);
            a.setCustomerId(customerId);
            a.save();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            utils.doError("Update Appointment", "There was an unexpected error updating the appointment");
        }

        return false;
    }



    public void onCancelButtonClick(ActionEvent event) {
        utils.closeWindow(event);
    }

    /**
     * Checks that all fields have been provided
     *
     * @return Boolean
     */
    private Boolean doValidate() {

        clearErrors();

        String message = "Appointment %s is required!";

        if (titleField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "title"));
            titleLabel.setTextFill(Color.RED);
            return false;
        } else if (descriptionField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "description"));
            descriptionLabel.setTextFill(Color.RED);
            return false;
        } else if (locationField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "location"));
            locationLabel.setTextFill(Color.RED);
            return false;
        } else if (typeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "type"));
            typeLabel.setTextFill(Color.RED);
            return false;
        } else if (startDatePicker.getValue() == null) {
            addAppointmentErrorLabel.setText(String.format(message, "start date"));
            startLabel.setTextFill(Color.RED);
            return false;
        } else if (startTimeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "start time"));
            startLabel.setTextFill(Color.RED);
            return false;
        } else if (endDatePicker.getValue() == null) {
            addAppointmentErrorLabel.setText(String.format(message, "end date"));
            endLabel.setTextFill(Color.RED);
            return false;
        } else if (endTimeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "end time"));
            endLabel.setTextFill(Color.RED);
            return false;
        } else if (customerSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "customer"));
            customerLabel.setTextFill(Color.RED);
            return false;
        } else if (contactSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "contact"));
            contactLabel.setTextFill(Color.RED);
            return false;
        } else if (userSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "user"));
            userLabel.setTextFill(Color.RED);
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
        addAppointmentErrorLabel.setText("");

        titleLabel.setTextFill(Color.BLACK);
        descriptionLabel.setTextFill(Color.BLACK);
        locationLabel.setTextFill(Color.BLACK);
        typeLabel.setTextFill(Color.BLACK);
        startLabel.setTextFill(Color.BLACK);
        endLabel.setTextFill(Color.BLACK);
        customerLabel.setTextFill(Color.BLACK);
        contactLabel.setTextFill(Color.BLACK);
        userLabel.setTextFill(Color.BLACK);
    }

    /**
     * This method changes the date format in a JavaFX DatePicker
     *
     * @return formatted date string
     */
    private StringConverter<LocalDate> getConverter() {

        return new javafx.util.StringConverter<LocalDate>() {

            private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {

                if (localDate == null) {
                    return "";
                }

                return dtf.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {

                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }

                return LocalDate.parse(dateString, dtf);
            }
        };
    }
}
