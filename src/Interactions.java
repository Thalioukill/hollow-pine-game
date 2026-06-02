import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

//Gère exclusivement les entrées (Scanner) et sorties (System.out) console.
public class Interactions {
    private final Scanner m_scanner;
    // DISCLAIMER : potentiellement mattre le player en attributs

    public Interactions() {
        // on n'instancie qu'un seul Scanner pour System.in
        this.m_scanner = new Scanner(System.in);
    }

    // Constructeur de test
    public Interactions(Scanner scanner) {
        this.m_scanner = scanner;
    }

    // Demande à l'utilisateur de choisir sa prochaine action via un menu et retourne membre de l'énumération Actions validé.
    public boolean askNextAction(User human) {
        displayMenu(human);
        String input = m_scanner.next().toUpperCase(); // On tolère 'p' ou 'P
        if (input.equals("P")) {
            return handlePlacement(human);
        }
        if (input.equals("F")) {
            return false;
        }
        // Sinon la saisie est invalide...
        this.displayMessage("Choix inconnu ! Veuillez saisir P ou F.");
        return true;
    }


    public void displayMenu(User human){
        System.out.println("\n--- ACTIONS POSSIBLES ---");
        if (human.getHandSize() > 0) {
            System.out.println("[P] Placer une carte sur le plateau\n");
        } else {
            System.out.println("Votre main est vide, vous ne pouvez plus placer de carte !");
        }
        System.out.println("[F] Finir le tour");
        System.out.print("Votre choix : ");

    }

    private boolean handlePlacement(User human) {
        if (human.getHandSize() == 0) {
            System.out.println("Vous n'avez plus de cartes en main !");
            return askNextAction(human);
        }

        int handIndex = askCardLetter(human);
        Animal card = human.getHandCard(handIndex);

        // Carte gratuite (écureuil) → on pose directement
        if (card.getBloodCost() == 0 && card.getBoneCost() == 0) {
            placeCard(human, handIndex);
            return true;
        }

        // Carte à coût en SANG
        if (card.getBloodCost() > 0) {
            if (!human.hasEnoughBlood(card)) {
                displayMessage("Pas assez de créatures à sacrifier pour poser cette carte, veuillez en choisir une autre coutant moins de "+ human.getSacrificableCount()+".");
                return true;
            }
            handleSacrifices(card.getBloodCost(), human);
            placeCard(human, handIndex);
            return true;
        }

        // Carte à coût en OS
        if (card.getBoneCost() > 0) {
            if (!human.hasEnoughBones(card)) {
                displayMessage("Pas assez d'os (" + human.getBonesToken() + "/" + card.getBoneCost() + ").");
                return true;
            }
            human.spendBones(card.getBoneCost());
            placeCard(human, handIndex);
            return true;
        }

        return true;
    }

    private void placeCard(User human, int handIndex) {
        int position = askPosition(human);
        human.playCardToBoard(handIndex, position);
        displayMessage("Carte posée !");
    }


    private void handleSacrifices(int bloodCost, User human) {
        for (int i = 0; i < bloodCost; i++) {
            displayMessage("Sacrifice " + (i + 1) + "/" + bloodCost + " :");
            int sacrificePos = askPositionSacrifice(human);
            Card target = human.getCardAt(sacrificePos).get();

            if (!target.isSacrificable()) {
                displayMessage("Impossible : un obstacle ne peut pas être sacrifié !");
                i--;
                continue;
            }

            if (target.survivesSacrifice()) {
                displayMessage(target.getName() + " survit au sacrifice ! +1 os. +1 goutte de sang.");
                human.addBonesToken();
            } else {
                human.sacrifice(sacrificePos);
                displayMessage("Emplacement libéré. +1 os. +1 goutte de sang.");
            }
        }
    }


    public boolean askConfirmation(String s) {
        while (true) {
            System.out.print(s);
            String input = m_scanner.next().toUpperCase();
            if (input.equals("OUI")){
                return true;
            } else if (input.equals("NON")){
                return false;
            }
            // ERREURRRR
            System.out.println("Veuillez saisir OUI ou NON");
        }
    }


    // Demande une position sur le plateau (chifres 1 à 4).
    public int askPosition(User human) {
        while (true) {
            System.out.print("Choisissez un emplacement où poser votre carte (1, 2, 3 ou 4) : ");

            // vérif du type
            if (!m_scanner.hasNextInt()) {
                System.out.println("Saisie invalide ! Veuillez entrer un chiffre entier.");
                m_scanner.next(); // on enlève la saisie incorrecte du scanner (sinon elle reste)
                continue;
            }

            int pos = m_scanner.nextInt();
            int index = toIndex(pos);

            // vérif de l'intervalle :
            if (pos < 1 || pos > 4) {
                System.out.println("Chiffre incorrect ! L'emplacement doit être entre 1 et 4.");
            }
            // vérif si on occupe l'emplacement ou pas
            else if (human.getBoardLineCardAt(pos).isPresent()) {
                System.out.println("L'emplacement " + pos + " est déjà occupé, choisissez-en un autre.");
            }
            else {
                return index;
            }
        }
    }


