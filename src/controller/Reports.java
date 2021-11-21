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
import java.time.format.DateTimeFormatter;

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

    /**
     * Display the customer report
     *
     * @param event The button click event
     */
    public void onCustomerReportLinkClick(ActionEvent event)  {

        customerReportLink.setVisited(false);

        reportsTextFlow.getChildren().clear();
        reportsTextFlow.setTextAlignment(TextAlignment.LEFT);

        // Set the report title
        Text title = new Text("Number of Customer Appointments by Type And Month\n\n");
        title.setFill(Color.BLACK);
        title.setFont(Font.font("CiscoSans", FontWeight.BOLD, 24));
        reportsTextFlow.getChildren().addAll(title);

        // Set the CSV headers
        Text header = new Text("Type, Month, Count\n");
        header.setFill(Color.BLACK);
        header.setFont(Font.font("CiscoSans", FontWeight.BOLD, 16));
        reportsTextFlow.getChildren().addAll(header);

        ResultSet results = new AppointmentDaoImpl().reportQuery("customer");

        try {
            while (results.next()) {
                String row = results.getString("Type") + ", " +
                        results.getString("Month") + ", " +
                        results.getString("Count");

                Text text = new Text(row + "\n");
                text.setFill(Color.BLACK);
                text.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 16));

                reportsTextFlow.getChildren().addAll(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the contact report
     *
     * @param event The button click event
     */
    public void onContactReportLinkClick(ActionEvent event) {
        contactReportLink.setVisited(false);

        reportsTextFlow.getChildren().clear();
        reportsTextFlow.setTextAlignment(TextAlignment.LEFT);

        // Set the report title
        Text title = new Text("Contact Schedule\n\n");
        title.setFill(Color.BLACK);
        title.setFont(Font.font("CiscoSans", FontWeight.BOLD, 24));
        reportsTextFlow.getChildren().addAll(title);

        // Set the CSV headers
        Text header = new Text("Contact, Appointment ID, Title, Description, Start, End, Customer ID\n");
        header.setFill(Color.BLACK);
        header.setFont(Font.font("CiscoSans", FontWeight.BOLD, 16));
        reportsTextFlow.getChildren().addAll(header);

        ResultSet results = new AppointmentDaoImpl().reportQuery("contact");

        try {
            while (results.next()) {

                DateTimeFormatter dtf = utils.getDateTimeFormatter();
                String start = dtf.format(results.getTimestamp("Start").toLocalDateTime());
                String end = dtf.format(results.getTimestamp("End").toLocalDateTime());

                String row = results.getString("Contact_Name") + ", " +
                        results.getInt("Appointment_ID") + ", " +
                        results.getString("Title") + ", " +
                        results.getString("Description") + ", " +
                        start + ", " + end + ", " +
                        results.getInt("Customer_ID");

                Text text = new Text(row + "\n");
                text.setFill(Color.BLACK);
                text.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 16));

                reportsTextFlow.getChildren().addAll(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the user report
     *
     * @param event The button click event
     */
    public void onUserReportLinkClick(ActionEvent event) {
        userReportLink.setVisited(false);

        reportsTextFlow.getChildren().clear();
        reportsTextFlow.setTextAlignment(TextAlignment.LEFT);

        // Set the report title
        Text title = new Text("User Schedule by Location\n\n");
        title.setFill(Color.BLACK);
        title.setFont(Font.font("CiscoSans", FontWeight.BOLD, 24));
        reportsTextFlow.getChildren().addAll(title);

        // Set the CSV headers
        Text header = new Text("User, Location, Appointment ID, Title, Description, Start, End, Customer ID\n");
        header.setFill(Color.BLACK);
        header.setFont(Font.font("CiscoSans", FontWeight.BOLD, 16));
        reportsTextFlow.getChildren().addAll(header);

        ResultSet results = new AppointmentDaoImpl().reportQuery("user");

        try {
            while (results.next()) {

                DateTimeFormatter dtf = utils.getDateTimeFormatter();
                String start = dtf.format(results.getTimestamp("Start").toLocalDateTime());
                String end = dtf.format(results.getTimestamp("End").toLocalDateTime());

                String row = results.getString("User_Name") + ", " +
                        results.getString("Location") + ", " +
                        results.getInt("Appointment_ID") + ", " +
                        results.getString("Title") + ", " +
                        results.getString("Description") + ", " +
                        start + ", " + end + ", " +
                        results.getInt("Customer_ID");

                Text text = new Text(row + "\n");
                text.setFill(Color.BLACK);
                text.setFont(Font.font("CiscoSans", FontWeight.NORMAL, 16));

                reportsTextFlow.getChildren().addAll(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
