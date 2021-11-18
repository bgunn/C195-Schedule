package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Appointments;
import model.Customer;
import utils.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.Optional;

public class AppointmentDaoImpl implements Dao<Appointment> {

    private final Utils utils = Utils.getInstance();
    private final Appointments appointments = Appointments.getInstance();
    private final ObservableList<Appointment> allAppointments = appointments.getAllAppointments();;

    @Override
    public Optional<Appointment> get(int id) {

        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return Optional.of(createAppointment(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Appointment> getBy(String field, String value) {
        return Optional.empty();
    }

    /**
     * Getter to select all records
     *
     * @return all database records {@link ObservableList}
     */
    @Override
    public ObservableList<Appointment> getAll() {

        String query = "SELECT * FROM appointments";

        allAppointments.clear();

        try {

            ResultSet results = JDBC.getConnection().createStatement().executeQuery(query);

            while (results.next()) {
                allAppointments.add(createAppointment(results));
            }

            return allAppointments;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Getter to select all records by Customer_ID
     *
     * @return appointments
     */
    public ObservableList<Appointment> getAllByCustomerId(int customer_id) {

        String query = "SELECT * FROM appointments WHERE Customer_ID = ?";

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customer_id);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                appointments.add(createAppointment(results));
            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getId() == 0) {
            return addAppointment(appointment);
        } else {
            return update(appointment);
        }
    }

    /**
     * Generates and executes the SQL to add an appointment to the database
     *
     * @return The appointment object
     */
    private Appointment addAppointment(Appointment appointment) {

        String query = "INSERT INTO appointments(" +
                "Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setString(5, appointment.getStart().format(utils.getDateTimeFormatter()));
            stmt.setString(6, appointment.getEnd().format(utils.getDateTimeFormatter()));
            stmt.setString(7, utils.getUTCDateTimeString());
            stmt.setString(8, utils.getUser().getUsername());
            stmt.setString(9, utils.getUTCDateTimeString());
            stmt.setString(10, utils.getUser().getUsername());
            stmt.setInt(11, appointment.getCustomerId());
            stmt.setInt(12, appointment.getUserId());
            stmt.setInt(13, appointment.getContactId());


            stmt.execute();

            if (stmt.getUpdateCount() == 0) {
                throw new SQLException("Failed to add appointment to the database");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    Optional<Appointment> optional = get(generatedKeys.getInt(1));
                    return optional.get();

                } else {
                    throw new SQLException("Failed to get appointment ID from the inserted appointment");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates and executes the SQL to update an existing appointment in the database
     *
     * @return The appointment object
     */
    @Override
    public Appointment update(Appointment appointment) {

        String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                " User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setString(5, utils.localDateTimeToUTCString(appointment.getStart()));
            stmt.setString(6, utils.localDateTimeToUTCString(appointment.getEnd()));
            stmt.setString(7, utils.getUTCDateTimeString());
            stmt.setString(8, utils.getUser().getUsername());
            stmt.setInt(9, appointment.getCustomerId());
            stmt.setInt(10, appointment.getUserId());
            stmt.setInt(11, appointment.getContactId());
            stmt.setInt(12, appointment.getId());

            stmt.execute();

            if (stmt.getUpdateCount() == 0) {
                throw new SQLException("Failed to update appointment");
            }

            Optional<Appointment> optional = get(appointment.getId());
            return optional.get();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean delete(Appointment appointment) {

        String query = "DELETE FROM appointments WHERE Appointment_ID = ?;";

        try {

            Connection conn = JDBC.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, appointment.getId());

            stmt.execute();

            if (stmt.getUpdateCount() > 0) {
                allAppointments.remove(appointment);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Instantiates an appointment object from the database results
     *
     * @param r  The result set containing the appointment database record
     * @return Appointment
     */
    private Appointment createAppointment(ResultSet r) throws SQLException {

        return new Appointment(
                r.getInt("Appointment_ID"),
                r.getString("Title"),
                r.getString("Description"),
                r.getString("Location"),
                r.getString("Type"),
                r.getTimestamp("Start").toLocalDateTime(),
                r.getTimestamp("End").toLocalDateTime(),
                r.getTimestamp("Create_Date").toLocalDateTime(),
                r.getString("Created_By"),
                r.getTimestamp("Last_Update").toLocalDateTime(),
                r.getString("Last_Updated_By"),
                r.getInt("Customer_ID"),
                r.getInt("User_ID"),
                r.getInt("Contact_ID")
        );
    }
}
