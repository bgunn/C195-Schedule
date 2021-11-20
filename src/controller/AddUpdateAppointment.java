package controller;

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
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
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

            ObservableList<Contact> contacts = Contact.getAll();
            ObservableList<User> users = User.getAll();
            ObservableList<Customer> customers = Customer.getAll();

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

        Boolean status;

        if (idField.getText().isEmpty()) {
            status = createAppointment();
        } else {
            status = updateAppointment();
        }

        if (Boolean.TRUE.equals(status)) {
            utils.closeWindow(event);
        }
    }

    /**
     * Creates a new appointment
     *
     * @return true on success or false on failure
     */
    private Boolean createAppointment() {

        int contactId = getIdFromComboBox(contactSelect);
        int userId = getIdFromComboBox(userSelect);
        int customerId = getIdFromComboBox(customerSelect);

        try {

            new Appointment(
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeField.getText(),
                    getStart(),
                    getEnd(),
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

        int contactId = getIdFromComboBox(contactSelect);
        int userId = getIdFromComboBox(userSelect);
        int customerId = getIdFromComboBox(customerSelect);

        try {

            Appointment a = Appointment.get(Integer.parseInt(idField.getText()));

            a.setTitle(titleField.getText());
            a.setDescription(descriptionField.getText());
            a.setLocation(locationField.getText());
            a.setType(typeField.getText());
            a.setStart(getStart());
            a.setEnd(getEnd());
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

        return !hasMissingData() && !hasInvalidDateTime() && !hasOverlappingAppointment();
    }

    /**
     * Checks that the entered date and time are valid and meet business rules.
     *
     * @return Boolean
     */
    private Boolean hasInvalidDateTime() {

        boolean hasInvalidDateTime = true;

        String errorTitle = "Appointment Date/Time";

        int startHour = utils.localDateTimeToEST(getStart()).getHour();
        int endHour = utils.localDateTimeToEST(getEnd()).getHour();

        if (startHour < 8 || startHour > 22 || endHour < 8 || endHour > 22) {

            utils.doError(errorTitle, "The appointment time is outside of business hours (08:00 - 22:00)");

        } else if (getStart().isBefore(ChronoLocalDateTime.from(ZonedDateTime.now()))) {

            utils.doError(errorTitle, "The appointment start date/time is in the past!");

        } else if (getEnd().isBefore(ChronoLocalDateTime.from(ZonedDateTime.now()))) {

            utils.doError(errorTitle, "The appointment end date/time is in the past!");

        } else if (getEnd().isBefore(ChronoLocalDateTime.from(getStart()))) {

            utils.doError(errorTitle, "The appointment start date/time is after the end date/time!");

        } else {
            hasInvalidDateTime = false;
        }

        return hasInvalidDateTime;
    }

    /**
     * Checks to see if any existing appointment times for the 
     * selected customer overlap.
     *
     * @return Boolean
     */
    private Boolean hasOverlappingAppointment() {

        ObservableList<Appointment> appointments = Customer.get(getIdFromComboBox(customerSelect)).getAppointments();

        int id = idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText());

        for (Appointment a : appointments) {

            // Don't compare an appointment being edited to itself
            if (id == a.getId()) {
                continue;
            }

            if (a.getStart().isBefore(getEnd()) && getStart().isBefore(a.getEnd())) {
                utils.doError("Appointment date/time", "The appointment overlaps with appointment " + a.getId());
                return true;
            }
        };

        return false;
    }

    /**
     * Checks that all fields have been entered
     *
     * @return Boolean
     */
    private Boolean hasMissingData() {

        boolean hasMissingData = true;

        String message = "Appointment %s is required!";

        if (titleField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "title"));
            titleLabel.setTextFill(Color.RED);
        } else if (descriptionField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "description"));
            descriptionLabel.setTextFill(Color.RED);
        } else if (locationField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "location"));
            locationLabel.setTextFill(Color.RED);
        } else if (typeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "type"));
            typeLabel.setTextFill(Color.RED);
        } else if (startDatePicker.getValue() == null) {
            addAppointmentErrorLabel.setText(String.format(message, "start date"));
            startLabel.setTextFill(Color.RED);
        } else if (startTimeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "start time"));
        } else if (endDatePicker.getValue() == null) {
            addAppointmentErrorLabel.setText(String.format(message, "end date"));
            endLabel.setTextFill(Color.RED);
        } else if (endTimeField.getText().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "end time"));
            endLabel.setTextFill(Color.RED);
        } else if (customerSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "customer"));
            customerLabel.setTextFill(Color.RED);
        } else if (contactSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "contact"));
            contactLabel.setTextFill(Color.RED);
        } else if (userSelect.getSelectionModel().isEmpty()) {
            addAppointmentErrorLabel.setText(String.format(message, "user"));
            userLabel.setTextFill(Color.RED);
        } else {
            hasMissingData = false;
        }

        return hasMissingData;
    }

    private int getIdFromComboBox(ComboBox comboBox) {
        return utils.getIdFromComboString((String) comboBox.getSelectionModel().getSelectedItem());
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

    /**
     * Convenience method to get the start date/time
     *
     * @return LocalDateTime containing the Appointment start
     */
    private LocalDateTime getStart() {
        String startString = startDatePicker.getValue() + " " + startTimeField.getText();
        return LocalDateTime.parse(startString, utils.getDateTimeFormatter());
    }

    /**
     * Convenience method to get the end date/time
     *
     * @return LocalDateTime containing the Appointment end
     */
    private LocalDateTime getEnd() {
        String endString = endDatePicker.getValue() + " " + endTimeField.getText();
        return LocalDateTime.parse(endString, utils.getDateTimeFormatter());
    }
}
