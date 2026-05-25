import java.util.Scanner;
import java.util.Optional;

public class Game {
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private User m_player;
    private Opponent m_opponent;

    public Game(GameBoard board, User player, Opponent opponent) {
        this.m_gameboard = board;
        this.m_player = player;
        this.m_opponent = opponent;
        this.m_score = 0; // La balance commence à l'équilibre
    }

    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        do {
            Round currentRound = new Round();
            currentRound.opponentTurn();
            currentRound.startRoundPlayer();
            currentRound.playerTurn();
            currentRound.combatPhase(); // Attaque des cartes et mise à jour du score

        } while (Math.abs(this.m_score) < 5)
        return this.m_score >= 5;
    }

    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
