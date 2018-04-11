/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.prof;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import opisiame.database.Connection_db;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Delete_allController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void Confirmer() throws IOException {
        try {
            Connection connection = Connection_db.getDatabase();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM enseignant");
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
