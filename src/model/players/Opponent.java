package model.players;

import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Opponent extends Player {
    private final BoardLine m_previsionLine;
    // private int m_actions

    public Opponent(ArrayList<Animal> deck, BoardLine bord, BoardLine previsionBoard) {
        super(deck, bord);
        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_previsionLine = previsionBoard;
    }


    public void applyPrevision() {
        for (int i = 0; i < 4; i++) {
            Optional<Card> card = this.m_previsionLine.getCardAt(i);
            if (card.isPresent() && !this.m_boardLine.isOccupied(i)) {
                this.m_boardLine.placeCardAt(card.get(), i);
                this.m_previsionLine.removeCardAt(i);
            }
        }
    }

    public void placePrevisionCard(int position){
        if (!m_previsionLine.isOccupied(position)) {
            Animal animal = this.draw(); // lance NoSuchElementException si vide = bug
            m_previsionLine.placeCardAt(animal, position);
        }
    }


    public void opponentAction(List<Integer> positions) {
        //Applique la prévision du round précédent sur le board
        applyPrevision();

        //Prépare la prévision pour le round suivant à l'aide de la liste donnée en param
        for (int position : positions) {
            placePrevisionCard(position);
        }
    }

    @Override
    public String toString() {
        return "players.Opponent[" + super.toString() + ", prévision=" + m_previsionLine + "]";
    }

    public BoardLine getPrevisionLine() {
        return this.m_previsionLine;
    }
}
