import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Opponent extends Player{
    private BoardLine m_previsionLine;

    public Opponent(ArrayList<Animal> deck, BoardLine bord, BoardLine previsionBoard) {
        super(deck, bord);
        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_previsionLine = previsionBoard;
    }

    public void placePrevisionCard(int position){
        if (!m_previsionLine.isOccupied(position)) {
            Animal animal = this.draw(); // lance NoSuchElementException si vide = bug
            m_previsionLine.placeCardAt(animal, position);
        }
    }

    public void applyPrevision() {
        for (int i = 0; i < 4; i++) {
            Optional<Card> card = this.m_previsionLine.getCardAt(i);
            if (card.isPresent()) {
                this.m_boardLine.placeCardAt(card.get(), i);
                this.m_previsionLine.removeCardAt(i);
            }
        }
    }

    public Optional<Card> getPrevisionCardAt(int position) {
        return m_previsionLine.getCardAt(position);
    }

    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
