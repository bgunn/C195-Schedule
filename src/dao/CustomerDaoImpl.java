package dao;

import javafx.collections.ObservableList;
import model.Customer;
import utils.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.Optional;

public class CustomerDaoImpl implements Dao<Customer> {

    private Utils utils = Utils.getInstance();

    @Override
    public Optional<Customer> get(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> getBy(String field, String value) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Customer> getAll() {
        return null;
    }

    @Override
    public int save(Customer customer) {

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
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void update(Customer customer, String[] params) {

    }

    @Override
    public void delete(Customer customer) {

    }
}
