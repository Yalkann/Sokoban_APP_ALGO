package Vue;

import Modele.Jeu;
import Modele.RedacteurNiveau;

import java.util.Scanner;

public class InterfaceTextuelle implements InterfaceUtilisateur {
	Jeu j;
	CollecteurEvenements c;

	InterfaceTextuelle(Jeu j, CollecteurEvenements c) {
		this.j = j;
		this.c = c;
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		InterfaceTextuelle i = new InterfaceTextuelle(j, c);
		c.fixerInterfaceUtilisateur(i);
		new VueTextuelle(j);
		i.executerJeu();
	}

	void executerJeu() {
		c.fixerInterfaceUtilisateur(this);
		Scanner s = new Scanner(System.in);
		while (s.hasNextLine()) {
			String ligne = s.nextLine();
			if (!c.commande(ligne)) {
				System.out.println("Commande inconnue");
			}
		}
	}

	@Override
	public void basculePleinEcran() {
		System.out.println("Plein ecran non implémenté pour l'interface textuelle");
	}

	@Override
	public void decale(double dL, double dC, int l, int c) {
	}

	@Override
	public void etapePousseur() {
	}

	@Override
	public void changeDirectionPousseur(int dL, int dC) {
	}

	@Override
	public void changeEtatIA(boolean e) {
	}

	@Override
	public void changeEtatAnim(boolean e) {
	}
}
