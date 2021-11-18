package model;

import dao.AppointmentDaoImpl;
import dao.CustomerDaoImpl;
import dao.DivisionDaoImpl;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <pre>
 * The customer model
 *
 * Provides a constructor for instantiating customer objects
 * and provides getters and setters for working with customer
 * properties.
 * </pre>
 */
public class Customer {

    /**
     * The customer ID
     */
    private int id = 0;

    /**
     * The customer name
     */
    private String name;

    /**
     * The customer address
     */
    private String address;

    /**
     * The customer postal code
     */
    private String postCode;

    /**
     * The customer phone number
     */
    private String phone;

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
     * The customer division ID
     */
    private int divisionId;

    /**
     * The customer division object
     */
    private Division division;

    /**
     * @param name The customer name
     * @param address The customer address
     * @param postCode The customer postal code
     * @param phone The customer phone number
     * @param createDate The create date
     * @param createdBy Created By
     * @param lastUpdate Last Update
     * @param lastUpdatedBy Last Updated By
     * @param divisionId The division ID
     */
    public Customer(int id, String name, String address, String postCode, String phone, LocalDateTime createDate,
                    String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;

        // Get the division object and set the division and country name on the customer
        Optional<Division> optional = new DivisionDaoImpl().get(divisionId);
        this.division = optional.get();
    }

    /**
     * @param name The customer name
     * @param address The customer address
     * @param postCode The customer postal code
     * @param phone The customer phone number
     * @param divisionId The customer division ID
     */
    public Customer(String name, String address, String postCode, String phone, int divisionId) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;
        this.divisionId = divisionId;

        // Set the division
        this.division = new DivisionDaoImpl().get(divisionId).get();
    }

    /**
     * Convenience method to get the customer object
     *
     * @return customer
     */
    public static Customer get(int id) {
        return new CustomerDaoImpl().get(id).get();
    }

    /**
     * ID getter
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Name getter
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     *
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Address getter
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Address setter
     *
     * @return void
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Post code getter
     *
     * @return postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Postal code setter
     *
     * @return void
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * Phone getter
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Phone setter
     *
     * @return void
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Division ID getter
     *
     * @return division,id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Division ID setter
     *
     * @return division,id
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Division getter
     *
     * @return division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Division name getter
     *
     * @return division
     */
    public String getDivisionName() {
        return getDivision().getName();
    }

    /**
     * Country name getter
     *
     * @return country.name
     */
    public String getCountryName() {
        return getDivision().getCountry().getName();
    }

    /**
     * Fetches all customers from the database
     *
     * @return list of all customers
     */
    public static ObservableList<Customer> getAll() {
        return new CustomerDaoImpl().getAll();
    }

    /**
     * Fetches all appointments for this customer
     *
     * @return list of all customer appointments
     */
    public ObservableList<Appointment> getAppointments() {
        return new AppointmentDaoImpl().getAllByCustomerId(id);
    }

    /**
     * Save the customer record to the database and update properties
     */
    public Customer save() {

        Customer c = new CustomerDaoImpl().save(this);

        // Copy additional properties into the current object
        this.id = c.id;
        this.createDate = c.createDate;
        this.createdBy = c.createdBy;
        this.lastUpdate = c.lastUpdate;
        this.lastUpdatedBy = c.lastUpdatedBy;
        this.division = c.division;

        return this;
    }

    /**
     * Deletes the current customer from the database
     */
    public Boolean delete() {
        return new CustomerDaoImpl().delete(this);
    }
}
