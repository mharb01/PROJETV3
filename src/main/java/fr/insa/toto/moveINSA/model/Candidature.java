/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

/**
 *
 * @author lbsb
 */
import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.list.ListUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.time.LocalDate;


/**
 * Classe "miroir" de la table Candidature.
 * <p>
 * pour interfacer facilement un programme java avec une base de donnée
 * relationnelle, il est souvent pratique de définir des classes correspondant
 * au tables d'entité de la base de données.
 * </p>
 * <p>
 * on pourrait aller plus loin et représenter également les relations et les
 * hiérarchies de classes. Mais ce serait refaire (en moins bien) ce que l'on
 * appelle un ORM : Object Relational Mapper. Il existe un ORM standard en Java
 * : JPA (Java Persistency API).
 * </p>
 * <p>
 * l'utilisation d'un ORM masque les détails de la base de données relationnelle
 * sous-jacente ainsi que le langage SQL. Hors, le but de ce module est de voir
 * l'utilisation de SQL et des bases relationnelles. Nous n'utiliserons donc pas
 * d'ORM.
 * </p>
 * <p>
 * Pour les relations, nous nous contenterons de conserver les identificateurs
 * comme cela est fait dans les tables (voir attribut proposePar de la classe
 * OffreMobilité par exemple.
 * </p>
 *
 * @author francois
 */
public class Candidature {

    private int idCandidature;
    private String INE;
    private int idOffreMobilite;
    private Date date;

    /**
     * création d'une nouvelle Candidature en mémoire, non existant dans la Base de
     * donnée.
     *
     * @param id
     */
    
    public Candidature(String INE, int idOffreMobilite, Date date){
        this(-1,INE, idOffreMobilite, date);
    }
   
