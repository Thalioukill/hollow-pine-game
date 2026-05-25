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

    private int toIndex(int position) {
        return position - 1;
    }

     // Vérif inter pas besoin de l'utiliser autre part qu'ici
    private boolean validatePosition(int position) {
        return position >= 1 && position <= 4;
    }

    // renvoie un Optional vide si l'index est mauvais => géré dans le User avec une Interactions : froce à vérifier que tout va bien.
    public Optional<Card> getCardAt(int position) {
        if (!validatePosition(position)) {
            return Optional.empty();
        }
        return m_slots.get(toIndex(position));
    }

     // lance une exception car si le code tente de poser une carte hors-limite après les vérifications du User, c'est un bug...
    public void placeCardAt(Card card, int position) {
        if (!validatePosition(position)) {
            throw new IndexOutOfBoundsException("Bug interne : placement hors limites");
        }
        m_slots.set(toIndex(position), Optional.of(card));
    }

    public boolean isOccupied(int position) {
        // Une position invalide n'est, par définition, pas occupée.
        return validatePosition(position) && m_slots.get(toIndex(position)).isPresent();
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
        // on libère l'meplacement = Optional vide
        m_slots.set(toIndex(position), Optional.empty());
    }

    @Override
    public String toString() {
        return m_slots.toString();
    }
}
