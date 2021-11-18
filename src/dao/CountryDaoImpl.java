package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
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
public class CountryDaoImpl implements Dao<Country> {

    @Override
    public Optional<Country> get(int id) {

        String query = "SELECT * FROM countries WHERE Country_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createCountry(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Country> getBy(String field, String value) {
        return Optional.empty();
    }

    @Override
    public ObservableList<Country> getAll() {

        String query = "SELECT * FROM countries";

        ObservableList<Country> countries = FXCollections.observableArrayList();

        try {

            ResultSet results = JDBC.getConnection().createStatement().executeQuery(query);

            while (results.next()) {
                countries.add(createCountry(results));
            }

            return countries;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Country save(Country country) {
        return null;
    }

    @Override
    public Country update(Country country) {
        return null;
    }

    @Override
    public Boolean delete(Country country) {
        return true;
    }

    /**
     * Instantiates a country object from the database results
     *
     * @param r  The result set containing the country database record
     * @return Country
     */
    private Country createCountry(ResultSet r) throws SQLException {
        return new Country(
                r.getInt("Country_ID"),
                r.getString("Country"),
                r.getTimestamp("Create_Date").toLocalDateTime(),
                r.getString("Created_By"),
                r.getTimestamp("Last_Update").toLocalDateTime(),
                r.getString("Last_Updated_By")
        );
    }
}
