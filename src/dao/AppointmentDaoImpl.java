package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Appointments;
import utils.JDBC;
import utils.Utils;

import java.sql.*;
import java.util.Optional;

public class AppointmentDaoImpl implements Dao<Appointment> {

    private final Utils utils = Utils.getInstance();
    private final ObservableList<Appointment> appointments = Appointments.getInstance().getAppointments();
    private Connection conn = JDBC.getConnection();

    @Override
    public Optional<Appointment> get(int id) {

        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?;";

        try {

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

        appointments.clear();

        try {

            ResultSet results = conn.createStatement().executeQuery(query);

            while (results.next()) {
                appointments.add(createAppointment(results));
            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Getter to select all appointments for the current week
     *
     * @return list of appointments {@link ObservableList}
     */
    public ObservableList<Appointment> getThisWeek() {
        return getByDateQuery("SELECT * FROM appointments WHERE WEEK(Start) = WEEK(?);");
    }

    /**
     * Getter to select all appointments for the current month
     *
     * @return list of appointments {@link ObservableList}
     */
    public ObservableList<Appointment> getThisMonth() {
        return getByDateQuery("SELECT * FROM appointments WHERE MONTH(Start) = MONTH(?);");
    }

    /**
     * Executes the provided query and applies a single prepared statement parameter
     * of the local date/time string.
     *
     * @return list of appointments {@link ObservableList}
     */
    private ObservableList<Appointment> getByDateQuery(String query) {

        appointments.clear();

        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, utils.getLocalDateTimeString());

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

    /**
     * Getter to select all records by the specified customer ID
     *
     * @return appointments
     */
    public ObservableList<Appointment> getAllByCustomerId(int id) {

        String query = "SELECT * FROM appointments WHERE Customer_ID = ?;";

        ObservableList<Appointment> aptList = FXCollections.observableArrayList();

        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                aptList.add(createAppointment(results));
            }

            return aptList;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Getter to select the next upcoming appointment within 15 minutes by user ID
     *
     * @return appointment
     */
    public Appointment getUpcomingByUserId(int id) {

        String query = "SELECT * FROM appointments WHERE User_ID = ? " +
                "AND Start >= now() AND Start <= date_add(now(),interval 15 minute) " +
                "ORDER BY Start ASC LIMIT 1;";

        ObservableList<Appointment> aptList = FXCollections.observableArrayList();

        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                return createAppointment(results);
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Execute the specified report query
     *
     * @param report the report query to execute
     * @return the query results
     */
    public ResultSet reportQuery(String report) {

        String query;

        switch (report) {

            case "customer":
                query = "SELECT Type, MONTHNAME(Start) as Month, count(*) AS Count " +
                        "FROM appointments GROUP BY Type, MONTHNAME(Start) ORDER BY Type, Start;";
                break;

            case "contact":
                query = "SELECT c.Contact_Name, a.* FROM appointments a, contacts c " +
                        "WHERE a.Contact_ID = c.Contact_ID ORDER BY Contact_Name, Start;";
                break;

            case "user":
                query = "SELECT u.User_Name, a.* FROM appointments a, users u " +
                        "WHERE a.User_ID = u.User_ID ORDER BY User_Name, Location, Start;";
                break;

            default: return null;
        }

        try {
            return conn.createStatement().executeQuery(query);
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

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setString(5, utils.localDateTimeToUTCString(appointment.getStart()));
            stmt.setString(6, utils.localDateTimeToUTCString(appointment.getEnd()));
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

                    Appointment a = optional.get();

                    appointments.add(a);

                    return a;

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

            Appointment a = optional.get();

            appointments.set(appointments.indexOf(a), a);

            return a;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean delete(Appointment appointment) {

        String query = "DELETE FROM appointments WHERE Appointment_ID = ?;";

        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, appointment.getId());

            stmt.execute();

            if (stmt.getUpdateCount() > 0) {
                appointments.remove(appointment);
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
