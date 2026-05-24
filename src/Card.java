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
        return m_name;
    }

    public int getHealthPoints() {
        return m_healthPoints;
    }

    public void takeDamage(int damage) {
        m_healthPoints -= damage;
    }

    public boolean isDead() {
        return m_healthPoints <= 0;
    }
    public abstract boolean isSacrificable();

    // Méthode abstraite pour forcer la copie polymorphique
    public abstract Card cloneCard();
}
