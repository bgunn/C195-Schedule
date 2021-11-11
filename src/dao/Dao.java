package dao;

import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * <pre>
 * DAO interface
 *
 * This interface defines the standard CRUD operations to be
 * performed on the model objects
 * </pre>
 */
public interface Dao<T> {

    /**
     * Fetch the record by ID
     *
     * @param id  The object ID
     * @return the object wrapped in an {@link Optional}
     */
    Optional<T> get(long id);

    /**
     * <pre>
     * Getter to select by any field name with a string value
     *
     * This method returns the first matching result
     * </pre>
     *
     * @param field  The field name to compare in the WHERER condition
     * @param value  The field value to compare in the WHERE condition
     * @return the object wrapped in an {@link Optional}
     */
    Optional<T> getBy(String field, String value);

    /**
     * Getter to select all records
     *
     * @return all database records {@link ObservableList}
     */
    ObservableList<T> getAll();

    /**
     * Save an object to the database
     *
     * @return void
     */
    void save(T t);

    /**
     * Update a database record
     *
     * @param t The object to update
     * @param params An array of values to update on the object
     * @return void
     */
    void update(T t, String[] params);

    /**
     * Delete a database record
     *
     * @param t The object to delete
     * @return void
     */
    void delete(T t);
}
