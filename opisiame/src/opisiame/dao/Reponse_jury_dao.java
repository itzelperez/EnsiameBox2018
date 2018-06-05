/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import opisiame.database.Connection_db;


public class Reponse_jury_dao {

    public void insert_rep_jury(String option_vote, Integer idPoll, String xbee_address_mac) {
        String SQL = "INSERT INTO VOTES (option_vote, idPoll, address_mac) VALUES (?,?,?)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, option_vote);
            ps.setInt(2, idPoll);
            ps.setString(3, xbee_address_mac);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création de la réponse, aucune ligne ajoutée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
