package controller;

import javafx.event.ActionEvent;
import utils.Utils;

public class Customers {

    public void onAppointmentsButtonClick(ActionEvent event) {
        Utils.switchScenes(event, "appointments");
    }

    public void onNewCustomerButtonClick(ActionEvent event) {
        Utils.switchScenes(event, "addCustomer");
    }
}
