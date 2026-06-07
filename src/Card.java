import java.util.ArrayList;
import java.util.Collection;

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

    public boolean hasAbility() {
        return false;
    }


    public ArrayList<Ability> getAbilities() {
        return new ArrayList<>(); // liste vide par défaut
    }

    public int getHealthPoints() {
        return m_healthPoints;
    }

    public void isAttacked(int amount, Animal attacker){
        this.takeDamage(amount);
    }

    // Attaque la carte en face
    public abstract void attack(Card target);

    public boolean isTargetBypassed() {
        return false;
    }

    public void triggerPostAttack(int position, BoardLine line) {
        // Méthode vide
    }

    public void triggerStartTurn(int position, BoardLine line) {
        // les obstacles ignorent
    }

    public boolean survivesSacrifice() {
        return false;
    }

    public abstract int getAttackPoints();


    public boolean isDead() {
        return this.m_healthPoints <= 0;
    }

    public abstract boolean isSacrificable();

    public void takeDamage(int amount) {
            this.m_healthPoints-= amount;
    }

}
