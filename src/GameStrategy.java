import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface GameStrategy {

    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent();

    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser();

    public ArrayList<Animal> getOpponentDeck(ArrayList<Animal> deck);

    public ArrayList<Animal> getUserDeck();

    public ArrayList<Animal> getUserDeck(ArrayList<Animal> deck);

    public ArrayList<Animal> getOpponentDeck();

    //public void opponentAction(Opponent opponent, int roundNumber);

    // Retourne les positions de previsionLine à remplir pour un round donné, appelé par Opponent.oppnentAction()
    public ArrayList<Integer> getPrevisionAction(int roundNumber);

    // pour initialiser le previsionLine dans le GameManager avant de faire les rounds...
    public ArrayList<Integer> getInitialPrevision();

    public ArrayList<String> getRewardPool(ArrayList<Animal> playerDeck);

}


