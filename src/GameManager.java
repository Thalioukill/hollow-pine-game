import java.util.*;

public class GameManager {
    // Attributs privés (Encapsulation forte)
    private final Interactions m_dialogue;
    private int m_gamesCount;
    private final int MAX_GAMES = 3;
    private User m_user;

    // liste des stratégies du Opponent...
    private final ArrayList<GameStrategy> m_strategies = new ArrayList<>(List.of(
            new FirstGame(),
            new SecondGame(),
            new ThirdGame()
    ));


    public GameManager(Interactions dialogue) {
        this.m_dialogue = dialogue;
        this.m_gamesCount = 1;
    }



    public User initPlayer(int nbGames, ArrayList<Animal> bonusCards){
        ArrayList<Animal> playerDeck = new ArrayList<Animal>();

        BoardLine userBoard = new BoardLine();

        // On recup la strat propre au Game en cours
        GameStrategy strat = this.m_strategies.get(nbGames - 1);

        if(nbGames == 1){
            strat = this.m_strategies.getFirst();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, userBoard);
        }
        else if(nbGames == 2){
            strat = this.m_strategies.get(1);

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, userBoard);
        }
        else{
            strat = this.m_strategies.getLast();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, userBoard);
        }

        ArrayList<Animal> userDeck;

        if (bonusCards.isEmpty()) {
            userDeck = strat.getUserDeck();
        } else {
            userDeck = strat.getUserDeck(bonusCards);
        }
        User u = new User(userDeck, userBoard);

        u.shuffle();
        return u;
    }


    public void startCampaign() {
        boolean campaignWon = true;
        ArrayList<Animal> bonusCards = new ArrayList<>(); // vide au départ

        while (m_gamesCount <= MAX_GAMES && campaignWon) {
            // Orchestration : on crée les nouveaux objets nécessaires à chaque match
            campaignWon = playNewGame(bonusCards);

            if (campaignWon) {
                // Gestion de la progression entre les matchs
                if (m_gamesCount == 2) {
                    // L'utilisateur choisit une carte, on la stocke pour la game 3
                    Animal chosen = offerNewCard();
                    bonusCards.add(chosen);
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



    private boolean playNewGame(ArrayList<Animal> bonusCards) {
        // Création des objets spécifiques au match (Composition)
        GameBoard board = new GameBoard();

        int index = m_gamesCount-1;
        Optional<GameStrategy> optStrategy = getStrategy(index);

        // à enlever quand on aura regardé que le code passe bien...
        if (optStrategy.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Stratégie de match non trouvée.");
        }
        GameStrategy strategy = optStrategy.get();

        this.m_user = initPlayer(this.m_gamesCount, bonusCards);

        Opponent opponent = initiateOpponent(this.m_gamesCount);

        //this.resetForNewGame();

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, this.m_user ,this.m_dialogue, strategy, opponent);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }


    // faire Classe Deck !!!! + dans user faire une méthode qui réinit son deck (injection dépendances)
    // classe Player deepCopy ?
    // on clear le BoardLine pour la nouvelle game => il est vide et on peut placer des Obstacles dessus si on veut...
    // on doit utiliser un méthode ici...
            /*
    public void resetForNewGame() {
        // récup le deckSave
        ArrayList<Animal> templates = m_player.getDeckSave();

        // copie profonde
        ArrayList<Animal> sessionDeck = User.deepCopyDeck(templates);

        // Mélange
        Collections.shuffle(sessionDeck);
        // Étape 4 : Injecter le deck prêt dans le joueur
        m_player.resetForNewGame(sessionDeck);
    }
    */


    private Opponent initiateOpponent(int nbGames){
        // On recup la strat propre au Game en cours
        GameStrategy strat = this.m_strategies.get(nbGames - 1);

        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();

        if(nbGames == 1){
            strat = this.m_strategies.getFirst();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesOpponent();
            placeObstacles(board, attackBoard);
        }
        else if(nbGames == 2){
            strat = this.m_strategies.get(1);

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesOpponent();
            placeObstacles(board, attackBoard);
        }
        else{
            strat = this.m_strategies.getLast();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesOpponent();
            placeObstacles(board, attackBoard);
        }

        Opponent opponent = new Opponent(strat.getOpponentDeck(), attackBoard, previsionBoard);

        // Initialisation de la previsionLine AVANT de passer au Game
        RoundAction init = strat.getInitialPrevision();
        for (int position : init.getPositions()) {
            opponent.placePrevisionCard(position);
        }
        return opponent;
    }


    private void placeObstacles(HashMap<Integer, Optional<Obstacle>> obstacleBoard, BoardLine line) {
        for (int i = 0; i < obstacleBoard.size(); i++) {
            Optional<Obstacle> opt = obstacleBoard.get(i);
            if (opt.isPresent()) {
                Obstacle obs = opt.get();
                line.placeCardAt(obs, i);
            }
        }
    }


    private Animal offerNewCard() {
        GameStrategy strat = this.m_strategies.get(m_gamesCount - 1);
        ArrayList<String> pool = strat.getRewardPool(this.m_user.getDeck());

        Collections.shuffle(pool);
        AnimalStock animalStock = new AnimalStock();

        String nameA = pool.get(0);
        String nameB = pool.get(1);

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
        return this.m_dialogue.offerNewCardChoice(choiceA, choiceB);
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }

}