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

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import fr.insa.toto.moveINSA.model.Etudiant;

/**
 *
 * @author HP
 */
public class EtudiantForm extends FormLayout {
    
    private Etudiant model;
    private TextField tfIne = new TextField("INE");    
    private TextField tfNom = new TextField("Nom");
    private TextField tfClasse = new TextField("Classe");
    private NumberField nfClassement = new NumberField("Classement");
    private TextField tfIdcoEtudiant = new TextField("IdcoEtudiant");
    private TextField tfMdpEtudiant = new TextField("Mot de Passe");
    
    public EtudiantForm(Etudiant model, boolean modifiable) {
        
        
        this.model = model;
        this.setEnabled(modifiable);
        this.updateView();
        this.add(this.tfIne);
        this.add(this.tfNom);
        this.add(this.tfClasse);
        this.add(this.nfClassement);
        this.add(this.tfIdcoEtudiant);
        this.add(this.tfMdpEtudiant);
        
        
    }
   
    public void updateModel() {
        
        this.model.setIne(this.tfIne.getValue());
        this.model.setNom(this.tfNom.getValue());
        this.model.setClasse(this.tfClasse.getValue());
        this.model.setClassement(this.nfClassement.getValue().intValue());
        this.model.setIdcoEtudiant(this.tfIdcoEtudiant.getValue());
        this.model.setMdpEtudiant(this.tfMdpEtudiant.getValue());        
    }
    
    public void updateView() {
        this.tfIne.setValue(this.model.getIne());
        this.tfNom.setValue(this.model.getNom());
        this.tfClasse.setValue(this.model.getClasse());
        this.nfClassement.setValue((double) this.model.getClassement());
        this.tfIdcoEtudiant.setValue(this.model.getIdcoEtudiant());
        this.tfMdpEtudiant.setValue(this.model.getMdpEtudiant());
    }

}
