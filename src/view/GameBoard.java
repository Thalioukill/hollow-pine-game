package view;

import model.abilities.Ability;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Card;
import model.players.Opponent;
import model.players.User;
import textColors.Color;
import java.util.ArrayList;
import java.util.Optional;

public class GameBoard {

    // Chaque carte fait exactement 17 caractères de large
    private static final int CARD_HEIGHT  = 9;
    private static final String SEP = "   "; // 3 espaces entre cartes
    private static final String PREFIX = "          "; // 10 espaces : marge gauche
    private static final String BORDER_EMPTY = "*****************"; // 17 chars
    private static final String BORDER_CARD = "*---------------*"; // 17 chars

    private final StringBuilder m_buffer = new StringBuilder();


    // Méthode principale
    public void displayCombat(User user, Opponent opponent, int balance, ArrayList<String> messages) {
        m_buffer.setLength(0);

        m_buffer.append("\n").append(PREFIX).append("=============================================================================\n");
        m_buffer.append(PREFIX).append("                                 PLATEAU\n");
        m_buffer.append(PREFIX).append("=============================================================================\n\n");

        // Ligne de prévision de l'adversaire
        renderBoardLine(opponent.getPrevisionLine(), Color.RED, false, false, 0);

        // marge de gauche de 10 espaces(PREFIX) puis les flèches alignées sur chaque carte
        m_buffer.append(PREFIX).append("       ||                   ||                 ||                  ||      \n");
        m_buffer.append(PREFIX).append("       \\/                   \\/                 \\/                  \\/      \n");

        // Ligne de combat adversaire + BALANCE dans la marge gauche
        renderBoardLine(opponent.getBoardLine(), Color.RED, false, true, balance);

        // Flèches
        m_buffer.append(PREFIX).append("       /\\                  /\\                  /\\                  /\\      \n");
        m_buffer.append(PREFIX).append("       --                  --       VOUS       --                  --      \n\n");

        // Ligne de combat joueur
        renderBoardLine(user.getBoardLine(), Color.GREEN, true, false, 0);

        m_buffer.append(PREFIX).append("=============================================================================\n");

        System.out.print(m_buffer);
        displayMessages(messages); // après la main
        messages.clear();           // vide la liste après affichage
    }

    public void display(User user, Opponent opponent, int balance, ArrayList<String> messages) {
        displayCombat(user, opponent, balance, messages);
        displayHandAndDeck(user);
    }

    private void displayMessages(ArrayList<String> messages) {
        if (messages.isEmpty()) return;
        System.out.println("\n--- Événements ---");
        for (String msg : messages) {
            System.out.println("  > " + msg);
        }
    }


    // Rendu d'une ligne de 4 slots, couche par couche
    // showScore=true : affiche "BALANCE" puis la valeur dans la marge gauche
    private void renderBoardLine(BoardLine line, String color, boolean isUserLine, boolean showScore, int score) {
        // layer 3 = milieu visuel de la carte (entre le nom et les stats)
        int labelLayer = 3;
        int valueLayer = 4;

        for (int layer = 0; layer < CARD_HEIGHT; layer++) {
            // Marge gauche
            if (showScore && layer == labelLayer) {
                m_buffer.append(String.format("%-10s", "BALANCE"));
            } else if (showScore && layer == valueLayer) {
                // centré sur 10 chars
                String val = String.valueOf(score);
                int padding = (8 - val.length()) / 2;
                m_buffer.append(String.format("%" + (padding + val.length()) + "s%-"+ (10 - padding - val.length()) + "s", val, ""));
            } else {
                m_buffer.append(PREFIX);
            }

            // 4 slots
            for (int slot = 0; slot < 4; slot++) {
                appendCardLayer(line.getCardAt(slot), layer, slot, isUserLine, color);
                m_buffer.append(SEP);
            }
            m_buffer.append("\n");
        }
    }

    // Contenu d'un slot pour une layer
    private void appendCardLayer(Optional<Card> optCard, int layer, int slot, boolean isUser, String color) {
        if (optCard.isEmpty()) {
            m_buffer.append(getEmptySlotContent(layer, slot, isUser));
            return;
        }
        m_buffer.append(color).append(getLayerContent(optCard.get(), layer)).append(Color.RESET);
    }

