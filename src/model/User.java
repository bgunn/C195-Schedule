package model;

import dao.AppointmentDaoImpl;
import dao.CustomerDaoImpl;
import dao.UserDaoImpl;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * The user model class
 */
public class User {

    /**
     * The user ID
     */
    private int id;

    /**
     * The username
     */
    private String username;

    /**
     * The password
     */
    private String password;

    /**
     * The created date
     */
    private LocalDateTime createDate;

    /**
     * The created by user
     */
    private String createdBy;

    /**
     * The last update date
     */
    private LocalDateTime lastUpdate;

    /**
     * The last updated by user
     */
    private String lastUpdatedBy;

    /**
     * user Constructor.
     *
     * @param id User ID
     * @param username username
     * @param password password
     * @param createDate Create Date
     * @param createdBy Created By
     * @param lastUpdate Last Update
     * @param lastUpdatedBy Last Updated By
     */
    public User(int id, String username, String password, LocalDateTime createDate,
                String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * ID getter
     * @return userId
     */
    public int getId() {
        return id;
    }

    /**
     * Username getter
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * password getter
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Create date  getter
     * @return createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Created by  getter
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Last update date getter
     * @return lastUpdate
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Last updated by getter
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Fetches all users from the database
     *
     * @return list of all users
     */
    public static ObservableList<User> getAll() {
        return new UserDaoImpl().getAll();
    }

    /**
     * Fetches all upcoming appointments for this user
     *
     * @return list of appointments
     */
    public Appointment getUpcomingAppointment() {
        return new AppointmentDaoImpl().getUpcomingByUserId(id);
    }
}
