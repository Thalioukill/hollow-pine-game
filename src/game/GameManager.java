package game;

import game.strategy.FirstGame;
import game.strategy.GameStrategy;
import game.strategy.SecondGame;
import game.strategy.ThirdGame;
import model.abilities.Ability;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Obstacle;
import model.players.Opponent;
import model.players.User;
import model.stock.AnimalStock;
import view.*;

import java.util.*;

public class GameManager {
    private final Interactions m_dialogue;
    private int m_gamesCount;
    private final User m_user;

    // liste des stratégies du players.Opponent...
    private final ArrayList<GameStrategy> m_strategies = new ArrayList<>(List.of(new FirstGame(), new SecondGame(), new ThirdGame()));

    public GameManager(Interactions dialogue, User user) {
        this.m_dialogue = dialogue;
        this.m_user = user;
        this.m_gamesCount = 1;
    }

    public void startCampaign() {
        boolean campaignWon = true;

        int MAX_GAMES = 3;

        while (m_gamesCount <= MAX_GAMES && campaignWon) {
            // Orchestration : on crée les nouveaux objets nécessaires à chaque match
            campaignWon = playNewGame();

            if (campaignWon) {
                this.displayGameResult();
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

        // Injection de dépendances : on donne les joueurs et le plateau à la game.Game
        Game currentGame = new Game(board, this.m_user,this.m_dialogue, strategy, opponent);
        // Appel de la logique du match (boucle de rounds)
        return currentGame.runGame();
    }

    private void placeUserObstacles(int nbGames) {
        // On recup la strat propre au game.Game en cours
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
    }

        private Opponent initiateOpponent(int nbGames){
        // On recup la strat propre au game.Game en cours
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

        // Initialisation de la previsionLine AVANT de passer au game.Game
        ArrayList<Integer> init = strat.getInitialPrevision();
        for (int position : init) {
            opponent.placePrevisionCard(position);
        }
        return opponent;
    }

    private void resetForNewGame() {
        // récup le deckSave
        ArrayList<Animal> templates = m_user.getDeckSave();

        // copie profonde
        ArrayList<Animal> sessionDeck = User.deepCopyDeck(templates);

        // Mélange
        Collections.shuffle(sessionDeck);

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

        // on part du principe que le deck du players.User sera généré au moins avec 1 carte avec des Abilities.
        if (sacrificePool.isEmpty()) {
            throw new IllegalStateException("Pierre de sacrifice inutilisable : AUCUNE carte avec un pouvoir dans le deck.");
        }

        Animal from = m_dialogue.askToSacrificeCard(sacrificePool);

        Ability ability = from.getTransferableAbility().get();

        ArrayList<Animal> candidates = new ArrayList<>(m_user.getDeckSave());
        candidates.remove(from);  // on enlève juste la carte de la liste locale pour ne pas la proposer
        candidates = deduplicateByName(candidates); // on ne propose qu'une fois la carte pour ne pas avoir une liste entière d'écureuil par exemple....

        Animal receiver = m_dialogue.askCardToReceiveAbility(candidates, ability);
        m_user.transferAbility(from, ability, receiver); // retire définitivement from du deckSave
        this.m_dialogue.displayTransferResult(from.getName(), ability.getName(), receiver.getName());
    }

    private void displayFinalResult(boolean victory) {
        this.m_dialogue.displayFinalResult(victory);
    }

    private void displayGameResult(){
        this.m_dialogue.displayGameResult(this.m_gamesCount);
    }

}