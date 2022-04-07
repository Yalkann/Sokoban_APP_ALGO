package Modele;

import Modele.Etat;

public class Noeud {
    public Etat etatCourant;
    public Noeud ndPrecedent;
    public int distance;
    public int lastMove;

    public Noeud(Etat cour, Noeud pred, int dist, int last){
        etatCourant = cour;
        ndPrecedent = pred;
        distance = dist;
        lastMove = last;
    }
}
