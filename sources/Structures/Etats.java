package Structures;

import Modele.Niveau;

public class Etats {
    int nbCaisses;
    int[] positionsCaisses;
    int positionPousseur;
    boolean[] deplacementsPousseur;

    Etats(Niveau niv) {
        nbCaisses = niv.nbButs;
        positionsCaisses = new int[nbCaisses];
        int caisseCur = 0;
        for (int i = 0; i < niv.lignes(); i++) {
            for (int j = 0; j < niv.colonnes(); j++) {
                if (niv.aCaisse(i, j)) {
                    positionsCaisses[caisseCur] = i*j+j;
                    caisseCur++;
                }
            }
        }
        positionPousseur = niv.lignePousseur() * niv.colonnePousseur() + niv.colonnePousseur();
        deplacementsPousseur = new boolean[niv.lignes() * niv.colonnes()];
        deplacementsPousseur[positionPousseur] = true;
        initDeplacementsPousseur(niv);
    }

    void insereVoisins(FAP<Integer> file, Niveau niv, boolean[] explorees, int caseCourante){
        int lig = niv.lignes();
        int col = niv.colonnes();
        int i,j;
        //HAUT
        int caseHaut = caseCourante - col;
        if(caseHaut >= 0 && !explorees[caseHaut]){
            i = caseHaut/col; j = caseHaut%col;
            if(niv.estOccupable(i,j)) file.insere(caseHaut);
        }
        //BAS
        int caseBas = caseCourante + col;
        if(caseBas < lig*col && !explorees[caseBas]){
            i = caseBas/col; j = caseBas%col;
            if(niv.estOccupable(i,j)) file.insere(caseBas);
        }
        //DROITE
        int caseDroite = caseCourante + 1;
        if(caseCourante % col != col-1 && !explorees[caseDroite]){
            i = caseDroite/col; j = caseDroite%col;
            if(niv.estOccupable(i,j)) file.insere(caseDroite);
        }
        //GAUCHE
        int caseGauche = caseCourante - 1;
        if(caseCourante % col != 0 && !explorees[caseGauche]){
            i = caseGauche/col; j = caseGauche%col;
            if(niv.estOccupable(i,j)) file.insere(caseGauche);
        }

    }

    void initDeplacementsPousseur(Niveau niv) {
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
}
