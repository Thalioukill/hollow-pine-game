public class Animal extends Card {
    private Integer m_attackPoints;
    private Integer m_bloodCost;
    private Integer m_boneCost;
    private ArrayList<Ability> m_abilities;

    public Animal(String name, Integer pv, Integer attack, Integer blood, Integer bone) {
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

    public Animal(String name, Integer pv, Integer attack, Integer blood, Integer bone) {
        this(name, pv, attack, blood, bone);
        this.m_abilities.add(ty);
    }

    public void attack(int position, Player opponent, Game game) {
        Optional<Card> target = opponent.getCardAt(position);

        if (target.isPresent()) {
            // Une carte adverse est en face
            Card card = target.get();
            card.takeDamage(this.m_attackPoints);

            if (card.isDead()) {
                opponent.removeCardAt(position);  // retirer la carte du plateau
                game.addBonesToCurrentPlayer(1);  // incrémenter les os
            }
        } else {
            // Emplacement vide → attaque directe au score
            game.updateScore(this.m_attackPoints);
        }
    }

    @Override
    public Card cloneCard() {

        return new Animal(this); // Utilise le constructeur de copie
    }



}
