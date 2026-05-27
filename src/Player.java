import java.util.ArrayList;
import java.util.Optional;

public abstract class Player {

    /*
        PROPOSER UN SACRIFICE SI L'EMPLACEMENT OU POSER LA CARTE EST DEJA PRIS !!!!!!
     */

    protected int m_bonesToken;
    protected Deck m_deck;
    protected BoardLine m_boardLine;

    public Player(Deck d) {
        this.m_deck = d;
        this.m_bonesToken = 0;

        // Initialisation de la ligne de plateau (4 emplacements vides)
        this.m_boardLine = new BoardLine();
    }

    public Optional<Card> getCardAt(int position) {
        return m_boardLine.getCardAt(position);
    }

    public int getBonesToken(){
        return this.m_bonesToken;
    }

    public void removeCardAt(int position) {
        m_boardLine.removeCardAt(position);
    }

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

    public int CombatPhase(Player opponent){
        int damagePoint = 0;
        // On peut faire une méthode dans BoardLine
        for (int i = 0; i < 4; i++) {
            Optional<Card> optCard = this.getCardAt(i);
            if(optCard.isPresent()) {
                Card card = optCard.get();
                int damage = card.getAttackPoints(); // PENSER A METTRE CA DANS OBSTACLE ET RETURN 0
                if (!opponent.takeDamage(damage, i)) {
                    damagePoint += damage;
                }
            }
        }
        return damagePoint;
    }

    /*
     for (int i = 0; i < 4; i++) {
                int damage = m_currentPlayer.getCardAt(i).get().getAttackPoints(); // PENSER A METTRE CA DANS OBSTACLE ET RETURN 0
                if (!m_opponent.takeDamage(damage, i)) {
                    damagePoint += damage;
                }

            }
     */

    public void placeCardAt(Card card, int position) {
        this.m_boardLine.placeCardAt(card, position);
    }

    //public abstract void takeTurn(Interactions dialogue);

    @Override
    public String toString() {
        // Utilisation de getClass().getSimpleName() pour différencier User/Opponent
        return this.getClass().getSimpleName() +
                "[Os=" + m_bonesToken + ", Plateau=" + m_boardLine + "]";
    }
}
