package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Customers singleton is used to hold the customers ObservableList
 *
 *  * @see CustomerDaoImpl#getAll()
 */
public class Customers {

    /**
     * The Customers singleton
     */
    private static final Customers instance = new Customers();

    /**
     * Holds the observable customers list
     */
    private final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Return the utils instance
     * @return instance
     */
    public static Customers getInstance(){
        return instance;
    }

    /**
     * Returns all customers
     */
    public ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
}
