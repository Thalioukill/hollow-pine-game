package model.players;

import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Card;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class Player {
    protected BoardLine m_boardLine;
    protected ArrayList<Animal> m_deck;

    public Player(ArrayList<Animal> deck, BoardLine bord) {
        this.m_deck = deck;

        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_boardLine = bord;
    }

    //-------- code pour le Deck
    //Ajoute une carte au deck : utilisé pour l'initialisation et les récompenses
    public void addCard(Animal card) {
        m_deck.add(card);
    }

    //Optional contenant la carte, ou Optional.empty() si vide
    public Animal draw() {
        // retire et renvoie la première carte de la liste
        if (m_deck.isEmpty()) {
            throw new NoSuchElementException("La pioche est vide !");
        }
        return m_deck.removeFirst();
    }

    public int getDeckSize() {
        return m_deck.size();
    }

    public boolean isEmpty() {
        return m_deck.isEmpty();
    }

    // ---------

    //------- code pour la board.BoardLine

    // il ne faut pas gérer la position ici ?
    public Optional<Card> getBoardLineCardAt(int index) {
        return m_boardLine.getCardAt(index);
    }

    public int combatPhase(Player opponent){
        return this.m_boardLine.attack(opponent.m_boardLine);
    }

    public void placeCardAt(Card card, int position) {
        this.m_boardLine.placeCardAt(card, position);
    }


    public int getSacrificableCount(){
        return this.m_boardLine.getSacrificableCount();
    }

    public BoardLine getBoardLine() {
        return this.m_boardLine;
    }

    public void notifyTurnStarted(){
        this.m_boardLine.notifyTurnStarted();
    }

}
