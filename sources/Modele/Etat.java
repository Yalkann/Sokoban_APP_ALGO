package Modele;

import Modele.Coordonnee;

import java.util.HashSet;

public class Etat {

    public HashSet<Coordonnee> caisses;
    public Coordonnee pousseur;

    public Etat(HashSet<Coordonnee> posCaisses, Coordonnee posPousseur) {
        caisses = posCaisses;
        pousseur = posPousseur;
    }

    private boolean existeDans(HashSet<Coordonnee> set, Coordonnee c){
        for(Coordonnee cs : set){
            if(cs.equals(c)) return true;
        }
        return false;
    }

    public boolean equals(Etat e){
        if(!pousseur.equals(e.pousseur)) return false;
        for (Coordonnee c : caisses){
            if(!existeDans(e.caisses, c)) return false;
        }
        return true;
    }
}
