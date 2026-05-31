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

    //-------- code pour le Deck -----
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

    //------- code pour la BoardLine ------

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

    public void removeCardAt(int index) {
        m_boardLine.removeCardAt(index);
    }

    // ------

    @Override
    public String toString() {
        // Utilisation de getClass().getSimpleName() pour différencier User/Opponent
        return this.getClass().getSimpleName() +
                " Plateau=" + m_boardLine + "]";
    }


        /*
    // En gros on regarde si la position est valide, et on regarde si il y a une carte, si il y a une carte, on
    public boolean takeDamage(int damage, int position) {
        Optional<Card> optCard = this.getCardAt(position);
        if (optCard.isPresent()) {
            optCard.get().takeDamage(damage);
            if (optCard.get().isDead()) {
                this.removeCardAt(position);
                // +1 os pour l'adversaire — à gérer dans Game
            }
            return true; // une carte a encaissé les dégâts
        }
        return false; // emplacement vide → dégâts directs au score
    }
*/

        /*
     for (int i = 0; i < 4; i++) {
                int damage = m_currentPlayer.getCardAt(i).get().getAttackPoints(); // PENSER A METTRE CA DANS OBSTACLE ET RETURN 0
                if (!m_opponent.takeDamage(damage, i)) {
                    damagePoint += damage;
                }

            }
     */
}
