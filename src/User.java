import java.util.ArrayList;

public class User extends Player{
    private Hand m_hand;

    public User(Deck deck) {
        super(deck); // Appel au constructeur de la classe mère
        this.m_hand = new Hand();
    }

    // on pioche 1 carte dans le Deck et on l'ajoute à la main.
    public void draw() {
        m_deck.draw().ifPresent(card -> m_hand.addCard(card));
    }

    public void playCardToBoard(int handIndex, int boardPosition) {
        Card toPlay = m_hand.removeCardAt(handIndex);
        this.placeCardAt(toPlay, boardPosition);
    }

    @Override
    public void takeTurn() {
        // La logique ici dépendra de votre classe de dialogue (Phase 1)
        // Elle attendra les saisies (piocher, poser, finir le tour)
    }

    @Override
    public String toString() {
        // Utilisation de super.toString() pour la cohésion
        return "User[" + super.toString() + ", main=" + m_hand + "]";
    }
}
