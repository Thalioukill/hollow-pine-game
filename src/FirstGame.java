import java.util.ArrayList;
import java.util.Optional;

public class FirstGame implements GameStrategy{
    public Opponent initOpponent(){
        // création du deck de l'opponent
        CardStock stock = new CardStock();
        ArrayList<Animal> playerDeck = new ArrayList<Animal>();
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.ELAN).get());

        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();
        previsionBoard.placeCardAt(stock.getCard(CardStock.COYOTE).get(), 3);
        return new Opponent(playerDeck, attackBoard, previsionBoard);
    }

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
                opponent.placePrevisionCard(0);
            }
            default ->{}
        }
    };
}
