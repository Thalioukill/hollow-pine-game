import textcolors.Color;

import java.util.ArrayList;
import java.util.Optional;

public class GameBoard {
    /*
    private static final String SEP = "   ";

    public void display(User user, Opponent opponent, int score) {
        System.out.println("\n================= PLATEAU (Balance: " + score + " | Os: " + user.getBonesToken() + ") =================\n");
        System.out.println("              -- Prévisions adversaire --");
        renderBoardLine(opponent, true);
        System.out.println("      ||                ||              ||            ||");
        System.out.println("     \\/               \\/             \\/           \\/");
        System.out.println("                      -- Adversaire --");
        renderBoardLine(opponent, false);
        System.out.println("\n                           -- Vous --");
        renderBoardLine(user, false);
        displayHand(user);
        displayDeck(user);
    }

    private void renderBoardLine(Player player, boolean isPrevision) {
        ArrayList<ArrayList<String>> allLines = collectLines(player, isPrevision);
        printAllLines(allLines);
    }

    private ArrayList<ArrayList<String>> collectLines(Player player, boolean isPrevision) {
        ArrayList<ArrayList<String>> allLines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Optional<Card> optCard = isPrevision
                    ? ((Opponent) player).getPrevisionCardAt(i)
                    : player.getBoardLineCardAt(i);
            allLines.add(getCardLines(optCard));
        }
        return allLines;
    }

    private void printAllLines(ArrayList<ArrayList<String>> allLines) {
        for (int row = 0; row < 7; row++) {
            StringBuilder sb = new StringBuilder("  ");
            for (ArrayList<String> cardLines : allLines) {
                sb.append(cardLines.get(row)).append(SEP);
            }
            System.out.println(sb);
        }
    }

    private ArrayList<String> getCardLines(Optional<Card> optCard) {
        return optCard.map(Card::getDisplayLines)
                .orElseGet(this::emptyCardLines);
    }

    private ArrayList<String> emptyCardLines() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("*************");
        for (int i = 0; i < 5; i++) {
            lines.add("*           *");
        }
        lines.add("*************");
        return lines;
    }

    private void displayHand(User user) {
        System.out.println("\nVotre main :");
        for (int i = 0; i < user.getHandSize(); i++) {
            char letter = (char) ('A' + i);
            Animal card = user.getHandCard(i);
            System.out.println("  [" + letter + "] "
                    + card.getName()
                    + " | PV: "   + card.getHealthPoints()
                    + " | Atk: "  + card.getAttackPoints()
                    + " | Sang: " + card.getBloodCost()
                    + " | Os: "   + card.getBoneCost());
        }
    }

    private void displayDeck(User user) {
        System.out.println("\n*-----------*");
        System.out.println("|           |");
        System.out.printf( "|   %-2d      |%n", user.getDeckSize());
        System.out.println("|  cartes   |");
        System.out.println("|           |");
        System.out.println("*-----------*\n");
    }

    @Override
    public String toString() {
        return "GameBoard[]";
    }

    */

    //--------------------------------------------------------------------------------
// Constantes de dimensions
    private static final int CARD_HEIGHT = 7;
    private static final String SEP = "   "; // Espace entre les cartes
    private static final String BORDER_EMPTY = "*************";
    private static final String BORDER_CARD  = "*-----------*";

    private final StringBuilder m_buffer = new StringBuilder();

