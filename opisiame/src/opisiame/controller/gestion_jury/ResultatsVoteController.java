/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;

import java.net.URL;
import javafx.fxml.Initializable;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Point;
import java.io.File;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opisiame.database.Connection_db;
import opisiame.model.Rep_eleves_quiz;
import opisiame.model.Reponse_question;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import opisiame.model.Vote;
import opisiame.dao.*;
import java.util.Vector;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;


/**
 * FXML Controller class
 *
 * @author itzel
 */
public class ResultatsVoteController implements Initializable {

  @FXML
    AnchorPane content;
  
  private Integer oui = 0, non = 0, blanc = 0, non_concerne = 0, total = 0;
  
  
  @FXML
    PieChart ResultsVote;
  
  @FXML
  private Label yes; 
  
  @FXML
  private Label nein; 
  
  @FXML
  private Label blanco; 
  
  @FXML
  private Label nc; 
  
  private Integer idPoll;
  
  private Stage stage;
  
  public void setStage(Stage stage){
      this.stage = stage;
  }
  
  public void setIdPoll(Integer idPoll){
      this.idPoll = idPoll;
  }

    ObservableList<Reponse_question> reponse_questions;
    ObservableList<Rep_eleves_quiz> resultats_voters;
    String onglet_actif;

    public void setReponse_questions(ObservableList<Reponse_question> reponse_questions) {
        this.reponse_questions = reponse_questions;
    }

    public void setResultatsEleves(ObservableList<Rep_eleves_quiz> res_eleves) {
        this.resultats_voters = res_eleves;
    }

    public void setOngletActif(String onglet) {
        this.onglet_actif = onglet;
    }

