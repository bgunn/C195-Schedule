package controller;

import javafx.event.ActionEvent;
import utils.Utils;

public class Customers {

    private Utils utils = Utils.getInstance();

    public void onAppointmentsButtonClick(ActionEvent event) {
        utils.switchScenes(event, "appointments", "Appointments");
    }

    public void onNewButtonClick(ActionEvent event) {
        utils.openWindow(event, "addCustomer", "Add Customer");
    }
}
