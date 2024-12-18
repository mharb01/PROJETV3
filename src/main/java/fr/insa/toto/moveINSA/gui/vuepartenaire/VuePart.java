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

import fr.insa.toto.moveINSA.gui.MainLayoutPart;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 *
 * @author HP
 */
@Route(value = "partenaire/vue",layout= MainLayoutPart.class)
public class VuePart extends VerticalLayout{
    public VuePart () {
        
    this.setWidthFull();
    this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
     
    
    Image partenaire = new Image("https://cdn-icons-png.flaticon.com/512/167/167707.png", "Partenaire");
    partenaire.setWidth("100px");
    partenaire.setHeight("100px");
    add(partenaire);
    
    this.add(new H3("Mov'INSA: l'application de l'avenir"));
    this.add(new H3("Bienvenu cher étudiant !"));
    this.add(new H3("Que voulez vous faire ?"));
    }
}
