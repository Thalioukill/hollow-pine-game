public abstract class Player {
    private Integer m_bonesToken;
    private Deck m_deck;

    public Player(Deck d){
        this.m_deck = d;
    }

    // 4 emplacements, tous vides au départ
    private final List<Optional<Card>> m_boardLine = new ArrayList<>(
            Collections.nCopies(4, Optional.empty())
    );

    public Optional<Card> getCardAt(int position) {
        return m_boardLine.get(position);
    }

    public void removeCardAt(int position) {
        m_boardLine.set(position, Optional.empty());
    }

    public void placeCardAt(Card card, int position) {
        m_boardLine.set(position, Optional.of(card));
    }

}
