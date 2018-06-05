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

/**
 *
 * @author Sandratra
 */
public class Reponse_participation_dao {

    public void insert_rep_participation(Integer Rep_id, Integer Participation_id) {
        String SQL = "INSERT INTO reponse_participant_quiz (Rep_id, PRarticipation_id) VALUES (?,?)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, Rep_id);
            ps.setInt(2, Participation_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création de la question, aucune ligne ajoutée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
