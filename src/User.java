import java.util.Optional;

public class User extends Player{
    private Hand m_hand;

    public User(Deck deck) {
        super(deck); // Appel au constructeur de la classe mère
        this.m_hand = new Hand();
    }

    // on pioche 1 carte dans le Deck et on l'ajoute à la main.
    public void draw(Interactions dialogue) {
        // L'utilisateur a le choix
        boolean wantsToDraw = dialogue.askConfirmation("Voulez-vous piocher une carte pour ce tour ?");

        if (wantsToDraw) {
            //pioche si le deck pas vide (gestion via Optional)
            this.m_deck.draw().ifPresent(card -> {
                m_hand.addCard(card);
                dialogue.displayMessage("Vous avez pioché : " + card.getName());
            });
        } else {
            dialogue.displayMessage("Vous avez décidé de ne pas piocher.");
        }
    }


    public void takeTurn(Interactions dialogue) {
        boolean turnEnded = false;
        while (!turnEnded) {
            Actions choice = dialogue.askNextAction(); // Dialogue gère le Scanner
            switch (choice) {
                case PLACE -> handlePlacement(dialogue);
                //case SACRIFICE -> handleSacrifice(dialogue);
                case END -> turnEnded = true;
            }
        }
    }

    public Optional<Animal> getHandCard(int index){
        return m_hand.getCard(index);
    }

    public int getHandSize(){
        return m_hand.getSize();
    }

    public Optional<Card> getCardAt(int position){
        return this.m_boardLine.getCardAt(toIndex(position));
    }

    public boolean verifBoneCost(Animal test){
        if (test.getBoneCost() > 0 && this.getBonesToken() < test.getBoneCost()){
            return true;
        }
        return false;
    }

    public boolean verifBloodCost(Animal test){
        if(test.getBloodCost() > 0 && this.m_boardLine.getSacrificableCount() < test.getBloodCost()){
            return true;
        }
        return false;
    }

    public boolean validatePosition(int position) {
        return this.m_boardLine.validatePosition(toIndex(position));
    }

    private int toIndex(int position) {
        return position - 1;
    }

    private void handlePlacement(Animal card, int position, int bloodAlreadyPaid) {

        if (!prepareSlot(pos, dialogue, bloodAlreadyPaid)) return false;


        // paiement du RESTE du coût
        // On calcule combien il reste à payer (ex: 2 sang - 1 déjà payé = 1 restant)
        int remainingBlood = Math.max(0, card.getBloodCost() - bloodAlreadyPaid);
        int remainingBones = card.getBoneCost(); // Les os ne sont jamais payés par sacrifice direct

        if (remainingBlood > 0) {
            executeBloodPayment(remainingBlood, dialogue);
        } else if (remainingBones > 0) {
            // on enlève juste le nombre de
            m_bonesToken -= remainingBones;
        }

        // On pose la carte sur le plateau.
        this.playCardToBoard(letter, pos);
        dialogue.displayMessage(card.getName() + " est prêt au combat !");
    }

    /*
    private boolean canAfford(Card card, Interactions dialogue) {
        if (card.getBoneCost() > 0 && m_bonesToken < card.getBoneCost()) {
            dialogue.displayMessage("Pas assez d'os (" + m_bonesToken + "/" + card.getBoneCost() + ").");
            return false;
        }
        if (card.getBloodCost() > 0 && m_boardLine.getSacrificableCount() < card.getBloodCost()) {
            dialogue.displayMessage("Pas assez de créatures à sacrifier pour poser cette carte, veuillez en choisir une autre coutant moins de "+ m_boardLine.getSacrificableCount());
            return false;
        }
        return true;
    }
     */

    private void sacrifice(int remainingBlood, Card card, Interactions dialogue) {
        if (remainingBlood > 0) {
            for (int i = 0; i < remainingBlood; i++) {
                dialogue.displayMessage("Sacrifice " + (i + 1) + "/" + remainingBlood + " :");
                int sacrificePos = dialogue.askPositionSacrifice();

                Optional<Card> optTarget = m_boardLine.getCardAt(sacrificePos);

                // On vérifie si l'emplacement contient bien une carte
                if (optTarget.isPresent()) {
                    Card target = optTarget.get();

                    // On vérifie si elle est sacrifiable (Obstacle ou pas ?)
                    if (target.isSacrificable()) {
                        m_boardLine.removeCardAt(sacrificePos);
                        m_bonesToken++; // Règle : +1 os par sacrifice
                        dialogue.displayMessage("Emplacement libéré. +1 os. +1 goutte de sang.");
                    } else {
                        dialogue.displayMessage("Impossible : un obstacle ne peut pas être sacrifié !");
                        i--; // On redemande cette goutte de sang
                    }
                } else {
                    // CAS MANQUANT : L'emplacement est vide
                    dialogue.displayMessage("Erreur : Il n'y a aucune carte en position " + sacrificePos + ".");
                    i--; // IMPORTANT: On décrémente pour forcer l'utilisateur à refaire ce sacrifice
                }
            }
        }
    }

/*
    private boolean prepareSlot(int pos, Interactions dialogue) {
        if (m_boardLine.isOccupied(pos)) {
            dialogue.displayMessage("L'emplacement " + pos + " est occupé.");
            return false;
        }
        return true;
    }
*/

    private boolean prepareSlot(int pos, Interactions dialogue, int bloodAlreadyPaid) {
        Optional<Card> optTarget = m_boardLine.getCardAt(pos);

        // Si l'emplacement est vide, rien à faire = on peut poser la carte !
        if (optTarget.isEmpty()) {
            return true;
        }

        Card target = optTarget.get();

        // Obstacle ?
        if (!target.isSacrificable()) {
            dialogue.displayMessage("L'emplacement est occupé par un " + target.getName() + " (Obstacle incassable).");
            return false; // On ne peut pas poser ici
        }

        // Si pas obstacle => demander à l'utilisateur si il veut sacrifier cette carte pour poser la sienne à la place
        boolean confirm = dialogue.askConfirmation("L'emplacement est occupé par " + target.getName() + ". Le sacrifier pour libérer la place ?");

        if (confirm) {
            m_boardLine.removeCardAt(pos);
            bloodAlreadyPaid = 1;
            m_bonesToken++; // Règle : tout sacrifice donne +1 os
            dialogue.displayMessage("Sacrifice de " + target.getName() + " pour libérer la place (+1 sang).");
            dialogue.displayMessage("Emplacement libéré. +1 os.");
            return true; // L'emplacement est maintenant libre, on renvoie true !
        }

        return false; // L'utilisateur a annulé, on ne peut pas poser
    }


    // Transfert définitif d'une carte de la main vers le plateau.
    public void playCardToBoard(char handLetter, int boardPosition) {
        Card cardToPlay = m_hand.removeCard(handLetter);

        // On appelle placeCardAt pour la déposer sur la BoardLine
        this.placeCardAt(cardToPlay, boardPosition);
    }


    @Override
    public String toString() {
        // Utilisation de super.toString() pour la cohésion
        return "User[" + super.toString() + ", main=" + m_hand + "]";
    }
}




