import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public void clear() {
        for (int i = 0; i < 4; i++) {
            m_slots.set(i, Optional.empty());
        }
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

    public void notifyTurnStarted() {
        for (int i = 0; i < 4; i++) {
            final int pos = i; // Nécessaire pour la lambda
            m_slots.get(i).ifPresent(card -> {
                    card.triggerStartTurn(pos, this);
            });
        }
    }

    @Override
    public String toString() {
        return m_slots.toString();
    }

    public CombatResult attack(BoardLine opponentLine) {
        int totalDirectDamage = 0;
        int kills = 0;
        // ON SAUVEGARDE LES POSITIONS POUR PAS QUE L'ELAN SE DEPLACE ET ATTAQUE UNE DEUXIEME FOIS....
        Set<Card> alreadyAttacked = new HashSet<>(); // ← cartes déjà traitées

        for (int i = 0; i < 4; i++) {

            Optional<Card> optCard = this.getCardAt(i);

            if (optCard.isPresent()) {
                Card attackerCard = optCard.get();

                if (alreadyAttacked.contains(attackerCard)){
                    continue; // ← skip si déjà traitée
                }
                alreadyAttacked.add(attackerCard);

                // Renvoie la carte à attaquer si elle existe, empty sinon
                Optional<Card> optTarget = opponentLine.getCardAt(i);

                if (attackerCard.isTargetBypassed() || optTarget.isEmpty()) {
                    totalDirectDamage += attackerCard.getAttackPoints();

                } else {
                    // Combat classique carte contre carte
                    Card target = optTarget.get();
                    attackerCard.attack(target);

                    if (target.isDead()) {
                        opponentLine.removeCardAt(i);
                        kills ++;
                    }
                }
                attackerCard.triggerPostAttack(i, this);
            }
        }
        return new CombatResult(totalDirectDamage, kills);
    }

    public void replaceCard(int position, Card newCard) {
        if (position >= 0 && position < 4) {
            m_slots.set(position, Optional.of(newCard));
        }
    }

    public void moveCard(int from, int to) {
        if (m_slots.get(from).isPresent() && m_slots.get(to).isEmpty()) {
            Optional<Card> card = m_slots.get(from);
            m_slots.set(to, card);           // Place la carte à la nouvelle position
            m_slots.set(from, Optional.empty()); // Vide l'ancienne position
        }
    }

}
