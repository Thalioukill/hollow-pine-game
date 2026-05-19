import java.util.Scanner;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private Friend m_player;
    private int m_matchCount;
    private final int MAX_MATCHES = 3;

    /**
     * Le constructeur instancie les entités persistantes.
     * Le joueur et son deck sont créés ici une seule fois pour toute la campagne.
     */
    public GameManager() {
        // Initialisation à l'extérieur des objets Game pour la persistance
        Deck playerDeck = new Deck();
        this.m_player = new Friend(playerDeck);
        this.m_matchCount = 1;
    }

    /**
     * Lance la boucle principale de la campagne (les 3 parties).
     */
    public void startCampaign() {
        boolean campaignWon = true;

        while (m_matchCount <= MAX_MATCHES && campaignWon) {
            // Orchestration : on crée les nouveaux objets nécessaires à chaque match
            campaignWon = playNewMatch();

            if (campaignWon) {
                // Gestion de la progression entre les matchs
                if (m_matchCount == 2) {
                    this.offerNewCard();
                }
                m_matchCount++;
            }
        }

        this.displayFinalResult(campaignWon);
    }

    /**
     * Initialise et lance une instance de match unique.
     * @return true si le joueur a gagné le match, false sinon.
     */
    private boolean playNewMatch() {
        // Création des objets spécifiques au match (Composition)
        GameBoard board = new GameBoard();
        Opponent opponent = new Opponent(new Deck());

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, m_player, opponent);

        // Appel de la logique du match (boucle de rounds)
        return currentGame.runMatch();
    }

    /**
     * Logique de progression : ajout d'une carte après le 2ème match.
     */
    private void offerNewCard() {
        // Selon le README : proposer deux cartes et en ajouter une au deck
        // m_player.getDeck().addCard(...)
    }

    private void displayFinalResult(boolean victory) {
        if (victory) {
            System.out.println("Félicitations ! Vous avez survécu à la forêt.");
        } else {
            System.out.println("L'obscurité vous a englouti... Partie terminée.");
        }
    }

    @Override
    public String toString() {
        return "GameManager(match=" + m_matchCount + ", player=" + m_player + ")";
    }
}