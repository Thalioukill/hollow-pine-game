import java.util.ArrayList;
import java.util.Optional;

public class Hand {
    private final ArrayList<Card> m_cards;

    public Hand() {
        this.m_cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        // Une main peut techniquement grandir, contrairement au plateau
        m_cards.add(card);
    }

    public int getSize() {
        return m_cards.size();
    }

     // Convertit lettre (A, B, C...) en index (0, 1, 2...)

    public int toIndex(char letter) {
        // Character.toUpperCase pour accepter 'a' ou 'A'
        // On utilise l'ASCII pour trouver l'index  'A' = 65
        return Character.toUpperCase(letter) - 'A';
    }

/* Inutile ? on renvoie 1 Optional Of Card et si il est vide alors l'index n'était pas BON.
    // Validation pour éviter les IndexOutOfBoundsException
    public Optional<Card> validateIndex(int index) {
        if (index < 0 || index >= m_cards.size()) {
            return Optional.empty();
        }
        return
    }
 */

     // Permet de regarder une carte sans la retirer.
     // Renvoie un Optional vide si la lettre ne correspond à aucune carte => pour gérer les soucis dans le User...
    public Optional<Card> getCard(char letter) {
        int index = toIndex(letter);
        // Au lieu de lancer une exception, on renvoie un indicateur de "vide"
        if (index < 0 || index >= m_cards.size()) {
            return Optional.empty();
        }
        return Optional.of(m_cards.get(index));
    }



    // retire la carte de la main et retourne l'élément qui était dedans à l'index de la lettre donnée (ici la Card), on l'utilise uniquement quand yune carte est
    // jouée et donc placée autre part, donc si l'index est mauvais c'est une erreur du code...
    public Card removeCard(char letter) {
        int index = toIndex(letter);
        if (index < 0 || index >= m_cards.size()) {
            // Exception non-contrôlée pour signaler un bug interne [4]
            throw new IndexOutOfBoundsException("Bug interne : retrait d'une lettre invalide '" + letter + "'");
        }
        return m_cards.remove(index);
    }


/* Est ce que je dois renvoyer un Optional ? on ne l'appelle que si on veut enlever une carte à un endroit non ?
    public Optional<Card> removeCard(char letter) {
        int index = toIndex(letter);
        if (index < 0 || index >= m_cards.size()) {
            return Optional.empty();
        }
        return Optional.of(m_cards.remove(index));
    }
*/

    @Override
    public String toString() {
        // affichage pour montrer les lettres
        StringBuilder sb = new StringBuilder("Main : ");
        for (int i = 0; i < m_cards.size(); i++) {
            char letter = (char) ('A' + i);
            sb.append("[").append(letter).append("] ").append(m_cards.get(i).getName()).append(" ");
        }
        return sb.toString();
    }
}
