package Controleur;

import Modele.Coup;
import Modele.Niveau;
import Structures.Sequence;

abstract class IA {
	abstract Sequence<Coup> joue(Niveau n);
}
