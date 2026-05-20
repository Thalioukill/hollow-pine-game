public abstract class Card {
    public String m_name;
    public Integer m_healthPoints;

    public Card(String name, Integer hp){
        this.m_name = name;
        this.m_healthPoints = hp;
    }

    // Constructeur de copie pour CardStock
    public Card(Card other) {
        this.m_name = other.m_name;
        this.m_healthPoints = other.m_healthPoints;
    }

    public Card getCard(String name){

    }

    // Méthode abstraite pour forcer la copie polymorphique
    public abstract Card cloneCard();
}
