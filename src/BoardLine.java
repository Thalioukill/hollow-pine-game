import java.util.ArrayList;
import java.util.Optional;

public class BoardLine {
    private final ArrayList<Optional<Card>> m_slots;

    public BoardLine() {
        m_slots = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            m_slots.add(Optional.empty());
        }
    }

    public boolean validateIndex(int position) {
        return position >= 0 && position < 4;
    }

    // renvoie un Optional vide si l'index est mauvais => géré dans le User avec une Interactions : froce à vérifier que tout va bien.
    public Optional<Card> getCardAt(int position) {
        return m_slots.get(position);
    }

     // lance une exception car si le code tente de poser une carte hors-limite après les vérifications du User, c'est un bug...
    public void placeCardAt(Card card, int position) {
        if (!validateIndex(position)) {
            throw new IndexOutOfBoundsException("Bug interne : placement hors limites");
        }
        m_slots.set(position, Optional.of(card));
    }

    public boolean isOccupied(int position) {
        // Une position invalide n'est, par définition, pas occupée.
        return validateIndex(position) && m_slots.get(position).isPresent();
    }

    public int getSacrificableCount() {
        int count = 0;
        for (Optional<Card> slot : m_slots) {
            // Seules les Animaux sont comptés...
            if (slot.isPresent() && slot.get().isSacrificable()) {
                count++;
            }
        }
        return count;
    }

     // Retire la carte de l'emplacement et libère la place et lance une exception si la position est invalidee
    public void removeCardAt(int position) {
        if (!validateIndex(position)) {
            throw new IndexOutOfBoundsException("Bug interne : retrait à une position hors limites (" + position + ")");
        }
        m_slots.set(position, Optional.empty());
    }

    public void set(int index, Optional<Card> card){
        this.m_slots.set(index, card);
    }

    @Override
    public String toString() {
        return m_slots.toString();
    }

    public int attack(BoardLine opponentLine) {
        int totalDirectDamage = 0;

        for (int i = 0; i < 4; i++) {

            Optional<Card> optCard = this.getCardAt(i);

            if (optCard.isPresent()) {
                Card attackerCard = optCard.get();
                Optional<Card> optTarget = opponentLine.isAttacked(i);

                if(optTarget.isPresent()){

                    Card target = optTarget.get();
                    attackerCard.attack(target);

                    if(target.isDead()){
                        opponentLine.removeCardAt(i);
                    }
                }
                else{
                    totalDirectDamage += attackerCard.getAttackPoints();
                }
            }
        }
        return totalDirectDamage;
    }

    // Renvoie la carte à attaquer si elle existe, empty sinon
    public Optional<Card> isAttacked(int position) {
        return this.getCardAt(position); // déjà un Optional<Card> !
    }
/*
    public int attack(BoardLine opponentLine, Game game) {
        for (int i = 0; i < 4; i++) {
            Optional<Card> optCard = m_slots.get(i);
            if (optCard.isPresent()) {
                Card card = optCard.get();
                if (card.getAttackPoints() > 0) {
                    card.attack(i, opponentLine, game);
                }
            }
        }
    }
 */
}
