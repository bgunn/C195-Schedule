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

public class UserImpl implements Dao<User> {

    @Override
    public Optional<User> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getBy(String field, String value) {

        Connection conn = null;
        PreparedStatement stmt = null;

        String query = String.format("SELECT * FROM users WHERE %s = ?;", field);

        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, value);
            ResultSet results = stmt.executeQuery();

            System.out.println(stmt);

            while (results.next()) {
                return Optional.of(createUser(results));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

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

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {

    }

    public Optional<User> getByUsername(String username) {
        return getBy("User_Name", username);
    }

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
