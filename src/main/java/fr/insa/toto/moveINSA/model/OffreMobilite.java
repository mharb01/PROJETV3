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

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.list.ListUtils;
import static fr.insa.toto.moveINSA.model.Partenaire.selectInConsole;
import static fr.insa.toto.moveINSA.model.Partenaire.tousLesPartaires;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe "miroir" de la table offremobilite.
 * <p>
 * pour un commentaire plus détaillé sur ces classes "miroir", voir dans la
 * classe Partenaire
 * </p>
 *
 * @author francois
 */
public class OffreMobilite {

    private int id;
    private int nbrPlaces;
    private int proposePar;
    private String classe;
    private String annee;

    /**
     * création d'une nouvelle Offre en mémoire, non existant dans la Base de
     * donnée.
     * @param nbrPlaces
     * @param proposePar
     * @param classe
     * @param annee
     */
    public OffreMobilite(int nbrPlaces, int proposePar, String classe, String annee) {
        this(-1, nbrPlaces, proposePar, classe, annee);
    }

    /**
     * création d'une Offre retrouvée dans la base de donnée.
     * @param id
     * @param nbrPlaces
     * @param proposePar
     * @param classe
     * @param annee
     */
    public OffreMobilite(int id, int nbrPlaces, int proposePar, String classe, String annee) {
        this.id = id;
        this.nbrPlaces = nbrPlaces;
        this.proposePar = proposePar;
        this.classe = classe;
        this.annee = annee;
    }

    @Override
    public String toString() {
        return "OffreMobilite{" + "id=" + this.getId() + " ; nbrPlaces=" + nbrPlaces + " ; proposePar=" + proposePar + "; classe = " + classe + "; année = " + annee + '}';
    }

    /**
     * Sauvegarde une nouvelle entité et retourne la clé affecté automatiquement
     * par le SGBD.
     * <p>
     * la clé est également sauvegardée dans l'attribut id
     * </p>
     *
     * @param con
     * @return la clé de la nouvelle entité dans la table de la BdD
     * @throws EntiteDejaSauvegardee si l'id de l'entité est différent de -1
     * @throws SQLException si autre problème avec la BdD
     */
    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new fr.insa.toto.moveINSA.model.EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into offremobilite (nbrplaces,proposepar,classe,annee) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, this.nbrPlaces);
            insert.setInt(2, this.proposePar);
            insert.setString(3, this.classe);
            insert.setString(4, this.annee);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }

    public static List<OffreMobilite> toutesLesOffres(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nbrplaces,proposepar,classe,annee from offremobilite")) {
            ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            return res;
        }
    }
    public static List<OffreMobilite> toutesLesOffresPartenaire(Connection con, Partenaire partenaire) throws SQLException {
        int id = partenaire.getId();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nbrplaces,proposepar,classe,annee from offremobilite where proposepar = ?")) {
            pst.setInt(1, id); 
            ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            return res;
        }
    }
    
    public static int creeConsole(Connection con) throws SQLException {
        Partenaire p = Partenaire.selectInConsole(con);
        int nbr = ConsoleFdB.entreeInt("nombre de places : ");
        String clss = ConsoleFdB.entreeString ("Classe :");
        int an = ConsoleFdB.entreeInt("Si l'offre est proposée au niveau : unedergraduate = 1, postgraduate = 2, les deux = 3");
        String annee = null;
        while (an !=1 || an!=2 || an!=3){
        if (an == 1){
            annee = "undergraduate";
        }
        else if (an==2){
            annee = "postgraduate";
        }
        else {
            annee = "both";
        }    
        }
        OffreMobilite nouveau = new OffreMobilite(nbr, p.getId(), clss,annee);
        return nouveau.saveInDB(con);
    }
    public static int creeConsoleang(Connection con, int id) throws SQLException {
        int nbr = ConsoleFdB.entreeInt("number of places : ");
        String clss = ConsoleFdB.entreeString ("Class :"); //modifier pour choisir dans une liste
        int an = ConsoleFdB.entreeInt("If the offer is for : unedergraduate = 1, postgraduate = 2, both = 3"); //proposer sous forme de liste
        String annee = null;
        while (an !=1 || an!=2 || an!=3){
        if (an == 1){
            annee = "undergraduate";
        }
        else if (an==2){
            annee = "postgraduate";
        }
        else {
            annee = "both";
        }    
        }
        OffreMobilite nouveau = new OffreMobilite(nbr, id, clss,annee);
        return nouveau.saveInDB(con);
    }
