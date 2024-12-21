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
import static fr.insa.toto.moveINSA.model.SRI.tousLesSRI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class SRI {

    /**
     *
     * @param con
     * @return
     */
    private int idSRI;
    private String idcoSRI;
    private String refSRI;
    private String mdpSRI;
    
    /**
     * création d'un nouveau membre SRI en mémoire, non existant dans la Base de
     * donnée.
     *
     * @param refSRI
     * @param mdpSRI
     * @param idcoSRI
     */
    public SRI( String idcoSRI, String refSRI, String mdpSRI) {
        this(-1, idcoSRI, refSRI, mdpSRI);
    }
    
    /**
     * création d'un membre SRI retrouvé dans la base de donnée.
     *
     * @param refSRI
     * @param idSRI
     * @param mdpSRI
     * @param idcoSRI
     */
    public SRI(int idSRI, String idcoSRI, String refSRI, String mdpSRI) {
        this.idSRI = idSRI;
        this.idcoSRI = idcoSRI;
        this.refSRI = refSRI;
        this.mdpSRI = mdpSRI;
    }
    
     public int getId() {
        return idSRI;
    }
     
     public String getidco(){
         return idcoSRI;   
     }
     
     public String getmdp() {
        return mdpSRI;
    }
     
     public String getrefSRI() {
        return refSRI;
    }
     
    public void setrefSRI(String refSRI) {
        this.refSRI = refSRI;
    }
    
    public void setidcoSRI(String idcoSRI) {
        this.idcoSRI= idcoSRI;
    }
    
    public void setmdpSRI(String mdpSRI) {
        this.mdpSRI = mdpSRI;
    }
    
    @Override
    public String toString() {
        return "Membre SRI{" + "idSRI =" + this.getId() +" ; idcoSRI = " + this.getidco() + " ; refSRI = " + this.getrefSRI() + " ; mdpSRI = " + this.getmdp() +'}';
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
                "insert into SRI (idcoSRI,refSRI,mdpSRI) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.idcoSRI);
            insert.setString(2, this.refSRI);
            insert.setString(3, this.mdpSRI);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.idSRI = rid.getInt(1);
                return this.getId();
            }
        }
    }
    
    public static List<SRI> tousLesSRI(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select idSRI, idcoSRI, refSRI, mdpSRI from SRI")) {
            ResultSet rs = pst.executeQuery();
            List<SRI> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new SRI(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            return res;
        }
    }
    
    public static int creeConsole(Connection con) throws SQLException {
        String idPidco = ConsoleFdB.entreeString("idcoSRI: ");
        String idPref = ConsoleFdB.entreeString("refSRI : ");
        String idPmdp = ConsoleFdB.entreeString("mdpSRI : ");
        SRI nouveau = new SRI(idPidco ,idPref, idPmdp);
        return nouveau.saveInDB(con);
    }
    
    public static SRI selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez un membre SRI :",
                tousLesSRI(con), (elem) -> elem.getrefSRI());
    }
    
    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void modifConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "UPDATE SRI set idcoSRI = ? , refSRI = ? , mdpSRI = ?  WHERE refSRI = ?  ")){ 
                 String idcoSRI = ConsoleFdB.entreeString("new idcoSRI: ");
                 String refSRI = ConsoleFdB.entreeString("new refSRI: ");
                 String mdpSRI = ConsoleFdB.entreeString("new mdpSRI: ");
                 SRI ancienSRI = selectInConsole(con);
                 String lastrefSRI = ancienSRI.getrefSRI();
                 update.setString(1,idcoSRI);
                 update.setString(2,refSRI);
                 update.setString(3,mdpSRI);
                 update.setString(4,lastrefSRI);
                 update.executeUpdate();       
                 }
        System.out.println("Membre modifie avec succes!");
    }
    
    public static void suppConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from SRI where refSRI = ? ")){ 
                 SRI membre = selectInConsole(con);
                 String refSRI = membre.getrefSRI();
                 update.setString(1,refSRI);
                 update.execute();       
                 }
        System.out.println("Membre supprime avec succes !");
    }
    
        public static void suppALLConsole(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from SRI")){
                 update.executeUpdate();       
                 }
        System.out.println("Tous les membres ont ete supprimes avec succes !");
    }
    
   
    
}