    // Slot vide
    private String getEmptySlotContent(int layer, int slot, boolean isUser) {
        if (layer == 0 || layer == 8) {
            return BORDER_EMPTY;
        }
        if (isUser && layer == 4) {
            // index du slot centré : 1 + 8 + 1chiffre + 8 + 1 = 18
            return String.format("*       %d       *", slot + 1);
        }
        return "*               *"; // 17 chars
    }

    // Carte occupée
    private String getLayerContent(Card card, int layer) {
        return switch (layer) {
            case 0, 8 -> BORDER_CARD;
            case 1 -> card.isSacrificable() ? "|    ANIMAL     |"
                    : "|   OBSTACLE    |";
            case 2 -> String.format("| %-13s |", truncate(card.getName()));
            case 3 -> "|---------------|";
            case 4 -> String.format("| PV: %-9d |", card.getHealthPoints());
            case 5 -> getStatLine(card);
            case 6 -> getAbilityLine(card, 0);
            case 7 -> getAbilityLine(card, 1);
            default -> "|               |";
        };
    }

    private String getStatLine(Card card) {
        if (card.isSacrificable()) {
            return String.format("| Atk: %-8d |", card.getAttackPoints());
        }
        return "|               |";
    }

    private String getAbilityLine(Card card, int index) {
        if (!card.hasAbility()) {
            return "|               |";
        }
        ArrayList<Ability> abilities = card.getAbilities();
        if (index >= abilities.size()) {
            return "|               |";
        }
        return String.format("| %-13s |", truncate(abilities.get(index).getShortName()));
    }

    // Tronque à 13 caractères max (espace interne d'une carte)
    private String truncate(String name) {
        return name.length() > 13 ? name.substring(0, 13) : name;
    }

     // Main + pioche côte à côte
    // cardLine fait exactement 92 chars (calculé)
    // La pioche (6 lignes) est centrée verticalement sur la main
    private void displayHandAndDeck(User user) {
        int handSize = user.getHandSize();
        int deckSize = user.getDeckSize();

        String[] deckLines = {"*-----------*", "|           |", String.format("|   %-2d      |", deckSize), "|  cartes   |", "|           |", "*-----------*"};
        int DECK_HEIGHT = deckLines.length; // 6

        // Ligne de la pioche commençant à deckStart dans la liste des lignes de main
        int deckStart = Math.max(0, (handSize - DECK_HEIGHT) / 2);

        // Largeur fixe d'une ligne de carte : 95
        int cardLineWidth = 95;

        System.out.println("\nVotre main :");

        int totalLines = Math.max(handSize, deckStart + DECK_HEIGHT);

        for (int i = 0; i < totalLines; i++) {

            // Ligne de carte ou vide
            String cardLine = "";
            if (i < handSize) {
                char letter = (char) ('A' + i);
                Animal card  = user.getHandCard(i);

                String abilities = "Aucun";
                if (card.hasAbility()) {
                    StringBuilder sb = new StringBuilder();
                    for (Ability a : card.getAbilities()) {
                        if(!sb.isEmpty()){
                            sb.append(", ");
                        }
                        sb.append(a.getShortName());
                    }
                    abilities = sb.toString();
                }
                // Tronquer à 17 chars pour l'alignement (deux abilities peut décaler la pioche...)
                if (abilities.length() > 20) {
                    abilities = abilities.substring(0, 17) + "...";
                }

                cardLine = String.format( "  [%c] %-12s | PV: %-3d | Atk: %-3d | Sang: %-2d | Os: %-2d | Pouvoir(s): %-20s", letter, card.getName(), card.getHealthPoints(), card.getAttackPoints(), card.getBloodCost(), card.getBoneCost(), abilities);
            }

            // Ligne de pioche ou vide
            int deckIndex = i - deckStart;
            if (deckIndex >= 0 && deckIndex < DECK_HEIGHT) {
                System.out.printf("%-" + cardLineWidth + "s  %s%n", cardLine, deckLines[deckIndex]);
            } else if (!cardLine.isEmpty()) {
                System.out.println(cardLine);
            }
        }

        // Os sous la main, avec une ligne de séparation
        System.out.println("Os disponibles : " + user.getBonesToken());
        System.out.println();
    }
}