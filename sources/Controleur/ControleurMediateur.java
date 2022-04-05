package Controleur;

import Global.Configuration;
import Modele.Coup;
import Modele.Deplacement;
import Modele.Jeu;
import Structures.Iterateur;
import Structures.Sequence;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements {
	Jeu jeu;
	InterfaceUtilisateur inter;
	Sequence<Animation> animations;
	Animation mouvement;
	boolean animationsActives, animationsSupportees;
	boolean iaActive;
	Animation ia;

	public ControleurMediateur(Jeu j) {
		jeu = j;
		animations = Configuration.instance().nouvelleSequence();
		mouvement = null;
		animationsActives = Boolean.parseBoolean(Configuration.instance().lis("AnimationsActives"));
		animationsSupportees = false;
		iaActive = Boolean.parseBoolean(Configuration.instance().lis("IAActive"));
		ia = new AnimationIA(this, j);
	}

	@Override
	public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
		inter = i;
		inter.changeEtatAnim(animationsActives);
		inter.changeEtatIA(iaActive);
		animations.insereQueue(new AnimationPousseur(inter));
	}

	@Override
	public void tictac() {
		animationsSupportees = true;
		if (iaActive && mouvement == null) {
			ia.tictac();
		}
		if (animationsActives || mouvement != null) {
			Iterateur<Animation> it = animations.iterateur();
			while (it.aProchain()) {
				Animation anim = it.prochain();
				anim.tictac();
				if (anim.estTerminee()) {
					it.supprime();
					if (anim == mouvement) {
						mouvement = null;
						testFinNiveau();
					}
				}
			}
		}
	}

	void jouerCoup(Coup cp) {
		jeu.jouerCoup(cp);
		animeCoup(cp,1);
		if (!animationsActives)
			testFinNiveau();
	}

	void animeCoup(Coup cp, int direction) {
		if (cp != null) {
			Iterateur<Deplacement> it = cp.deplacements().iterateur();
			while (it.aProchain()) {
				Deplacement d = it.prochain();
				int pousseurL = jeu.pousseurL();
				int pousseurC = jeu.pousseurC();
				int dest = (direction + 1) / 2;
				if ((d.l[dest] == pousseurL) && (d.c[dest] == pousseurC)) {
					int dL = d.l[1] - d.l[0];
					int dC = d.c[1] - d.c[0];
					inter.changeDirectionPousseur(dL, dC);
				}
			}
			if (animationsActives && animationsSupportees) {
				mouvement = new AnimationCoup(cp, inter, direction);
				animations.insereQueue(mouvement);
			}
		}
	}

	void deplace(int dL, int dC) {
		if (mouvement == null) {
			Coup cp = jeu.determinerCoup(dL, dC);
			if (cp != null) {
				jouerCoup(cp);
			}
		}
	}

	void testFinNiveau() {
		if (jeu.niveau().estTermine())
			if (!jeu.prochainNiveau())
				System.exit(0);
	}

	@Override
	// Clic dans la case (l, c)
	public void clicSouris(int l, int c) {
		Configuration.instance().logger().info("Clic dans la case (" + c + ", " + l +")");
		int dC = c - jeu.pousseurC();
		int dL = l - jeu.pousseurL();
		int sum = dC+dL;
		sum = sum*sum;
		if ((dC*dL == 0) && (sum == 1)) {
			deplace(dL, dC);
		}
	}

	void annule() {
		Coup cp = jeu.annule();
		animeCoup(cp, -1);
	}

	void refaire() {
		Coup cp = jeu.refaire();
		animeCoup(cp, 1);
	}

	@Override
	public boolean commande(String c) {
		switch (c) {
			case "up":
				deplace(-1, 0);
				break;
			case "down":
				deplace(1, 0);
				break;
			case "left":
				deplace(0, -1);
				break;
			case "right":
				deplace(0, 1);
				break;
			case "quit":
				System.exit(0);
				break;
			case "annule":
				annule();
				break;
			case "refaire":
				refaire();
				break;
			case "pause":
				animationsActives = !animationsActives;
				inter.changeEtatAnim(animationsActives);
				break;
			case "fullscreen":
				inter.basculePleinEcran();
				break;
			case "ia":
				iaActive = !iaActive;
				inter.changeEtatIA(iaActive);
				break;
			case "next":
				jeu.prochainNiveau();
				break;
			default:
				return false;
		}
		return true;
	}
}
