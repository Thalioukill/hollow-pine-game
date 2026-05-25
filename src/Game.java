import java.util.Scanner;
import java.util.Optional;

public class Game {
    // SAVOIR OU EST CE QU'ON VA CREER UNE INTERCATIONS (1 dialogie avec l'utilisateur...)
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private User m_user;
    private Opponent m_opponent;

    public Game(GameBoard board, User player, Opponent opponent) {
        this.m_gameboard = board;
        this.m_user = player;
        this.m_opponent = opponent;
        this.m_score = 0; // La balance commence à l'équilibre
    }

    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        do {
            Round currentRound = new Round(m_user, m_opponent, m_score);
            currentRound.startRound(); // carte que le Opponent jouera après le User (prévisions)
            currentRound.performDraw();
            currentRound.takeTurn();
            m_score += currentRound.combatPhase();
            currentRound.takeTurn(); // L'Opponent place ses cartes sur sa ligne de jeu.
            m_score -= currentRound.combatPhase(); // Attaque des cartes + mise à jour du score

        } while (Math.abs(this.m_score) < 5);
        return this.m_score >= 5;
    }

    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
