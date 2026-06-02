import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface GameStrategy {

    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent();

    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser();

    public ArrayList<Animal> getDeckOpponent(ArrayList<Animal> deck);

    public ArrayList<Animal> getDeckUser();

    public ArrayList<Animal> getDeckUser(ArrayList<Animal> deck);

    public ArrayList<Animal> getDeckOpponent();

    public void opponentAction(Opponent opponent, int roundNumber);
}


