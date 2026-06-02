import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class FirstGame implements GameStrategy{

    // méthode qui stocke juste les cartes à mettre dans le deck de l'Opponent !
    public ArrayList<Animal> getDeckOpponent(){
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> opponentDeck = new ArrayList<Animal>();
        stock.getCard(AnimalStock.HERMINE).get();
        stock.getCard(AnimalStock.HERMINE).get();
        stock.getCard(AnimalStock.LOUVETEAU).get();
        stock.getCard(AnimalStock.LOUVETEAU).get();
        stock.getCard(AnimalStock.LOUP).get();
        stock.getCard(AnimalStock.LOUP).get();
        stock.getCard(AnimalStock.ELAN).get();

        return opponentDeck;
    }

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent() {
        HashMap<Integer, Optional<Obstacle>> listeObstacles = new HashMap<>();
        ObstacleStock stock = new ObstacleStock();

        for (int i = 0; i < 4; i++) {
            listeObstacles.put(i, Optional.empty());
        }

        // On récupère l'Optional directement depuis le stock
        Optional<Obstacle> sapin = stock.getObstacle(ObstacleStock.SAPIN);
        listeObstacles.put(0, sapin);

        return listeObstacles;
    }


    /*
        initOpponent  → Coyote en position 3 sur previsionLine (visible dès le début)
        opponentAction(1) = fin du tour 1 → applique Coyote sur board + pioche 1 carte pour la prévision du tour 2
        opponentAction(2) = fin du tour 2 → applique la prévision + pioche pour tour 3
        ...
     */

    public ArrayList<Integer> opponentAction(){
        // return les positions à mettre pour ce deck
        return ArrayList<Integer>;
    };


    public ArrayList<Animal> getDeckUser(){

    }
}
