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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Animateur;


/**
 *
 * @author Sandratra
 */
public class Animateur_dao {

    public Animateur_dao() {
    }
       
    public ObservableList<Animateur> get_animateurs_not_in_quiz(Integer quiz_id) {
        ObservableList<Animateur> animateurs = FXCollections.observableArrayList();
        String SQL = "SELECT *  FROM animateur WHERE Anim_id NOT IN (SELECT Anim_id FROM animateur_quiz WHERE Quiz_id = ?)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Animateur anim = new Animateur();
                anim.setId(rs.getInt(1));
                anim.setNom(rs.getString(2));
                animateurs.add(anim);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animateurs;
    }
    
    public void remove_from_anim_quiz(Integer anim_id, Integer quiz_id){
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM animateur_quiz WHERE Anim_id = ? AND Quiz_id = ?");
            ps.setInt(1, anim_id);
            ps.setInt(2, quiz_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Erreur lors de la suppression");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insert_in_anim_quiz(Integer anim_id, Integer quiz_id){
        String SQL = "INSERT INTO animateur_quiz (Anim_id, Quiz_id) VALUES (?,?)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, anim_id);
            ps.setInt(2, quiz_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de l'insertion dans animateur_quiz");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<Animateur> get_animateurs_in_quiz(Integer quiz_id) {
        ObservableList<Animateur> animateurs = FXCollections.observableArrayList();
        String SQL = "SELECT animateur.Anim_id, animateur.Anim_nom FROM animateur_quiz JOIN animateur ON animateur.Anim_id = animateur_quiz.Anim_id  WHERE animateur_quiz.Quiz_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Animateur anim = new Animateur();
                anim.setId(rs.getInt(1));
                anim.setNom(rs.getString(2));
                animateurs.add(anim);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animateurs;
    }
}
