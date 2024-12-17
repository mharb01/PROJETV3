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

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.util.List;

/**
 *
 * @author HP
 */
public class EtudiantGrid extends Grid <Etudiant>{
            public EtudiantGrid(List<Etudiant> etudiants) {
                this.setColumnReorderingAllowed(true);
                this.addColumn(Etudiant::getId).setHeader("idEtudiant").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getIne).setHeader("INE").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getNom).setHeader("Nom").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getClasse).setHeader("Classe").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getClassement).setHeader("Classement").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getIdcoEtudiant).setHeader("idcoEtudiant").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getMdpEtudiant).setHeader("Mdp").setSortable(true).setResizable(true);
                this.setItems(etudiants);
            }
    

}
