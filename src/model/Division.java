package model;

import dao.CountryDaoImpl;

import java.time.LocalDateTime;
import java.util.Optional;

public class Division {

    /**
     * The division ID column
     */
    private int id;

    /**
     * The division column
     */
    private String division;

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
     * The country ID
     */
    private int countryId;

    /**
     * The parent country
     */
    private Country country;

    /**
     * user Constructor.
     *
     * @param id
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public Division(int id, String division, LocalDateTime createDate, String createdBy,
                LocalDateTime lastUpdate, String lastUpdatedBy, int countryId) {
        this.id = id;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;

        // Add the associated country object
        Optional<Country> optional = new CountryDaoImpl().get(countryId);
        this.country = optional.get();
    }

    /**
     * ID getter
     * @return userId
     */
    public int getId() {
        return id;
    }

    /**
     * Division name getter
     * @return username
     */
    public String getName() {
        return division;
    }

    /**
     * Create date  getter
     * @return createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Created by getter
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
     * Return the associated country
     * @return Country
     */
    public Country getCountry() {
        return country;
    }
}
