import java.util.ArrayList;
import java.util.Optional;

public class Animal extends Card {

    /*
            Méthode pour ajouter des Abilities dans le m_templates !!!!
            + addAbility(ability : Ability)
     */

    private int m_attackPoints;
    private int m_bloodCost;
    private int m_boneCost;
    private ArrayList<Ability> m_abilities;

    public Animal(String name, Integer pv, int attack, int blood, int bone) {
        super(name, pv);
        this.m_attackPoints = attack;
        this.m_bloodCost = blood;
        this.m_boneCost = bone;
        this.m_abilities = new ArrayList<Ability>();
    }

    // Constructeur de copie
    public Animal(Animal other) {
        super(other); // Copie le nom et les PV via la classe mère
        this.m_attackPoints = other.m_attackPoints;
        this.m_bloodCost = other.m_bloodCost;
        this.m_boneCost = other.m_boneCost;
        for (Ability a : other.m_abilities) {
            this.addAbility(a); // Ability sans état → pas besoin de copier
        }
    }


    public void addAbility(Ability a) {
        this.m_abilities.add(a);
    }

    public int getAttackPoints() {
        return this.m_attackPoints;
    }

    public int getBoneCost(){
        return this.m_boneCost;
    }

    public boolean isDead() {
        return this.m_healthPoints <= 0;
    }

    public boolean isSacrificable() { return true; }

    public Card cloneCard() {

        return new Animal(this); // Utilise le constructeur de copie
    }

    /*
    public void attack(int position, Player opponent, Game game) {
        if (m_abilities.isEmpty()) {
            defaultAttack(position, opponent, game);
        } else {
            for (Ability a : m_abilities) {
                a.onAttack(this, position, opponent, game);
            }
        }
    }
    */


    public void takeDamage(int amount, Animal attacker, Game game) {
        int finalDamage = amount;

        // On demande aux pouvoirs s'ils modifient les dégâts (ex: Puant)
        for (Ability a : m_abilities) {
            finalDamage = a.modifyIncomingAttack(finalDamage);
        }

        this.m_healthPoints -= finalDamage;

        // On déclenche les effets de réaction (ex: Piques pointues)
        for (Ability a : m_abilities) {
            a.onHit(this, attacker, game);
        }
    }


    private void defaultAttack(int position, Player opponent, Game game) {
        Optional<Card> target = opponent.getCardAt(position);
        if (target.isPresent()) {
            Card card = target.get();
            card.takeDamage(m_attackPoints);
            if (card.isDead()) {
                opponent.removeCardAt(position);
                game.addBones(1);
            }
        } else {
            game.updateScore(m_attackPoints);
        }
    }

    public int getBloodCost() {
        return this.m_bloodCost;
    }
}