    @FXML
    public void excel_export() {

        File excel_file = choix_chemin_enregistrement("Excel files (*.xls)", "*.xls");

            if (excel_file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Resultat du Vote");
                sheet.autoSizeColumn(5);
                //create_data1(sheet, 0, "Question", "Pourcentage reponse A", "Pourcentage reponse B", "Pourcentage reponse C", "Pourcentage reponse D", "Pourcentage bonne réponse");
                
                createHeader(sheet);
                Row row = sheet.getRow(0);
                HSSFCellStyle cellStyle = null;
                HSSFFont font = wb.createFont();
                font.setBold(true);
                cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                row.setRowStyle(cellStyle);
                
                
                String date = "", motifDuVote = "";
                try {
                    Connection connection = Connection_db.getDatabase();
                    PreparedStatement ps;
                    ps = connection.prepareStatement("SELECT date_creation, motif_du_vote "
                        + "FROM voting_poll \n"
                        + "WHERE idPoll = ?\n");
                    ps.setInt(1, idPoll);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        date = rs.getString(1);
                        motifDuVote = rs.getString(2);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                
                createData2Columns(sheet, 1, "Date (aaaa-mm-jj)", date);
                createData2Columns(sheet, 2, "ID du Vote", idPoll.toString());
                createData2Columns(sheet, 3, "Motif du Vote", motifDuVote);
                
                int rowl = createDataStudents(sheet, 4);
                createDataResultats(sheet, rowl, wb);
                
                
                
                
                /*
                for (int i = 0; i < reponse_questions.size(); i++) {
                    Reponse_question rq = reponse_questions.get(i);
                    create_data1(sheet, i + 1, rq.getQuestion(), rq.getStr_pourcentage_rep_a(), rq.getStr_pourcentage_rep_b(), rq.getStr_pourcentage_rep_c(), rq.getStr_pourcentage_rep_d(), rq.getStr_pourcentage());
                }
*/
                FileOutputStream fileOut;
                try {
                    fileOut = new FileOutputStream(excel_file);
                    wb.write(fileOut);
                    fileOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        
    }
    
    
    public void createHeader(HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellHeader = row.createCell(0);
        cellHeader.setCellValue("ENSIAME - Resultat du Vote");
    }
    
    public void createData2Columns(HSSFSheet sheet, Integer rowLine, String title, String data){
        HSSFRow row = sheet.createRow(rowLine);
        HSSFCell cellDateTitle = row.createCell(0);
        
        cellDateTitle.setCellValue(title);
        
        HSSFCell cellDate = row.createCell(1);
        cellDate.setCellValue(data);
    }
    
    public int createDataStudents(HSSFSheet sheet, Integer rowLine){
        HSSFRow rowTitle = sheet.createRow(rowLine);
        
        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("Elève(s) concerné(es)");
        
        HSSFRow rowColumnTitles = sheet.createRow(rowLine++);
        
        HSSFCell cellMatricule = rowColumnTitles.createCell(0);
        cellMatricule.setCellValue("Matricule");
        HSSFCell cellNom = rowColumnTitles.createCell(1);
        cellNom.setCellValue("Nom");
        HSSFCell cellPrenom = rowColumnTitles.createCell(2);
        cellPrenom.setCellValue("Prenom");
        HSSFCell cellFiliere = rowColumnTitles.createCell(3);
        cellFiliere.setCellValue("Filière");
        HSSFCell cellAnnee = rowColumnTitles.createCell(4);
        cellAnnee.setCellValue("Année");
        
        try {
                    Connection connection = Connection_db.getDatabase();
                    PreparedStatement ps;
                    ps = connection.prepareStatement("SELECT participant.part_id, part_Nom, part_Prenom, filiere, annee "
                            + "FROM `filiere`"
                            + " LEFT JOIN participant"
                            + " ON participant.Filiere_id = filiere.Filiere_id"
                            + " JOIN voting_poll_participant "
                            + "ON voting_poll_participant.part_id = participant.part_id "
                            + "WHERE idPoll = ?");
                    ps.setInt(1, idPoll);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        
                        HSSFRow rowColumns = sheet.createRow(rowLine++);
                        Integer matricule = rs.getInt(1);
                        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.createDataStudents() " + matricule);
                        HSSFCell cellMatriculeValue = rowColumns.createCell(0);
                        cellMatriculeValue.setCellValue(matricule);
                        
                        String nom = rs.getString(2);
                        HSSFCell cellNomValue = rowColumns.createCell(1);
                        cellNomValue.setCellValue(nom);
                        
                        String prenom = rs.getString(3);
                        HSSFCell cellPrenomValue = rowColumns.createCell(2);
                        cellPrenomValue.setCellValue(prenom);
                        
                        String filiere = rs.getString(4);
                        HSSFCell cellFiliereValue = rowColumns.createCell(3);
                        cellFiliereValue.setCellValue(filiere);
                        
                        Integer annee = rs.getInt(5);
                        HSSFCell cellAnneeValue = rowColumns.createCell(4);
                        cellAnneeValue.setCellValue(annee);
        
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
        return rowLine;
    }
    
    
    public void createDataResultats(HSSFSheet sheet, Integer rowLine, HSSFWorkbook wb){
        HSSFRow rowTitle = sheet.createRow(rowLine);
        
        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("Résultats");
        
        HSSFRow rowColumnTitles = sheet.createRow(++rowLine);
        
        HSSFCell cellOui = rowColumnTitles.createCell(0);
        cellOui.setCellValue("Oui");
        HSSFCell cellNon = rowColumnTitles.createCell(1);
        cellNon.setCellValue("Non");
        HSSFCell cellBlanc = rowColumnTitles.createCell(2);
        cellBlanc.setCellValue("Blanc");
        HSSFCell cellNonConcerne = rowColumnTitles.createCell(3);
        cellNonConcerne.setCellValue("Non Concerné");
        
        rowColumnTitles = sheet.createRow(++rowLine);
        
        cellOui = rowColumnTitles.createCell(0);
        cellOui.setCellValue(oui);
        cellNon = rowColumnTitles.createCell(1);
        cellNon.setCellValue(non);
        cellBlanc = rowColumnTitles.createCell(2);
        cellBlanc.setCellValue(blanc);
        cellNonConcerne = rowColumnTitles.createCell(3);
        cellNonConcerne.setCellValue(non_concerne);
        
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("0.00%"));
        
        rowColumnTitles = sheet.createRow(++rowLine);
        
        cellOui = rowColumnTitles.createCell(0);
        System.out.println("oui " + oui + " total " + total + " ouiii" + (100.0 * oui)/total);
        
        cellOui.setCellValue((total > 0) ?  ((1.0 * oui)/total) : 0.0);
        cellOui.setCellStyle(style);
        cellOui.setCellType(CellType.NUMERIC);
        cellNon = rowColumnTitles.createCell(1);
        cellNon.setCellValue((total > 0) ? ((1.0 * non)/total) : 0.0);
        cellNon.setCellStyle(style);
        cellBlanc = rowColumnTitles.createCell(2);
        cellBlanc.setCellValue((total > 0) ? ((1.0 * blanc)/total) : 0.0);
        cellBlanc.setCellStyle(style);
        cellNonConcerne = rowColumnTitles.createCell(3);
        cellNonConcerne.setCellValue((total > 0) ? ((1.0 * non_concerne)/total) : 0.0);
        cellNonConcerne.setCellStyle(style);
    }
    

    public void create_data1(HSSFSheet sheet, Integer i, String question, String rep_a, String rep_b, String rep_c, String rep_d, String pourcentage) {
        HSSFRow row = sheet.createRow(i); // ligne i

        HSSFCell cell_question = row.createCell((short) 0); // colonne 0
        cell_question.setCellValue(question);

        HSSFCell cell_rep_a = row.createCell((short) 1); // colonne 1
        cell_rep_a.setCellValue(rep_a);

        HSSFCell cell_rep_b = row.createCell((short) 2); // colonne 2
        cell_rep_b.setCellValue(rep_b);

        HSSFCell cell_rep_c = row.createCell((short) 3); // colonne 3
        cell_rep_c.setCellValue(rep_c);

        HSSFCell cell_rep_d = row.createCell((short) 4); // colonne 4
        cell_rep_d.setCellValue(rep_d);

        HSSFCell cell_pourcentage = row.createCell((short) 5); // colonne 5
        cell_pourcentage.setCellValue(pourcentage);
    }

    public File choix_chemin_enregistrement(String description, String extension) {
        //content.getScene().getWindow();
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 0");
        //Stage stage = (Stage) content.getScene().getWindow();
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 1");
        FileChooser fileChooser = new FileChooser();
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 2");
        fileChooser.setTitle("Choix d'enregistrement du fichier");
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 3");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 4");
        fileChooser.getExtensionFilters().add(extFilter);
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 5");
        File selected_directory = fileChooser.showSaveDialog(stage);
        System.out.println("opisiame.controller.gestion_jury.ResultatsVoteController.choix_chemin_enregistrement() 6");
        //System.out.println("file : " + selected_directory.getAbsolutePath());
        return selected_directory;
    }

    @FXML
    public void pdf_export() {
        File pdf_file = choix_chemin_enregistrement("PDF files (*.pdf)", "*.pdf");

            if (pdf_file != null) {
                Document document = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
                    document.open();
                    document.add(new Paragraph("Résultats Vote"));
                    
                    
                    String date = "", motifDuVote = "";
                    try {
                        Connection connection = Connection_db.getDatabase();
                        PreparedStatement ps;
                        ps = connection.prepareStatement("SELECT date_creation, motif_du_vote "
                            + "FROM voting_poll \n"
                            + "WHERE idPoll = ?\n");
                        ps.setInt(1, idPoll);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()){
                            date = rs.getString(1);
                            motifDuVote = rs.getString(2);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                
                    document.add(new Paragraph("Date (aaaa-mm-jj): " + date));
                    document.add(new Paragraph("ID du Vote: " + idPoll.toString()));
                    document.add(new Paragraph("Motif du Vote: " + motifDuVote));
                
                    create_table_students_pdf(document);
                    create_table_votes_pdf(document);
                    
                } catch (DocumentException | IOException de) {
                    de.printStackTrace();
                }
                
                document.close();
            }
        close_window();
    }
    
    public void create_table_students_pdf(Document document) throws DocumentException, BadElementException {
                    
                    document.add(new Paragraph("Elève(s) concerné(es): "));
                    Table table = new Table(1,1);
                    try {
                        Connection connection = Connection_db.getDatabase();
                        PreparedStatement ps;
                        ps = connection.prepareStatement("SELECT COUNT(participant.part_id)"
                                + "FROM `filiere`"
                                + " LEFT JOIN participant"
                                + " ON participant.Filiere_id = filiere.Filiere_id"
                                + " JOIN voting_poll_participant "
                                + "ON voting_poll_participant.part_id = participant.part_id "
                                + "WHERE idPoll = ?");
                        ps.setInt(1, idPoll);
                        ResultSet rs = ps.executeQuery();
                        Integer numberOfStudents = 0;
                        if (rs.next())
                            numberOfStudents = rs.getInt(1);
                        
                        
                        table = new Table(5, numberOfStudents);
                        table.setAutoFillEmptyCells(true);
                        table.setPadding(2);
                        
                                            
                        Cell cell = new Cell("Matricule");
                        cell.setHeader(true);
                        table.addCell(cell);
                        
                        cell = new Cell("Nom");
                        cell.setHeader(true);
                        table.addCell(cell);
                    
                        cell = new Cell("Prénom");
                        cell.setHeader(true);
                        table.addCell(cell);


                        cell = new Cell("Filiere");
                        cell.setHeader(true);
                        table.addCell(cell);
                        
                        cell = new Cell("Annee");
                        cell.setHeader(true);
                        table.addCell(cell);

                        table.endHeaders();
                        table.setWidth(100);
                        
                        
                        ps = connection.prepareStatement("SELECT participant.part_id, part_Nom, part_Prenom, filiere, annee "
                                + "FROM `filiere`"
                                + " LEFT JOIN participant"
                                + " ON participant.Filiere_id = filiere.Filiere_id"
                                + " JOIN voting_poll_participant "
                                + "ON voting_poll_participant.part_id = participant.part_id "
                                + "WHERE idPoll = ?");
                        ps.setInt(1, idPoll);
                        rs = ps.executeQuery();
                        Integer counter = 0;
                        while (rs.next()){
                            Integer matricule = rs.getInt(1);
                            table.addCell(matricule.toString(), new Point(counter + 1, 0));
                        
                            String nom = rs.getString(2);
                            table.addCell(nom, new Point(counter + 1, 1));
                        
                            String prenom = rs.getString(3);
                            table.addCell(prenom, new Point(counter + 1, 2));
                        
                            String filiere = rs.getString(4);
                            table.addCell(filiere, new Point(counter + 1, 3));
                        
                            Integer annee = rs.getInt(5);
                            table.addCell(annee.toString(), new Point(counter + 1, 4));
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    document.add(table);
    }
    
    
    public void create_table_votes_pdf(Document document) throws DocumentException, BadElementException {
                    
                    document.add(new Paragraph("Résultat: "));
                    Table table = new Table(4,2);
                    table.setAutoFillEmptyCells(true);
                    table.setPadding(2);
                                                             
                    Cell cell = new Cell("Oui");
                    cell.setHeader(true);
                    table.addCell(cell);
                        
                    cell = new Cell("Non");
                    cell.setHeader(true);
                    table.addCell(cell);
                    
                    cell = new Cell("Blanc");
                    cell.setHeader(true);
                    table.addCell(cell);

                    cell = new Cell("Non concerné");
                    cell.setHeader(true);
                    table.addCell(cell);
                        
                    table.endHeaders();
                    table.setWidth(100);
                        
                    Integer counter = 0;
                    table.addCell(oui.toString(), new Point(counter + 1, 0));
                    table.addCell(non.toString(), new Point(counter + 1, 1));
                    table.addCell(blanc.toString(), new Point(counter + 1, 2));
                    table.addCell(non_concerne.toString(), new Point(counter + 1, 3));
                    counter++;
                     
                    DecimalFormat dformat = new DecimalFormat("##.##%");
                    
                    table.addCell((total > 0) ? dformat.format((1.0 * oui)/total) : "0.00%", new Point(counter + 1, 0));
                    table.addCell((total > 0) ? dformat.format((1.0 * non)/total) : "0.00%", new Point(counter + 1, 1));
                    table.addCell((total > 0) ? dformat.format((1.0 * blanc)/total) : "0.00%", new Point(counter + 1, 2));
                    table.addCell((total > 0) ? dformat.format((1.0 * non_concerne)/total) : "0.00%", new Point(counter + 1, 3));
                       
                    document.add(table);
    }

    public void fill_data_pdf(Table table) throws BadElementException {
        if (onglet_actif.equals("questions")) {
            for (int i = 0; i < reponse_questions.size(); i++) {
                table.addCell(reponse_questions.get(i).getQuestion(), new Point(i + 1, 0));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_a(), new Point(i + 1, 1));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_b(), new Point(i + 1, 2));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_c(), new Point(i + 1, 3));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_d(), new Point(i + 1, 4));
                table.addCell(reponse_questions.get(i).getStr_pourcentage(), new Point(i + 1, 5));
            }
        } else if (onglet_actif.equals("eleves")) {
            for (int i = 0; i < resultats_voters.size(); i++) {
                table.addCell(resultats_voters.get(i).getNom_eleve(), new Point(i + 1, 0));
                table.addCell(resultats_voters.get(i).getPrenom_eleve(), new Point(i + 1, 1));
                table.addCell(resultats_voters.get(i).getNum_eleve().toString(), new Point(i + 1, 2));
                table.addCell(resultats_voters.get(i).getNote_eleve().toString(), new Point(i + 1, 3));
                table.addCell(resultats_voters.get(i).getPourcent_eleve().toString(), new Point(i + 1, 4));
            }
        }else if (onglet_actif.equals("eleves_pas_num")) {
            for (int i = 0; i < resultats_voters.size(); i++) {
                table.addCell(resultats_voters.get(i).getNom_eleve(), new Point(i + 1, 0));
                table.addCell(resultats_voters.get(i).getPrenom_eleve(), new Point(i + 1, 1));
                table.addCell(resultats_voters.get(i).getNote_eleve().toString(), new Point(i + 1, 2));
                table.addCell(resultats_voters.get(i).getPourcent_eleve().toString(), new Point(i + 1, 3));
            }
        }

    }

