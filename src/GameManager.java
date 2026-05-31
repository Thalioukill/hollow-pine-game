import java.util.*;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private final Interactions m_dialogue;
    private User m_player;
    private int m_gamesCount;
    private final int MAX_GAMES = 3;
    // liste des stratégies du Opponent...
    private final ArrayList<GameStrategy> m_strategies = new ArrayList<>(List.of(
            new FirstGame(),
            new SecondGame(),
            new ThirdGame()
    ));


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

    private Optional<GameStrategy> getStrategy(int index) {
        if (index >= 0 && index < m_strategies.size()) {
            return Optional.of(m_strategies.get(index));
        }
        return Optional.empty();
    }

    private boolean playNewGame() {
        // Création des objets spécifiques au match (Composition)
        GameBoard board = new GameBoard();

        int index = m_gamesCount+1;
        Optional<GameStrategy> optStrategy = getStrategy(index);

        // à enlever quand on aura regardé que le code passe bien...
        if (optStrategy.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Stratégie de match non trouvée.");
        }
        GameStrategy strategy = optStrategy.get();

        // on clear le BoardLine pour la nouvelle game => il est vide et on peut placer des Obstacles dessus si on veut...
        this.m_player.resetForNewGame();

        placeUserObstacles();

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, this.m_player,this.m_dialogue, strategy);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }


    private void offerNewCard() {
        ArrayList<String> rewards = new ArrayList<>(List.of(AnimalStock.GRIZZLY, AnimalStock.COYOTE, AnimalStock.VIPERE, AnimalStock.PORC_EPIC, AnimalStock.CORBEAU));

        Collections.shuffle(rewards);
        AnimalStock animalStock = new AnimalStock();

        String nameA = rewards.get(0);
        String nameB = rewards.get(1);

        Optional<Animal> optA = animalStock.getCard(nameA);
        Optional<Animal> optB = animalStock.getCard(nameB);

        if (optA.isEmpty()) {
            throw new IllegalArgumentException("Carte récompense inconnue");
        }
        Animal choiceA = optA.get();

        if(optB.isEmpty()){
            throw new IllegalArgumentException("Carte récompense inconnue");
        }
        Animal choiceB = optB.get();

        // on demande à l'utilisateur
        Animal chosen = m_dialogue.offerNewCardChoice(choiceA, choiceB);

        // ajout définitif au deckSaver
        m_player.addPermanentCard(chosen);
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }

    private void placeUserObstacles() {
        ObstacleStock objets = new ObstacleStock();
        switch (this.m_gamesCount) {
            case 1 -> {} // pas d'obstacles
            case 2 -> { m_player.placeCardAt(objets.getObstacle(ObstacleStock.SAPIN).get(), 0);
            }
            case 3 -> { m_player.placeCardAt(objets.getObstacle(ObstacleStock.ROCHER).get(), 2);}
        }
    }


    @Override
    public String toString() {
        return "GameManager(match=" + m_gamesCount + ", player=" + m_player + ")";
    }
}