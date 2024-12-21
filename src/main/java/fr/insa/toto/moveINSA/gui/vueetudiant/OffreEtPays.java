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

/**
 *
 * @author HP
 */

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
import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantClasse;
import fr.insa.toto.moveINSA.gui.vues.ChoixClasseCombo;
import fr.insa.toto.moveINSA.gui.vueetudiant.OffreEtGrid;
import fr.insa.toto.moveINSA.gui.vueSRI.PartenaireGrid;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import fr.insa.toto.moveINSA.gui.vues.ChoixPaysCombo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@PageTitle("MoveINSA")
@Route(value = "etudiant/vue/offre/rechercher/pays", layout = MainLayoutEt.class)
@Component
public class OffreEtPays extends VerticalLayout {
    private ChoixPaysCombo cpCombo;
    private Button bSave;
    private String pays;
    private OffreEtGrid offreGrid;
   
    @Autowired
    public OffreEtPays(){
        
        this.add(new H3("Recherche d'offres par pays"));
        
       this.cpCombo = new ChoixPaysCombo();
       this.add(this.cpCombo);
       this.bSave = new Button("Rechercher", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                
                this.pays = this.cpCombo.getValue().getPays();
                
                if (offreGrid != null) {
                this.remove(offreGrid);  }  //Efface la liste précédente 
               
                offreGrid =new OffreEtGrid(OffreEtPays.rechercherPays(con, this.pays));
                this.add(offreGrid);
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.bSave); 
    }
    
    public static List<OffreMobilite> rechercherPays(Connection con, String pays) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select offremobilite.id, offremobilite.nbrplaces, offremobilite.proposepar, offremobilite.classe, offreMobilite.annee from offremobilite,partenaire where offremobilite.proposepar = partenaire.id and partenaire.pays = ? ")) {
        pst.setString(1,pays);
        ResultSet rs = pst.executeQuery();
            List<OffreMobilite> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new OffreMobilite(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici les offres venant provenant du pays: " + pays);
            return res;
    }   
    }
}
