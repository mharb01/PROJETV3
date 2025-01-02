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
package fr.insa.toto.moveINSA.gui.vueetudiant;

import com.vaadin.flow.component.Text;
import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutEt;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vueetudiant.CandidatureEtGrid;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;
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
@PageTitle("MoveINSA")
@Route(value = "etudiant/vue/candidature/liste", layout = MainLayoutEt.class)
public class CandidatureEtPanel extends VerticalLayout {
    private CandidatureEtGrid CandidatureEtGrid;
    
    public CandidatureEtPanel() {
        
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        String loggedEtudiantNom = sessionInfo.getLoggedEtudiantNom();
        String loggedEtudiantINE = sessionInfo.getLoggedEtudiantINE();
        
        this.add(new H3("Voici vos candidatures, " + loggedEtudiantNom + " !"));
        try (Connection con = ConnectionPool.getConnection()) {                 
                 if (CandidatureEtGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                 CandidatureEtGrid = new CandidatureEtGrid(CandidatureEtPanel.MesCandidatures(con, loggedEtudiantINE));
                 this.add(CandidatureEtGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             }
    
    
    public static List<Candidature> MesCandidatures(Connection con, String INE) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select idCandidature,INE,idOffreMobilite,date, idPartenaire from candidature where INE = ? ")) {
           pst.setString(1, INE);
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getDate(4), rs.getInt(5)));
            }
            return res;
        }
    }   
    }

