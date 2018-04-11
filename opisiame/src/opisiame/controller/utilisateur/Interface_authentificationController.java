/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.utilisateur;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import opisiame.database.Connection_db;
import session.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author clement
 */
public class Interface_authentificationController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextField Login_field;
    @FXML
    private PasswordField Passwd_field;
    @FXML
    private Label Message_field;
    @FXML
    private Polygon Submit_field;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Login_field.requestFocus();
        Login_field.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                    Passwd_field.setFocusTraversable(true);
                    Passwd_field.requestFocus();
                }
            }
        });
        Passwd_field.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    Submit_passwd();
                }
            }
        });
    }

    public static String md5(String input) {

        String md5 = null;

        if (null == input) {
            return null;
        }

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private int lecture_admin() throws SQLException {
        final String log = Login_field.getText();
        final String pass = md5(Passwd_field.getText());
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int ok1 = 0;
        int ok2 = 0;
        int ok3 = 0;
        int ok = 0;

        //vérification du login
        //connection avec la base de donnée
        Connection database = Connection_db.getDatabase();
        PreparedStatement pslog = database.prepareStatement("SELECT COUNT(*) AS total FROM administrateur WHERE Admin_login = ? and Admin_mdp = ?");
        pslog.setString(1, log);
        pslog.setString(2, pass);
        ResultSet logres = pslog.executeQuery();
        while (logres.next()) {
            count1 = logres.getInt("total");
        }
        if (count1 == 1) {
            ok1 = 1;
        }

        PreparedStatement pslog2 = database.prepareStatement("SELECT COUNT(*) AS total2 FROM enseignant WHERE Ens_login = ? and Ens_mdp = ?");
        pslog2.setString(1, log);
        pslog2.setString(2, pass);
        ResultSet logres2 = pslog2.executeQuery();
        while (logres2.next()) {
            count2 = logres2.getInt("total2");
        }
        if (count2 == 1) {
            ok2 = 2;
        }

        PreparedStatement pslog3 = database.prepareStatement("SELECT COUNT(*) AS total3 FROM animateur WHERE Anim_login = ? and Anim_mdp = ?");
        pslog3.setString(1, log);
        pslog3.setString(2, pass);
        ResultSet logres3 = pslog3.executeQuery();
        while (logres3.next()) {
            count3 = logres3.getInt("total3");
        }
        if (count3 == 1) {
            ok3 = 3;
        }

        if ((ok1 != 0 && ok2 != 0) || (ok1 != 0 && ok3 != 0) || (ok2 != 0 && ok3 != 0)) {
            ok = 0;
        } else {
            ok = ok1 + ok2 + ok3;
        }

        return ok;
    }

    @FXML
    public void Submit_passwd() {

        try {
            switch (lecture_admin()) {
                case 1: // ADMINISTRATEUR
                {
                    final String log = Login_field.getText();
                    Session login = new Session(log, "admin");
                    Session.setType("admin");
                    Stage stage = (Stage) content.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                    break;
                }
                case 2: // ENSEIGNANT
                {
                    final String log = Login_field.getText();
                    Session login = new Session(log, "ens");
                    Session.setUser_id(get_ens_id(log));
                    Session.setType("ens");
                    Stage stage = (Stage) content.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_ens.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                    break;
                }
                case 3: //ANIMATEUR
                {
                    final String log = Login_field.getText();
                    Session login = new Session(log, "anim");
                    Session.setUser_id(get_anim_id(log));
                    Session.setType("anim");
                    Stage stage = (Stage) content.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_anim.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                    break;
                }
                default:// ERREUR LOGIN
                    Message_field.setText("erreur d'authentification");
                    Message_field.setStyle("-fx-font-weight: bold; -fx-text-fill : #f00");
                    break;
            }
        } catch (SQLException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error lors de la connexion");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la connexion avec la base de données");
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    Integer get_anim_id(String log) {
        Integer anim_id = null;
        Connection connexion = null;
        PreparedStatement ps;
        try {
            Connection connection = Connection_db.getDatabase();
            ps = connection.prepareStatement("SELECT * FROM animateur WHERE Anim_login = ?");
            ps.setString(1, log);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                anim_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anim_id;
    }

    Integer get_ens_id(String log) {
        Integer anim_id = null;
        Connection connexion = null;
        PreparedStatement ps;
        try {
            Connection connection = Connection_db.getDatabase();
            ps = connection.prepareStatement("SELECT * FROM enseignant WHERE Ens_login = ?");
            ps.setString(1, log);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                anim_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anim_id;
    }
}
