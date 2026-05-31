import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        Interactions dialogue = new Interactions();
        //dialogue.displayTitle();
        dialogue.displayMessage("Another challenger... It has been ages.");

        ArrayList<Animal> playerDeck = new ArrayList<Animal>();
        buildPlayerDeck(playerDeck);

        BoardLine playerBord = new BoardLine();

        User player = new User(playerDeck, playerBord);
        player.shuffle();


        GameManager manager = new GameManager(dialogue, player);
        manager.startCampaign();

    }
    public static void buildPlayerDeck(ArrayList<Animal> playerDeck){
        // Initialisation à l'extérieur des objets Game pour la persistance
        CardStock stock = new CardStock();

        // Création du deck avec les 15 cartes de départ (majorité d'écureuils)

        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.ECUREUIL).get());
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.HERMINE).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUVETEAU).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.LOUP).get());
        playerDeck.add(stock.getCard(CardStock.CHAT).get());
        playerDeck.add(stock.getCard(CardStock.CHAT).get());
    }

}
