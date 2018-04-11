/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Sandratra
 */
public class Participation_quiz {
    Integer id;
    Integer quiz_id;
    Timestamp date_participation;
    List<Integer> participants;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Timestamp getDate_participation() {
        return date_participation;
    }

    public void setDate_participation(Timestamp date_participation) {
        this.date_participation = date_participation;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Integer> participants) {
        this.participants = participants;
    }

    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
