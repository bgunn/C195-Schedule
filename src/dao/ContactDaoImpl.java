package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utils.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ContactDaoImpl implements Dao<Contact> {

    @Override
    public Optional<Contact> get(int id) {

        String query = "SELECT * FROM contacts WHERE Contact_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createContact(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Contact> getBy(String field, String value) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Contact> getAll() {

        String query = "SELECT * FROM contacts";

        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        try {

            ResultSet results = JDBC.getConnection().createStatement().executeQuery(query);

            while (results.next()) {
                contacts.add(createContact(results));
            }

            return contacts;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Contact save(Contact contact) {
        return null;
    }

    @Override
    public Contact update(Contact contact) {
        return null;
    }

    @Override
    public Boolean delete(Contact contact) {
        return true;
    }

    /**
     * Instantiates a contact object from the database results
     *
     * @param r  The result set containing the contact database record
     * @return Contact
     */
    private Contact createContact(ResultSet r) throws SQLException {
        return new Contact(
                r.getInt("Contact_ID"),
                r.getString("Contact_Name"),
                r.getString("Email")
        );
    }
}
