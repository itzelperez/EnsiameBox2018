/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Participant;
import opisiame.model.Participation_vote;
import opisiame.model.Vote;

/**
 *
 * @author itzel
 */
public class Participation_vote_dao {

    public static Integer insert_participation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        Vote_dao vote_dao = new Vote_dao();


    public ObservableList<Timestamp> get_dates_participations(Integer vote_id, ObservableList<Participation_vote> participation_votes) {
        ObservableList<Timestamp> dates = FXCollections.observableArrayList();
        for (Participation_vote participation_vote : participation_votes) {
            if (Objects.equals(participation_vote.getVote_id(), vote_id)) {
                if (!dates.contains(participation_vote.getDate_participation())) {
                    dates.add(participation_vote.getDate_participation());
                }
            }
        }
        return dates;
    }

    public Participation_vote get_part_vote(String d, Integer i, ObservableList<Participation_vote> participation_votes) {
        for (Participation_vote participation_vote : participation_votes) {
            if ((participation_vote.getDate_participation().toString().equals(d)) && Objects.equals(participation_vote.getVote_id(), i)) {
                return participation_vote;
            }
        }
        return null;
    }

    public ObservableList<Participation_vote> get_participation_votes(int ens_id) {
        ObservableList<Participation_vote> participation_votes = FXCollections.observableArrayList();
//        String SQL = "SELECT DISTINCT(Date_participation), Vote_id, Participation_id "
//                + "FROM participant_quiz";
        String SQL = "SELECT DISTINCT(PQ.Date_participation), PQ.Vote_id, PQ.Participation_id "
                + "FROM participant_quiz PQ JOIN quiz Q ON PQ.Vote_id = Q.Vote_id "
                + "WHERE Q.Ens_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, ens_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Participation_vote participation_vote = new Participation_vote();
                participation_vote.setId(rs.getInt(3));
                participation_vote.setDate_participation(rs.getTimestamp(1));
                participation_vote.setVote_id(rs.getInt(2));
                List<Integer> liste_parts = get_participants_votes(participation_vote);
                participation_vote.setParticipants(liste_parts);
                participation_votes.add(participation_vote);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return participation_votes;
    }

    public ObservableList<Participant> get_participants(int vote_id, Timestamp t) {
        ObservableList<Participant> participants_quiz = FXCollections.observableArrayList();
        String SQL = "SELECT DISTINCT participant_quiz.Part_id, participant.Part_nom, participant.Part_prenom "
                + "FROM participant_quiz JOIN participant "
                + "ON participant_quiz.Part_id = participant.Part_id "
                + "WHERE Vote_id = ? AND Date_participation = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, vote_id);
            ps.setTimestamp(2, t);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Participant parti = new Participant();
                parti.setPart_id(rs.getInt(1));
                parti.setPart_nom(rs.getString(2));
                parti.setPart_prenom(rs.getString(3));
                participants_quiz.add(parti);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return participants_quiz;
    }

        public List<Integer> get_participation_id(int vote_id, String t) {
        List<Integer> liste_participants = new ArrayList<>();
        String SQL = "SELECT Participation_id FROM participant_quiz WHERE Date_participation = ? AND Vote_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, t);
            ps.setInt(2, vote_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste_participants.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste_participants;
    }
    
    public List<Integer> get_participants_votes(Participation_vote participation_vote) {
        List<Integer> liste_participants = new ArrayList<>();
        String SQL = "SELECT Part_id FROM participant_quiz WHERE Date_participation = ? AND Vote_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setTimestamp(1, participation_vote.getDate_participation());
            //ps.setInt(2, participation_votes.getVote_id());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste_participants.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste_participants;
    }

    public void delete_participation(int participation_id) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM participant_quiz WHERE Participation_id = ?");
            ps.setInt(1, participation_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Erreur lors de la suppression de la participation");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public Integer insert_participation(int vote_id, Timestamp t) {
        Integer insert_id = null;
        try {
            String SQL = "INSERT INTO participant_quiz (Date_participation, Vote_id, Part_id) VALUES (?,?,?)";
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, t);
            ps.setInt(2, vote_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création de la question, aucune ligne ajoutée dans la table.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                insert_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Participation_vote_dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insert_id;
    }

    public int get_part_id(int id, int vote_id, Timestamp t) {
        int part_id = 0;
        String SQL = "SELECT Participation_id "
                + "FROM participant_quiz "
                + "WHERE Vote_id = ? AND Part_id = ? AND Date_participation = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, vote_id);
            ps.setInt(2, id);
            ps.setTimestamp(3, t);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                part_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return part_id;
    }

    public void delete_participation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}



