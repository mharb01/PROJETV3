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
import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantClasse;
import fr.insa.toto.moveINSA.gui.vues.ChoixClasseCombo;
import fr.insa.toto.moveINSA.gui.vueSRI.OffreGrid;
import fr.insa.toto.moveINSA.gui.vueSRI.PartenaireGrid;
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
/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "SRI/vue/partenaire/rechercher/pays", layout = MainLayoutSRI.class)
@Component
public class PartenairePays extends VerticalLayout {
    private ChoixPaysCombo cpCombo;
    private Button bSave;
    private String pays;
    private PartenaireGrid partGrid;
    
    @Autowired
    public PartenairePays() {
        
        this.add(new H3("Recherche des partenaires par pays"));
        
       this.cpCombo = new ChoixPaysCombo();
       this.add(this.cpCombo);
       this.bSave = new Button("Rechercher", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                
                this.pays = this.cpCombo.getValue().getPays();
                
                if (partGrid != null) {
                this.remove(partGrid);  }  //Efface la liste précédente 
               
                partGrid =new PartenaireGrid(PartenairePays.rechercherPays(con, this.pays));
                this.add(partGrid);
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.bSave); 
    }
    
    public static List<Partenaire> rechercherPays(Connection con, String pays) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select id,refPartenaire,pays, idcoPartenaire, mdpPartenaire from partenaire where pays = ? ")) {
        pst.setString(1,pays);
        ResultSet rs = pst.executeQuery();
            List<Partenaire> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Partenaire(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici les partenaires venant provenant du pays: " + pays);
            return res;
    }  
    }
}
