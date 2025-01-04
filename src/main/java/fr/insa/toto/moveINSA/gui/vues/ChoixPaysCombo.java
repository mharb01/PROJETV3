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

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.management.Query.value;
/**
 *
 * @author HP
 */
public class ChoixPaysCombo extends ComboBox<String> {
    
    public ChoixPaysCombo() {
       super("Pays");
        try (Connection con = ConnectionPool.getConnection()) {
            List<Partenaire> tous = Partenaire.tousLesPartaires(con);
            
            Set<String> PaysUniques = new HashSet<>();
            for (Partenaire part : tous) {
                PaysUniques.add(part.getPays());
            }
            this.setItems(PaysUniques);
            if (!PaysUniques.isEmpty()) {
                this.setValue(PaysUniques.iterator().next());
            }
        } catch (SQLException ex) {
            Notification.show("Probleme interne : " + ex.getLocalizedMessage());
        } 
    }
    
}
