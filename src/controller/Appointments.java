package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.Utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointments {

    public Label upcomingApptMessage;
    public Label currentDateTimeLabel;
    public Label officeDateTimeLabel;

    private Utils utils = Utils.getInstance();

    /**
     * Initializes the appointments view
     */
    @FXML
    public void initialize() {
        currentDateTimeLabel.setText(utils.getLocalDateTimeString());
        officeDateTimeLabel.setText(utils.getOfficeDateTimeString());
    }

    public void onCustomersButtonClick(ActionEvent event) {
        utils.switchScenes(event, "customers", "Customers");
    }
}
