package Modele;

public class Coordonnee {
    public int lig;
    public int col;

    public Coordonnee(int x, int y){
        lig = x;
        col = y;
    }

    public boolean equals(Coordonnee c){
        return lig == c.lig && col == c.col;
    }
}