    /**
     * Méthode principale d'affichage. Orchestre le rendu couche par couche.
     */
    public void display(User user, Opponent opponent, int balance) {
        m_buffer.setLength(0); // Réinitialisation du buffer à chaque tour

        // Balance et os
        m_buffer.append("\n================= PLATEAU (Balance: ").append(balance)
                .append(" | Os: ").append(user.getBonesToken()).append(") =================\n\n");

        // ligne de prévision de l'adversaire (Top)
        renderBoardLine(opponent.getPrevisionLine(), Color.RED, false);
        m_buffer.append("      ||              ||              ||              ||      \n");
        m_buffer.append("      \\/              \\/              \\/              \\/      \n");
        // ligne de combat de l'adversaire
        renderBoardLine(opponent.getBoardLine(), Color.RED, false);
        m_buffer.append("      /\\              /\\              /\\              /\\      \n");
        m_buffer.append("      --              --     VOUS     --              --      \n\n");

        // ligne de combat du joueur
        renderBoardLine(user.getBoardLine(), Color.GREEN, true); // JOUEUR (avec numéros)
        m_buffer.append("==============================================================\n");

        System.out.print(m_buffer.toString());
        displayHand(user);
        displayDeck(user);
    }

    // rendu d'une ligne complète (4 slots) couche par couche (hauteur).
    private void renderBoardLine(BoardLine line, String color, boolean isUserLine) {
        for (int layer = 0; layer < CARD_HEIGHT; layer++) {
            for (int slot = 0; slot < 4; slot++) {
                appendCardLayer(line.getCardAt(slot), layer, slot, isUserLine, color);
                m_buffer.append(SEP);
            }
            m_buffer.append("\n");
        }
    }

     //Ajoute au buffer le texte d'une seule couche pour une carte donnée..
    private void appendCardLayer(Optional<Card> optCard, int layer, int slot, boolean isUser, String color) {
        if (optCard.isEmpty()) {
            m_buffer.append(getEmptySlotContent(layer, slot, isUser));
            return;
        }

        // on applique la couleur passée en param
        m_buffer.append(color).append(getLayerContent(optCard.get(), layer)).append(Color.RESET);
    }

    private String getEmptySlotContent(int layer, int slot, boolean isUser) {
        // Si c'est la ligne du joueur et qu'on est au milieu de la carte (layer 3)
        if (isUser && layer == 3) {
            return String.format("*     %d     *", slot + 1); // Affiche 1, 2, 3 ou 4
        }
        // Sinon, bordures ou vide standard
        return (layer == 0 || layer == 6) ? BORDER_EMPTY : "*           *";
    }

     // texte interne de chaque ligne d'une carte
    private String getLayerContent(Card card, int layer) {
        return switch (layer) {
            case 0, 6 -> BORDER_CARD;
            case 1    -> String.format("| %-9s |", truncate(card.getName()));
            case 2    -> "|-----------|";
            case 3    -> String.format("| PV: %-5d |", card.getHealthPoints());
            case 4    -> getStatLine(card); // ligne d'attaque ou vide
            case 5    -> card.isSacrificable() ? "|  ANIMAL   |" : "| OBSTACLE  |";
            default   -> "|           |";
        };
    }

     // Récup la ligne de statistiques (Attaque) pour les animaux.
    private String getStatLine(Card card) {
        // On suppose que Animal possède getAttackPoints()
        if (card instanceof Animal animal) {
            return String.format("| Atk: %-4d |", animal.getAttackPoints());
        }
        return "|           |";
    }

     // casser les noms longs pour pas que l'affichage ne soit trop grand
    private String truncate(String name) {
        return name.length() > 9 ? name.substring(0, 9) : name;
    }

    private void displayHand(User user) {
        System.out.println("\nVotre main :");
        for (int i = 0; i < user.getHandSize(); i++) {
            char letter = (char) ('A' + i);
            Animal card = user.getHandCard(i);
            System.out.println("  [" + letter + "] "+ card.getName()+ " | PV: "   + card.getHealthPoints()+ " | Atk: "  + card.getAttackPoints()+ " | Sang: " + card.getBloodCost()+ " | Os: "   + card.getBoneCost());
        }
    }

    private void displayDeck(User user) {
        System.out.println("\n*-----------*");
        System.out.println("|           |");
        System.out.printf( "|   %-2d      |%n", user.getDeckSize());
        System.out.println("|  cartes   |");
        System.out.println("|           |");
        System.out.println("*-----------*\n");
    }
}
