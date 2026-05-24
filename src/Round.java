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
