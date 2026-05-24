import java.util.ArrayList;

public class Hand {
    private final ArrayList<Card> m_cards;

    public Hand() {
        this.m_cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        // Une main peut techniquement grandir, contrairement au plateau
        m_cards.add(card);
    }

    // retire la carte de la main et retourne l'élément qui était dedans (ici la Card)
    public Card removeCardAt(int index) {
        validateIndex(index);
        return m_cards.remove(index);
    }

    public int getSize() {
        return m_cards.size();
    }

    // Validation pour éviter les IndexOutOfBoundsException
    private void validateIndex(int index) {
        if (index < 0 || index >= m_cards.size()) {
            throw new IndexOutOfBoundsException("Index " + index +
                    " invalide pour une main de taille " + m_cards.size());
        }
    }

    @Override
    public String toString() {
        return m_cards.toString(); // Utilise le toString de chaque Card
    }
}
