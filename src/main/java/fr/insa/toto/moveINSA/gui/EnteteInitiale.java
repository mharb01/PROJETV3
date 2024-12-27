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
package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class EnteteInitiale extends HorizontalLayout {

    private TextField tfNom;
    private TextField tfPays;
    private Button bLogin;
    private Button bLogout;

    public EnteteInitiale() {
        this.setWidthFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        
        Image insa = new Image("http://www.alsacetech.org/wp-content/uploads/2017/08/Logo_INSAStrasbourgDeveloppe-quadri_marge.jpg", "Meilleure ecole");
         add(insa);
        insa.setWidth("300px");
        insa.setHeight("75px");
        add(insa);
        
        Image logo = new Image("icons/movinsa.jpg", "Meilleur projet");
        logo.setWidth("175px");
        logo.setHeight("75px");
        add(logo); 
    
    }

}
