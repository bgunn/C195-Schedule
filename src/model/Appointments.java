package model;

import dao.AppointmentDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Appointments singleton is used to hold the appointments ObservableList
 *
 * @see AppointmentDaoImpl#getAll()
 */
public class Appointments {

    /**
     * The Appointments singleton
     */
    private static final Appointments instance = new Appointments();

    /**
     * Holds the observable customers list
     */
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * Return the Appointments instance
     * @return instance
     */
    public static Appointments getInstance(){
        return instance;
    }

    /**
     * Returns all appointments
     * @return allAppointments All appointments
     */
    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }
}
