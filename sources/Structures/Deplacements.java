package Structures;

import Modele.Niveau;


public class Deplacements {
    int HAUT = 8;
    int BAS = 4;
    int GAUCHE = 2;
    int DROITE = 1;

    Niveau niv;
    int[][] pousseur;
    int[][] caisse;
    boolean[][] exploreePousseur;
    boolean[][] exploreeCaisse;
    

    Deplacements(Niveau n) {
        niv = n;
        int lig = n.lignes();
        int col = n.colonnes();
        pousseur = new int[lig][col];
        caisse = new int[lig][col];
    }

    void initPousseur(Niveau niv) {
        int lig = niv.lignes();
        int col = niv.colonnes();
        int x, y;

        FAP<Integer> queue = new FAPTableau<Integer>();
        queue.insere(niv.lignePousseur());
        queue.insere(niv.colonnePousseur());
        while (!queue.estVide()) {
            x = queue.extrait();
            y = queue.extrait();
            if (x > 0 && !exploreePousseur[x-1][y]) {
                if (niv.estOccupable(x-1, y)) {
                    pousseur[x][y] |= HAUT;
                    pousseur[x-1][y] |= BAS;
                    queue.insere(x-1);
                    queue.insere(y);
                }
            }
            if (x < lig-1 && !exploreePousseur[x+1][y]) {
                if (niv.estOccupable(x+1, y)) {
                    pousseur[x][y] |= BAS;
                    pousseur[x+1][y] |= HAUT;
                    queue.insere(x+1);
                    queue.insere(y);
                }
            }
            if (y > 0 && !exploreePousseur[x][y-1]) {
                if (niv.estOccupable(x, y-1)) {
                    pousseur[x][y] |= GAUCHE;
                    pousseur[x][y-1] |= DROITE;
                    queue.insere(x);
                    queue.insere(y-1);
                }
            }
            if (y < col-1 && !exploreePousseur[x][y+1]) {
                if (niv.estOccupable(x, y+1)) {
                    pousseur[x][y] |= DROITE;
                    pousseur[x][y+1] |= GAUCHE;
                    queue.insere(x);
                    queue.insere(y+1);
                }
            }
            exploreePousseur[x][y] = true;
        }
    }
}
