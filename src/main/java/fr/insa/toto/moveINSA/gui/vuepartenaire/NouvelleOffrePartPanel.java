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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutPart;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vues.ChoixPartenaireCombo;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "partenaire/vue/offres/nouveau",layout= MainLayoutPart.class)
public class NouvelleOffrePartPanel extends VerticalLayout {
   
    private Integer partId;
    private IntegerField ifPlaces;
    private TextField tfClasse; 
    private TextField tfAnnee;
    private Button bSave;
    
    public NouvelleOffrePartPanel () {
        
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        Integer partId = sessionInfo.getPartId();
        String partRef = sessionInfo.getPartRef();
        
        this.add(new H3("Create a new offer ( From " + partRef + " )"));
        
        this.ifPlaces = new IntegerField("Number of places");
        this.tfClasse = new TextField("Class targeted");
        this.tfAnnee = new TextField("Year");
        this.bSave = new Button("Save");
        this.bSave.addClickListener((t) -> {
                {
                Integer places = this.ifPlaces.getValue();
                String classe = this.tfClasse.getValue();
                String annee = this.tfAnnee.getValue();
                if (places == null || places <= 0) {
                    Notification.show("Enter a valid number");
                } else {
                    int id = partId.intValue();
                    OffreMobilite nouvelle = new OffreMobilite(places, id, classe, annee);
                    try (Connection con = ConnectionPool.getConnection()) {
                        nouvelle.saveInDB(con);
                        Notification.show("New offer saved !");
                    } catch (SQLException ex) {
                        Notification.show("Internal problem : " + ex.getLocalizedMessage());
                    }
                }
            }
        });
        this.add(this.ifPlaces, this.tfClasse, this.tfAnnee, this.bSave);
    }
}

