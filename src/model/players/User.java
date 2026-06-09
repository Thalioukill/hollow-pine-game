package model.players;

import model.abilities.Ability;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.AnimalType;
import model.cards.Card;
import model.stock.AnimalStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class User extends Player {
    private final ArrayList<Animal> m_hand;
    private final ArrayList<Animal> m_deckSave;

    public User(ArrayList<Animal> deck, BoardLine bord) {
        super(deck, bord); // Appel au constructeur de la classe mère
        this.m_deckSave = deck;
        this.m_hand = new ArrayList<>();
    }

    // ------ code pour le DECK SAVE ----
    public void addPermanentCard(Animal card) {
        m_deckSave.add(card); // persiste pour toutes les parties futures
    }

    public void resetForNewGame(ArrayList<Animal> freshDeck) {
        // remplace le deck par le nouveau deck fourni
        this.m_deck = freshDeck;

        // réinitialise les statistiques de jeu
        this.m_hand.clear();
        this.m_boardLine.clear();

        // Pioche initiale de 4 cartes
        for (int i = 0; i < 4; i++) {
            this.m_hand.add(this.draw());
        }
    }

    public ArrayList<AnimalType> getRewardPool() {
        AnimalStock animalStock = new AnimalStock();
        ArrayList<AnimalType> allTypes = animalStock.getAllAnimalTypes();

        ArrayList<AnimalType> playerTypes = new ArrayList<>();
        for (Animal a : this.m_deckSave) {
            playerTypes.add(a.getAnimalType());
        }
        allTypes.removeAll(playerTypes);

        return allTypes;
    }

    public ArrayList<Animal> getSacrificePool() {
        // On retire les cartes que le joueur a déjà
        ArrayList<Animal> playerSacrificeCards = new ArrayList<>();

        for (Animal a : this.m_deckSave) {
            if(a.hasAbility()){
               playerSacrificeCards.add(a);
            }
        }
        return playerSacrificeCards;
    }


    public static ArrayList<Animal> deepCopyDeck(ArrayList<Animal> source) {
        ArrayList<Animal> copy = new ArrayList<>();
        for (Animal a : source) {
            copy.add(a.cloneAnimal()); // cloneAnimal = copie profonde...
        }
        return copy;
    }

    public void transferAbility(Animal from, Ability ability, Animal to) {
        if (to.hasAnAbility(ability.getType())) {
            throw new IllegalArgumentException(
                    to.getName() + " possède déjà le pouvoir " + ability.getName() + " !");
        }
        to.addAbility(ability);
        this.m_deckSave.remove(from);
    }


    public ArrayList<Animal> getDeckSave() {
        // copie de la liste des Animals mais pas des Animals eux meme.
        return new ArrayList<>(this.m_deckSave);
    }

    // ----- code pour le DECK

    // melange les cartes du deck.
    public void shuffle() {
        Collections.shuffle(this.m_deck);
    }

    //------ Code pour la MAIN
    public void addCard(Animal card) {
        // Une main peut techniquement grandir, contrairement au plateau
        this.m_hand.add(card);
    }

    public int getHandSize() {
        return m_hand.size();
    }

    // Permet de regarder une carte sans la retirer.
    public Animal getHandCard(int index){
        return m_hand.get(index);// IndexOutOfBoundsException si bug interne
    }

    // ---------

    public int getBonesToken(){
        return this.m_boardLine.getBonesToken();
    }

    public void addBonesToken(){
        this.m_boardLine.addBonesToken();
    }

    // ----- code pour la board.BoardLine
    public Optional<Card> getCardAt(int position){
        return this.m_boardLine.getCardAt(position);
    }


    public boolean hasEnoughBones(Animal test){
        return test.getBoneCost() == 0 || this.getBonesToken() >= test.getBoneCost();
    }

    public boolean hasEnoughBlood(Animal test){
        return test.getBloodCost() == 0 || this.m_boardLine.getSacrificableCount() >= test.getBloodCost();
    }

    public void spendBones(int amount) {
        this.m_boardLine.spendBones(amount);
    }

    public void sacrifice(int position){
        this.m_boardLine.removeCardAt(position);
        this.m_boardLine.addBonesToken();
    }

    // Transfert définitif d'une carte de la main vers le plateau.
    public void playCardToBoard(int handIndex, int boardPosition) {
        Card cardToPlay = this.m_hand.remove(handIndex);

        // On appelle placeCardAt pour la déposer sur la board.BoardLine
        this.placeCardAt(cardToPlay, boardPosition);
    }

}




