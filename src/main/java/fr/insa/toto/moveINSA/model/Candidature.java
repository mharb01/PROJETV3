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
    private String date;

    /**
     * création d'une nouvelle Candidature en mémoire, non existant dans la Base de
     * donnée.
     *
     * @param id
     */
    
    public Candidature(String INE, int idOffreMobilite, String date) {
        this(-1,INE, idOffreMobilite, date);
    }
   
    /**
     * création d'une candidature retrouvée dans la base de donnée.
     *
     * @param INE, idOffreMobilite
     */
    public Candidature(int idCandidature, String INE, int idOffreMobilite, String date) {
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
            insert.setString(3, this.getDate());
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
                res.add(new Candidature(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getString(4)));
            }
            return res;
        }
    }

    public static int creeConsole(Connection con) throws SQLException {
        String nouveauINE = ConsoleFdB.entreeString("Entrez l'INE de l'etudiant : ");
        String nouvelleOffre = ConsoleFdB.entreeString("Entrez l'ID de l'offre de mobilite :");
        LocalDate currentDate = LocalDate.now();
        String dateCandidature = currentDate.toString();
        Candidature nouveau = new Candidature(nouveauINE,Integer.parseInt(nouvelleOffre), dateCandidature);
        return nouveau.saveInDB(con);
    }

    public static Candidature selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une candidature :",
                toutesLesCandidatures(con), (Candidature c) -> "ID: " + c.getIdCandidature() + ", INE: " + c.getINE() + ", Offre mobilite :" + c.getIdOffreMobilite() + ", Date : " + c.getDate());
    }

    public static void updateInConsole(Connection con) throws SQLException{
        try (PreparedStatement update = con.prepareStatement(
            "UPDATE candidature SET INE = ?, idOffreMobilite = ?, date = ?, WHERE idCandidature = ?")){
            String nouveauINE = ConsoleFdB.entreeString("Nouveau INE (laissez vide pour conserver le précédent): ");
            if (!nouveauINE.isEmpty()){
                this.setINE(nouveauINE);
            } 
            String nouvelleOffre = ConsoleFdB.entreeString("Nouveau id d'offre (laissez vide pour conserver le précédent) :");
            if (!nouvelleOffre.isEmpty()){
                this.setIdOffreMobilite(Integer.parseInt(nouvelleOffre));
            } 
        }
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

    public String getDate() {
        return date;
    }

    public void setIdOffreMobilite(int idOffreMobilite) {
        this.idOffreMobilite = idOffreMobilite;
    }
}
