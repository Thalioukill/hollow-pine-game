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

    public abstract int getAttackPoints();

    public void takeDamage(int amount) {
        this.m_healthPoints-= amount;
    }

    public boolean isDead() {
        return m_healthPoints <= 0;
    }
    public abstract boolean isSacrificable();

    // Pas besoin que cette méthode soit abstract parce que on en a que besoin dans Animal
    // public abstract Card cloneCard();
}
