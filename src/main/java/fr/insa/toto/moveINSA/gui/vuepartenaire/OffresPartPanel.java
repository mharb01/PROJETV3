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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ColumnDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.GridDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import fr.insa.toto.moveINSA.gui.MainLayoutPart;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vuepartenaire.OffresPartGrid;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author HP
 */

@PageTitle("MoveINSA")
@Route(value = "partenaire/vue/offres/liste", layout = MainLayoutPart.class)
public class OffresPartPanel extends VerticalLayout {
    
    public static class IntAsIcon extends HorizontalLayout {

        public IntAsIcon(int nbr) {
            for (int i = 0; i < nbr; i++) {
                this.add(new Icon(VaadinIcon.EXIT));
            }
        }
    }
    
    private OffresPartGrid offresGrid;

    public OffresPartPanel () {
        
        try (Connection con = ConnectionPool.getConnection()) {                 
                 if (offresGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Here's all your offers"));
                 offresGrid = new OffresPartGrid(OffreMobilite.toutesLesOffres(con));
                 this.add(offresGrid);
                } catch (SQLException ex) {
                    System.out.println("Problem : " + ex.getLocalizedMessage());
                    Notification.show("Problem : " + ex.getLocalizedMessage());
                }
    }

}

