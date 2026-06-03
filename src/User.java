import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

public class User extends Player{
    private final ArrayList<Animal> m_hand;
    private int m_bonesToken;
    //private final ArrayList<Animal> m_deckSave;

    public User(ArrayList<Animal> deck, BoardLine bord) {
        super(deck, bord); // Appel au constructeur de la classe mère
        //this.m_deckSave = deck;
        this.m_hand = new ArrayList<Animal>();
        // Pioche initiale de 4 cartes
        for (int i = 0; i < 4; i++) {
            this.m_hand.add(this.draw());
        }
        this.m_bonesToken = 0;
    }

    // faire Classe Deck !!!! + dans user faire une méthode qui réinit son deck (injection dépendances)
    // classe Player deepCopy ?
    public void resetForNewGame(ArrayList<Animal> freshDeck) {
        /*this.m_deck = deepCopy(m_deckSave);
        Collections.shuffle(this.m_deck);
        this.m_boardLine.clear();
        this.m_hand.clear();
        this.m_bonesToken = 0;
        // Pioche initiale de 4 cartes
        for (int i = 0; i < 4; i++) {
            this.m_hand.add(this.draw());
        }
         */

        // remplace le deck par le nouveau deck fourni
        this.m_deck = freshDeck;

        // réinitialise les statistiques de jeu
        this.m_bonesToken = 0;
        this.m_hand.clear();
        this.m_boardLine.clear();

        // Pioche initiale de 4 cartes
        for (int i = 0; i < 4; i++) {
            this.draw();
        }
    }

    /*
    public void addPermanentCard(Animal card) {
        m_deckSave.add(card); // persiste pour toutes les parties futures
    }

    public static ArrayList<Animal> deepCopyDeck(ArrayList<Animal> source) {
        ArrayList<Animal> copy = new ArrayList<>();
        for (Animal a : source) {
            copy.add(a.cloneAnimal()); // cloneAnimal = copie profonde...
        }
        return copy;
    }


    public ArrayList<Animal> getDeckSave() {
        return new ArrayList<>(this.m_deckSave);
    }
 */

    // replaceDeck()


    // ----- code pour le DECK ------

    // melange les cartes du deck.
    public void shuffle() {
        Collections.shuffle(this.m_deck);
    }

    //------ Code pour la MAIN ------
    public void addCard(Animal card) {
        // Une main peut techniquement grandir, contrairement au plateau
        this.m_hand.add(card);
    }

    public int getHandSize() {
        return m_hand.size();
    }

    // Permet de regarder une carte sans la retirer.
    public Animal getHandCard(int index){
        return m_hand.get(index);// IndexOutOfBoundsException si bug interne
    }

    // retire la carte de la main et retourne l'élément qui était dedans à l'index de la lettre donnée (ici la Card), on l'utilise uniquement quand yune carte est
    // jouée et donc placée autre part, donc si l'index est mauvais c'est une erreur du code...

    public Animal removeCard(int index) {
        if (index < 0 || index >= m_hand.size()) {
            // Exception non-contrôlée pour signaler un bug interne
            throw new IndexOutOfBoundsException("Bug interne : retrait d'une lettre invalide '" + (char)('A' + index) + "'");
        }
        return m_hand.remove(index);
    }

    // ---------

    public int getBonesToken(){
        return this.m_bonesToken;
    }

    public void addBonesToken(){
        this.m_bonesToken++;
    }

    // ----- code pour la BoardLine -----
    public Optional<Card> getCardAt(int position){
        return this.m_boardLine.getCardAt(position);
    }


    public boolean hasEnoughBones(Animal test){
        return test.getBoneCost() == 0 || this.getBonesToken() >= test.getBoneCost();
    }

    /*
        if (test.getBoneCost() > 0 && this.getBonesToken() < test.getBoneCost()){
        return false;
        }
        return true;
     */

    public boolean hasEnoughBlood(Animal test){
        return test.getBloodCost() == 0 || this.m_boardLine.getSacrificableCount() >= test.getBloodCost();
    }
    /*
        if(test.getBloodCost() > 0 && this.m_boardLine.getSacrificableCount() < test.getBloodCost()){
            return false;
        }
     */

    public boolean validatePosition(int position) {
        return this.m_boardLine.validateIndex(position);
    }
    /*
    private int toIndex(int position) {
        return position - 1;
    }
    */
    public void spendBones(int amount) {
        this.m_bonesToken -= amount;
    }

    public void sacrifice(int position){
        this.m_boardLine.removeCardAt(position);
        //m_bonesToken++; // Règle : +1 os par sacrifice
        this.m_bonesToken++;
    }

    // Transfert définitif d'une carte de la main vers le plateau.
    public void playCardToBoard(int handIndex, int boardPosition) {
        Card cardToPlay = this.m_hand.remove(handIndex);

        // On appelle placeCardAt pour la déposer sur la BoardLine
        this.placeCardAt(cardToPlay, boardPosition);
    }

    public void notifyTurnStarted(){
        this.m_boardLine.notifyTurnStarted();
    }


    @Override
    public String toString() {
        // Utilisation de super.toString() pour la cohésion
        return "User[" + super.toString() + ", main=" + m_hand + "]";
    }
}




