package Modele;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Coup extends Commande {
	Sequence<Deplacement> mouvements;
	Sequence<Marque> marques;
	Niveau n;

	public Coup() {
		mouvements = Configuration.instance().nouvelleSequence();
		marques = Configuration.instance().nouvelleSequence();
	}

	void fixerNiveau(Niveau niv) {
		n = niv;
	}

	public void deplace(int dL, int dC, int vL, int vC) {
		mouvements.insereQueue(new Deplacement(dL, dC, vL, vC));
	}

	public void marque(int valeur, int l, int c) {
		marques.insereQueue(new Marque(valeur, l, c));
	}

	public Sequence<Deplacement> deplacements() {
		return mouvements;
	}
	public Sequence<Marque> marques() {
		return marques;
	}

	void deplace(int depuis, int vers, int modif) {
		Iterateur<Deplacement> it = mouvements.iterateur();
		while (it.aProchain()) {
			Deplacement d = it.prochain();
			d.contenu = n.contenu(d.l[depuis], d.c[depuis]);
			n.supprime(d.contenu, d.l[depuis], d.c[depuis]);
		}
		it = mouvements.iterateur();
		while (it.aProchain()) {
			Deplacement d = it.prochain();
			n.ajoute(d.contenu, d.l[vers], d.c[vers]);
			n.modifDecompte(d.contenu, modif);
		}
		Iterateur<Marque> it2 = marques.iterateur();
		while (it2.aProchain()) {
			Marque m = it2.prochain();
			m.valeur[depuis] = n.marque(m.l, m.c);
			n.marquer(m.valeur[vers], m.l, m.c);
		}
	}

	void execute() {
		deplace(0, 1, 1);
	}

	void desexecute() {
		deplace(1, 0, -1);
	}
}
