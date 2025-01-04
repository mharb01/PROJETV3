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

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.toto.moveINSA.gui.VuePrincipale;
import fr.insa.toto.moveINSA.gui.jeu.BoiteACoucou;
import fr.insa.toto.moveINSA.gui.jeu.TrouveEntier;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestDataGrid;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestGridDirect;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestResultSetGrid;
import fr.insa.toto.moveINSA.gui.RAZBdDPanel;
import fr.insa.toto.moveINSA.gui.TestDriverPanel;

/**
 *
 * @author HP
 */
   public class MenuGaucheSRI extends SideNav {

    public MenuGaucheSRI () {
        SideNavItem main = new SideNavItem("Menu Principal",VuePrincipale.class);
        SideNavItem partenaires = new SideNavItem("Partenaires");
        partenaires.addItem(new SideNavItem("liste", PartenairesPanel.class));
        partenaires.addItem(new SideNavItem("nouveau", NouveauPartenairePanel.class));
        SideNavItem offres = new SideNavItem("Offres");
        offres.addItem(new SideNavItem("liste", OffresPanel.class));
        offres.addItem(new SideNavItem("nouvelle", NouvelleOffrePanel.class));
        SideNavItem etudiant = new SideNavItem("Étudiant");
        etudiant.addItem(new SideNavItem("liste", EtudiantPanel.class));
        etudiant.addItem(new SideNavItem("nouveau", NouveauEtudiantPanel.class));   
        SideNavItem candidature = new SideNavItem("Candidatures");
        candidature.addItem(new SideNavItem("liste", CandidaturePanel.class));
        
        
        SideNavItem debug = new SideNavItem("debug");
        debug.addItem(new SideNavItem("test driver", TestDriverPanel.class));
        debug.addItem(new SideNavItem("raz BdD", RAZBdDPanel.class));
        debug.addItem(new SideNavItem("test ResultSetGrid", TestResultSetGrid.class));
        debug.addItem(new SideNavItem("test DataGrid", TestDataGrid.class));
        debug.addItem(new SideNavItem("test Grid direct", TestGridDirect.class));
//        SideNavItem jeux = new SideNavItem("jeux");
//        jeux.addItem(new SideNavItem("boite à coucou", BoiteACoucou.class));
//        jeux.addItem(new SideNavItem("trouve", TrouveEntier.class));
       this.addItem(main,etudiant,partenaires,offres,candidature,debug);
    }
}

