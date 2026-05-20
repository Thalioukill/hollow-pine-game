public class Round {
    /**
     * Gère les actions du joueur Ami (pioche, placement, fin de tour).
     */
    private void playerTurn() {
        this.m_player.drawCard(); // Pioche obligatoire en début de tour

        // Ici, une boucle de Scanner pour lire les commandes : [placer], [piocher], [fin]
        // Exemple simplifié d'appel de placement :
        // this.playCard(m_player, indexEnMain, positionPlateau);
    }

    /**
     * Orchestre le placement d'une carte sur le plateau.
     */
    public void playCard(Player p, int cardIndex, int position) {
        // Logique de retrait de la main du joueur et ajout sur le GameBoard
        // Doit vérifier les coûts (sang/os)
    }

    /**
     * Phase où chaque carte sur le plateau déclenche son attaque.
     */
    private void combatPhase() {
        // Parcourir les lignes du plateau et appeler card.attack(target)
        // Si la cible est vide ou si la carte est Volante -> m_score est modifié
    }

    private void startRound() {
        // Affichage des prévisions de l'adversaire sur la ligne centrale
    }

    private void opponentTurn() {
        // L'adversaire joue ses coups (hardcodés pour la Phase 1)
    }

}
