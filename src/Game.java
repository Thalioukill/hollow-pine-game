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
            // appeler la méthode de GameBoard qui affiche qq chose
            nbRounds ++;
            //startRound(nbRounds); // carte que le Opponent jouera après le User (prévisions)
            // On met le USER comme premier joueur du round
            this.m_currentPlayer = this.m_user;

            this.m_user.notifyTurnStarted(); // Croissance

            m_gameboard.display(m_user, m_opponent, m_score);

            // Le joueur pioche au départ du tour...
            m_dialogue.draw(this.m_user);

            // propose à l'utilisateur ce qu'il veut faire => placer une acrte ou finir son tour...
            takeTurn(m_dialogue, this.m_user);

            // appelle la méthode de combat : User attaque l'Opponent
            m_score += combatPhase(this.m_opponent); // Attaque des cartes + mise à jour du score

            m_gameboard.display(this.m_user, this.m_opponent, this.m_score);

            this.m_currentPlayer = this.m_opponent;

            this.m_strategy.opponentAction(this.m_opponent, nbRounds); // L'Opponent place ses cartes sur sa ligne de jeu.

            // appelle la méthode de combat : Opponent attaque le User
            m_score -= combatPhase(this.m_user); // Attaque des cartes + mise à jour du score
            //m_user.addBones(opponentResult.enemiesKilled);

            m_gameboard.display(this.m_user, this.m_opponent, this.m_score);

        } while (Math.abs(this.m_score) < 5);
        return this.m_score >= 5;
    }

    public void takeTurn(Interactions m_dialogue, User human) {
        m_gameboard.display(human, this.m_opponent, this.m_score);
        boolean turnEnded = false;
        while (!turnEnded) {
            turnEnded = !m_dialogue.askNextAction(human);
            m_gameboard.display(human, this.m_opponent, this.m_score);
        }
    }

    public int combatPhase(Player attackedPlayer) {

        return this.m_currentPlayer.combatPhase(attackedPlayer);
    }

    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
