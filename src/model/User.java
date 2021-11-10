package model;

import java.time.LocalDateTime;

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

    public int getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
}
