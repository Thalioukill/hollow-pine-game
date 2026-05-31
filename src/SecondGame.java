import java.util.ArrayList;
import java.util.Optional;

public class SecondGame implements GameStrategy{
    public Opponent initOpponent(){
        // création du deck de l'opponent
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> playerDeck = new ArrayList<Animal>();
        playerDeck.add(stock.getCard(AnimalStock.HERMINE).get());
        playerDeck.add(stock.getCard(AnimalStock.HERMINE).get());
        playerDeck.add(stock.getCard(AnimalStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(AnimalStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(AnimalStock.LOUP).get());
        playerDeck.add(stock.getCard(AnimalStock.LOUP).get());
        playerDeck.add(stock.getCard(AnimalStock.ELAN).get());

        ObstacleStock obj = new ObstacleStock();

        BoardLine attackBoard = new BoardLine();
        attackBoard.placeCardAt(obj.getObstacle(ObstacleStock.SAPIN).get(), 0);

        BoardLine previsionBoard = new BoardLine();
        previsionBoard.placeCardAt(stock.getCard(AnimalStock.COYOTE).get(), 3);
        return new Opponent(playerDeck, attackBoard, previsionBoard);
    };

    public void opponentAction(Opponent opponent, int roundNumber){
        switch(roundNumber){
            case 1 -> {
                // transfère la previsionLine vers la boardLine
                opponent.applyPrevision();
                // prépare la previsionLine pour le prochain round...
                opponent.placePrevisionCard(0);
            }
            case 2 -> {
                opponent.applyPrevision();
                opponent.placePrevisionCard(2);
            }
            case 3 -> {
                opponent.applyPrevision();
                opponent.placePrevisionCard(3);
            }
            case 4 -> {
                opponent.applyPrevision();
            }
            default ->{}
        }
    };
}
