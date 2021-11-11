package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Utils {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/i18n", Locale.getDefault());

    /**
     * Convenience method to handle switching scenes
     *
     * @param event Button click ActionEvent
     * @param view  The name of the view to load
     * @return void
     */
    public static void switchScenes(ActionEvent event, String view) {

        boolean error = false;

        try {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(Utils.class.getResource("/view/" + view + ".fxml"));
            screen.setTitle(resourceBundle.getString("app_title"));
            screen.setScene(new Scene(scene));
            screen.show();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }

        if (error) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unexpected Error");
            alert.setContentText("There was an unexpected error!");
            alert.showAndWait();
        }
    }
}
