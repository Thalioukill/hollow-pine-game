import java.util.Optional;

public class Round {
    private final User m_user;
    private final Opponent m_opponent;
    private Player m_currentPlayer; // Joueur qui joue actuellement
    private final Deck m_deck;
    private final Interactions m_dialogue;

    // Le score sera demandé au Game.

    public Round(User user, Player opponent, Deck deck, Interactions dialogue) {
        this.m_user = user;
        this.m_opponent = opponent;
        m_currentPlayer = user; // On met le USER comme premier joueur du round
        this.m_deck = deck;
        this.m_dialogue = dialogue;
    }


    /*
            Round currentRound = new Round();
            currentRound.opponentTurn(m_opponent);
            currentRound.startRoundPlayer();
            currentRound.playerTurn();
            currentRound.combatPhase();
     */

    public void takeTurn() {
        // boucle de Scanner pour lire les commandes : [placer], [fin]
        this.m_currentPlayer.takeTurn(this.m_dialogue);

        }


    public void performDraw(User currentPlayer, Deck deck) {
        m_user.draw(deck, this.m_dialogue); // Pioche obligatoire en début de tour

    }

    public int CombatPhase() {

        // On verifie qui est le joueur qui joue actuellement (Ici m_user ce qui veut dire que la personne qui subit les dégats est le m_opponent)
        int damagePoint = 0;
        if (m_currentPlayer == m_user)
        {
            for (int i = 0; i < 4; i++) {
                int damage = m_currentPlayer.getCardAt(i).getAttackPoints(); // PENSER A METTRE CA DANS OBSTACLE ET RETURN 0
                if (!m_opponent.takeDamage(damage, i)) {
                    damagePoint += damage;
                }

            }
            // ON change le joueur en cours
            m_currentPlayer = m_opponent;
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                int damage = m_currentPlayer.getCardAt(i).getAttackPoints();
                if (!m_user.takeDamage(damage, i)) // Faire methode qui renvoie un boolean (True si une creature s'y trouve et prend des dégats, false si y a pas de créature)
                {
                    damagePoint+= damage;
                }
            }
            m_currentPlayer = m_user;
        }
        // on retourne les dégats pour changer le score de Game
        return damagePoint;

//        for (int i = 1; i <= 4; i++) {
//            final int position = i; // Pour l'usage dans les lambdas
//
//            attacker.getCardAt(position).ifPresent(card -> {
//                // On récupère la puissance d'attaque de l'animal
//                int damage = card.getAttackPoints();
//                if (damage <= 0) return; // Un animal à 0 d'atk n'agit pas
//
//                // On cherche s'il y a un adversaire en face
//                Optional<Card> target = m_opponent.getCardAt(position);
//
//                if (card.canFly()) {
//                    game.updateScore(attacker == game.getPlayer() ? damage : -damage);
//                    dialogue.displayMessage(card.getName() + " (Volant) ignore le bloqueur et attaque directement !");
//                }
//                else if (target.isPresent()) {
//                    // CAS 1 : Une carte adverse bloque le passage
//                    Card enemy = target.get();
//                    enemy.takeDamage(damage);
//                    dialogue.displayMessage(card.getName() + " inflige " + damage + " dégâts à " + enemy.getName());
//
//                    if (enemy.isDead()) {
//                        opponent.removeCardAt(position);
//                        attacker.addBones(1); // Règle : 1 os par carte tuée
//                        dialogue.displayMessage(enemy.getName() + " est éliminé ! +1 os pour l'attaquant.");
//                    }
//                } else {
//                    // CAS 2 : Emplacement vide -> Attaque directe au score
//                    game.updateScore(attacker == game.getPlayer() ? damage : -damage);
//                    dialogue.displayMessage(card.getName() + " attaque l'adversaire directement ! Score modifié de " + damage);
//                }
//            }
//        }
    }


    private void startRound() {
        // Affichage des prévisions de l'adversaire sur la ligne centrale
    }

    private void opponentTurn() {
        // L'adversaire joue ses coups
    }

}
