import java.util.Scanner;
import java.util.Optional;

public class Game {
    // SAVOIR OU EST CE QU'ON VA CREER UNE INTERCATIONS (1 dialogie avec l'utilisateur...)
    // Attributs privés (Encapsulation forte)
    private int m_score; // La balance : >0 pour l'Ami, <0 pour l'Adversaire
    private GameBoard m_gameboard;
    private User m_user;
    private Opponent m_opponent;
    private Player m_currentPlayer; // Joueur qui joue actuellement
    private final Interactions m_dialogue;

    public Game(GameBoard board, User player, Opponent opponent) {
        this.m_gameboard = board;
        this.m_user = player;
        this.m_opponent = opponent;
        this.m_score = 0; // La balance commence à l'équilibre
    }

    // va appeler
    public boolean runGame() {
        // La partie s'arrête si l'écart atteint 5 points
        int nbRounds = 0;
        do {
            nbRounds ++;
            startRound(); // carte que le Opponent jouera après le User (prévisions)

            // On met le USER comme premier joueur du round
            this.m_currentPlayer = this.m_user;
            // Le joueur pioche au départ du tour...
            this.m_user.draw(this.m_dialogue);

            // propose à l'utilisateur ce qu'il veut faire => placer une acrte ou finir son tour...



            // appelle la méthode de combat : User attaque l'Opponent
            m_score += combatPhase(this.m_opponent);

            this.m_opponent.takeTurn(this.m_dialogue); // L'Opponent place ses cartes sur sa ligne de jeu.

            // appelle la méthode de combat : Opponent attaque le User
            m_score -= combatPhase(this.m_user); // Attaque des cartes + mise à jour du score

        } while (Math.abs(this.m_score) < 5);
        return this.m_score >= 5;
    }

    public void takeTurn(Interactions m_dialogue) {
        boolean turnEnded = false;
        while (!turnEnded) {
            Actions choice = m_dialogue.askNextAction(); // Dialogue gère le Scanner
            switch (choice) {
                case PLACE -> m_dialogue.handlePlacement();
                //case SACRIFICE -> handleSacrifice(dialogue);
                case END -> turnEnded = true;
            }
        }
    }



    public int combatPhase(Player p) {

        this.m_currentPlayer.CombatPhase(p);
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

    private void startRound() {
        // Affichage des prévisions de l'adversaire sur la ligne du haut.
        // Méthode à implémenter que pour le OPPONENT !!!


    }


    @Override
    public String toString() {
        return "Game(score=" + m_score + ", board=" + m_gameboard + ")";
    }
}
