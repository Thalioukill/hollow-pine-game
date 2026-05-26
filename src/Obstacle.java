public class Obstacle extends Card{

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

    /*
    public Card cloneCard() {

        return new Obstacle(this); // Utilise le constructeur de copie
    }
     */

    public int getAttackPoints(){
        return 0;
    }
}
