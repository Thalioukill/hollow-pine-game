import java.util.Scanner;

//Gère exclusivement les entrées (Scanner) et sorties (System.out) console.
public class Interactions {
    private final Scanner m_scanner;

    public Interactions() {
        // on n'instancie qu'un seul Scanner pour System.in
        this.m_scanner = new Scanner(System.in);
    }

     // Demande à l'utilisateur de choisir sa prochaine action via un menu et retourne membre de l'énumération Actions validé.
    public Actions askNextAction() {
        while (true) {
            System.out.println("\n--- ACTIONS POSSIBLES ---");
            System.out.println("[placer <numero carte> <position>] Placer une carte sur le plateau\n");
            System.out.println("[F] Finir le tour");
            System.out.print("Votre choix : ");

            String input = m_scanner.next().toUpperCase(); // On tolère 'p' ou 'P'

            if (input.equals("P")){
                System.out.println("\nVeuillez choisir une carte à jouer dans votre Main : ");
                System.out.println("[P] Poser une carte");
                System.out.println("[F] Finir le tour");
                System.out.print("Votre choix : ");
            }

            if (input.equals("F")){
                return
            }
            // Sinon la saisie est invalide...
            System.out.println("Choix inconnu ! Veuillez saisir P ou F.");
        }
    }

     // Demande une position sur le plateau (chifres 1 à 4).
    public int askPosition() {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement où vous souhaitez poser la carte (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                if (pos >= 1 && pos <= 4) {
                    return pos;
                }
            } else {
                m_scanner.next(); // Lit le prochain mot
            }
            // ERREURRRR
            System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
        }
    }

    public int askPositionSacrifice() {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement de la carte que vous souhaitez sacrifier (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                if (pos >= 1 && pos <= 4) {
                    return pos;
                }
            } else {
                m_scanner.next(); // Lit le prochain mot
            }
            // ERREURRRR
            System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
        }
    }

    // Demande la lettre d'une carte en main (lettres A, B, C...) + handSize taille actuelle de la main pour limiter le chiox
    public char askCardLetter(int handSize) {
        char maxLetter = (char) ('A' + handSize - 1);
        while (true) {
            System.out.print("Choisissez une carte dans votre main (lettre entre A et " + maxLetter + ") : ");
            String input = m_scanner.next().toUpperCase();

            if (input.length() == 1) {
                char letter = input.charAt(0);
                if (letter >= 'A' && letter <= maxLetter) {
                    return letter;
                }
            }
            System.out.println("Lettre incorrecte ! Votre main va de A à " + maxLetter + ".");
        }
    }


    // Affiche un message simple (dégâts, pioche,etc)
    public void displayMessage(String message) {
        System.out.println(message);
    }
}