    public int askPositionSacrifice(User human) {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement de la carte que vous souhaitez sacrifier (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                if(pos > 0 && pos <= 4){
                    int index = toIndex(pos);
                    Optional<Card> result = human.getCardAt(index);
                    if (result.isPresent()) {
                        if(result.get().isSacrificable()){
                            return index; // on retourne juste l'int
                        }
                    }
                }
                else{
                    System.out.println("Veuillez choisir un chiffre entre 1 et 4...");
                }
            } else {
                m_scanner.next(); // Lit le prochain mot
                // ERREURRRR
                System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
            }
        }
    }

    // Demande la lettre d'une carte en main (lettres A, B, C...) + handSize taille actuelle de la main pour limiter le chiox
    public int askCardLetter(User human) {
        char maxLetter = (char) ('A' + human.getHandSize() - 1);
        while (true) {
            System.out.print("Choisissez une carte dans votre main (lettre entre A et " + maxLetter + ") : ");
            String input = m_scanner.next().toUpperCase();

            // test de longueur
            if (input.length() != 1) {
                System.out.println("Saisie invalide ! Veuillez n'entrer qu'une seule lettre.");
            }
            else {
                char letter = input.charAt(0);
                // test si c'est bien une lettre et dans l'intervalle [A, maxLetter]
                if (Character.isLetter(letter) && letter >= 'A' && letter <= maxLetter) {
                    return letter - 'A'; // Retourne l'index 0, 1, 2... directement
                } else {
                    System.out.println("Lettre incorrecte ! Veuillez choisir entre A et " + maxLetter + ".");
                }
            }
            // La boucle continue tant qu'on n'a pas fait de 'return'
        }
    }


    private int letterToIndex(char letter) {
        // Character.toUpperCase pour accepter 'a' ou 'A'
        // On utilise l'ASCII pour trouver l'index  'A' = 65
        return Character.toUpperCase(letter) - 'A';
    }


    private int toIndex(int position) {
        return position - 1;
    }

    // on pioche 1 carte dans le Deck et on l'ajoute à la main.
    public void draw(User joueur) {
        // Si le deck ET la main sont vides, pas la peine de demander
        if (joueur.getDeckSize() == 0 && joueur.getHandSize() == 0) {
            this.displayMessage("Vous n'avez plus de cartes ni en main ni dans la pioche... Il va falloir faire avec !");
            return;
        }
        // L'utilisateur a le choix
        boolean wantsToDraw = this.askConfirmation("Voulez-vous piocher une carte pour ce tour ? Veuillez saisir OUI ou NON : ");

        if (wantsToDraw) {
            try {
                Animal drawnAnimalCard = joueur.draw();
                joueur.addCard(drawnAnimalCard);
                this.displayMessage("Vous avez pioché : " + drawnAnimalCard.getName());
            } catch (NoSuchElementException n) {
                this.displayMessage("Oups il n'y a plus de cartes dans la pioche...");
            }
        } else {
            this.displayMessage("Vous avez décidé de ne pas piocher... Étais-ce un bon choix...");
        }
    }




    // Affiche un message simple (dégâts, pioche,etc)
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayFinalResult(boolean victory){
        if (victory) {
            System.out.println("Félicitations ! Vous avez survécu à la forêt.");
        } else {
            System.out.println("L'obscurité vous englouti... Partie terminée.");
        }
    }


    public Animal offerNewCardChoice(Animal card1, Animal card2) {
        System.out.println("\n--- RÉCOMPENSE ---");
        System.out.println("Félicitations ! Vous avez survécu aux deux première parties ! Cela en sera t-il pour la dernière ? ");
        System.out.println("Pour vous aider à vaincre Leshy voici deux cartes, choisissez en UNE pour parfaire votre deck :");

        // Affichage des cartes pour aider
        displayCardOption(1, card1);
        displayCardOption(2, card2);

        while (true) {
            System.out.print("Votre choix (1 ou 2) : ");

            if (m_scanner.hasNextInt()) {
                int choice = m_scanner.nextInt();
                if (choice != 1 && choice != 2) {
                    System.out.println("Saisie invalide ! Veuillez entrer 1 ou 2.");
                    continue;
                }
                if (choice == 1) {
                    return card1;
                }
                return card2;
            }
            System.out.println("Erreur : Choix inconnu. Veuillez saisir le chiffre 1 ou 2.");
        }
    }


    private void displayCardOption(int index, Animal card) {
        String labelCout;

        if (card.getBloodCost() > 0) {
            // Si la carte coûte du sang
            labelCout = card.getBloodCost() + " Sang";
        } else {
            // Sinon, on affiche le coût en os
            labelCout = card.getBoneCost() + " Os";
        }

        // ligne finale :
        System.out.println("  [" + index + "] " + card.getName() + " (Atk: " + card.getAttackPoints() + ", HP: " + card.getHealthPoints() + ", Coût: " + labelCout + ")");
    }
}
