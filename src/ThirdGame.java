import java.util.ArrayList;
import java.util.Optional;

public class ThirdGame implements GameStrategy{
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

        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();
        previsionBoard.set(3, Optional.of(stock.getCard(AnimalStock.COYOTE).get()));
        return new Opponent(playerDeck, attackBoard, previsionBoard);
    };

    public void opponentAction(Opponent opponent, int roundNumber){
        switch(roundNumber){
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
            case 4 -> {

            }
            case 5 -> {

            }
            case 6 -> {

            }
            default ->{}
        }
    };

}
