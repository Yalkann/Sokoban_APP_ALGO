package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonRefaire extends JButton implements Observateur {
	Jeu jeu;

	BoutonRefaire(String s, String c, CollecteurEvenements controle, Jeu j) {
		super(s);
		jeu = j;
		addActionListener(new AdaptateurCommande(controle, c));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setFocusable(false);
		jeu.ajouteObservateur(this);
	}

	@Override
	public void metAJour() {
		setEnabled(jeu.niveau().peutRefaire());
	}
}