    public void close_window() {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
  }
    
    public void show_results(){
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps;
            ps = connection.prepareStatement("SELECT COUNT(idVote) "
                    + "FROM votes \n"
                    + "WHERE idPoll = ? AND option_vote = 'Oui' \n");
            ps.setInt(1, idPoll);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                oui = rs.getInt(1);
            
            ps = connection.prepareStatement("SELECT COUNT(idVote) "
                    + "FROM votes \n"
                    + "WHERE idPoll = ? AND option_vote = 'Non' \n");
            ps.setInt(1, idPoll);
            rs = ps.executeQuery();
            if (rs.next())
                non = rs.getInt(1);
            
            ps = connection.prepareStatement("SELECT COUNT(idVote) "
                    + "FROM votes \n"
                    + "WHERE idPoll = ? AND option_vote = 'Blanc' \n");
            ps.setInt(1, idPoll);
            rs = ps.executeQuery();
            if (rs.next())
                blanc = rs.getInt(1);
            
            ps = connection.prepareStatement("SELECT COUNT(idVote) "
                    + "FROM votes \n"
                    + "WHERE idPoll = ? AND option_vote = 'Non concerné' \n");
            ps.setInt(1, idPoll);
            rs = ps.executeQuery();
            if (rs.next())
                non_concerne = rs.getInt(1);

        } catch (Exception e){
            e.printStackTrace();
        }
        
        total = oui + blanc + non + non_concerne;
        
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
      new PieChart.Data("Non concerné", non_concerne),
      new PieChart.Data("Blanc",    blanc),
      new PieChart.Data("Oui",      oui),
      new PieChart.Data("Non",      non)
            
    );
    

     ResultsVote.setData(pieChartData);
     ResultsVote.setLegendVisible(false);
     
       
      applyCustomColorSequence(
      pieChartData,         
      "blue", 
      "white",
      "green", 
      "red"
    );
      
        yes.setText("" + oui);
        nein.setText("" + non);
        blanco.setText("" + blanc);
        nc.setText("" + non_concerne);

}

  private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
    int i = 0;
    for (PieChart.Data data : pieChartData) {
      data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
      
      i++;
    }
  }
}
    


