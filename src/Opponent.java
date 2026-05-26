import java.util.ArrayList;
import java.util.Optional;

public class Opponent extends Player{
    private BoardLine m_previsionLine;

    public Opponent(Deck deck) {
        super(deck);
        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_previsionLine = new BoardLine();
    }
/*
    public void takeTurn(Interactions dialogue) {
        // Déplacer les cartes de la ligne de prévision vers la boardLine
        // Remplir la ligne de prévision pour le tour suivant
        m_previsionLine.add(1,m_deck.draw());

        m_previsionLine.add(2,m_deck.draw());
    }
*/
    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