    /**
     * création d'une candidature retrouvée dans la base de donnée.
     *
     * @param INE, idOffreMobilite
     */
    public Candidature(int idCandidature, String INE, int idOffreMobilite, Date date){
        this.idCandidature = idCandidature;
        this.INE = INE;
        this.idOffreMobilite = idOffreMobilite;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Candidature{" + "idCandidature =" + this.getIdCandidature() + " ; INE=" + INE + " ; Offre mobilite=" + this.getIdOffreMobilite() + " ; Date = " +this.getDate()+ '}' ;
    }

    /**
     * Sauvegarde une nouvelle entité et retourne la clé affectée automatiquement
     * par le SGBD.
     * <p>
     * la clé est également sauvegardée dans l'attribut idCandidature
     * </p>
     *
     * @param con
     * @return la clé de la nouvelle entité dans la table de la BdD
     * @throws EntiteDejaSauvegardee si l'id de l'entité est différent de -1
     * @throws SQLException si autre problème avec la BdD
     */
    public int saveInDB(Connection con) throws SQLException {
        if (this.getIdCandidature() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into candidature (INE, idOffreMobilite, date) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.getINE());
            insert.setInt(2, this.getIdOffreMobilite());
            insert.setDate(3, (java.sql.Date) this.getDate());
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.idCandidature = rid.getInt(1);
                return this.getIdCandidature();
            }
        }
    }

    public static List<Candidature> toutesLesCandidatures(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select idCandidature,INE,idOffreMobilite,date from candidature")) {
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getDate(4)));
            }
            return res;
        }
    }

    public static OffreMobilite getOffre(Connection con, int idOffre) throws SQLException{
        String rech = "SELECT * FROM offremobilite WHERE id = ?";
        try(PreparedStatement pst = con.prepareStatement(rech)){
            pst.setInt(1, idOffre);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int nbr = rs.getInt("nbrplaces");
                    String classe = rs.getString("classe");
                    int proposepar = rs.getInt("proposepar");
                    
                    return new OffreMobilite(id, nbr, proposepar, classe, "2024");
                }
            }     
        }
        return null;
    }
    
    public static Boolean controleCandidature(Connection con, String ine, int idOffre) throws SQLException {
    String rechEtudiant = "SELECT classe FROM etudiant WHERE ine = ?";
    String rechOffre = "SELECT classe FROM offremobilite WHERE id = ?";
    
    String classeEtudiant = null;
    String classeOffre = null;

    try (PreparedStatement pstEtudiant = con.prepareStatement(rechEtudiant)) {
        pstEtudiant.setString(1, ine);
        try (ResultSet rsEtudiant = pstEtudiant.executeQuery()) {
            rsEtudiant.next(); 
            classeEtudiant = rsEtudiant.getString("classe");
        }
    }

    try (PreparedStatement pstOffre = con.prepareStatement(rechOffre)) {
        pstOffre.setInt(1, idOffre);
        try (ResultSet rsOffre = pstOffre.executeQuery()) {
            rsOffre.next(); 
            classeOffre = rsOffre.getString("classe");
        }
    }
    return classeEtudiant.equals(classeOffre);
}

    public static Boolean compteCandidature(Connection con, String ine) throws SQLException{
        String rech = "SELECT COUNT (*) AS total FROM candidature WHERE = ?";
        try (PreparedStatement pst = con.prepareStatement(rech)){
            pst.setString(1, ine);
            try (ResultSet rs = pst.executeQuery()){
                if (rs.next()){
                    int totalCandidatures = rs.getInt("total");
                    return totalCandidatures < 5; 
                }
            }
        }
        return false;
    }
    
    public static int creeConsole(Connection con, Etudiant etudiant) throws SQLException {
        String ine = etudiant.getIne();
        OffreMobilite offre;        
        
        System.out.println("A quelle offre souhaitez-vous candidater?");
        List<OffreMobilite> users = OffreMobilite.toutesLesOffres(con);
        System.out.println(users.size() + " offres : ");
        System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
        int idOffre = ConsoleFdB.entreeEntier("Saisir l'id de l'offre voulue: ");
        offre = getOffre(con, idOffre);
        
        if (controleCandidature(con, ine, offre.getId()) == true && compteCandidature(con, ine)){
            LocalDate dateNow = LocalDate.now();
            Date dateCandidature;
            dateCandidature = java.sql.Date.valueOf(dateNow);
            Candidature nouveau = new Candidature(ine,offre.getId(),dateCandidature);
            System.out.println("Candidature envoyee avec succes!");
            return nouveau.saveInDB(con);
        } else {
            System.out.println("Vous ne pouvez pas candidater à cette offre: vous n'etes pas dans la bonne classe");
            return -1;
        }
    }

    public static Candidature selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une candidature :",
                toutesLesCandidatures(con), (Candidature c) -> "ID: " + c.getIdCandidature() + ", INE: " + c.getINE() + ", Offre mobilite :" + c.getIdOffreMobilite() + ", Date : " + c.getDate());
    }


    public static void updateInConsole(Connection con) throws SQLException{
        String nouveauINE = ConsoleFdB.entreeString("INE du nouveau candidat (laisser vide si vous souhaitez conserver l'ancien):");
        int nouvelleOffre = ConsoleFdB.entreeInt("Identifiant de la nouvelle offre (0 pour garder l'ancien):");
        LocalDate dateNow = LocalDate.now();
        Date nouvelledate = java.sql.Date.valueOf(dateNow);
        int idCandidature = ConsoleFdB.entreeInt("Identifiant de la candidature à modifier:");
        StringBuilder ordresql = new StringBuilder("update candidature set");
        Boolean first = true;
        int i = 1;
        if (!nouveauINE.isEmpty()) {
            ordresql.append("ine = ?");
            first = false;  // La première colonne a été ajoutée
            }
        if (0!=nouvelleOffre) {
            if (first=false){
                ordresql.append(", ");
            }
            ordresql.append("idOffreMobilite = ?");
            first = false;
        }
        ordresql.append("date= ? where idCandidature = ?");
        String resultatordre = ordresql.toString();
            try (PreparedStatement update = con.prepareStatement(
            resultatordre)) {
            if (!nouveauINE.isEmpty()) {
            update.setInt(i, nouvelleOffre);
            i++;
            }
            if (nouvelleOffre!=0) {
            update.setInt(i, nouvelleOffre);
            i++;
            }
            update.setDate(i, (java.sql.Date) nouvelledate);
            i++;
            update.setInt(i, idCandidature);
            update.executeUpdate();
        }
        System.out.println("Candidature modifiée avec succès !");
    }
    
    public static void deleteInConsole(Connection con) throws SQLException{
        try (PreparedStatement update = con.prepareStatement(
                "delete from candidature where idCandidature = ? ")){ 
                 String idCand = ConsoleFdB.entreeString("idCandidature: ");
                 update.setString(1,idCand);
                 update.execute();       
                 }
        System.out.println("Candidature supprimee avec succes !");
    }
    
    public static void deleteAllConsole(Connection con) throws SQLException{
        try (PreparedStatement delete = con.prepareStatement("DELETE from candidature")){
            int suppr = delete.executeUpdate();
            System.out.println("Candidatures supprimees avec succes!");
        }
    }
    /**
     * @return the INE
     */
    public String getINE() {
        return INE;
    }

    /**
     * @param INE the INE to set
     */
    public void setINE(String INE) {
        this.INE = INE;
    }
    
    /**
     * @return the idCandidature
     */
    public int getIdCandidature() {
        return idCandidature;
    }

    public int getIdOffreMobilite() {
        return idOffreMobilite;
    }

    public Date getDate(){
        return date;
    }

    public void setIdOffreMobilite(int idOffreMobilite) {
        this.idOffreMobilite = idOffreMobilite;
    }
}
