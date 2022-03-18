package Vue;

import Modele.Jeu;
import Modele.RedacteurNiveau;
import Patterns.Observateur;

public class VueTextuelle implements Observateur {
	Jeu j;
	RedacteurNiveau r;

	VueTextuelle(Jeu j) {
		this.j = j;
		j.ajouteObservateur(this);
		r = new RedacteurNiveau(System.out);
	}

	@Override
	public void metAJour() {
		r.ecrisNiveau(j.niveau());
	}
}
