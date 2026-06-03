import textcolors.Color;

import java.util.ArrayList;
import java.util.Optional;

public class GameBoard {
// Constantes pour les dimensions
    private static final int CARD_HEIGHT = 7;
    private static final String SEP = "   "; // Espace entre les cartes
    private static final String BORDER_EMPTY = "*************";
    private static final String BORDER_CARD  = "*-----------*";

    private final StringBuilder m_buffer = new StringBuilder();

    // affichage couche par couches
    public void display(User user, Opponent opponent, int balance) {
        m_buffer.setLength(0); // on réinitialise à chaque tour

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

     //Ajout le texte d'une seule couche pour une carte donnée..
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
        if((layer == 0 || layer == 6)){
            return BORDER_EMPTY;
        }
        else{
            return"*           *";
        }
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

     // Récup la ligne de statistiques (attaque) pour les animaux.
    private String getStatLine(Card card) {
        if (card.isSacrificable()) {
            return String.format("| Atk: %-4d |", card.getAttackPoints());
        }
        return "|           |";
    }

     // casser les noms longs pour pas que l'affichage ne soit trop grand
    private String truncate(String name) {

        String nameString = name;
        if(name.length() > 9){
            nameString = name.substring(0, 9);
        }
        return nameString;
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
