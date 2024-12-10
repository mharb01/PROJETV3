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

    /**
     * création d'une nouvelle Offre en mémoire, non existant dans la Base de
     * donnée.
     * @param nbrPlaces
     * @param proposePar
     * @param classe
     */
    public OffreMobilite(int nbrPlaces, int proposePar, String classe) {
        this(-1, nbrPlaces, proposePar, classe);
    }

    /**
     * création d'une Offre retrouvée dans la base de donnée.
     * @param id
     * @param nbrPlaces
     * @param proposePar
     * @param classe
     */
    public OffreMobilite(int id, int nbrPlaces, int proposePar, String classe) {
        this.id = id;
        this.nbrPlaces = nbrPlaces;
        this.proposePar = proposePar;
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "OffreMobilite{" + "id=" + this.getId() + " ; nbrPlaces=" + nbrPlaces + " ; proposePar=" + proposePar + "; classe = " + classe + '}';
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
                "insert into offremobilite (nbrplaces,proposepar,classe) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, this.nbrPlaces);
            insert.setInt(2, this.proposePar);
            insert.setString(3, this.classe);
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
                "select id,nbrplaces,proposepar,classe from offremobilite")) {
            ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
            }
            return res;
        }
    }

    
    public static int creeConsole(Connection con) throws SQLException {
        Partenaire p = Partenaire.selectInConsole(con);
        int nbr = ConsoleFdB.entreeInt("nombre de places : ");
        String clss = ConsoleFdB.entreeString ("Classe :");
        OffreMobilite nouveau = new OffreMobilite(nbr, p.getId(), clss);
        return nouveau.saveInDB(con);
    }

    public static void modifConsole(Connection con) throws SQLException {
//    try (PreparedStatement update = con.prepareStatement(
//            "update offremobilite set nbrplaces = ? , proposepar = ? , classe = ? where nbrplaces = ? and proposepar = ? and classe = ? ")) {
//        int newnbrPlaces = ConsoleFdB.entreeInt("new nbrplaces :");
//        String newclasse = ConsoleFdB.entreeString("new classe :");
//        Partenaire newp = Partenaire.selectInConsole(con);
//        int lastnbrPlaces = ConsoleFdB.entreeInt("last nbrplaces :");
//        String lastclasse = ConsoleFdB.entreeString("last classe: ");
//        Partenaire lastp = Partenaire.selectInConsole(con);
//        update.setInt(1,newnbrPlaces);
//        update.setInt(2,newp.getId());
//        update.setString(3, newclasse);
//        update.setInt(4,lastnbrPlaces);
//        update.setInt(5,lastp.getId());
//        update.setString(6, lastclasse);
//        update.executeUpdate();
//        }
//    System.out.println("Offre modifiee avec succes !");
//        
        System.out.println("Selectionner l'offre à modifier");
        OffreMobilite offremodif = selectInConsoleOffre(con);
        int nouveaunbrplaces = ConsoleFdB.entreeInt("Nouveau nombre de places (0 si vous souhaitez conserver l'ancien):");
        String nouvelleclasse = ConsoleFdB.entreeString("Nouvelleclasse (laisser vide si vous souhaitez conserver l'ancien):");
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
            update.setInt(i, nouveaupartenaire.getId());
            i++;
            update.setInt(i, offremodif.getId());
            update.executeUpdate();
        }
        System.out.println("Offre modifiée avec succès !");
    }
    
      public static void suppConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite where nbrplaces = ? and proposepar = ?  and classe = ? ")){ 
                 int nbrPlaces = ConsoleFdB.entreeInt("nbrPlaces: ");
                 Partenaire p = Partenaire.selectInConsole(con);
                 String classe = ConsoleFdB.entreeString("classe: ");
                 update.setInt(1,nbrPlaces);
                 update.setInt(2, p.getId());
                 update.setString(3, classe);
                 update.execute();       
                 }
        System.out.println("Offre supprimee avec succes !");
    }
      
      public static void suppALLConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite")){
                 update.executeUpdate();       
                 }
        System.out.println("Toutes les offres ont ete supprimees avec succes !");
    }
      
      public static List<OffreMobilite> rechercherRef(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.refPartenaire = ? ")) {
        Partenaire part = Partenaire.selectInConsole(con);
        pst.setString(1, part.getRefPartenaire());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
            }
            System.out.println("Voici toutes les offres venant du partenaire: ' " + part.getRefPartenaire()+ " ' !" );
            return res;
    }   
    }


      public static List<OffreMobilite> rechercherPays(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.pays = ?  ")) {
        Partenaire part = Partenaire.selectInConsolePays(con);
        pst.setString(1, part.getPays());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
            }
            System.out.println("Voici toutes les offres venant du pays: ' " + part.getPays() + " ' !" );
            return res;
            
 
    }   
    }
      
      public static List<OffreMobilite> rechercherClasse(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and offremobilite.classe = ?  ")) {
        OffreMobilite offre = OffreMobilite.selectInConsoleClasse(con);
        pst.setString(1, offre.getClasse());
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
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
    
    public String getClasse() {
        return classe;
    }
    public String getPartenaire() {
        return Integer.toString(proposePar); 
    }
    public static OffreMobilite selectInConsoleOffre(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une offre :",
                toutesLesOffres(con), (elem) -> elem.getPartenaire()); //a voir si compréhensible
    }
    public static OffreMobilite selectInConsoleClasse(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez une classe :",
                toutesLesOffres(con), (elem) -> elem.getClasse());
    }

}
