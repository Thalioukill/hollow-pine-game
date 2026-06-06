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


    public GameManager(Interactions dialogue, User user) {
        this.m_dialogue = dialogue;
        this.m_user = user;
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
                    this.sacrificeStone();
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

        int index = m_gamesCount-1;
        Optional<GameStrategy> optStrategy = getStrategy(index);

        // à enlever quand on aura regardé que le code passe bien...
        if (optStrategy.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Stratégie de match non trouvée.");
        }
        GameStrategy strategy = optStrategy.get();

        Opponent opponent = initiateOpponent(this.m_gamesCount);

        this.resetForNewGame();

        placeUserObstacles(this.m_gamesCount);

        // Injection de dépendances : on donne les joueurs et le plateau à la Game
        Game currentGame = new Game(board, this.m_user,this.m_dialogue, strategy, opponent);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }

    private void placeUserObstacles(int nbGames) {
        // On recup la strat propre au Game en cours
        GameStrategy strat = this.m_strategies.get(nbGames - 1);

        if (nbGames == 1) {
            strat = this.m_strategies.getFirst();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, this.m_user.getBoardLine());
        }
        else if (nbGames == 2) {
            strat = this.m_strategies.get(1);

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, this.m_user.getBoardLine());
        } else {
            strat = this.m_strategies.getLast();

            HashMap<Integer, Optional<Obstacle>> board = strat.initObstaclesUser();
            placeObstacles(board, this.m_user.getBoardLine());

        }
        //
    }



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
        ArrayList<Integer> init = strat.getInitialPrevision();
        for (int position : init) {
            opponent.placePrevisionCard(position);
        }
        return opponent;
    }

    public void resetForNewGame() {
        // récup le deckSave
        ArrayList<Animal> templates = m_user.getDeckSave();

        // copie profonde
        ArrayList<Animal> sessionDeck = User.deepCopyDeck(templates);

        // Mélange
        Collections.shuffle(sessionDeck);

        // Étape 4 : Injecter le deck prêt dans le joueur
        m_user.resetForNewGame(sessionDeck);
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


    private void offerNewCard() {
        //GameStrategy strat = this.m_strategies.get(m_gamesCount - 1);
        //ArrayList<Animal> fullUserDeck = this.m_user.getDeckSave(); // deck de base complet
        //ArrayList<String> pool = strat.getRewardPool(fullUserDeck);
        ArrayList<String> pool = this.m_user.getRewardPool();

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
        Animal chosenAnimal = this.m_dialogue.offerNewCardChoice(choiceA, choiceB);
        this.m_user.addPermanentCard(chosenAnimal);
    }

    private ArrayList<Animal> deduplicateByName(ArrayList<Animal> list) {
        ArrayList<Animal> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (Animal a : list) {
            if (seen.add(a.getName())) { // add() retourne false si déjà présent
                result.add(a);
            }
        }
        return result;
    }

    private void sacrificeStone(){
        ArrayList<Animal> sacrificePool = this.m_user.getSacrificePool();

        // on part du principe que le deck du User sera généré au moins avec 1 carte avec des Abilities.
        if (sacrificePool.isEmpty()) {
            throw new IllegalStateException("Pierre de sacrifice inutilisable : AUCUNE carte avec un pouvoir dans le deck.");
        }

        Animal from = m_dialogue.askToSacrificeCard(sacrificePool);

        Ability ability = from.getTransferableAbility().get();

        ArrayList<Animal> candidates = new ArrayList<>(m_user.getDeckSave());
        candidates.remove(from);  // on enlève juste la carte de la liste locale pour ne pas la proposer
        candidates = deduplicateByName(candidates); // on ne propose qu'une fois la carte pour ne pas avoir une liste entière d'écureuil par exemple....

        Optional<Animal> receiver = Optional.empty();
        while (receiver.isEmpty()) {
            // on demande à l'utilisateur sur quelle carte il veut ajouter l'ability choisie
            Animal candidate = m_dialogue.askCardToReceiveAbility(candidates);
            try {
                // on essaie de transférer si ce n'est pas su une carte qui a déjà la meme ability...
                m_user.transferAbility(from, ability, candidate);
                receiver = Optional.of(candidate);

            // si c'est une carte qui a dejà la m ability alors on dit qu'on ne peut pas et on reboucle...
            } catch (IllegalArgumentException e) {
                m_dialogue.displayMessage(e.getMessage());
            }
        }



        Animal to = this.m_dialogue.askCardToReceiveAbility(candidates);


        this.m_user.transferAbility(from, ability, to); // retire définitivement from du deckSave
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }

}