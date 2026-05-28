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

    @Override
    public void attack(Card target) {
        // Un obstacle n'attaque pas...
    }

    public int getAttackPoints(){
        return 0;
    }

    @Override
    public void isAttacked(int amount){
        this.m_healthPoints-= amount;
    }

        /*
    public Card cloneCard() {

        return new Obstacle(this); // Utilise le constructeur de copie
    }
     */
}
