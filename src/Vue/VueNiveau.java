package Vue;

/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Global.Configuration;
import Modele.Jeu;
import Modele.Niveau;

import java.io.File;
import java.io.InputStream;

class VueNiveau {
	Jeu jeu;
	ImageSokoban but, caisse, caisseSurBut, mur, pousseur, sol;
	int largeurCase, hauteurCase;
	NiveauGraphique ng;
	double [][] dL;
	double [][] dC;
	ImageSokoban [][] pousseurs;
	int etape, direction;

	private ImageSokoban chargeImage(String nom) {
		ImageSokoban img = null;
		InputStream in = Configuration.charge("Images" + File.separator + nom + ".png");
		return ImageSokoban.getImageSokoban(in);
	}

	public VueNiveau(Jeu j, NiveauGraphique n) {
		but = chargeImage("But");
		caisse = chargeImage("Caisse");
		caisseSurBut = chargeImage("Caisse_sur_but");
		mur = chargeImage("Mur");
		sol = chargeImage("Sol");
		jeu = j;
		ng = n;

		pousseurs = new ImageSokoban[4][4];
		for (int i=0; i<4; i++)
			for (int k=0; k<4; k++) {
				pousseurs[i][k] = chargeImage("Pousseur_" + i + "_" + k);
			}
		etape = 0;
		direction = 2;
		pousseur = pousseurs[direction][etape];
	}

	void tracerNiveau() {
		Niveau niv = jeu.niveau();
		largeurCase = ng.largeur() / niv.colonnes();
		hauteurCase = ng.hauteur() / niv.lignes();
		largeurCase = Math.min(largeurCase, hauteurCase);
		hauteurCase = largeurCase;

		if ((dL == null) || (dL.length < ng.hauteur()) || (dL[0].length < ng.largeur())) {
			dL = new double[ng.hauteur()][ng.largeur()];
			dC = new double[ng.hauteur()][ng.largeur()];
		}
		for (int i = 0; i<niv.colonnes(); i++)
			for (int j=0; j<niv.lignes(); j++) {
				int x = i * largeurCase;
				int y = j * hauteurCase;
				if (niv.aBut(j, i))
					ng.tracerImage(but, x, y, largeurCase, hauteurCase);
				else
					ng.tracerImage(sol, x, y, largeurCase, hauteurCase);
				int marque = niv.marque(j, i);
				if (marque != 0)
					ng.tracerCroix(marque, x, y, largeurCase, hauteurCase);
			}
		for (int i = 0; i<niv.colonnes(); i++)
			for (int j=0; j<niv.lignes(); j++) {
				int x = (int) Math.round(i * largeurCase + dC[j][i] * largeurCase);
				int y = (int) Math.round(j * hauteurCase + dL[j][i] * hauteurCase);
				if (niv.aMur(j, i))
					ng.tracerImage(mur, x, y, largeurCase, hauteurCase);
				else if (niv.aCaisse(j,i)) {
					if (niv.aBut(j, i))
						ng.tracerImage(caisseSurBut, x, y, largeurCase, hauteurCase);
					else
						ng.tracerImage(caisse, x, y, largeurCase, hauteurCase);
					int marque = niv.marque(j, i);
					if (marque != 0)
						ng.tracerCroix(marque, x, y, largeurCase, hauteurCase);
				} else if (niv.aPousseur(j, i))
					ng.tracerImage(pousseur, x, y, largeurCase, hauteurCase);
			}
	}

	int largeurCase() {
		return largeurCase;
	}

	int hauteurCase() {
		return hauteurCase;
	}

	void fixerDecalage(double dL, double dC, int l, int c) {
		this.dL[l][c] = dL;
		this.dC[l][c] = dC;
	}

	void etapePousseur() {
		etape = (etape+1) % 4;
		pousseur = pousseurs[direction][etape];
	}

	void changeDirectionPousseur(int dL, int dC) {
		if (dL > 0)
			direction = 2;
		else if (dL < 0)
			direction = 0;
		else if (dC > 0)
			direction = 3;
		else
			direction = 1;
		pousseur = pousseurs[direction][etape];
	}
}