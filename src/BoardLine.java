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

    private void validatePosition(int position) {
        if (position < 1 || position > 4) {
            throw new IndexOutOfBoundsException("Position " + position + " invalide (1-4 requis)");
        }
    }

    private int toIndex(int position) {
        return position - 1;
    }

    public Optional<Card> getCardAt(int position) {
        validatePosition(position);
        return m_slots.get(toIndex(position));
    }

    public void placeCardAt(Card card, int position) {
        validatePosition(position);
        // jamais null, on utilise Optional.of
        m_slots.set(toIndex(position), Optional.of(card));
    }

    public void removeCardAt(int position) {
        validatePosition(position);
        m_slots.set(toIndex(position), Optional.empty());
    }

    public boolean isOccupied(int position) {
        validatePosition(position);
        return m_slots.get(toIndex(position)).isPresent();
    }

    @Override
    public String toString() {
        return m_slots.toString();
    }
}
