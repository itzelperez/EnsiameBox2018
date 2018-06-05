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
import java.sql.Date;
import java.sql.Statement;


public class Voting_poll_Participant_dao {

    public void insert_voting_poll_Participant(Integer Part_id, Integer idPoll) {
        String SQL = "INSERT INTO VOTING_POLL_PARTICIPANT (Part_id, idPoll) VALUES (?,?)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, Part_id);
            ps.setInt(2, idPoll);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création du vote, aucune ligne ajoutée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void get_voting_poll(java.sql.Date date_creation, Integer number_of_participants) {
        String SQL = "SELECT last_inserted_id()";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, date_creation);
            ps.setInt(2, number_of_participants);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création du vote, aucune ligne ajoutée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
