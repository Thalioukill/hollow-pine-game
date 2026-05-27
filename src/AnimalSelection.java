public class AnimalSelection {
    public final int m_index;
    public final Animal m_card;

    public AnimalSelection(int index, Animal card) {
        this.m_index = index;
        this.m_card = card;
    }

    public Animal getAnimal(){
        return this.m_card;
    }

    public int getIndex(){
        return this.m_index;
    }
}