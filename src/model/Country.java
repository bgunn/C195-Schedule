package model;

import java.time.LocalDateTime;

public class Country {

    /**
     * The country ID column
     */
    private int countryId;

    /**
     * The country column
     */
    private String country;

    /**
     * The create date column
     */
    private LocalDateTime createDate;

    /**
     * The created by column
     */
    private String createdBy;

    /**
     * The last update column
     */
    private LocalDateTime lastUpdate;

    /**
     * The last updated by column
     */
    private String lastUpdatedBy;

    /**
     * user Constructor.
     *
     * @param countryId
     * @param country
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public Country(int countryId, String country, LocalDateTime createDate, String createdBy,
                LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.country = country;
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
        return countryId;
    }

    /**
     * Username getter
     * @return username
     */
    public String getName() {
        return country;
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
