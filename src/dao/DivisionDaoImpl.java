package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;
import utils.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * <pre>
 * Country DAO concrete class
 *
 * This class implements the DAO interface and is responsible for handling all
 * database operations on the countries table.
 * </pre>
 */
public class DivisionDaoImpl implements Dao<Division> {

    @Override
    public Optional<Division> get(int id) {

        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createDivision(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Division> getBy(String field, String value) {

        String query = String.format("SELECT * FROM first_level_divisions WHERE %s = ? LIMIT 1;", field);

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, value);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createDivision(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public ObservableList<Division> getAll() {
        return null;
    }

    @Override
    public Division save(Division country) {
        return null;
    }

    @Override
    public Division update(Division country) {
        return null;
    }

    @Override
    public Boolean delete(Division country) {
        return true;
    }

    /**
     * Getter to select all division records by country name
     *
     * @return divisions {@link ObservableList}
     */
    public ObservableList<Division> getByCountry(String country) {

        String query = "SELECT d.* FROM countries c, first_level_divisions d WHERE c.country = ? and c.Country_ID = d.Country_ID;";

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, country);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                divisions.add(createDivision(results));
            }

            return divisions;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Instantiates a division object from the database results
     *
     * @param r  The result set containing the country database record
     * @return Country
     */
    private Division createDivision(ResultSet r) throws SQLException {
        return new Division(
                r.getInt("Division_ID"),
                r.getString("division"),
                r.getTimestamp("Create_Date").toLocalDateTime(),
                r.getString("Created_By"),
                r.getTimestamp("Last_Update").toLocalDateTime(),
                r.getString("Last_Updated_By"),
                r.getInt("Country_ID")
        );
    }
}

