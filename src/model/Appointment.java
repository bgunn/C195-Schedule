package model;

import dao.*;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <pre>
 * The appointment model
 *
 * Provides a constructor for instantiating appointment objects
 * and provides getters and setters for working with appointment
 * properties.
 * </pre>
 */
public class Appointment {

    /**
     * The appointment ID
     */
    private int id = 0;

    /**
     * The appointment title
     */
    private String title;

    /**
     * The appointment description
     */
    private String description;

    /**
     * The appointment location
     */
    private String location;

    /**
     * The appointment type
     */
    private String type;

    /**
     * The appointment start date/time
     */
    private LocalDateTime start;

    /**
     * The appointment end date/time
     */
    private LocalDateTime end;

    /**
     * The created date
     */
    private LocalDateTime createDate;

    /**
     * The created by user
     */
    private String createdBy;

    /**
     * The last update date
     */
    private LocalDateTime lastUpdate;

    /**
     * The last updated by user
     */
    private String lastUpdatedBy;

    /**
     * The appointment customer ID
     */
    private int customerId;

    /**
     * The appointment user ID
     */
    private int userId;

    /**
     * The appointment contact ID
     */
    private int contactId;

    /**
     * The appointment contact
     */
    private Contact contact;


    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate,
                       String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;

        // Get the contact object and store it on the appointment
        Optional<Contact> optional = new ContactDaoImpl().get(contactId);
        this.contact = optional.get();
    }

    public Appointment(String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, int contactId, int userId, int customerId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.contactId = contactId;
        this.userId = userId;
        this.customerId = customerId;

        // Get the contact object and store it on the appointment
        Optional<Contact> optional = new ContactDaoImpl().get(contactId);
        this.contact = optional.get();
    }

    /**
     * Convenience method to get the appointment object
     *
     * @return appointment
     */
    public static Appointment get(int id) {
        return new AppointmentDaoImpl().get(id).get();
    }

    /**
     * ID getter
     *
     * @return The appointment id
     */
    public int getId() {
        return id;
    }

    /**
     * Title getter
     *
     * @return The appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Title setter
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Description getter
     *
     * @return The appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Location getter
     *
     * @return The appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Location setter
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Type getter
     *
     * @return The appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Type setter
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Start getter
     *
     * @return The appointment start date/time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Start setter
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * End getter
     *
     * @return The appointment end date/time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * End setter
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Customer getter
     *
     * @return The appointment customer
     */
    public Customer getCustomer() {
        return new CustomerDaoImpl().get(customerId).get();
    }

    /**
     * Customer ID getter
     *
     * @return The appointment customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Customer ID setter
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * User getter
     *
     * @return The appointment user
     */
    public User getUser() {
        return new UserDaoImpl().get(userId).get();
    }

    /**
     * User ID getter
     *
     * @return The appointment user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * User ID setter
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Contact getter
     *
     * @return The appointment contact
     */
    public Contact getContact() {
        return new ContactDaoImpl().get(contactId).get();
    }

    /**
     * Contact ID getter
     *
     * @return The appointment contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Contact ID setter
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Contact name getter
     *
     * @return The appointment contact name
     */
    public String getContactName() {
        return getContact().getName();
    }

    /**
     * Fetches all appointments from the database
     *
     * @return list of all appointments
     */
    public static ObservableList<Appointment> getAll() {
        return new AppointmentDaoImpl().getAll();
    }

    /**
     * Fetches all appointments for the current week
     *
     * @return list of appointments
     */
    public static ObservableList<Appointment> getThisWeek() {
        return new AppointmentDaoImpl().getThisWeek();
    }

    /**
     * Fetches all appointments for the current month
     *
     * @return list of appointments
     */
    public static ObservableList<Appointment> getThisMonth() {
        return new AppointmentDaoImpl().getThisMonth();
    }

    /**
     * Save the appointment record to the database and update properties
     */
    public Appointment save() {

        Appointment a = new AppointmentDaoImpl().save(this);

        // Copy additional properties into the current object
        this.id = a.id;
        this.createDate = a.createDate;
        this.createdBy = a.createdBy;
        this.lastUpdate = a.lastUpdate;
        this.lastUpdatedBy = a.lastUpdatedBy;
        this.contact = a.contact;

        return this;
    }

    /**
     * Deletes the current appointment from the database
     */
    public Boolean delete() {
        return new AppointmentDaoImpl().delete(this);
    }

    /**
     * Override the equals method to compare appointment objects
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Appointment other = (Appointment) obj;

        return this.id == other.getId();
    }
}
