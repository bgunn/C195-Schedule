package controller;

import dao.AppointmentDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Reports {

    /**
     * Store the customer report link
     */
    @FXML
    public Hyperlink customerReportLink;

    /**
     * Store the contact report link
     */
    @FXML
    public Hyperlink contactReportLink;

    /**
     * Store the user report link
     */
    @FXML
    public Hyperlink userReportLink;

    /**
     * Part TextFlow area for managing messages
     */
    @FXML
    private TextFlow reportsTextFlow;

    /**
     * Store the Utils singleton
     */
    private final Utils utils = Utils.getInstance();

    /**
     * Initializes the reports view
     */
    @FXML
    public void initialize() {
        Text blank = new Text("\n\n\nSelect a report\n");
        blank.setFill(Color.GRAY);
        blank.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 14));
        reportsTextFlow.getChildren().addAll(blank);
        reportsTextFlow.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Switches the scene to the customers view
     *
     * @param event The button click event
     */
    public void onCustomersButtonClick(ActionEvent event) {
        utils.switchScenes(event, "customers", "Customers");
    }

    /**
     * Switches the scene to the appointments view
     *
     * @param event The button click event
     */
    public void onAppointmentsButtonClick(ActionEvent event) {
        utils.switchScenes(event, "appointments", "Appointments");
    }

    public void onCustomerReportLinkClick(ActionEvent event)  {

        customerReportLink.setVisited(false);

        reportsTextFlow.getChildren().clear();
        reportsTextFlow.setTextAlignment(TextAlignment.LEFT);

        // Set the report title
        Text title = new Text("Customer Appointments by Type And Month\n\n");
        title.setFill(Color.BLACK);
        title.setFont(Font.font("CiscoSans", FontWeight.BOLD, 24));
        reportsTextFlow.getChildren().addAll(title);

        // Set the CSV headers
        Text header = new Text("Type, Month, Count\n");
        header.setFill(Color.BLACK);
        header.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 18));
        reportsTextFlow.getChildren().addAll(header);

        ResultSet results = new AppointmentDaoImpl().customerReport();

        try {
            while (results.next()) {
                String row = results.getString("Type") + ", " +
                        results.getString("Month") + ", " +
                        results.getString("Count");

                Text text = new Text(row + "\n");
                text.setFill(Color.BLACK);
                text.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 18));

                reportsTextFlow.getChildren().addAll(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onContactReportLinkClick(ActionEvent event) {
        contactReportLink.setVisited(false);
    }

    public void onUserReportLinkClick(ActionEvent event) {
        userReportLink.setVisited(false);
    }
}
