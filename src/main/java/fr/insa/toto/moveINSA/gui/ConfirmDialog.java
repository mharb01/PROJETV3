package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Span;

public class ConfirmDialog extends Dialog {
    public ConfirmDialog(String message, Runnable onConfirm) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        Span question = new Span(message);
        question.getStyle().set("font-size", "16px"); 

        Button yesButton = new Button("Oui", event -> {
            System.out.println("Bouton 'Oui' cliqué !");
            onConfirm.run(); 
            close();
        });

        Button cancelButton = new Button("Annuler", event -> close());

        VerticalLayout layout = new VerticalLayout(question, yesButton, cancelButton);
        add(layout);
    }
}

