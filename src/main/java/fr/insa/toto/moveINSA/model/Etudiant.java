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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lbsb
 */
public class Etudiant {
        private int idEtudiant;
    private String ine; // voir si fusionne ine et idetudiant
    private String nom ; // voir si besoin prénom
    private String classe ;
    private int classement ;
    private String idcoEtudiant ;
    private String mdpEtudiant ;
    

    /**
     * création d'une nouvelle Offre en mémoire, non existant dans la Base de
     * donnée.
     */
    public Etudiant(String ine, String nom, String classe, int classement, String idco, String mdp) {
        this(-1, ine, nom, classe, classement, idco, mdp);
    }

    /**
     * création d'une Offre retrouvée dans la base de donnée.
     */
    public Etudiant(int id, String ine, String nom,String classe, int classement, String idco, String mdp) {
        this.idEtudiant = id;
        this.ine = ine;
        this.nom = nom;
        this.classe = classe;
        this.classement = classement;
        this.idcoEtudiant = idco;
        this.mdpEtudiant = mdp;
    }

    @Override
    public String toString() {
        return "Etudiant{" + "idEtudiant=" + this.getId() + "; ine=" + ine + " ; nom=" + nom + " ; classe=" + classe + " ; classement=" + classement + " ; idoc=" + idcoEtudiant + " ; mdp=" + mdpEtudiant +'}';
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
                "insert into etudiant (ine,nom,classe, classement,idco,mdp) values (?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.nom);
            insert.setString(3, this.classe);
            insert.setInt(4, this.classement);
            insert.setString(5, this.idcoEtudiant);
            insert.setString(6, this.mdpEtudiant);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.idEtudiant = rid.getInt(1);
                return this.getId();
            }
        }
    }

    public static List<Etudiant> tousLesEtudiants(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select ine,nom,classe,classement,idco,mdp from etudiant")) {
            ResultSet rs = pst.executeQuery();
            List<Etudiant> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Etudiant(rs.getString(1), rs.getString(2), rs.getString(3),rs.getInt(4),rs.getString(5),rs.getString(6)));
            }
            return res;
        }
    }

    public static int creeConsole(Connection con) throws SQLException {
        String nom = ConsoleFdB.entreeString("nom de l'étudiant : ");
        String ine = ConsoleFdB.entreeString("ine de l'étudiant : ");
        String classe = ConsoleFdB.entreeString("classe de l'étudiant : ");
        int classement = ConsoleFdB.entreeInt("classement de l'étudiant : ");
        String idco = ConsoleFdB.entreeString("idco de l'étudiant : ");
        String mdp = ConsoleFdB.entreeString("mdp de l'étudiant : ");
        Etudiant nouveau = new Etudiant(ine,nom,classe,classement, idco,mdp);
        return nouveau.saveInDB(con);
    }

    /**
     * @return the id
     */
    public int getId() {
        return idEtudiant;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public String getIne() {
        return ine;
    }

    public String getNom() {
        return nom;
    }

    public String getClasse() {
        return classe;
    }

    public int getClassement() {
        return classement;
    }

    public String getIdcoEtudiant() {
        return idcoEtudiant;
    }

    public String getMdpEtudiant() {
        return mdpEtudiant;
    }
    
    
    
    public static void modifConsoleparSRI(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
        "update etudiant set ine = ? , nom = ? , classe = ? , classement = ? , idcoEtudiant = ? , where ine = ?")){
            String ine = ConsoleFdB.entreeString("new ine:") ;
            String nom = ConsoleFdB.entreeString("new nom:") ;
            String classe = ConsoleFdB.entreeString("new classe:") ;
            int classement = ConsoleFdB.entreeInt("new classement:") ;
            String idcoEtudiant = ConsoleFdB.entreeString("new idcoEtudiant:") ;
            String lastine = ConsoleFdB.entreeString("last ine:") ;
            update.setString(1, ine);
            update.setString(2, nom);
            update.setString(3, classe);
            update.setInt(4, classement);
            update.setString(5, idcoEtudiant);
            update.setString(6, lastine);
            update.executeUpdate();
        }
    System.out.println("Profil étudiant modifié avec succès !");
    }
    public static void modifConsolemdpEtudiant (Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
        "update etudiant set mdpEtudiant = ?")) {
            String mdpEtudiant = ConsoleFdB.entreeString("new mdpEtudiant:") ;
            update.setString(1, mdpEtudiant);
            update.executeUpdate();
        }
    }
    public static void supprConsole (Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from etudiant where ine = ?")){ 
                 String ine = ConsoleFdB.entreeString("ine de l'étudiant à supprimer: ");
                 update.setString(1,ine);
                 update.execute();       
                 }
        System.out.println("Etudiant supprimé avec succès !");
    }
          public static void supprallConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from etudiant")){
                 update.executeUpdate();       
                 }
        System.out.println("Tous les etudiants ont ete supprimés avec succès !");
    }
}
