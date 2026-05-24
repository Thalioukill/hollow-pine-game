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

    public int getSize() {
        return m_cards.size();
    }

     // Convertit lettre (A, B, C...) en index (0, 1, 2...)

    private int toIndex(char letter) {
        // Character.toUpperCase pour accepter 'a' ou 'A'
        // On utilise l'ASCII pour trouver l'index  'A' = 65
        return Character.toUpperCase(letter) - 'A';
    }


    // Validation pour éviter les IndexOutOfBoundsException
    private void validateIndex(int index) {
        if (index < 0 || index >= m_cards.size()) {
            throw new IndexOutOfBoundsException("Index " + index +
                    " invalide pour une main de taille " + m_cards.size());
        }
    }

    public Card getCard(char letter) {
        int index = toIndex(letter);
        validateIndex(index);
        return m_cards.get(index);
    }

     // retire la carte de la main et retourne l'élément qui était dedans à l'index de la lettre donnée (ici la Card)
    public Card removeCard(char letter) {
        int index = toIndex(letter);
        validateIndex(index);
        return m_cards.remove(index);
    }


    @Override
    public String toString() {
        // affichage pour montrer les lettres
        StringBuilder sb = new StringBuilder("Main : ");
        for (int i = 0; i < m_cards.size(); i++) {
            char letter = (char) ('A' + i);
            sb.append("[").append(letter).append("] ").append(m_cards.get(i).getName()).append(" ");
        }
        return sb.toString();
    }
}
