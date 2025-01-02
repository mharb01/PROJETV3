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
package fr.insa.toto.moveINSA.gui.vuepartenaire;

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
import fr.insa.toto.moveINSA.gui.MainLayoutPart;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vueetudiant.CandidatureEtGrid;
import fr.insa.toto.moveINSA.gui.vueetudiant.CandidatureEtPanel;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "partenaire/vue/candidature/liste", layout = MainLayoutPart.class)
public class CandidaturePartPanel extends VerticalLayout {
    
    private CandidaturePartGrid CandidaturePartGrid;
    
    public CandidaturePartPanel() {
        
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        String partRef = sessionInfo.getPartRef();
        int partId = sessionInfo.getLoggedPartId();
        
        this.add(new H3("Here are all your applications, " + partRef + " !"));
        try (Connection con = ConnectionPool.getConnection()) {                 
                 if (CandidaturePartGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                 CandidaturePartGrid = new CandidaturePartGrid(CandidaturePartPanel.MesCandidatures(con, partId));
                 this.add(CandidaturePartGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             }
    
    
    public static List<Candidature> MesCandidatures(Connection con, int id) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select idCandidature,INE,idOffreMobilite,date, idPartenaire from candidature where idPartenaire = ? ")) {
           pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getDate(4), rs.getInt(5)));
            }
            return res;
        }
    }   
    }
    
    

