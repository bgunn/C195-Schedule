package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import utils.Utils;

public class Appointments {

    public Label upcomingApptMessage;
    public Label currentDateTimeLabel;

    public void onCustomersButtonClick(ActionEvent event) {
        Utils.switchScenes(event, "customers");
    }
}
