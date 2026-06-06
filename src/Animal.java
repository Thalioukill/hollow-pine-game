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

    public Animal(String name, int attack, Integer pv, int blood, int bone) {
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
        this.m_abilities = new ArrayList<>();
        for (Ability a : other.m_abilities) {
            this.addAbility(a);
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

    public Animal cloneAnimal() {

        return new Animal(this); // Utilise le constructeur de copie
    }

    public void revive() { this.m_healthPoints = 1; }

    public int getBloodCost() {
        return this.m_bloodCost;
    }

    public void attack(Card target) {
        target.isAttacked(this.m_attackPoints, this);

        if (!target.isDead() && target.isSacrificable()) {
            for (Ability a : m_abilities) {
                a.onAttack(target);
            }
        }
    }

    public void isAttacked(int amount, Animal attacker) {
        int finalDamage = amount;

        // Puant — modifie les dégâts
        for (Ability a : this.m_abilities) {
            finalDamage = a.modifyIncomingDamage(finalDamage);
        }

        takeDamage(finalDamage); // ← délègue les dégâts bruts

        // Piques pointues — riposte
        for (Ability a : this.m_abilities) {
            a.onHit(this, attacker);
        }
    }



    @Override
    public boolean isTargetBypassed() {
        for (Ability a : m_abilities) {
            if (a.bypassTarget()) {
                return true; // Dès qu'un pouvoir (ex: Volant) dit vrai, on bypass
            }
        }
        return false;
    }

    // Moment : Après avoir attaqué (Élan / Coureur)
    @Override
    public void triggerPostAttack(int position, BoardLine line) {
        // On notifie chaque pouvoir de l'animal que l'attaque est terminée
        for (Ability a : m_abilities) {
            // Chaque ability décidera si elle doit agir ou non
            a.afterAttack(this, position, line);
        }
    }

    // manylives => ManyLivesAbiliy
    @Override
    public boolean survivesSacrifice() {
        for (Ability a : m_abilities) {
            if (a.onSacrifice(this)) return true; // Le pouvoir "Nombreuses Vies" renverra true
        }
        return false;
    }

    // Louveteau / Croissance
    @Override
    public void triggerStartTurn(int position, BoardLine line) {
        // On parcourt la liste générique des capacités
        for (Ability a : m_abilities) {
            // 'this' est passé car l'Ability a besoin de l'animal pour le transformer
            a.onTurnUpdate(this, position, line);
        }
    }

    // takeDamage — dégâts bruts, pas d'abilities
    public void takeDamage(int amount) {
        this.m_healthPoints -= Math.max(0, amount);
    }

    //for cat
    public void setHealthPoints(int amount) {
        this.m_healthPoints = amount;
    }


     // Vérifie si l'animal possède une capacité spécifique par son nom.
    public boolean hasAnAbility(String name) {
        for (Ability ability : m_abilities) {
            // On compare les noms (ne jamais utiliser == sur des String)
            if (ability.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Ability> getAbilities() {
        return new ArrayList<>(m_abilities); // copie défensive
    }
/*
    public String getAbilitiesNames() {
        if (m_abilities.isEmpty()){
            return "Aucune";
        }
        StringBuilder sb = new StringBuilder();
        for (Ability a : m_abilities) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(a.getName());
        }
        return sb.toString();
    }
 */

    // Vérifie si l'animal possède des capacités...
    public boolean hasAbility() {
        return !this.m_abilities.isEmpty();
    }

    // récup de la premire capacité transférable pour sacrifice...
    public Optional<Ability> getTransferableAbility() {
        if (m_abilities.isEmpty()) {
            return Optional.empty();
        }
        // Dans Inscryption, on transfère généralement le premier sigil
        return Optional.of(m_abilities.get(0));
    }
}
