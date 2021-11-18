package model;

/**
 * <pre>
 * The contact model
 *
 * Provides a constructor for instantiating contact objects
 * and provides getters and setters for working with contact
 * properties.
 * </pre>
 */
public class Contact {

    /**
     * The contact ID
     */
    private int id;

    /**
     * The contact name
     */
    private String name;

    /**
     * The contact email
     */
    private String email;

    /**
     * The contact constructor
     *
     * @param id The contact ID
     * @param name The contact name
     * @param email The contact email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * The contact ID getter
     *
     * @return id The contact ID
     */
    public int getId() {
        return id;
    }

    /**
     * The contact name getter
     *
     * @return name The contact name
     */
    public String getName() {
        return name;
    }

    /**
     * The contact email getter
     *
     * @return email The contact email
     */
    public String getEmail() {
        return email;
    }
}
