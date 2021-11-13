package model;

import dao.CustomerDaoImpl;
import dao.DivisionDaoImpl;

import java.util.Optional;

public class Customer {

    /**
     * The customer ID
     */
    private int id;

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
     * The customer division object
     */
    private Division division;

    /**
     * @param name The customer name
     * @param address The customer address
     * @param postCode The customer postal code
     * @param phone The customer phone number
     * @param divisionName The customer division name
     */
    public Customer(String name, String address, String postCode, String phone, String divisionName) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;

        Optional<Division> optional = new DivisionDaoImpl().getBy("Division", divisionName);
        this.division = optional.get();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionId() {
        return division.getId();
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String getDivisionName() {
        return division.getName();
    }

    public String getCountryName() {
        return division.getCountry().getName();
    }

    public void save() {
        int customerId = new CustomerDaoImpl().save(this);
        if (customerId > 0) {
            id = customerId;
        }
    }
}
