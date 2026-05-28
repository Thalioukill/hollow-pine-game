import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Deck {
    private ArrayList<Animal> m_cards;

     //initialise un deck vide
    public Deck() {
        this.m_cards = new ArrayList<>();
    }

    // melange les cartes du deck.
    public void shuffle() {
        Collections.shuffle(m_cards);
    }


    //Ajoute une carte au deck : utilisé pour l'initialisation et les récompenses
    public void addCard(Animal card) {
        m_cards.add(card);
    }

    //Optional contenant la carte, ou Optional.empty() si vide
    public Animal draw() {
        // retire et renvoie la première carte de la liste
        if (m_cards.isEmpty()) {
            throw new NoSuchElementException("La pioche est vide !");
        }
        return m_cards.removeFirst();


    }

    public int getSize() {
        return m_cards.size();
    }

    public boolean isEmpty() {
        return m_cards.isEmpty();
    }

    @Override
    public String toString() {
        return "Deck(taille=" + m_cards.size() + ")";
    }
}
