import java.util.ArrayList;
import java.util.Optional;

public class BoardLine {
    private final ArrayList<Optional<Card>> m_slots;

    public BoardLine() {
        m_slots = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            m_slots.add(Optional.empty());
        }
    }


    public boolean validatePosition(int position) {
        return position >= 1 && position <= 4;
    }

    // renvoie un Optional vide si l'index est mauvais => géré dans le User avec une Interactions : froce à vérifier que tout va bien.
    public Optional<Card> getCardAt(int position) {
        return m_slots.get(position);
    }

     // lance une exception car si le code tente de poser une carte hors-limite après les vérifications du User, c'est un bug...
    public void placeCardAt(Card card, int position) {
        if (!validatePosition(position)) {
            throw new IndexOutOfBoundsException("Bug interne : placement hors limites");
        }
        m_slots.set(position, Optional.of(card));
    }

    public boolean isOccupied(int position) {
        // Une position invalide n'est, par définition, pas occupée.
        return validatePosition(position) && m_slots.get(position).isPresent();
    }

    public int getSacrificableCount() {
        int count = 0;
        for (Optional<Card> slot : m_slots) {
            // Seules les Animaux sont comptés...
            if (slot.isPresent() && slot.get().isSacrificable()) {
                count++;
            }
        }
        return count;
    }

     // Retire la carte de l'emplacement et libère la place et lance une exception si la position est invalidee
    public void removeCardAt(int position) {
        if (!validatePosition(position)) {
            throw new IndexOutOfBoundsException("Bug interne : retrait à une position hors limites (" + position + ")");
        }
        m_slots.set(position, Optional.empty());
    }

    @Override
    public String toString() {
        return m_slots.toString();
    }

/*
    public int attack(BoardLine opponentLine, Game game) {
        for (int i = 0; i < 4; i++) {
            Optional<Card> optCard = m_slots.get(i);
            if (optCard.isPresent()) {
                Card card = optCard.get();
                if (card.getAttackPoints() > 0) {
                    card.attack(i, opponentLine, game);
                }
            }
        }
    }
 */
}
