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
import Modele.Coordonnee;
import Modele.Coup;
import Modele.Etat;
import Modele.Noeud;
import Structures.Sequence;
import Structures.SequenceListe;

import java.util.*;
import java.util.logging.Logger;

class IAAssistanceDeadlock extends IA {
	Logger logger;
	Etat etatDepart;
	Noeud noeudDepart;
	Sequence<Integer> solution;
	HashSet<Coordonnee> murs;
	HashSet<Coordonnee> buts;
	HashSet<Coordonnee> caissesInit;
	Coordonnee pousseurInit;
	boolean resolve = false;
	HashSet<Coordonnee> notDeadlock;

	public IAAssistanceDeadlock() {

	}

	@Override
	public void initialise() {
		logger = Configuration.instance().logger();
		logger.info("Démarrage de l'IA sur un niveau de taille " + niveau.lignes() + "x" + niveau.colonnes());
	}

	@Override
	public Sequence<Coup> joue() {
		Sequence<Coup> resultat = Configuration.instance().nouvelleSequence();
		int moveCode;

		if (!resolve){
			solution = Dijkstra();
			resolve = true;
		}
		if(solution.estVide()){
			logger.info("Pas de solution");
			finalise();
			return null;
		} else {
			moveCode = solution.extraitTete();
			switch (moveCode) {
				case 1: //HAUT
					logger.info("Deplacement vers la case (" + (niveau.lignePousseur() - 1) + "," + niveau.colonnePousseur() + ")");
					resultat.insereQueue(niveau.creerCoup(-1, 0));
					break;
				case 2: //BAS
					logger.info("Deplacement vers la case (" + (niveau.lignePousseur() + 1) + "," + niveau.colonnePousseur() + ")");
					resultat.insereQueue(niveau.creerCoup(1, 0));
					break;
				case 3: //GAUCHE
					logger.info("Deplacement vers la case (" + niveau.lignePousseur() + "," + (niveau.colonnePousseur() - 1) + ")");
					resultat.insereQueue(niveau.creerCoup(0, -1));
					break;
				case 4: //DROITE
					logger.info("Deplacement vers la case (" + niveau.lignePousseur() + "," + (niveau.colonnePousseur() + 1) + ")");
					resultat.insereQueue(niveau.creerCoup(0, 1));
					break;
			}
		}
		return resultat;
	}

	@Override
	public void finalise() {
		resolve = false;
		logger.info("Fin de traitement du niveau par l'IA");
	}

	void initTabDeadlock(){
		int maxLig = niveau.lignes(); int maxCol = niveau.colonnes();
		notDeadlock = new HashSet<>();
		Coordonnee cour, suiv, backSuiv;
		for(Coordonnee b : buts){
			HashSet<Coordonnee> histoire = new HashSet<>();
			Queue<Coordonnee> queue = new LinkedList<>();
			queue.add(b);
			while(!queue.isEmpty()){
				cour = queue.poll();
				histoire.add(cour);
				notDeadlock.add(cour);
				//check HAUT
				suiv = new Coordonnee(cour.lig-1,cour.col);
				backSuiv = new Coordonnee(cour.lig-2,cour.col);
				if(suiv.lig >=0 && backSuiv.lig >= 0 && !existeDans(histoire,suiv) &&
						!existeDans(murs,suiv) && !existeDans(murs,backSuiv))
					queue.add(suiv);
				//check BAS
				suiv = new Coordonnee(cour.lig+1,cour.col);
				backSuiv = new Coordonnee(cour.lig+2,cour.col);
				if(suiv.lig <=maxLig && backSuiv.lig <=maxLig && !existeDans(histoire,suiv) &&
						!existeDans(murs,suiv) && !existeDans(murs,backSuiv))
					queue.add(suiv);
				//check GAUCHE
				suiv = new Coordonnee(cour.lig,cour.col-1);
				backSuiv = new Coordonnee(cour.lig,cour.col-2);
				if(suiv.col >=0 && backSuiv.col >= 0 && !existeDans(histoire,suiv) &&
						!existeDans(murs,suiv) && !existeDans(murs,backSuiv))
					queue.add(suiv);
				//check DROITE
				suiv = new Coordonnee(cour.lig,cour.col+1);
				backSuiv = new Coordonnee(cour.lig,cour.col+2);
				if(suiv.lig <=maxCol && backSuiv.lig <=maxCol && !existeDans(histoire,suiv) &&
						!existeDans(murs,suiv) && !existeDans(murs,backSuiv))
					queue.add(suiv);
			}
		}
	}