// faire version anglaise modif console, peut pas changer le partenaire
    public static void modifConsole(Connection con) throws SQLException {

        System.out.println("Selectionner l'offre à modifier");
        OffreMobilite offremodif = selectInConsoleOffre(con);
        int nouveaunbrplaces = ConsoleFdB.entreeInt("Nouveau nombre de places (0 si vous souhaitez conserver l'ancien):");
        String nouvelleclasse = ConsoleFdB.entreeString("Nouvelle classe (laisser vide si vous souhaitez conserver l'ancien):");
        String nouvelleannee = ConsoleFdB.entreeString("Nouvelle année undergraduate/postgraduate/both (laisser vide si vous souhaitez conserver l'ancien):");
        System.out.println("Selectionner le nouveau partenaire");
        Partenaire nouveaupartenaire = Partenaire.selectInConsole(con);
        
        StringBuilder ordresql = new StringBuilder("update offremobilite set");
        Boolean first = true;
        int i = 1;
        if (nouveaunbrplaces!=0) {
            ordresql.append("nbrplaces = ?");
            first = false;  // La première colonne a été ajoutée
        }
        if (!nouvelleclasse.isEmpty()) {
            if (first=false){
                ordresql.append(", ");
            }
            first = false ;
            ordresql.append("classe = ?");
        }
        if (!nouvelleannee.isEmpty()) {
            if (first=false){
                ordresql.append(", ");
            }
            first = false ;
            ordresql.append("annee = ?");
        }
        ordresql.append("proposepar = ? where id = ?");
        String resultatordre = ordresql.toString();
        try (PreparedStatement update = con.prepareStatement(
            resultatordre)) {
            if (nouveaunbrplaces!=0) {
            update.setInt(i, nouveaunbrplaces);
            i++;
            }
            if (!nouvelleclasse.isEmpty()) {
            update.setString(i, nouvelleclasse); 
            i++;
            }
            if (!nouvelleclasse.isEmpty()) {
            update.setString(i, nouvelleannee); 
            i++;
            }
            update.setInt(i, nouveaupartenaire.getId());
            i++;
            update.setInt(i, offremodif.getId());
            update.executeUpdate();
        }
        System.out.println("Offre modifiée avec succès !");
    }
    //voir si peut choisir dans liste à supprimer
      public static void suppConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite where nbrplaces = ? and proposepar = ?  and classe = ? and annee = ? ")){ 
            OffreMobilite offre = selectInConsoleOffre(con);
            int nbrPlaces = offre.getId();
            String partenaire = offre.getPartenaire();
            String classe = offre.getClasse();
            String annee = offre.getAnnee();
                 update.setInt(1,nbrPlaces);
                 update.setString(2, partenaire);
                 update.setString(3, classe);
                 update.setString(3, annee);
                 update.execute();      
                 }
        System.out.println("Offre supprimee avec succes !");
    }
      //voir si peut juste mettre selectionner dans la liste des offres
      public static void suppConsoleang(Connection con, int id) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite where nbrplaces = ? and classe = ? and annee = ? and proposepar = "+id)){ 
            OffreMobilite offre = selectInConsoleOffreang(con);
            int nbrPlaces = offre.getId();
            String partenaire = offre.getPartenaire();
            String classe = offre.getClasse();
            String annee = offre.getAnnee();
                 update.setInt(1,nbrPlaces);
                 update.setString(2, partenaire);
                 update.setString(3, classe);
                 update.setString(3, annee);
                 update.execute();       
                 }
        System.out.println("Offer deleted with success!");
    }
      
      public static void suppALLConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite")){
                 update.executeUpdate();       
                 }
        System.out.println("Toutes les offres ont ete supprimees avec succes !");
    }
      public static void suppALLConsoleang(Connection con, int id) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                
                "delete from offremobilite where proposePar = '"+id+"'")){
                 update.executeUpdate();       
                 }
        System.out.println("All your offers are deleted with success !");
    }
      
      public static List<OffreMobilite> rechercherRef(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offremobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.refPartenaire = ? ")) {
        Partenaire part = Partenaire.selectInConsole(con);
        pst.setString(1, part.getRefPartenaire());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici toutes les offres venant du partenaire : ' " + part.getRefPartenaire()+ " ' !" );
            return res;
    }   
    }
      public static List<OffreMobilite> rechercherRefang(Connection con, int id) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offremobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.refPartenaire = "+id)) {
        Partenaire part = Partenaire.selectInConsole(con);
        pst.setString(1, part.getRefPartenaire());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("All your offers : ' " + part.getRefPartenaire()+ " ' !" );
            return res;
    }   
    }


      public static List<OffreMobilite> rechercherPays(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offremobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.pays = ?  ")) {
        Partenaire part = Partenaire.selectInConsolePays(con);
        pst.setString(1, part.getPays());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici toutes les offres venant du pays: ' " + part.getPays() + " ' !" );
            return res;
            
 
    }   
    }
      
      public static List<OffreMobilite> rechercherClasse(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offremobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and offremobilite.classe = ?  ")) {
        OffreMobilite offre = OffreMobilite.selectInConsoleClasse(con);
        pst.setString(1, offre.getClasse());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici toutes les offres visant la classe: ' " + offre.getClasse() + " ' !" );
            return res;
            
 
    }   
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    public int getNbrPlaces() {
        return nbrPlaces;
    }
    
    public String getClasse() {
        return classe;
    }
    public String getPartenaire() {
        return Integer.toString(proposePar); 
    }
    public int getProposePar() {
        return proposePar;
    }
    public String getAnnee() {
        return annee;
    }
    // les selects in console sont à revoir pour faire apparaitre les bon éléments, pour classe et annee, creer les listes avec les possiblités
    public static OffreMobilite selectInConsoleOffre(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une offre :",
                toutesLesOffres(con), (elem) -> elem.getPartenaire()); //a voir si compréhensible
    }
    public static OffreMobilite selectInConsoleOffreang(Connection con) throws SQLException {
        return ListUtils.selectOne("choose an offer :",
                toutesLesOffres(con), (elem) -> elem.getPartenaire()); 
    }
    public static OffreMobilite selectInConsoleClasse(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une classe :",
                toutesLesOffres(con), (elem) -> elem.getClasse());
    }
    public static OffreMobilite selectInConsoleClasseang(Connection con) throws SQLException {
        return ListUtils.selectOne("choose a class :",
                toutesLesOffres(con), (elem) -> elem.getClasse());
    }
    public static OffreMobilite selectInConsoleAnnee(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une annee :",
                toutesLesOffres(con), (elem) -> elem.getClasse());
    }
    public static OffreMobilite selectInConsoleAnneeang(Connection con) throws SQLException {
        return ListUtils.selectOne("choose a years :",
                toutesLesOffres(con), (elem) -> elem.getClasse());
    }

}
