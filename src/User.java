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


    public void playCardToBoard(char handLetter, int boardPosition) {
        // On retire de la main via la lettre
        Card toPlay = m_hand.removeCard(handLetter);
        // On place sur le plateau via le chiffre (1-4)
        this.placeCardAt(toPlay, boardPosition);
    }


    public void takeTurn(Interactions dialogue) {
        boolean turnEnded = false;
        while (!turnEnded) {
            Actions choice = dialogue.askNextAction(); // Dialogue gère le Scanner
            switch (choice) {
                case PLACE -> handlePlacement(dialogue);
                case SACRIFICE -> handleSacrifice(dialogue);
                case END -> turnEnded = true;
            }
        }
    }

    private void handlePlacement(Interactions dialogue) {
        // récup la lettre de la carte en main
        char letter = dialogue.askCardLetter(m_hand.getSize());

        // Récup la position sur le plateau
        int pos = dialogue.askPosition();

        // Vérif des couts (os ou sang...)
        this.playCardToBoard(letter, pos);

        dialogue.displayMessage("Carte posée en position " + pos);
    }

    private void handleSacrifice(Interactions dialogue) {
        // Demander quelle position l'utilisateur veut libérer (1 à 4)
        int pos = dialogue.askPosition();

        // Vérifier si une carte occupe l'emplacement
        if (m_boardLine.isOccupied(pos)) {
            // On récupère la carte avant de la supprimer (optionnel, pour l'affichage)
            Card card = m_boardLine.getCardAt(pos).get();

            // Est ce que la carte récupérée est un Obstacle ?
            if (!card.isSacrificable()) {
                dialogue.displayMessage("Impossible : un obstacle (" + card.getName() + ") ne peut pas être sacrifié !");
                return; // On arrête la méthode ici
            }

            // retirer la cart du plateau si ce n'est pas un obstacle
            m_boardLine.removeCardAt(pos);

            // Augmenter le compteur d'os de 1
            m_bonesToken++;

            // Toujours informer l'utilisateur...
            dialogue.displayMessage("L'animal " + card.getName() + " a été sacrifié. +1 os (Total : " + m_bonesToken + ").");
        } else {
            // Erreur d'utilisation : l'emplacement est déjà vide
            dialogue.displayMessage("Impossible : il n'y a aucune carte en position " + pos + ".");
        }
    }


    @Override
    public String toString() {
        // Utilisation de super.toString() pour la cohésion
        return "User[" + super.toString() + ", main=" + m_hand + "]";
    }
}
