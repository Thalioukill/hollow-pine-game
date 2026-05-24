import java.util.ArrayList;
import java.util.Optional;

public abstract class Player {

    /*
        PROPOSER UN SACRIFICE SI L'EMPLACEMENT OU POSER LA CARTE EST DEJA PRIS !!!!!!
     */

    protected int m_bonesToken;
    protected Deck m_deck;
    protected BoardLine m_boardLine;

    public Player(Deck d) {
        this.m_deck = d;
        this.m_bonesToken = 0; // Initialisation explicite [4]

        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_boardLine = new BoardLine();
    }

    public Optional<Card> getCardAt(int position) {
        return m_boardLine.getCardAt(position);
    }

    public void removeCardAt(int position) {
        m_boardLine.removeCardAt(position);
    }

    public void placeCardAt(Card card, int position) {
        // proposer un sacrifice si occupé
        if (m_boardLine.isOccupied(position)) {
            // future méthode sacrifice(position)
            // Pour l'instant, on se contente de remplacer ou bloquer
            System.out.println("L'emplacement " + position + " est déjà pris !");
        }

        m_boardLine.placeCardAt(card, position);
    }

    public abstract void takeTurn(Interactions dialogue);

    @Override
    public String toString() {
        // Utilisation de getClass().getSimpleName() pour différencier User/Opponent
        return this.getClass().getSimpleName() +
                "[Os=" + m_bonesToken + ", Plateau=" + m_boardLine + "]";
    }
}
