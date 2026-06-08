package model.cards;

public class Obstacle extends Card {

    public Obstacle(String name, Integer hp){
        super(name, hp);
    }

    // Constructeur de COPIE, pour l'instant pas très utile
    public Obstacle(Obstacle other){
        super(other);
    }

    public boolean isSacrificable() {
        return false;
    }

    @Override
    public void attack(Card target) {
    }

    public int getAttackPoints(){
        return 0;
    }
}
