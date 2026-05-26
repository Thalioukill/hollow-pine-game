import java.util.ArrayList;
import java.util.Optional;

public class Opponent extends Player{
    private BoardLine m_previsionLine;

    public Opponent(Deck deck) {
        super(deck);
        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_previsionLine = new BoardLine();
    }

    public void placePrevisionCard(int position){
        if(!m_previsionLine.isOccupied(position)){
            Optional<Animal> animal = this.m_deck.draw();
            // si dans le deck on a bien donnée une carte alors on la pose
            animal.ifPresent(value -> m_previsionLine.placeCardAt(value, position));
            // on ne place pas la carte si
        }
    }

    public void placeCards(Interactions dialogue) {
        // Déplacer les cartes de la ligne de prévision vers la boardLine
        // Remplir la ligne de prévision pour le tour suivant

    }

    public void round1(){
        placePrevisionCard(1);
        placePrevisionCard(3);
    }




    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
