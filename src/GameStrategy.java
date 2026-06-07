import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface GameStrategy {

    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent();

    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser();

    public ArrayList<Animal> getOpponentDeck();

    public ArrayList<Integer> getPrevisionAction(int roundNumber);

    // pour initialiser le previsionLine dans le GameManager avant de faire les rounds...
    public ArrayList<Integer> getInitialPrevision();

}


