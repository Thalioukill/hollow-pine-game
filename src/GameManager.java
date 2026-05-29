import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private final Interactions m_dialogue;
    private User m_player;
    private int m_gamesCount;
    private final int MAX_GAMES = 3;

    public GameManager(Interactions dialogue) {
        this.m_dialogue = dialogue;
        // Initialisation à l'extérieur des objets Game pour la persistance
        CardStock stock = new CardStock();

        // Création du deck avec les 15 cartes de départ (majorité d'écureuils)
        ArrayList<Animal> playerDeck = new ArrayList<Animal>();
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.CHAT).get());
        playerDeck.add(stock.getCard(CardStock.CHAT).get());

        BoardLine playerBord= new BoardLine();

        this.m_player = new User(playerDeck, playerBord);
        this.m_player.shuffle();
        this.m_gamesCount = 1;
    }

    public void startCampaign() {
        boolean campaignWon = true;

        while (m_gamesCount <= MAX_GAMES && campaignWon) {
            // Orchestration : on crée les nouveaux objets nécessaires à chaque match
            campaignWon = playNewGame(m_gamesCount);

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

    private boolean playNewGame(int gamesCount) {
        // Création des objets spécifiques au match (Composition)
        GameBoard board = new GameBoard();

        GameStrategy opponent = new FirstGame();

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, this.m_player,this.m_dialogue, opponent);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }

    private void offerNewCard() {
        // Selon le README : proposer deux cartes et en ajouter une au deck
        // m_player.getDeck().addCard(...)
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }



    @Override
    public String toString() {
        return "GameManager(match=" + m_gamesCount + ", player=" + m_player + ")";
    }
}