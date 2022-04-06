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
package Controleur;

import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;
import Controleur.CalculChemin;
import Structures.Etats;
import Modele.Niveau;

import java.util.Random;
import java.util.logging.Logger;

class IAAssistance extends IA {
	Random r;
	Logger logger;
	Etats depart;
	Sequence<Integer> moves;

	public IAAssistance() {
		r = new Random();
	}

	@Override
	public void initialise() {
		logger = Configuration.instance().logger();
		logger.info("Démarrage de l'IA sur un niveau de taille " + niveau.lignes() + "x" + niveau.colonnes());
		depart = new Etats(niveau);
		moves = CalculChemin.Dijkstra(depart);
	}

	@Override
	public Sequence<Coup> joue() {
		Sequence<Coup> resultat = Configuration.instance().nouvelleSequence();
		Coup coup = null;
		boolean mur = true;
		int dL = 0, dC = 0;
		int nouveauL = 0;
		int nouveauC = 0;

		int pousseurL = niveau.lignePousseur();
		int pousseurC = niveau.colonnePousseur();
		// Mouvement du pousseur
		while (mur) {
			int direction = r.nextInt(2) * 2 - 1;
			if (r.nextBoolean()) {
				dL = direction;
			} else {
				dC = direction;
			}
			nouveauL = pousseurL + dL;
			nouveauC = pousseurC + dC;
			coup = niveau.creerCoup(dL, dC);
			if (coup == null) {
				if (niveau.aMur(nouveauL, nouveauC))
					logger.info("Tentative de déplacement (" + dL + ", " + dC + ") heurte un mur");
				else if (niveau.aCaisse(nouveauL, nouveauC))
					logger.info("Tentative de déplacement (" + dL + ", " + dC + ") heurte une caisse non déplaçable");
				else
					logger.severe("Tentative de déplacement (" + dL + ", " + dC + "), erreur inconnue");
				dL = dC = 0;
			} else
				mur = false;
		}
		resultat.insereQueue(coup);
		return resultat;
	}

	@Override
	public void finalise() {
		logger.info("Fin de traitement du niveau par l'IA");
	}
}
