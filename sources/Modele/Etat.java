package Modele;

import Structures.FAP;
import Structures.FAPTableau;
import Structures.Sequence;
import Structures.SequenceListe;

public class Etat {
    int nbCaisses;
    public int[] positionsCaisses;
    public int positionPousseur;
    public boolean[] deplacementsPousseur;
    public Niveau niv;
    public int lastMove;

    public Etat(Niveau n) {
        niv = n.clone();
        int lig = niv.lignes();
        int col = niv.colonnes();
        nbCaisses = niv.nbButs;
        positionsCaisses = new int[nbCaisses];
        int caisseCur = 0;
        for (int i = 0; i < lig; i++) {
            for (int j = 0; j < col; j++) {
                if (niv.aCaisse(i, j)) {
                    positionsCaisses[caisseCur] = i*col+j;
                    caisseCur++;
                }
            }
        }
        positionPousseur = niv.lignePousseur() * col + niv.colonnePousseur();
        deplacementsPousseur = new boolean[lig*col];
        deplacementsPousseur[positionPousseur] = true;
        //deplacementsCaisses = new boolean[nbCaisses][4];
        initDeplacementsPousseur();
        /*
        for(int i=0 ; i<nbCaisses ; i++){
            initDeplacementsCaisse(niv,i);
        }*/
    }

    public Etat(Etat e){

    }

    void initDeplacementsPousseur() {
        int caseCourante;
        int lig = niv.lignes();
        int col = niv.colonnes();
        int i,j;
        int caseHaut, caseBas, caseDroite, caseGauche;
        //boolean[] explorees = new boolean[niv.lignes() * niv.colonnes()];
        FAP<Integer> file = new FAPTableau<Integer>();
        file.insere(positionPousseur);
        while(!file.estVide()) {
            caseCourante = file.extrait();
            deplacementsPousseur[caseCourante] = true;
            //INSERE VOISINS
            //HAUT
            caseHaut = caseCourante - col;
            if(caseHaut >= 0 && !deplacementsPousseur[caseHaut]){
                i = caseHaut/col; j = caseHaut%col;
                if(niv.estOccupable(i,j)) file.insere(caseHaut);
            }
            //BAS
            caseBas = caseCourante + col;
            if(caseBas < lig*col && !deplacementsPousseur[caseBas]){
                i = caseBas/col; j = caseBas%col;
                if(niv.estOccupable(i,j)) file.insere(caseBas);
            }
            //DROITE
            caseDroite = caseCourante + 1;
            if(caseCourante % col != col-1 && !deplacementsPousseur[caseDroite]){
                i = caseDroite/col; j = caseDroite%col;
                if(niv.estOccupable(i,j)) file.insere(caseDroite);
            }
            //GAUCHE
            caseGauche = caseCourante - 1;
            if(caseCourante % col != 0 && !deplacementsPousseur[caseGauche]){
                i = caseGauche/col; j = caseGauche%col;
                if(niv.estOccupable(i,j)) file.insere(caseGauche);
            }
        }
    }

    boolean estEquivalent(Etat e){
        if(positionPousseur != e.positionPousseur)
            return false;
        for(int i = 0 ; i < nbCaisses ; i++){
            if(positionsCaisses[i] != e.positionsCaisses[i])
                return false;
        }
        return true;
    }

    boolean aCaisse(int c){
        for(int k = 0; k < nbCaisses ; k++){
            if(positionsCaisses[k] == c) return true;
        }
        return false;
    }

    public Sequence<Etat> successeurs(){
        Sequence<Etat> succ = new SequenceListe<Etat>();
        int caseCourante;
        int lig = niv.lignes();
        int col = niv.colonnes();
        int i,j;
        int caseHaut, caseBas, caseDroite, caseGauche;
        Coup c;
        caseCourante = positionPousseur;
        //HAUT
        caseHaut = caseCourante - col;
        if(caseHaut >= 0){
            if(deplacementsPousseur[caseHaut]){
                //?
            }
            else{
                //?
            }
            Niveau nextNiv1 = niv.clone();
            c = nextNiv1.creerCoup(-1,0);
            nextNiv1.jouer(c);
            Etat nextEtat1 = new Etat(nextNiv1);
            nextEtat1.lastMove = 1;
            succ.insereQueue(nextEtat1);
        }
        //BAS
        caseBas = caseCourante + col;
        if(caseBas < lig*col && deplacementsPousseur[caseBas]){
            Niveau nextNiv2 = niv.clone();
            c = nextNiv2.creerCoup(1,0);
            nextNiv2.jouer(c);
            Etat nextEtat2 = new Etat(nextNiv2);
            nextEtat2.lastMove = 2;
            succ.insereQueue(nextEtat2);
        }
        //DROITE
        caseDroite = caseCourante + 1;
        if(caseCourante % col != col-1 && deplacementsPousseur[caseDroite]){
            Niveau nextNiv3 = niv.clone();
            c = nextNiv3.creerCoup(0,1);
            nextNiv3.jouer(c);
            Etat nextEtat3 = new Etat(nextNiv3);
            nextEtat3.lastMove = 3;
            succ.insereQueue(nextEtat3);
        }
        //GAUCHE
        caseGauche = caseCourante - 1;
        if(caseCourante % col != 0 && deplacementsPousseur[caseGauche]){
            Niveau nextNiv4 = niv.clone();
            c = nextNiv4.creerCoup(0,-1);
            nextNiv4.jouer(c);
            Etat nextEtat4 = new Etat(nextNiv4);
            nextEtat4.lastMove = 4;
            succ.insereQueue(nextEtat4);
        }
        return succ;
    }
}
