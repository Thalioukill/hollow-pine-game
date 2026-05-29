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
            Animal animal = this.m_deck.removeFirst(); // lance NoSuchElementException si vide = bug
            m_previsionLine.placeCardAt(animal, position);
        }
    }

    public void placeCards() {
        // Déplacer les cartes de la ligne de prévision vers la boardLine
        // Remplir la ligne de prévision pour le tour suivant

    }

    /*
    public void round1(){
        placePrevisionCard(1);
        placePrevisionCard(3);
    }
     */

    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
