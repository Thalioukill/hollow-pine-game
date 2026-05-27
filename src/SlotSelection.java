public class SlotSelection {
    public final int m_position;
    public final Card m_card;

    public SlotSelection(int position, Card card) {
        this.m_position = position;
        this.m_card = card;
    }

    public Card getCard(){
        return this.m_card;
    }

    public int getPosition(){
        return this.m_position;
    }
}
