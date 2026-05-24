import java.util.ArrayList;
import java.util.Optional;

public class Opponent extends Player{
    private ArrayList<Optional<Card>> m_previsionLine;

    public Opponent(Deck deck) {
        super(deck);
        this.m_previsionLine = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            m_previsionLine.add(Optional.empty());
        }
    }

    @Override
    public void takeTurn() {
        // Déplacer les cartes de la ligne de prévision vers la boardLine
        // Remplir la ligne de prévision pour le tour suivant
    }

    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
