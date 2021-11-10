package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.JDBC;


//import model.*;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <pre>
 * Schedule Keeper application main entry point
 *
 * @author William Gunn
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/i18n", Locale.getDefault());
        
        stage.setTitle(resourceBundle.getString("app_title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}