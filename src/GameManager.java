import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private final Interactions m_dialogue;
    private User m_player;
    private int m_gamesCount;
    private final int MAX_GAMES = 3;

    public GameManager(Interactions dialogue, User player) {
        this.m_player = player;

        this.m_dialogue = dialogue;
        this.m_gamesCount = 1;
    }


    public void startCampaign() {
        boolean campaignWon = true;

        while (m_gamesCount <= MAX_GAMES && campaignWon) {
            m_player.resetForNewGame();
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
        GameStrategy opponent;

        if(gamesCount == 1){
            opponent = new FirstGame();
        }
        else if (gamesCount == 2){
            opponent = new SecondGame();
        }
        else if (gamesCount == 3){
            opponent = new ThirdGame();
        }

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, this.m_player,this.m_dialogue, opponent);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }

    private void offerNewCard() {
        //proposer deux cartes et en ajouter une au deck
        //il faut aussi gérer le dialogue
        //this.m_dialogue.offerNewCard();
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }



    @Override
    public String toString() {
        return "GameManager(match=" + m_gamesCount + ", player=" + m_player + ")";
    }
}