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
package fr.insa.toto.moveINSA.gui;

import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SimpleAuthService {

    public static Boolean authenticate(String username, String password, String role) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
             System.out.println("Authentification pour le rôle: " + role);
            System.out.println("Identifiants: " + username + ", " + password);  


        if ("Etudiant".equals(role)){
            String rech = "SELECT 1 FROM etudiant WHERE idcoEtudiant =? AND mdpEtudiant = ? LIMIT 1";
            try (PreparedStatement pst = connection.prepareStatement(rech)){
                pst.setString(1, username);
                pst.setString(2, password);
                try (ResultSet rs = pst.executeQuery()){
                    boolean exists = rs.next();
                    System.out.println("Résultat de la requête: " + exists); 
                    return exists;
                }   
            }
        } else if ("Partenaire".equals(role)){
            String rech = "SELECT 1 FROM partenaire WHERE idcoPartenaire =? AND mdpPartenaire = ? LIMIT 1";
            try (PreparedStatement pst = connection.prepareStatement(rech)){
                pst.setString(1, username);
                pst.setString(2, password);
                try (ResultSet rs = pst.executeQuery()){
                    boolean exists = rs.next();
                    System.out.println("Résultat de la requête: " + exists);
                    return exists;
                }
            }       
        } else if ("SRI".equals(role)){
            String rech = "SELECT 1 FROM SRI WHERE idcoSRI =? AND mdpSRI = ? LIMIT 1";
            try (PreparedStatement pst = connection.prepareStatement(rech)){
                pst.setString(1, username);
                pst.setString(2, password);
                try (ResultSet rs = pst.executeQuery()){
                    boolean exists = rs.next();
                    System.out.println("Résultat de la requête: " + exists);
                    return exists;
                }
            }       
        } else {
            return false;
        }
    } catch (SQLException ex) {
        System.err.println("Erreur dans la requête: " + ex.getMessage());
        throw ex; 
    }
}
           /* if ("Etudiant".equals(role)){
                String rech = "SELECT 1 FROM etudiant WHERE idcoEtudiant =? AND mdpEtudiant = ? LIMIT 1";
                try (PreparedStatement pst = connection.prepareStatement(rech)){
                    pst.setString(1, username);
                    pst.setString(2, password);
                    try (ResultSet rs = pst.executeQuery()){
                        return rs.next();
                    }   
                }
            } else if ("Partenaire".equals(role)){
                String rech = "SELECT 1 FROM partenaire WHERE idcoPartenaire =? AND mdpPartenaire = ? LIMIT 1";
                try (PreparedStatement pst = connection.prepareStatement(rech)){
                    pst.setString(1, username);
                    pst.setString(2, password);
                    try (ResultSet rs = pst.executeQuery()){
                        return rs.next();
                    }
                }       
            } else if ("SRI".equals(role)){
                String rech = "SELECT 1 FROM SRI WHERE idcoSRI =? AND mdpSRI = ? LIMIT 1";
                try (PreparedStatement pst = connection.prepareStatement(rech)){
                    pst.setString(1, username);
                    pst.setString(2, password);
                    try (ResultSet rs = pst.executeQuery()){
                        return rs.next();
                    }
                }       
            } else {
                return false;
            }*/
            
            
            
            
    }
      

/*PreparedStatement stmt;
            String query = switch (role) {
                case "Etudiant" -> "SELECT 'Etudiant' AS role FROM etudiant WHERE idcoEtudiant = ? AND mdpEtudiant = ?";
                case "Partenaire" -> "SELECT 'Partenaire' AS role FROM partenaire WHERE idcoPartenaire = ? AND mdpPartenaire = ?";
                case "SRI" -> "SELECT 'SRI' AS role FROM SRI WHERE idcoSRI = ? AND mdpSRI = ?";
                default -> null;
            };

            if (query != null) {
                stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);    
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("role");
                } else {
                    System.out.println("Aucun utilisateur trouvé pour ces identifiants");
                }
            }
        } catch (SQLException ex){
            System.err.println("Erreur: " + ex.getMessage());
        }
        return null; */
