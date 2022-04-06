package Controleur;

import Structures.Sequence;
import Structures.SequenceListe;
import Modele.Etat;

import java.util.Hashtable;

public class CalculChemin {

    public static Sequence<Integer> Dijkstra(Etat start){
        Hashtable<Etat, Etat> predList = new Hashtable<Etat, Etat>();
        Sequence<Etat> queue = new SequenceListe<Etat>();
        Sequence<Integer> moveList = new SequenceListe<Integer>();
        Sequence<Etat> succ;
        predList.put(start,start);
        queue.insereQueue(start);
        Etat courant, voisin, etatPred;
        Etat finale = start;
        boolean gagne = false;
        while(!queue.estVide()){
            courant = queue.extraitTete();
            System.out.println("Calculating state player " + courant.positionPousseur + " box " + courant.positionsCaisses[0]);
            if(courant.niv.estTermine()){
                gagne = true;
                finale = courant;
                break;
            }
            succ = courant.successeurs();
            while(!succ.estVide()){
                voisin = succ.extraitTete();
                if(!predList.containsKey(voisin)){
                    predList.put(voisin,courant);
                    queue.insereQueue(voisin);
                }
            }
        }
        if(gagne){
            etatPred = predList.get(finale);
            moveList.insereTete(finale.lastMove);
            while(etatPred != predList.get(etatPred)){
                moveList.insereTete(etatPred.lastMove);
                etatPred = predList.get(etatPred);
            }
        }
        return moveList;
    }
}
