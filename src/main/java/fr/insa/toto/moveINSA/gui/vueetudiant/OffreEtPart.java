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

import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutEt;
import fr.insa.toto.moveINSA.model.OffreMobilite;
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
@Route(value = "etudiant/vue/offre/rechercher/partenaire", layout = MainLayoutEt.class)
@Component
public class OffreEtPart extends VerticalLayout {
    
    private TextField tfPart = new TextField("Nom du partenaire");
    private Button bSave;
    private OffreEtGrid offreGrid;
    
    @Autowired
    public OffreEtPart() {
        
     this.add(new H3("Recherche d'offre par partenaire"));
    this.add(this.tfPart);
    this.bSave = new Button("Rechercher", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                if (offreGrid != null) {
                this.remove(offreGrid);  }  //Efface la liste précédente 
                               
                offreGrid = new OffreEtGrid(OffreEtPart.rechercherPart(con, this.tfPart.getValue()));
                this.add(offreGrid);
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.bSave);
    }
    
    public static List<OffreMobilite> rechercherPart(Connection con, String part) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offremobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.refPartenaire = ? ")) {
        pst.setString(1,part);
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici les offres provenant de l'école: " + part);
            return res;
    }   
    }   
    
}
