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
package fr.insa.toto.moveINSA.gui.vueSRI;

import fr.insa.toto.moveINSA.gui.vueSRI.CandidatureGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
import fr.insa.toto.moveINSA.gui.vues.ChoixPartenaireCombo;
import fr.insa.toto.moveINSA.model.Candidature;
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
@Route(value = "SRI/vue/candidature/rechercher/partenaire", layout = MainLayoutSRI.class)
@Component
public class CandidaturePart extends VerticalLayout {
    
    private ChoixPartenaireCombo cpCombo; 
    private Button bSave;
    private CandidatureGrid CandidatureGrid;
    private int idPart;
    
    @Autowired
    public CandidaturePart() {
        
    this.add(new H3("Recherche des candidatures par référence"));
    this.cpCombo = new ChoixPartenaireCombo();
    this.add(this.cpCombo);
    this.bSave = new Button("Rechercher", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                this.idPart = this.cpCombo.getValue().getId();
                
                if (CandidatureGrid != null) {
                this.remove(CandidatureGrid);  }  //Efface la liste précédente 
                               
                CandidatureGrid = new CandidatureGrid(CandidaturePart.CandidaturesPart(con, this.idPart));
                this.add(CandidatureGrid);
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.bSave);
        
    }
    
    public static List<Candidature> CandidaturesPart(Connection con, int idPart) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select idCandidature,ine,idOffreMobilite,date,idPartenaire from candidature where idPartenaire = ?")) {
           pst.setInt(1, idPart);
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getInt(5)));
            }
            return res;
        }
    }    
}
