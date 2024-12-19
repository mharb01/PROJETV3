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
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Classe "miroir" de la table partenaire.
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
public class Partenaire implements Serializable{

    /**
     * permet de tester lors du chargement d'un objet sérialisé que la version
     * sauvegarder a la même version que la version courante de la classe.
     * <pre>
     * </pre>
     */
    private static final long serialVersionUID = 1;
    
    private int id;
    private String refPartenaire;
    private String pays;
    private String idcoPartenaire;
    private String mdpPartenaire;


    /**
     * création d'un nouveau Partenaire en mémoire, non existant dans la Base de
     * donnée.
     *
     * @param refPartenaire
     * @param pays
     * @param idcoPartenaire
     * @param mdpPartenaire
     */
    public Partenaire(String refPartenaire, String pays, String idcoPartenaire, String mdpPartenaire) {
        this(-1,refPartenaire,pays,idcoPartenaire,mdpPartenaire);
    }

    /**
     * création d'un Partenaire retrouvé dans la base de donnée.
     *
     * @param id
     * @param refPartenaire
     * @param pays
     * @param idcoPartenaire
     * @param mdpPartenaire
     */
    public Partenaire(int id, String refPartenaire, String pays, String idcoPartenaire, String mdpPartenaire) {
        this.id = id;
        this.refPartenaire = refPartenaire;
        this.pays = pays;
        this.idcoPartenaire = idcoPartenaire;
        this.mdpPartenaire = mdpPartenaire;        
    }
    
    public Partenaire(int id, String refPartenaire, String pays) {
        this.id = id;
        this.refPartenaire = refPartenaire;
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Partenaire{" + "id =" + this.getId() + " ; refPartenaire=" + refPartenaire + "; pays =" + pays + "; idcoPartenaire =" + idcoPartenaire + "; mdpPartenaire =" + mdpPartenaire +'}';
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
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into partenaire (refPartenaire, pays, idcoPartenaire, mdpPartenaire) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.getRefPartenaire());
            insert.setString(2, this.getPays());
            insert.setString(3, this.getidco());
            insert.setString(4, this.getmdp());
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }

    public static List<Partenaire> tousLesPartaires(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,refPartenaire, pays, idcoPartenaire, mdpPartenaire from partenaire")) {
            ResultSet rs = pst.executeQuery();
            List<Partenaire> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Partenaire(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            return res;
        }
    }

    public static Optional<Partenaire> trouvePartaire(Connection con,
            String refPart, String pays) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,refPartenaire,pays, idcoPartenaire, mdpPartenaire from partenaire"
                + " where refPartenaire = ? and pays = ? ")) {
            pst.setString(1, refPart);
            pst.setString(2, pays);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return Optional.of(new Partenaire(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            } else {
                return Optional.empty();
            }
        }
    }

    public static int creeConsole(Connection con) throws SQLException {
        String idP = ConsoleFdB.entreeString("refPartenaire : ");
        String paysP = ConsoleFdB.entreeString("pays : ");
        String idcoP = ConsoleFdB.entreeString("identifiant de connexion : ");
        String mdpP = ConsoleFdB.entreeString("mot de passe provisoire : ");
        Partenaire nouveau = new Partenaire(idP, paysP, idcoP, mdpP);
        return nouveau.saveInDB(con);
    }

    
    public static void updateInConsole(Connection con) throws SQLException{
        System.out.println("Selectionner le partenaire à modifier");
        Partenaire partenairemodif = selectInConsole(con);
        int idPartenaire = partenairemodif.getId();
        String nouvelleref = ConsoleFdB.entreeString("Référence du nouveau  partenaire (laisser vide si vous souhaitez conserver l'ancien):");
        String nouveaupays = EntiteDejaSauvegardee.selectInConsolePays();
        String nouveauidco = ConsoleFdB.entreeString("Identifiant de connexion du nouveau partenaire (laisser vide si vous souhaitez conserver l'ancien):");
        String nouveaumdp = ConsoleFdB.entreeString("Mot de passe provisoire du nouveau partenaire (laisser vide si vous souhaitez conserver l'ancien):");
        StringBuilder ordresql = new StringBuilder("update partenaire set");
        Boolean first = true;
        int i = 1;
        if (!nouvelleref.isEmpty()) {
            ordresql.append("refPartenaire = ?");
            first = false;  // La première colonne a été ajoutée
        }
        if (!nouveaupays.isEmpty()) {
            if (first=false){
                ordresql.append(", ");
            }
            first = false ;
            ordresql.append("pays = ?");
        }
        if (!nouveauidco.isEmpty()) {
            if (first = false){
                ordresql.append(", ");
            }
            ordresql.append("idcoPartenaire = ?");
            first = false ;
        }
        if (!nouveaumdp.isEmpty()) {
            if (first = false){
                ordresql.append(", ");
            }
            ordresql.append("mdpPartenaire = ?");
            first = false;
        }
        ordresql.append("where id = ?");
        String resultatordre = ordresql.toString();
        try (PreparedStatement update = con.prepareStatement(
            resultatordre)) {
            if (!nouvelleref.isEmpty()) {
            update.setString(i, nouvelleref);
            i++;
            }
            if (!nouveaupays.isEmpty()) {
            update.setString(i, nouveaupays); 
            i++;
            }
            if (!nouveauidco.isEmpty()) {
            update.setString(i, nouveauidco);
            i++;
            }
            if (!nouveaumdp.isEmpty()) {
            update.setString(i, nouveaumdp); 
            i++;
            }
            update.setInt(i, idPartenaire);
            update.executeUpdate();
        }
        System.out.println("Profil partenaire modifié avec succès !");
    }
    
    public static Partenaire selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez un partenaire :",
                tousLesPartaires(con), (elem) -> elem.getRefPartenaire());
    }

    public static Partenaire selectInConsolePays(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez un pays :",
                tousLesPartaires(con), (elem) -> elem.getPays());
    }
    
    
    public static void suppALLConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from partenaire")){
                 update.executeUpdate();       
                 }
        System.out.println("Toutes les partenaires ont ete supprimes avec succes !");
    }
    
    /**
     * @return the refPartenaire
     */
    public String getRefPartenaire() {
        return refPartenaire;
    }
    
    public String getPays() {
        return pays;
    }

    /**
     * @param refPartenaire the refPartenaire to set
     */
    public void setRefPartenaire(String refPartenaire) {
        this.refPartenaire = refPartenaire;
    }

    public void setPays(String pays) {
        this.pays = pays;    
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
public String getidco() {
        return idcoPartenaire;
    }

    public void setidco(String idco) {
        this.idcoPartenaire = idco;
    }
    public String getmdp() {
        return mdpPartenaire;
    }

    public void setmdp(String mdp) {
        this.mdpPartenaire = mdp;
    }
}