	boolean estDeadlock(Noeud n){
		for(Coordonnee c : n.etatCourant.caisses){
			if(!existeDans(notDeadlock, c)) return true;
		}
		return false;
	}

	boolean etatGagnant(Etat e){
		for(Coordonnee c : e.caisses){
			if(!existeDans(buts,c)) return false;
		}
		return true;
	}

	Noeud noeudSucc(Noeud n, int direction){
		Etat etatSuiv;
		etatSuiv = etatSuccesseur(n.etatCourant, direction);
		if(etatSuiv == null) {
			//System.out.println("Movement to direction "+direction+" blocked");
			return null;
		}
		else return new Noeud(etatSuiv, n, n.distance+1, direction);
	}

	boolean existeDans(HashSet<Coordonnee> set, Coordonnee c){
		for(Coordonnee cs : set){
			if(cs.equals(c)) return true;
		}
		return false;
	}

	boolean etatExiste(HashSet<Etat> set, Etat e){
		for(Etat es : set){
			if(es.equals(e)) return true;
		}
		return false;
	}

	Comparator<Noeud> dijkstraComp = new Comparator<Noeud>() {
		@Override
		public int compare(Noeud o1, Noeud o2) {
			return o1.distance - o2.distance;
		}
	};

	Sequence<Integer> Dijkstra(){
		murs = new HashSet<Coordonnee>();
		buts = new HashSet<Coordonnee>();
		caissesInit = new HashSet<Coordonnee>();
		for(int i = 0 ; i < niveau.lignes() ; i++){
			for(int j = 0 ; j < niveau.colonnes() ; j++){
				if(niveau.aMur(i,j)) murs.add(new Coordonnee(i,j));
				if(niveau.aBut(i,j)) buts.add(new Coordonnee(i,j));
				if(niveau.aCaisse(i,j)) caissesInit.add(new Coordonnee(i,j));
			}
		}
		pousseurInit = new Coordonnee(niveau.lignePousseur(), niveau.colonnePousseur());
		etatDepart = new Etat(caissesInit, pousseurInit);
		noeudDepart = new Noeud(etatDepart, null, 0, 0);
		initTabDeadlock();

		Sequence<Integer> moves = new SequenceListe<>();
		boolean gagne = false;
		HashSet<Etat> dejaVisite = new HashSet<>();
		Queue<Noeud> queue = new PriorityQueue<>(dijkstraComp);
		queue.add(noeudDepart);
		Noeud ndFinale = noeudDepart;
		while(!queue.isEmpty()){
			Noeud ndCourant = queue.remove();
			Etat etatCour = ndCourant.etatCourant;
			dejaVisite.add(etatCour);
			if(etatGagnant(etatCour)){
				gagne = true;
				ndFinale = ndCourant;
				break;
			}
			for(int i=1 ; i<=4 ; i++) {
				Noeud ndSucc = noeudSucc(ndCourant, i);
				if(ndSucc != null){
					if(!etatExiste(dejaVisite, ndSucc.etatCourant) && !estDeadlock(ndSucc)){
						queue.add(ndSucc);
					}
				}
			}
		}
		if(gagne){
			while(ndFinale.ndPrecedent != null){
				moves.insereTete(ndFinale.lastMove);
				ndFinale = ndFinale.ndPrecedent;
			}
		}
		return moves;
	}


