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
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import static javax.management.Query.value;
/**
 *
 * @author HP
 */
public class ChoixClasseCombo extends ComboBox<String> {
    
    public ChoixClasseCombo() {
        
        super("Classe");
        try (Connection con = ConnectionPool.getConnection()) {
            List<Etudiant> tous = Etudiant.tousLesEtudiants(con);
            
            // Extraire les classes uniques
            Set<String> classesUniques = new HashSet<>();
            for (Etudiant etudiant : tous) {
                classesUniques.add(etudiant.getClasse()); // Utiliser un Set pour éviter les doublons
            }
            this.setItems(classesUniques);
            if (!classesUniques.isEmpty()) {
                this.setValue(classesUniques.iterator().next()); // Sélectionner la première classe unique
            }
        } catch (SQLException ex) {
            Notification.show("Probleme interne : " + ex.getLocalizedMessage());
        }
    }
    
}
