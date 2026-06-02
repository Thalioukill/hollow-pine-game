import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Opponent extends Player{
    private BoardLine m_previsionLine;
    // private int m_actions

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

    public void opponentAction(int roundNumber, int gameNumber){
        // transfère la previsionLine vers la boardLine
        this.applyPrevision();
        switch(roundNumber){
            case 1 -> {
                // transfère la previsionLine vers la boardLine
                // prépare la previsionLine pour le prochain round...
                if(gameNumber == 1){
                    this.placePrevisionCard(0);
                }
                else if(gameNumber == 2){
                    this.placePrevisionCard(2);
                }
            }
            case 2 -> {

                this.placePrevisionCard(2);
            }
            case 3 -> {

                this.placePrevisionCard(3);
            }
            case 4 -> {

            }
            default ->{}
        }
    };




    public Optional<Card> getPrevisionCardAt(int position) {
        return m_previsionLine.getCardAt(position);
    }

    @Override
    public String toString() {
        return "Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }
}
