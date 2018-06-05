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


public class Voting_poll_dao {

    public Integer insert_voting_poll(java.sql.Date date_creation, Integer number_of_participants, String motifDuVote) {
        String SQL = "INSERT INTO VOTING_POLL (date_creation, number_of_participants, motif_du_vote) VALUES (?,?,?)";
        Integer key = 0;
        System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()");
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()AEEEEEEEEE");
            ps.setDate(1, date_creation);
            System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()JESUS");
            ps.setInt(2, number_of_participants);
            System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()PORRA");
           
            ps.setString(3, motifDuVote);
            System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()UHUUUUUU");
            int succes = ps.executeUpdate();
            System.out.println("opisiame.dao.Voting_poll_dao.insert_voting_poll()DEUS");
            if (succes == 0) {
                System.err.println("Échec de la création du vote, aucune ligne ajoutée dans la table.");
            } else {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    key = rs.getInt(1);
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
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
