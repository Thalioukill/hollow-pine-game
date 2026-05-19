import java.util.Scanner;
import java.util.Optional;

public class Game {
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private Friend m_player;
    private Opponent m_opponent;

    /**
     * Constructeur initialisant une partie avec ses dépendances injectées.
     */
    public Game(GameBoard board, Friend player, Opponent opponent) {
        this.m_gameboard = board;
        this.m_player = player;
        this.m_opponent = opponent;
        this.m_score = 0; // La balance commence à l'équilibre
    }

    /**
     * Boucle principale d'un match.
     *
     * @return true si le joueur Ami a gagné, false s'il a perdu.
     */
    public boolean runMatch() {
        // La partie s'arrête si l'écart atteint 5 points
        while (Math.abs(this.m_score) < 5) {
            this.startRound();
            this.playerTurn();
            this.combatPhase(); // Attaque des cartes et mise à jour du score

            if (Math.abs(this.m_score) < 5) {
                this.opponentTurn();
            }
        }
        return this.m_score >= 5;
    }

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

    /**
     * Méthode obligatoire pour le débuggage selon les guidelines
     */
    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
