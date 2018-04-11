/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import opisiame.model.Participant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import opisiame.database.Connection_db;
import opisiame.model.Reponse;

/**
 *
 * @author Sandratra
 */
public class Reponse_dao {

    public Reponse_dao() {
    }

    public void insert_new_reponse(Reponse rep) {
        String SQL = "INSERT INTO reponse (Rep_libelle, Rep_bonne,Quest_id) VALUES (?,?,?)";
        Integer insert_id = null;
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, rep.getLibelle());
            ps.setInt(2, rep.getIs_bonne_reponse());
            ps.setInt(3, rep.getQuest_id());
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création de la réponse, aucune ligne ajoutée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update_reponse(Reponse rep) {
        String SQL = "UPDATE reponse SET Rep_libelle = ?, Rep_bonne = ? WHERE Rep_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, rep.getLibelle());
            ps.setInt(2, rep.getIs_bonne_reponse());
            ps.setInt(3, rep.getId());
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la modification de la réponse, aucune ligne modifiée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> get_id_rep_for_quest(Integer quest_id) {
        ArrayList<Integer> reponses = new ArrayList<>();
        String SQL = "SELECT Rep_id  FROM reponse WHERE Quest_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, quest_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reponses.add(rs.getInt(1));
                System.out.println("id : " + rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reponses;
    }

    public ArrayList<Reponse> get_reponses_by_quest(Integer quest_id) {
        ArrayList<Reponse> reponses = new ArrayList<>();
        String SQL = "SELECT *  FROM reponse WHERE Quest_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, quest_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse rep = new Reponse();
                rep.setId(rs.getInt(1));
                rep.setLibelle(rs.getString(2));
                rep.setIs_bonne_reponse(rs.getInt(3));
                rep.setQuest_id(rs.getInt(4));
                reponses.add(rep);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reponses;
    }

    
    public ArrayList<Reponse> get_reponses_by_participationID(Integer part_id) {
        ArrayList<Reponse> reponses = new ArrayList<>();
        String SQL = "SELECT Rep_id FROM reponse_participant_quiz WHERE Participation_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, part_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse rep = new Reponse();
                rep.setId(rs.getInt(1));
                reponses.add(rep);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reponses;
    }

    // récupère les participants qui ont choisi la réponse (en paramètre) pour une date de participation donnée
    public ArrayList<Participant> get_repondant_rep(Integer rep_id, Timestamp date_participation) {
        ArrayList<Participant> participants = new ArrayList<>();
        String SQL = "SELECT * FROM participant "
                + "JOIN participant_quiz "
                + "ON participant_quiz.Part_id = participant.Part_id "
                + "JOIN reponse_participant_quiz "
                + "ON reponse_participant_quiz.Participation_id = participant_quiz.Participation_id "
                + "WHERE reponse_participant_quiz.Rep_id = ? "
                + "AND CAST(participant_quiz.Date_participation AS CHAR) =  ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, rep_id);
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dt = date_format.format(date_participation);
            ps.setString(2, dt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Participant p = new Participant();
                p.setPart_id(rs.getInt(1));
                p.setPart_nom(rs.getString(2));
                p.setPart_prenom(rs.getString(3));
                p.setFiliere_id(rs.getInt(4));
                participants.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return participants;
    }

    public ArrayList<Reponse> get_reponses_eleve(Integer part_id) {
        ArrayList<Reponse> reponses = new ArrayList<>();
        String SQL = "SELECT RPQ.Rep_id, R.quest_id"
                + " FROM reponse_participant_quiz RPQ"
                + " JOIN reponse R ON R.Rep_id = RPQ.Rep_id"
                + " WHERE RPQ.Participation_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, part_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse rep = new Reponse();
                rep.setId(rs.getInt(1));
                rep.setQuest_id(rs.getInt(2));
                reponses.add(rep);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reponses;
    }

}
