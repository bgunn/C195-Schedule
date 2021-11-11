package model;

import java.time.LocalDateTime;

/**
 * The user model class
 */
public class User {

    private int userId;

    private String username;

    private String password;

    private LocalDateTime createDate;

    private String createdBy;

    private LocalDateTime lastUpdate;

    private String lastUpdatedBy;

    /**
     * user Constructor.
     *
     * @param userId User ID
     * @param username username
     * @param password password
     * @param createDate Create Date
     * @param createdBy Created By
     * @param lastUpdate Last Update
     * @param lastUpdatedBy Last Updated By
     */
    public User(int userId, String username, String password, LocalDateTime createDate,
                String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
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
        return userId;
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
}
