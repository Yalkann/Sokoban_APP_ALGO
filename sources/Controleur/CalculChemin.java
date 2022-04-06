package Controleur;

import Structures.Sequence;
import Structures.SequenceListe;
import Structures.Etats;
import Structures.FAP;
import Modele.Coup;
import Modele.Niveau;
import java.util.Hashtable;

public class CalculChemin {

    public static Sequence<Integer> Dijkstra(Etats start){
        Hashtable<Etats,Etats> predList = new Hashtable<Etats,Etats>();
        Sequence<Etats> queue = new SequenceListe<Etats>();
        Sequence<Integer> moveList = new SequenceListe<Integer>();
        Sequence<Etats> succ;
        predList.put(start,start);
        queue.insereQueue(start);
        Etats courant, voisin, etatPred;
        Etats finale = start;
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
