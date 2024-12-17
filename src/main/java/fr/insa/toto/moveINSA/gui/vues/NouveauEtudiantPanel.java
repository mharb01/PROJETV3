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
package fr.insa.toto.moveINSA.gui.vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "etudiant/nouveau", layout = MainLayoutSRI.class)
public class NouveauEtudiantPanel extends VerticalLayout{
    
    private Etudiant nouveau;
    private EtudiantForm fEtudiant;
    private Button bSave;
    
    public NouveauEtudiantPanel() {
        this.add(new H3("Creation d'un nouvel étudiant"));
        this.nouveau = new Etudiant("" , "", "", 1, "", "");
        this.fEtudiant = new EtudiantForm(this.nouveau, true);
        this.bSave = new Button("Sauvegarder", (t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                this.fEtudiant.updateModel();
                 this.nouveau.saveInDB(con);
                 Notification.show("Nouvel étudiant enregistré !");
            } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
        });
        this.add(this.fEtudiant,this.bSave);
    }
    
}
    
    

