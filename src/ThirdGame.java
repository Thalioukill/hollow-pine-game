import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ThirdGame implements GameStrategy{

    // méthode qui stocke juste les cartes à mettre dans le deck de l'Opponent !
    public ArrayList<Animal> getOpponentDeck(){
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

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser() {
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

    @Override
    public ArrayList<Animal> getOpponentDeck(ArrayList<Animal> deck) {
        return null;
    }


    public ArrayList<Animal> getUserDeck(){
        return null;
    }

    @Override
    public ArrayList<Animal> getUserDeck(ArrayList<Animal> deck) {
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> userDeck = new ArrayList<Animal>();

        // Création du deck avec les 15 cartes de départ (majorité d'écureuils)

        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.ECUREUIL).get());
        userDeck.add(stock.getCard(AnimalStock.HERMINE).get());
        userDeck.add(stock.getCard(AnimalStock.HERMINE).get());
        userDeck.add(stock.getCard(AnimalStock.LOUVETEAU).get());
        userDeck.add(stock.getCard(AnimalStock.ELAN).get());
        userDeck.add(stock.getCard(AnimalStock.LOUP).get());
        userDeck.add(stock.getCard(AnimalStock.LOUP).get());
        userDeck.add(stock.getCard(AnimalStock.PUNAISE).get());
        userDeck.add(stock.getCard(AnimalStock.CHAT).get());

        // copie profonde des animaux passés en paramètre...
        for (Animal a : deck) {
            Optional<Animal> copy = stock.getCard(a.getName());
            if (copy.isPresent()) {
                userDeck.add(copy.get());
            }
        }

        return userDeck;
    }

    @Override
    public RoundAction getPrevisionAction(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new RoundAction(List.of(0));      // après round 1 : prépare pos 0
            case 2 -> new RoundAction(List.of(2));      // après round 2 : prépare pos 2
            case 3 -> new RoundAction(List.of(2, 3));   // plusieurs cartes possible !
            default -> RoundAction.empty();
        };
    }

    @Override
    public RoundAction getInitialPrevision() {
        return new RoundAction(List.of(3)); // Coyote en position 3 dès le départ
    }

    @Override
    public ArrayList<String> getRewardPool(ArrayList<Animal> playerDeck) {
        AnimalStock animalStock = new AnimalStock();
        ArrayList<String> allCards = animalStock.getAllCardNames();

        // On retire les cartes que le joueur a déjà
        ArrayList<String> playerCardNames = new ArrayList<>();
        for (Animal a : playerDeck) {
            playerCardNames.add(a.getName());
        }
        allCards.removeAll(playerCardNames);

        return allCards;
    }

}
