import java.util.Scanner;
import java.util.Optional;

public class Game {
    // SAVOIR OU EST CE QU'ON VA CREER UNE INTERCATIONS (1 dialogie avec l'utilisateur...)
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private User m_user;
    private Player m_currentPlayer; // Joueur qui joue actuellement
    private Interactions m_dialogue;
    private GameStrategy m_strategy;
    private Opponent m_opponent;

    public Game(GameBoard board, User player, Interactions dialogue, GameStrategy strat) {
        this.m_gameboard = board;
        this.m_user = player;
        this.m_dialogue = dialogue;
        this.m_score = 0; // La balance commence à l'équilibre
        this.m_strategy = strat;
        this.m_opponent = this.m_strategy.initOpponent();

    }

    // va appeler
    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        int nbRounds = 0;
        do {
            // appeler la méthode de GameBoard qui affiche qq chose
            nbRounds ++;
            //startRound(nbRounds); // carte que le Opponent jouera après le User (prévisions)
            // On met le USER comme premier joueur du round
            this.m_currentPlayer = this.m_user;

            // Le joueur pioche au départ du tour...
            m_dialogue.draw(this.m_user);

            // propose à l'utilisateur ce qu'il veut faire => placer une acrte ou finir son tour...
            takeTurn(m_dialogue, this.m_user);

            // appelle la méthode de combat : User attaque l'Opponent
            m_score += combatPhase(this.m_opponent); // Attaque des cartes + mise à jour du score

            this.m_currentPlayer = this.m_opponent;

            this.m_opponent.opponentAction(); // L'Opponent place ses cartes sur sa ligne de jeu.

            // appelle la méthode de combat : Opponent attaque le User
            m_score -= combatPhase(this.m_user); // Attaque des cartes + mise à jour du score

        } while (Math.abs(this.m_score) < 5);
        return this.m_score >= 5;
    }

    public void takeTurn(Interactions m_dialogue, User human) {
        boolean turnEnded = false;
        while (!turnEnded) {
            boolean wantsToPlay = m_dialogue.askNextAction(human);
            if (!wantsToPlay) {
                turnEnded = true;
            }
        }
    }

    public int combatPhase(Player attackedPlayer) {

        return this.m_currentPlayer.CombatPhase(attackedPlayer);
        /*
        // On verifie qui est le joueur qui joue actuellement (Ici m_user ce qui veut dire que la personne qui subit les dégats est le m_opponent)
        int damagePoint = 0;
        // equals
        if (m_currentPlayer == m_user) {
            for (int i = 0; i < 4; i++) {
                int damage = m_currentPlayer.getCardAt(i).get().getAttackPoints(); // PENSER A METTRE CA DANS OBSTACLE ET RETURN 0
                if (!m_opponent.takeDamage(damage, i)) {
                    damagePoint += damage;
                }

            }
            // ON change le joueur en cours
            m_currentPlayer = m_opponent;
        } else {
            for (int i = 0; i < 4; i++) {
                int damage = m_currentPlayer.getCardAt(i).getAttackPoints();
                if (!m_user.takeDamage(damage, i)) // Faire methode qui renvoie un boolean (True si une creature s'y trouve et prend des dégats, false si y a pas de créature)
                {
                    damagePoint += damage;
                }
            }
            m_currentPlayer = m_user;
        }
        // on retourne les dégats pour changer le score de Game
        return damagePoint;
        */
    }

/*
// il faudrait faire un switchCase avec le nbre de Rounds, chaque round est prévu à l'avance
    private void startRound(int nbRounds) {
        // Affichage des prévisions de l'adversaire sur la ligne du haut.
        // Méthode à implémenter que pour le OPPONENT !!!

    }
*/

    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
