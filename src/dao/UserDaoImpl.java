package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * <pre>
 * User DAO concrete class
 *
 * This class implements the DAO interface and is responsible for handling all
 * database operations on the users table.
 * </pre>
 */
public class UserDaoImpl implements Dao<User> {

    /**
     * Fetch the record by ID
     *
     * @param id  The user ID
     * @return the object wrapped in an {@link Optional}
     */
    @Override
    public Optional<User> get(int id) {

        String query = "SELECT * FROM users WHERE User_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createUser(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * <pre>
     * Getter to select by any field name with a string value
     *
     * This method returns the first matching result
     * </pre>
     *
     * @param field  The field name to compare in the WHERER condition
     * @param value  The field value to compare in the WHERE condition
     * @return the user wrapped in an {@link Optional}
     */
    @Override
    public Optional<User> getBy(String field, String value) {

        String query = String.format("SELECT * FROM users WHERE %s = ? LIMIT 1;", field);

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, value);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createUser(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Getter to select all records
     *
     * @return all database records {@link ObservableList}
     */
    @Override
    public ObservableList<User> getAll() {

        String query = "SELECT * FROM users;";

        ObservableList<User> users = FXCollections.observableArrayList();

        try {

            ResultSet results = JDBC.getConnection().createStatement().executeQuery(query);

            while (results.next()) {
                users.add(createUser(results));
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Save an object to the database
     *
     * @return void
     */
    @Override
    public User save(User user) {
        return null;
    }

    /**
     * Update a database record
     *
     * @param user The object to update
     * @return void
     */
    @Override
    public User update(User user) {
        return null;
    }

    /**
     * Delete a database record
     *
     * @param user The object to delete
     * @return void
     */
    @Override
    public Boolean delete(User user) {
        return true;
    }

    /**
     * Convenience method to fetch a user by username
     *
     * @param username
     * @return the user wrapped in an {@link Optional}
     */
    public Optional<User> getByUsername(String username) {
        return getBy("User_Name", username);
    }

    /**
     * Instantiates a user object from the database results
     *
     * @param r  The result set containing the user database record
     * @return User
     */
    private User createUser(ResultSet r) throws SQLException {
        return new User(
                r.getInt("User_ID"),
                r.getString("User_Name"),
                r.getString("Password"),
                r.getTimestamp("Create_Date").toLocalDateTime(),
                r.getString("Created_By"),
                r.getTimestamp("Last_Update").toLocalDateTime(),
                r.getString("Last_Updated_By")
        );
    }
}
