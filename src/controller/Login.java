package controller;

import dao.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.User;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Login controller class
 */
public class Login {

    /**
     * The login screen title label
     */
    @FXML
    public Label titleLabel;

    /**
     * The username label
     */
    @FXML
    public Label usernameLabel;

    /**
     * The username text field
     */
    @FXML
    public TextField usernameField;

    /**
     * The password label
     */
    @FXML
    public Label passwordLabel;

    /**
     * The password text field
     */
    @FXML
    public TextField passwordField;

    /**
     * The location label
     */
    @FXML
    public Label locationLabel;

    /**
     * The location value
     */
    @FXML
    public Label locationValueLabel;

    /**
     * The login button
     */
    @FXML
    public Button loginButton;

    /**
     * The error messages label
     */
    @FXML
    public Label loginErrorMessages;

    /**
     * Initializes the resource bundle
     */
    private ResourceBundle resourceBundle;

    /**
     * Initializes the login view and updates labels based on the locale.
     */
    @FXML
    public void initialize() {

        // Set to French for testing labels
        //Locale locale = new Locale("fr");
        //Locale.setDefault(locale);

        resourceBundle = ResourceBundle.getBundle("i18n/i18n", Locale.getDefault());

        titleLabel.setText(resourceBundle.getString("login_title"));
        locationValueLabel.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        loginButton.setText(resourceBundle.getString("login"));
    }

    /**
     * Handles the login button click
     *
     * @param event Button click ActionEvent
     */
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {

        clearLoginError();

        if (!doValidate()) return;

        Optional<User> optional = new UserDaoImpl().getByUsername("admin");

        if (optional.isPresent()) {

            User user = optional.get();

            if (Objects.equals(user.getPassword(), passwordField.getText())) {
                writeLoginActivity("Successful login for user '" +  usernameField.getText() + "'");
                Utils.switchScenes(event, "appointments");
            } else {
                setLoginError("invalid_credentials");
            }

        } else {
            setLoginError("invalid_credentials");
        }
    }

    /**
     * Checks the both a username and password were provided
     *
     * @return Boolean
     */
    private Boolean doValidate() {

        if (usernameField.getText().isEmpty()) {
            setLoginError("username_required");
            usernameLabel.setTextFill(Color.RED);
            return false;
        } else if (passwordField.getText().isEmpty()) {
            setLoginError("password_required");
            passwordLabel.setTextFill(Color.RED);
            return false;
        }

        return true;
    }

    /**
     * Handles logging all login activity
     *
     * @return void
     */
    private void writeLoginActivity(String message) {
        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true)) {
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = new Date(System.currentTimeMillis());
            fileWriter.write(sdt.format(dt) + " " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the approrpiate login erorr on the login screen
     *
     * @return void
     */
    private void setLoginError(String key) {
        loginErrorMessages.setText((resourceBundle.getString(key)));
        writeLoginActivity("Failed login attempt for user '" +  usernameField.getText() + "'");
    }

    /**
     * Clears all login errors
     *
     * @return void
     */
    private void clearLoginError() {
        loginErrorMessages.setText("");
        usernameLabel.setTextFill(Color.BLACK);
        passwordLabel.setTextFill(Color.BLACK);
    }
}


