package game;
import model.players.User;
import model.players.Opponent;
import model.players.Player;
import game.strategy.GameStrategy;
import view.GameBoard;
import view.Interactions;
import java.util.ArrayList;

public class Game {
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private final GameBoard m_gameboard;
    private final User m_user;
    private Player m_currentPlayer; // Joueur qui joue actuellement
    private final Interactions m_dialogue;
    private final GameStrategy m_strategy;
    private final Opponent m_opponent;
    private final ArrayList<String> m_turnMessages = new ArrayList<>();

    public Game(GameBoard board, User player, Interactions dialogue, GameStrategy strat, Opponent opponent) {
        this.m_gameboard = board;
        this.m_user = player;
        this.m_dialogue = dialogue;
        this.m_score = 0; // La balance commence à l'équilibre
        this.m_strategy = strat;
        this.m_opponent = opponent;

    }

    // va appeler
    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        int nbRounds = 0;
        do {
            // appeler la méthode de view.GameBoard qui affiche qq chose
            nbRounds ++;

            // On met le USER comme premier joueur du round
            this.m_currentPlayer = this.m_user;

            m_gameboard.display(this.m_user, this.m_opponent, m_score, this.m_turnMessages);

            // Le joueur pioche au départ du tour...
            String drawMessage = m_dialogue.draw(this.m_user);
            this.m_turnMessages.add(drawMessage);

            // Reaffiche le board avec le message de pioche
            m_gameboard.display(m_user, m_opponent, m_score, this.m_turnMessages);

            // propose à l'utilisateur ce qu'il veut faire => placer une acrte ou finir son tour...
            takeTurn(this.m_user);
            this.m_currentPlayer.notifyTurnStarted(); // Croissance


            runCombat(this.m_user, this.m_opponent, 1, "Vous");

            m_gameboard.displayCombat(this.m_user, this.m_opponent, this.m_score, this.m_turnMessages);
            // on vérifie déjà ici qu'on ait pas dépassé 5 au niveau de la balance sinon on arrete direct a boucle..
            if(Math.abs(this.m_score) >= 5){
                break;
            }

            // Tour de l'adversaire
            this.m_currentPlayer = this.m_opponent;

            ArrayList<Integer> action = this.m_strategy.getPrevisionAction(nbRounds); // L'players.Opponent place ses cartes sur sa ligne de jeu.
            this.m_currentPlayer.notifyTurnStarted(); // croissance pour les louveteaux de l'opponent.
            this.m_opponent.opponentAction(action);

            runCombat(this.m_opponent, this.m_user, -1, "L'adversaire");

            m_gameboard.displayCombat(this.m_user, this.m_opponent, this.m_score, this.m_turnMessages);

        } while (Math.abs(this.m_score) < 5);
        return this.m_score >= 5;
    }

    public void takeTurn(User human) {
        //m_gameboard.display(human, this.m_opponent, this.m_score, this.m_turnMessages);
        boolean turnEnded = false;
        while (!turnEnded) {
            turnEnded = !this.m_dialogue.askNextAction(human, this.m_turnMessages);
            if (!turnEnded) {
                // On ne réaffiche PAS si l'utilisateur veut terminer le tour ('est affocher après avec displayCombat...)
                this.m_gameboard.display(human, this.m_opponent, this.m_score, this.m_turnMessages);
            }
        }
    }

    public int combatPhase(Player attackedPlayer) {

        return this.m_currentPlayer.combatPhase(attackedPlayer);
    }

    private void runCombat(Player attacker, Player defender, int scoreSign, String attackerName) {
        this.m_currentPlayer = attacker;
        int degats = combatPhase(defender);
        m_score += scoreSign * degats;
        m_turnMessages.add(m_dialogue.getAttackMessage(attackerName, degats));
    }
}
