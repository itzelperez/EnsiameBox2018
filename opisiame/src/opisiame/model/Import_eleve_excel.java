/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import opisiame.database.Connection_db;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author clement
 */
//Dans cette classe, on utilise la librarie apache POI pour lire le fichier Excel
public class Import_eleve_excel {
    
    private List<String> Liste_nom = new ArrayList<>();
    private List<String> Liste_prenom = new ArrayList<>();
    private List<String> Liste_filiere = new ArrayList<>();
    private List<Integer> Liste_annee = new ArrayList<>();
    private List<Integer> Liste_erreur = new ArrayList<>();
    private List<Integer> Liste_Id = new ArrayList<>();
    private int nb_element; //nombre d'élément dans le fichier excel
    private HSSFWorkbook classeur;
    //private String adresse;

    public Import_eleve_excel(String adresse) throws IOException {
        //openning the document
        //System.out.print("appelle constructeur import eleve excel \n");
        nb_element = 0;
        //ouverture_fichier(adresse);
        try {
         Workbook wb = WorkbookFactory.create(new File(adresse));
         Sheet sheet = wb.getSheetAt(0);
        
        //System.out.print("je suppose que ça doit marcher vu qu'il n'y a pas d'erreur \n");

        //reading the document
        //Sheet sheet = classeur.getSheetAt(0);
        //creer un itérateur sur les colonnes
        Iterator<Row> iterator = sheet.iterator();
        
        nb_element = 0;
        Random rand = new Random();
        int anonyme = rand.nextInt(999999);
        while (iterator.hasNext()) {
            ++nb_element;
            Row row = iterator.next();
            
            if (nb_element == 1){
                iterator.next();
                row = iterator.next();
            }
            
            Iterator<Cell> cell_iterator = row.cellIterator();
            Cell cell = cell_iterator.next();
            
            String name = cell.getStringCellValue();
            if (name.trim().isEmpty()){
                name = "Anonyme " + nb_element;
                anonyme++;
            } else
                System.out.println(name);
            
            Liste_nom.add(name);
            cell = cell_iterator.next();
            
            name = cell.getStringCellValue();
            if (name.trim().isEmpty()){
                name = "Etudiant ";
            } else
                System.out.println(name);
            
            Liste_prenom.add(name);
            cell = cell_iterator.next();
            
            name = cell.getStringCellValue();
            Integer matricule = anonyme;
            try {
                matricule = Integer.parseInt(name);
            } catch (NumberFormatException e){
                // do nothing
            }
            Liste_Id.add(matricule);
            cell = cell_iterator.next();
            
            name = cell.getStringCellValue();
            if (name.isEmpty()){
                Liste_nom.remove(Liste_nom.size() - 1);
                Liste_prenom.remove(Liste_prenom.size() - 1);
                Liste_Id.remove(Liste_Id.size() - 1);
                break;
            }
            Liste_filiere.add(name);
            cell = cell_iterator.next();
            Liste_annee.add(1);
        }
        
        } catch(Exception e){
            e.printStackTrace();
        }

        //test des sorties
        /* for (int i = 0; i < Liste_nom.size(); ++i) {
            System.out.print(Liste_nom.get(i) + " ");
            System.out.print(Liste_prenom.get(i) + " ");
            System.out.print(Liste_filiere.get(i) + " ");
            System.out.print(Liste_annee.get(i) + " \n");
        }*/
        update_database();
        
    }

    public Import_eleve_excel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void ouverture_fichier(String adresse) throws IOException {
        InputStream is = new FileInputStream(adresse);
        System.out.println("opisiame.model.Import_eleve_excel.ouverture_fichier() 1");
        OPCPackage opc;
        try {
            opc = OPCPackage.open(is);
            System.out.println("opisiame.model.Import_eleve_excel.ouverture_fichier() 2");
            //classeur = new XSSFWorkbook(opc);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Import_eleve_excel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void update_database() {
            for (int i = 0; i < nb_element - 1; ++i) {
                 try {Connection connection = Connection_db.getDatabase();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO participant (Part_id, Part_nom, Part_prenom, Filiere_id) "
                        + "VALUES (?, ?, ?, (SELECT Filiere_id FROM filiere Where Filiere = ? AND Annee = ? ) )");
                ps.setInt(1, Liste_Id.get(i));
                ps.setString(2, Liste_nom.get(i));
                ps.setString(3, Liste_prenom.get(i));
                ps.setString(4, Liste_filiere.get(i));
                ps.setInt(5, Liste_annee.get(i));
                ps.executeUpdate();
                 } catch (SQLException ex) {
            //Logger.getLogger(Import_eleve_excel.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            Liste_erreur.add(i+1);
            System.out.printf("une erreur est apparu à la ligne suivante : \n");
            System.out.printf(Liste_erreur.get(0) + "\n");
        }
            }
    }
    
    public List<Integer> getErreur(){
        return Liste_erreur;
    }
    
};
