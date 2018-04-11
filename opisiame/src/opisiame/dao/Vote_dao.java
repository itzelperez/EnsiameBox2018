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
import java.sql.Statement;
import java.sql.Types;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Vote;
import session.Session;

/**
 *
 * @author itzel
 */
public class Vote_dao {

    public Vote_dao() {
    }

    public Vote get_vote_by_id(Integer vote_id) {
        Vote vote = new Vote();
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM vote WHERE Vote_id = ? ");
            ps.setInt(1, vote_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vote.setId(rs.getInt(1));
                vote.setNom(rs.getString(2));
                vote.setDate_creation(rs.getString(3));
                vote.setTimer(rs.getInt(4));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vote;
    }
    
    public Integer count_nb_quest(Integer vote_id) {
        try {
            int nb_quest = 0;
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM question WHERE Vote_id = ? ");
            ps.setInt(1, vote_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nb_quest = rs.getInt(1);
            }
            return nb_quest;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void delete_vote(Integer id){
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM vote WHERE Vote_id = ?");
            ps.setInt(1, id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Erreur lors de la suppression du vote");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ObservableList<Vote> getAllvote() {
        ObservableList<Vote> votes = FXCollections.observableArrayList();
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps;
            String SQL;
            switch (Session.getType()) {
                case "ens":
                    SQL = "SELECT * FROM vote WHERE Ens_id = ?";
                    ps = connection.prepareStatement(SQL);
                    ps.setInt(1, Session.getUser_id());
                    break;
                case "anim":
                    SQL = "SELECT * FROM vote JOIN animateur_vote ON animateur_vote.Vote_id = vote.Vote_id WHERE animateur_vote.Anim_id = ?";
                    ps = connection.prepareStatement(SQL);
                    ps.setInt(1, Session.getUser_id());
                    break;
                default:
                    SQL = "SELECT * FROM vote";
                    ps = connection.prepareStatement(SQL);
                    break;
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vote vote = new Vote();
                vote.setId(rs.getInt(1));
                vote.setNom(rs.getString(2));
                vote.setDate_creation(rs.getString(3));
                vote.setTimer(rs.getInt(4));
                votes.add(vote);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return votes;
    }
    
    public ObservableList<Vote> search_vote_sql(String str){
        ObservableList<Vote> votes = FXCollections.observableArrayList();
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM vote WHERE Vote_id LIKE ? OR Vote_nom LIKE ? OR Vote_timer LIKE ? OR CAST(Vote_date_creation AS CHAR) LIKE ?");
            ps.setString(1, "%"+str+"%");
            ps.setString(2, "%"+str+"%");
            ps.setString(3, "%"+str+"%");
            ps.setString(4, "%"+str+"%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vote vote = new Vote();
                vote.setId(rs.getInt(1));
                vote.setNom(rs.getString(2));
                vote.setDate_creation(rs.getString(3));
                vote.setTimer(rs.getInt(4));
                votes.add(vote);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return votes;
    }
    
    public void update_vote(Integer vote_id,String value_nom, String value_timer) {
        String SQL;
        if (value_timer.compareTo("") != 0) {
            SQL = "UPDATE vote SET Vote_nom = ?, Vote_timer = ? WHERE Vote_id = ?";
        } else {
            SQL = "UPDATE vote SET Vote_nom = ? WHERE Vote_id = ?";
        }
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            Integer num = 1;
            ps.setString(num++, value_nom);
            if (value_timer.compareTo("") != 0) {
                ps.setInt(num++, Integer.valueOf(value_timer));
            }
            ps.setInt(num, vote_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la modification du vote, aucune ligne modifiée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Integer insert_new_vote(String value_nom, String value_timer) {
        String SQL;
        Integer insert_id = null;
        if (value_timer.compareTo("") != 0) {
            SQL = "INSERT INTO vote (Vote_nom,Vote_timer,Ens_id) VALUES (?,?,?)";
        } else {
            SQL = "INSERT INTO vote (Vote_nom,Ens_id) VALUES (?,?)";
        }
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            Integer num = 1;
            ps.setString(num++, value_nom);
            if (value_timer.compareTo("") != 0) {
                ps.setInt(num++, Integer.valueOf(value_timer));
            } 
            // à enlever après test :D
            if (Session.getUser_id() == null) {
               ps.setNull(num, Types.INTEGER);
            } else {
                ps.setInt(num, Session.getUser_id());
            }
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création du vote, aucune ligne ajoutée dans la table.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                insert_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert_id;
    }
    
}



    

