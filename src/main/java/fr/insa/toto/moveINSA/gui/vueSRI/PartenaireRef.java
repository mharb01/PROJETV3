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

import fr.insa.toto.moveINSA.gui.vueSRI.PartenaireGrid;
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
import fr.insa.toto.moveINSA.model.Partenaire;
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
@Route(value = "SRI/vue/partenaire/rechercher/reference", layout = MainLayoutSRI.class)
@Component
public class PartenaireRef extends VerticalLayout {
    
    private TextField tfRef = new TextField("Référence");
    private Button bSave;
    private PartenaireGrid partGrid;
    
    @Autowired
    public PartenaireRef() {
        
        this.add(new H3("Recherche du partenaire par référence"));
    this.add(this.tfRef);
    this.bSave = new Button("Rechercher", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                if (partGrid != null) {
                this.remove(partGrid);  }  //Efface la liste précédente 
                               
                partGrid = new PartenaireGrid(PartenaireRef.rechercherRef(con, this.tfRef.getValue()));
                this.add(partGrid);
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.bSave);
        
    }
    
    public static List<Partenaire> rechercherRef(Connection con, String ref) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "select id,refPartenaire,pays, idcoPartenaire, mdpPartenaire from partenaire where refPartenaire = ?  ")) {
        pst.setString(1,ref);
        ResultSet rs = pst.executeQuery();
            List<Partenaire> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Partenaire(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            System.out.println("Voici le partenaire recherché: ");
            return res;
    }   
    }
}
