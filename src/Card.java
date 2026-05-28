public abstract class Card {
    protected final String m_name; // nom cahneg pas
    protected Integer m_healthPoints;

    public Card(String name, Integer hp){
        this.m_name = name;
        this.m_healthPoints = hp;
    }

    // Constructeur de copie pour CardStock
    public Card(Card other) {
        this.m_name = other.m_name;
        this.m_healthPoints = other.m_healthPoints;
    }

    public String getName() {
        return this.m_name;
    }

    public int getHealthPoints() {
        return m_healthPoints;
    }

    // Attaque la carte en face
    public abstract void attack(Card target);
    // Reçoit une attaque
    public abstract void isAttacked(int amount);


    public abstract int getAttackPoints();


    public boolean isDead() {
        return this.m_healthPoints <= 0;
    }

    public abstract boolean isSacrificable();
}
