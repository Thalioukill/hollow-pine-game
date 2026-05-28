public class FirstGame implements GameStrategy{
    public Opponent initOpponent(){
        // création du deck de l'opponent
        CardStock stock = new CardStock();
        Deck playerDeck = new Deck();
        playerDeck.addCard(stock.getCard(CardStock.HERMINE).get());
        playerDeck.addCard(stock.getCard(CardStock.HERMINE).get());
        playerDeck.addCard(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.addCard(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.addCard(stock.getCard(CardStock.LOUP).get());
        playerDeck.addCard(stock.getCard(CardStock.LOUP).get());
        playerDeck.addCard(stock.getCard(CardStock.ELAN).get());

        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();
        previsionBoard.set()
    }

    public void opponentAction(Opponent opponent, int roundNumber){

    };
}
