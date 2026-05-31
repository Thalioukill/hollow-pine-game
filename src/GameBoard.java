import java.util.ArrayList;
import java.util.Optional;

public class GameBoard {
    private static final String SEP = "   ";

    public void display(User user, Opponent opponent, int score) {
        System.out.println("\n================ PLATEAU (Balance: " + score + " | Os: " + user.getBonesToken() + ") ================\n");
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
                    : player.getBoardLineCardAt(i + 1);
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
}
