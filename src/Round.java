import java.util.Optional;

public class Round {


    private void playerTurn() {
        this.m_player.drawCard(); // Pioche obligatoire en début de tour

        // Ici, une boucle de Scanner pour lire les commandes : [placer], [piocher], [fin]
        // Exemple simplifié d'appel de placement :
        // this.playCard(m_player, indexEnMain, positionPlateau);
    }

    public void playCard(Player p, int cardIndex, int position) {
        // Logique de retrait de la main du joueur et ajout sur le GameBoard
        // Doit vérifier les coûts (sang/os)
    }

    public void performDraw(Deck deck, Hand hand) {
        // On pioche une carte
        Optional<Card> drawnCard = deck.draw();

        // On l'ajoute à la main seulement si elle existe
        drawnCard.ifPresent(card -> hand.addCard(card));

        // mettre un message via Interactions
        if (drawnCard.isEmpty()) {
            // gérer le cas où la pioche est vide
        }
    }

    public void resolveCombatPhase(Player attacker, Player opponent, Game game, Interactions dialogue) {

        for (int i = 1; i <= 4; i++) {
            final int position = i; // Pour l'usage dans les lambdas

            attacker.getCardAt(position).ifPresent(card -> {
                // On récupère la puissance d'attaque de l'animal
                int damage = card.getAttackPoints();
                if (damage <= 0) return; // Un animal à 0 d'atk n'agit pas

                // On cherche s'il y a un adversaire en face
                Optional<Card> target = opponent.getCardAt(position);

                if (card.canFly()) {
                    game.updateScore(attacker == game.getPlayer() ? damage : -damage);
                    dialogue.displayMessage(card.getName() + " (Volant) ignore le bloqueur et attaque directement !");
                }
                else if (target.isPresent()) {
                    // CAS 1 : Une carte adverse bloque le passage
                    Card enemy = target.get();
                    enemy.takeDamage(damage);
                    dialogue.displayMessage(card.getName() + " inflige " + damage + " dégâts à " + enemy.getName());

                    if (enemy.isDead()) {
                        opponent.removeCardAt(position);
                        attacker.addBones(1); // Règle : 1 os par carte tuée
                        dialogue.displayMessage(enemy.getName() + " est éliminé ! +1 os pour l'attaquant.");
                    }
                } else {
                    // CAS 2 : Emplacement vide -> Attaque directe au score
                    game.updateScore(attacker == game.getPlayer() ? damage : -damage);
                    dialogue.displayMessage(card.getName() + " attaque l'adversaire directement ! Score modifié de " + damage);
                }
            }
        }
    }

    private void combatPhase() {
        // Parcourir les lignes du plateau et appeler card.attack(target)
        // Si la cible est vide ou si la carte est Volante -> m_score est modifié
    }

    private void startRound() {
        // Affichage des prévisions de l'adversaire sur la ligne centrale
    }

    private void opponentTurn() {
        // L'adversaire joue ses coups
    }

}
