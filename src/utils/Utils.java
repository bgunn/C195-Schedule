package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    /**
     * The utils singleton
     */
    private static Utils instance = new Utils();

    /**
     * The date/time string
     */
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");

    /**
     * Holds the authenticated user object
     */
    private User user;

    /**
     * Private constructor so the class cannot be instantiated
     */
    private Utils() {}

    /**
     * Return the utils instance
     * @return instance
     */
    public static Utils getInstance(){
        return instance;
    }

    /**
     * Get the authenticated user
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the authenticated user
     * @return user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public String getLocalDateTimeString() {
        return dtf.format(ZonedDateTime.now());
    }

    public String getOfficeDateTimeString() {
        return dtf.format(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("America/New_York")));
    }

    public String getUTCDateTimeString() {
        return dtf.format(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
    }

    /**
     * Open the specified window and set the title
     */
    public void openWindow(ActionEvent event, String view, String title) {
        try {
            Parent root = FXMLLoader.load(Utils.class.getResource("/view/" + view + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the current window
     */
    public void closeWindow(ActionEvent event) {
        try {
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convenience method to handle switching scenes
     *
     * @param event Button click ActionEvent
     * @param view  The name of the view to load
     * @return void
     */
    public void switchScenes(ActionEvent event, String view, String title) {

        try {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(Utils.class.getResource("/view/" + view + ".fxml"));
            screen.setTitle(title);
            screen.setScene(new Scene(scene));
            screen.show();
        } catch (Exception e) {
            doError("Unexpected Error", "There was an unexpected error!");
            e.printStackTrace();
        }
    }

    /**
     * Convenience method for setting popup errors
     */
    public void doError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
