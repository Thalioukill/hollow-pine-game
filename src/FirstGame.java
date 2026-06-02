import java.util.ArrayList;

public class FirstGame implements GameStrategy{
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
    }

    // méthode qui stocke juste les cartes à mettre dans le deck de l'Opponent !
    public void initDeck(){
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> opponentDeck = new ArrayList<Animal>();
        stock.getCard(AnimalStock.HERMINE).get();
        stock.getCard(AnimalStock.HERMINE).get();
        stock.getCard(AnimalStock.LOUVETEAU).get();
        stock.getCard(AnimalStock.LOUVETEAU).get();
        stock.getCard(AnimalStock.LOUP).get();
        stock.getCard(AnimalStock.LOUP).get();
        stock.getCard(AnimalStock.ELAN).get();

    }

    // méthode qui stocke les obstacles de ce tour....
    public void initObstacle(){
        ObstacleStock obj = new ObstacleStock();
        obj.getObstacle(ObstacleStock.SAPIN).get();
    }

    /*
        initOpponent  → Coyote en position 3 sur previsionLine (visible dès le début)
        opponentAction(1) = fin du tour 1 → applique Coyote sur board + pioche 1 carte pour la prévision du tour 2
        opponentAction(2) = fin du tour 2 → applique la prévision + pioche pour tour 3
        ...
     */

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
