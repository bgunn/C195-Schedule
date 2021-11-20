package dao;

import javafx.collections.ObservableList;
import model.Customer;
import model.Customers;
import utils.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.Optional;

public class CustomerDaoImpl implements Dao<Customer> {

    private final Utils utils = Utils.getInstance();
    private final ObservableList<Customer> customers = Customers.getInstance().getCustomers();

    @Override
    public Optional<Customer> get(int id) {

        String query = "SELECT * FROM customers WHERE Customer_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createCustomer(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> getBy(String field, String value) {
        return Optional.empty();
    }

    /**
     * Getter to select all records
     *
     * @return all database records {@link ObservableList}
     */
    @Override
    public ObservableList<Customer> getAll() {

        String query = "SELECT c.*, d.division FROM customers c, first_level_divisions d WHERE c.Division_ID = d.Division_ID;";

        customers.clear();

        try {

            ResultSet results = JDBC.getConnection().createStatement().executeQuery(query);

            while (results.next()) {
                customers.add(createCustomer(results));
            }

            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Customer save(Customer customer) {

        if (customer.getId() == 0) {
            return addCustomer(customer);
        } else {
            return update(customer);
        }
    }

    /**
     * Generates and executes the SQL to add a customer to the database
     *
     * @return The customer object
     */
    private Customer addCustomer(Customer customer) {

        String query = "INSERT INTO customers(" +
                "Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostCode());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, utils.getUTCDateTimeString());
            stmt.setString(6, utils.getUser().getUsername());
            stmt.setString(7, utils.getUTCDateTimeString());
            stmt.setString(8, utils.getUser().getUsername());
            stmt.setInt(9, customer.getDivisionId());

            stmt.execute();

            if (stmt.getUpdateCount() == 0) {
                throw new SQLException("Failed to add the customer to the database");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    Optional<Customer> optional = get(generatedKeys.getInt(1));
                    Customer c = optional.get();

                    customers.add(c);

                    return c;

                } else {
                    throw new SQLException("Failed to get the customer ID from the inserted customer");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates and executes the SQL to update an existing customer in the database
     *
     * @return The customer object
     */
    @Override
    public Customer update(Customer customer) {

        String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                       "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostCode());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, utils.getUTCDateTimeString());
            stmt.setString(6, utils.getUser().getUsername());
            stmt.setInt(7, customer.getDivisionId());
            stmt.setInt(8, customer.getId());

            stmt.execute();

            if (stmt.getUpdateCount() == 0) {
                throw new SQLException("Updating customer failed, no rows affected.");
            }

            Optional<Customer> optional = get(customer.getId());

            Customer c = optional.get();

            customers.set(customers.indexOf(c), c);

            return c;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean delete(Customer customer) {

        String query = "DELETE FROM customers WHERE Customer_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customer.getId());

            stmt.execute();

            if (stmt.getUpdateCount() > 0) {
                customers.remove(customer);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Instantiates a customer object from the database results
     *
     * @param r  The result set containing the customer database record
     * @return Customer
     */
    private Customer createCustomer(ResultSet r) throws SQLException {
        return new Customer(
                r.getInt("Customer_ID"),
                r.getString("Customer_Name"),
                r.getString("address"),
                r.getString("Postal_Code"),
                r.getString("Phone"),
                r.getTimestamp("Create_Date").toLocalDateTime(),
                r.getString("Created_By"),
                r.getTimestamp("Last_Update").toLocalDateTime(),
                r.getString("Last_Updated_By"),
                r.getInt("Division_ID")
        );
    }
}
