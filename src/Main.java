import model.board.BoardLine;
import model.cards.Animal;
import game.GameManager;
import model.cards.AnimalType;
import model.players.User;
import model.stock.AnimalStock;
import view.Interactions;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        Interactions dialogue = new Interactions();
        dialogue.displayMessage("Un nouveau challenger... Cela faisait une éternité.");

        ArrayList<Animal> playerDeck = new ArrayList<Animal>();
        buildPlayerDeck(playerDeck);

        BoardLine playerBord = new BoardLine();

        User player = new User(playerDeck, playerBord);
        player.shuffle();


        GameManager manager = new GameManager(dialogue, player);
        manager.startCampaign();

    }
    private static void buildPlayerDeck(ArrayList<Animal> playerDeck){
        // Initialisation à l'extérieur des objets Game pour la persistance
        AnimalStock stock = new AnimalStock();

        // Création du deck avec les 15 cartes de départ (majorité d'écureuils)

        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.ECUREUIL).get());
        playerDeck.add(stock.getCard(AnimalType.HERMINE).get());
        playerDeck.add(stock.getCard(AnimalType.HERMINE).get());
        playerDeck.add(stock.getCard(AnimalType.LOUVETEAU).get());
        playerDeck.add(stock.getCard(AnimalType.ELAN).get());
        playerDeck.add(stock.getCard(AnimalType.LOUP).get());
        playerDeck.add(stock.getCard(AnimalType.VIPERE).get());
        playerDeck.add(stock.getCard(AnimalType.PUNAISE).get());
        playerDeck.add(stock.getCard(AnimalType.PORC_EPIC).get());
    }



}
