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

    public Animal(String name, Integer pv, Integer attack, Integer blood, Integer bone, Ability ty) {
        this(name, pv, attack, blood, bone);
        this.m_abilities.Add(ty);
    }

    public attack(CardSlot target){
        if(target)
    }


}