	Etat etatSuccesseur(Etat courant, int direction){
		int maxLig = niveau.lignes(); int maxCol = niveau.colonnes();
		Etat res = null;
		HashSet<Coordonnee> caisses = courant.caisses;
		Coordonnee nextPousseur, devantCaisse;

		switch(direction) {
			case 1://HAUT
				nextPousseur = new Coordonnee(courant.pousseur.lig - 1, courant.pousseur.col);
				devantCaisse = new Coordonnee(courant.pousseur.lig - 2, courant.pousseur.col);
				if (nextPousseur.lig >= 0) { //still in map
					if (!existeDans(murs,nextPousseur)) { // no wall
						if (existeDans(caisses,nextPousseur) && devantCaisse.lig >= 0) { //is box and front of box still in map
							if (!existeDans(murs,devantCaisse) && !existeDans(caisses,devantCaisse)) { //front of box free
								HashSet<Coordonnee> nouvelleCaisses = new HashSet<>();
								for (Coordonnee c : caisses) {
									if (!c.equals(nextPousseur))
										nouvelleCaisses.add(c);
									else {
										nouvelleCaisses.add(devantCaisse);
									}
								}
								res = new Etat(nouvelleCaisses, nextPousseur);
							}
						} else { //not box or wall
							res = new Etat(caisses, nextPousseur);
						}
					}
				}
				break;
			case 2://BAS
				nextPousseur = new Coordonnee(courant.pousseur.lig + 1, courant.pousseur.col);
				devantCaisse = new Coordonnee(courant.pousseur.lig + 2, courant.pousseur.col);
				if (nextPousseur.lig <= maxLig) { //still in map
					if (!existeDans(murs,nextPousseur)) { // no wall
						if (existeDans(caisses,nextPousseur) && devantCaisse.lig <= maxLig) { //is box and front of box still in map
							if (!existeDans(murs,devantCaisse) && !existeDans(caisses,devantCaisse)) { //front of box free
								HashSet<Coordonnee> nouvelleCaisses = new HashSet<>();
								for (Coordonnee c : caisses) {
									if (!c.equals(nextPousseur))
										nouvelleCaisses.add(c);
									else {
										nouvelleCaisses.add(devantCaisse);
									}
								}
								res = new Etat(nouvelleCaisses, nextPousseur);
							}
						} else { //not box
							res = new Etat(caisses, nextPousseur);
						}
					}
				}
				break;
			case 3://GAUCHE
				nextPousseur = new Coordonnee(courant.pousseur.lig, courant.pousseur.col - 1);
				devantCaisse = new Coordonnee(courant.pousseur.lig, courant.pousseur.col - 2);
				if (nextPousseur.col >= 0) { //still in map
					if (!existeDans(murs, nextPousseur)) { // no wall
						if (existeDans(caisses, nextPousseur) && devantCaisse.col >= 0) { //is box and front of box still in map
							if (!existeDans(murs, devantCaisse) && !existeDans(caisses, devantCaisse)) { //front of box free
								HashSet<Coordonnee> nouvelleCaisses = new HashSet<>();
								for (Coordonnee c : caisses) {
									if (!c.equals(nextPousseur))
										nouvelleCaisses.add(c);
									else {
										nouvelleCaisses.add(devantCaisse);
									}
								}
								res = new Etat(nouvelleCaisses, nextPousseur);
							}
						} else { //not box
							res = new Etat(caisses, nextPousseur);
						}
					}
				}
				break;
			case 4://DROITE
				nextPousseur = new Coordonnee(courant.pousseur.lig, courant.pousseur.col + 1);
				devantCaisse = new Coordonnee(courant.pousseur.lig, courant.pousseur.col + 2);
				if (nextPousseur.col <= maxCol) { //still in map
					if (!existeDans(murs, nextPousseur)) { // no wall
						if (existeDans(caisses, nextPousseur) && devantCaisse.col <= maxCol) { //is box and front of box still in map
							if (!existeDans(murs, devantCaisse) && !existeDans(caisses, devantCaisse)) { //front of box free
								HashSet<Coordonnee> nouvelleCaisses = new HashSet<>();
								for (Coordonnee c : caisses) {
									if (!c.equals(nextPousseur))
										nouvelleCaisses.add(c);
									else {
										nouvelleCaisses.add(devantCaisse);
									}
								}
								res = new Etat(nouvelleCaisses, nextPousseur);
							}
						} else { //not box
							res = new Etat(caisses, nextPousseur);
						}
					}
				}
				break;
		}
		return res;
	}

}
