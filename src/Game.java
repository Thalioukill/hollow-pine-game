import java.util.Scanner;
import java.util.Optional;

public class Game {
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private User m_player;
    private Opponent m_opponent;

    /**
     * Constructeur initialisant une partie avec ses dépendances injectées.
     */
    public Game(GameBoard board, User player, Opponent opponent) {
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
    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        while (Math.abs(this.m_score) < 5) {
            Round currentRound = new Round();
            currentRound.startRound();
            currentRound.playerTurn();
            currentRound.combatPhase(); // Attaque des cartes et mise à jour du score

            if (Math.abs(this.m_score) < 5) {
                currentRound.opponentTurn();
            }
        }
        return this.m_score >= 5;
    }

    /**
     * Méthode obligatoire pour le débuggage selon les guidelines
     */
    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
