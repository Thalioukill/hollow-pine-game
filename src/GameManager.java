import java.util.Scanner;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private User m_player;
    private int m_gamesCount;
    private final int MAX_GAMES = 3;

    public GameManager() {
        // Initialisation à l'extérieur des objets Game pour la persistance
        Deck playerDeck = new Deck();
        this.m_player = new User(playerDeck);
        this.m_gamesCount = 1;
    }

    public void startCampaign() {
        boolean campaignWon = true;

        while (m_gamesCount <= MAX_GAMES && campaignWon) {
            // Orchestration : on crée les nouveaux objets nécessaires à chaque match
            campaignWon = playNewGame();

            if (campaignWon) {
                // Gestion de la progression entre les matchs
                if (m_gamesCount == 2) {
                    this.offerNewCard();
                }
                m_gamesCount++;
            }
        }

        this.displayFinalResult(campaignWon);
    }

    private boolean playNewGame() {
        // Création des objets spécifiques au match (Composition)
        GameBoard board = new GameBoard();
        Opponent opponent = new Opponent(new Deck());

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, m_player, opponent);

        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }

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
        return "GameManager(match=" + m_gamesCount + ", player=" + m_player + ")";
    }
}