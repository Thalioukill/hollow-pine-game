import java.util.ArrayList;
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
    public boolean askNextAction(User human, ArrayList<String> messages) {
        displayMenu(human);
        String input = m_scanner.next().toUpperCase(); // On tolère 'p' ou 'P
        if (input.equals("P")) {
            return handlePlacement(human, messages);
        }
        if (input.equals("F")) {
            return false;
        }
        // Sinon la saisie est invalide...
        this.displayMessage("Choix inconnu ! Veuillez saisir P ou F.");
        return askNextAction(human, messages); // reboucle sans réafficher
    }


    public void displayMenu(User human) {
        System.out.println("\n--- ACTIONS POSSIBLES ---");
        if (human.getHandSize() > 0) {
            System.out.println("[P] Placer une carte sur le plateau");
        } else {
            System.out.println("Votre main est vide, vous ne pouvez plus placer de carte !");
        }
        System.out.println("[F] Finir le tour");
        System.out.print("Votre choix : ");

    }

    private boolean handlePlacement(User human, ArrayList<String> messages) {
        if (human.getHandSize() == 0) {
            System.out.println("Vous n'avez plus de cartes en main !");
            return askNextAction(human, messages);
        }

        int handIndex = askCardLetter(human);
        Animal card = human.getHandCard(handIndex);

        // Carte gratuite (écureuil) → on pose directement
        if (card.getBloodCost() == 0 && card.getBoneCost() == 0) {
            placeCard(human, handIndex, messages);
            return true;
        }

        // Carte à coût en SANG
        if (card.getBloodCost() > 0) {
            if (!human.hasEnoughBlood(card)) {
                displayMessage("Pas assez de créatures à sacrifier pour poser cette carte, veuillez en choisir une autre coutant moins de " + human.getSacrificableCount() + ".");
                return true;
            }
            handleSacrifices(card.getBloodCost(), human);
            placeCard(human, handIndex, messages);
            return true;
        }

        // Carte à coût en OS
        if (card.getBoneCost() > 0) {
            if (!human.hasEnoughBones(card)) {
                displayMessage("Pas assez d'os (" + human.getBonesToken() + "/" + card.getBoneCost() + ").");
                return true;
            }
            human.spendBones(card.getBoneCost());
            placeCard(human, handIndex, messages);
            return true;
        }

        return true;
    }

    private void placeCard(User human, int handIndex, ArrayList<String> messages) {
        Animal card = human.getHandCard(handIndex); // on récupère le nom AVANT de la jouer
        String cardName = card.getName();
        int position = askPosition(human);
        human.playCardToBoard(handIndex, position);
        messages.add(cardName + " posé(e) en position " + (position + 1) + " !");
    }


    private void handleSacrifices(int bloodCost, User human) {
        ArrayList<Integer> alreadySacrificed = new ArrayList<>();
        for (int i = 0; i < bloodCost; i++) {
            displayMessage("Sacrifice " + (i + 1) + "/" + bloodCost + " :");
            int sacrificePos = askPositionSacrifice(human, alreadySacrificed);
            Card target = human.getCardAt(sacrificePos).get();

            if (!target.isSacrificable()) {
                displayMessage("Impossible : un obstacle ne peut pas être sacrifié !");
                i--;
                continue;
            }

            if (target.survivesSacrifice()) {
                displayMessage(target.getName() + " survit au sacrifice ! +1 os. +1 goutte de sang.");
                alreadySacrificed.add(sacrificePos);
                human.addBonesToken();
            } else {
                human.sacrifice(sacrificePos);
                displayMessage("Emplacement libéré. +1 os. +1 goutte de sang.");
            }
        }
    }

    public String getAttackMessage(String attackerName, int degats) {
        if (degats == 0) {
            if (attackerName.equals("Vous")) {
                return "Votre attaque n'a pas infligé de dégâts directs.";
            }
            return "L'adversaire ne vous a pas infligé de dégâts directs.";
        }
        else{
            if (attackerName.equals("Vous")) {
                return attackerName+" avez infligé " + degats + " dégât(s) direct(s) !";
            }
            return "L'adversaire vous a infligé " + degats + " dégât(s) !";
        }
    }

    public void displayTransferResult(String fromName, String abilityName, String toName) {
        System.out.println("\n✓ Le pouvoir \"" + abilityName + "\" de " + fromName
                + " a été transféré à " + toName + " !");
    }

    public boolean askConfirmation(String s) {
        while (true) {
            System.out.print(s);
            String input = m_scanner.next().toUpperCase();
            if (input.equals("OUI")) {
                return true;
            } else if (input.equals("NON")) {
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
            else if (human.getBoardLineCardAt(index).isPresent()) {
                System.out.println("L'emplacement " + pos + " est déjà occupé, choisissez-en un autre.");
            } else {
                return index;
            }
        }
    }


    public int askPositionSacrifice(User human, ArrayList<Integer> alreadySacrificed) {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement de la carte que vous souhaitez sacrifier (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                if (pos > 0 && pos <= 4) {
                    int index = toIndex(pos);

                    if (alreadySacrificed.contains(index)) {
                        displayMessage("Cette carte a déjà été sacrifiée pour ce tour >:) !");
                        continue;
                    }

                    Optional<Card> result = human.getCardAt(index);
                    if (result.isPresent() && result.get().isSacrificable()) {
                        return index; // on retourne juste l'int
                    }
                } else {
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
            String message = (maxLetter == 'A') ? "Choisissez une carte dans votre main (il n'y a plus que la carte A... Le choix va être dur) : " : "Choisissez une carte dans votre main (lettre entre A et " + maxLetter + ") : ";

            System.out.print(message);

            String input = m_scanner.next().toUpperCase();

            // test de longueur
            if (input.length() != 1) {
                System.out.println("Saisie invalide ! Veuillez n'entrer qu'une seule lettre.");
            } else {
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
    public String draw(User joueur) {
        // Si le deck ET la main sont vides, pas la peine de demander
        if (joueur.getDeckSize() == 0 && joueur.getHandSize() == 0) {
            return "Vous n'avez plus de cartes ni en main ni dans la pioche... Il va falloir faire avec !";
        }
        // L'utilisateur a le choix
        boolean wantsToDraw = this.askConfirmation("Voulez-vous piocher une carte pour ce tour ? Veuillez saisir OUI ou NON : ");

        if (wantsToDraw) {
            try {
                Animal drawnAnimalCard = joueur.draw();
                joueur.addCard(drawnAnimalCard);
                return "Vous avez pioché : " + drawnAnimalCard.getName();
            } catch (NoSuchElementException n) {
                return "Oups il n'y a plus de cartes dans la pioche...";
            }
        } else {
            return "Vous avez décidé de ne pas piocher... Étais-ce un bon choix...";
        }
    }


    // Affiche un message simple (dégâts, pioche,etc)
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayFinalResult(boolean victory) {
        if (victory) {
            System.out.println("\nFélicitations ! Vous avez vaincu Leshy et survécu à la forêt. La nuit s'achève enfin...");
        } else {
            System.out.println("\nL'obscurité vous englouti... Partie terminée.");
        }
    }

    public void displayGameResult(int gameNumber) {

        System.out.println("\nVous avez SURVÉCU à la partie " + gameNumber + " !");

        if (gameNumber == 1) {
            System.out.println("Leshy semble s'amuser... Les prochaines parties seront plus difficiles.");
        } else if (gameNumber == 2) {
            System.out.println("Impressionnant. Mais la dernière partie sera la plus redoutable...");
        }

        if (gameNumber < 3) {
            System.out.println("\nPARTIE N° " + (gameNumber + 1) + " :");
        }
    }


    public Animal offerNewCardChoice(Animal card1, Animal card2) {
    System.out.println("\n--- RÉCOMPENSE ---");
    System.out.println("Pour vous aider à vaincre Leshy voici deux cartes, choisissez en UNE pour parfaire votre deck :");

    // Affichage des cartes pour aider
    displayCardOption(1, card1);
    displayCardOption(2, card2);

        while (true) {
            System.out.print("Votre choix (1 ou 2) : ");
            //m_scanner.next(); // ENLEVE le mot qui est
            if (!m_scanner.hasNextInt()) {
                m_scanner.next(); // vide le token invalide
                System.out.println("Saisie invalide ! Veuillez entrer 1 ou 2.");
                continue;
            }

            int choice = m_scanner.nextInt();
            if (choice != 1 && choice != 2) {
                System.out.println("Chiffre incorrect ! Veuillez entrer 1 ou 2.");
                continue;
            }
            return choice == 1 ? card1 : card2;
        }
    }

    // le deck aura toujours une carte avec une Ability au départ instancié dans le main... mais sinon on peut gérer cela ici...
    public Animal askToSacrificeCard(ArrayList<Animal> pool) {
        System.out.println("\n--- PIERRE DE SACRIFICE ---");
        System.out.println("Choisissez une carte à sacrifier pour récupérer son pouvoir :");

        for (int i = 0; i < pool.size(); i++) {
            displayCardOption(i + 1, pool.get(i));
        }

        while (true) {
            System.out.print("\nVotre choix (1 à " + pool.size() + ") : ");

            if (!m_scanner.hasNextInt()) {
                m_scanner.next();
                System.out.println("Saisie invalide ! Veuillez entrer un chiffre.");
                continue;
            }

            int choice = m_scanner.nextInt();
            if (choice < 1 || choice > pool.size()) {
                System.out.println("Chiffre incorrect ! Veuillez choisir entre 1 et " + pool.size() + ".");
                continue;
            }

            return pool.get(choice - 1);
        }
    }

    public Animal askCardToReceiveAbility(ArrayList<Animal> candidates, Ability ability) {
        System.out.println("\nChoisissez une carte pour récupérer le pouvoir de la carte que vous venez de sacrifier :");

        for (int i = 0; i < candidates.size(); i++) {
            displayCardOption(i + 1, candidates.get(i));
        }

        while (true) {
            System.out.print("\nVotre choix (1 à " + candidates.size() + ") : ");

            if (!m_scanner.hasNextInt()) {
                m_scanner.next();
                System.out.println("Saisie invalide ! Veuillez entrer un chiffre.");
                continue;
            }

            int choice = m_scanner.nextInt();
            if (choice < 1 || choice > candidates.size()) {
                System.out.println("Chiffre incorrect ! Veuillez choisir entre 1 et " + candidates.size() + ".");
                continue;
            }

            Animal candidate = candidates.get(choice - 1);
            if (candidate.hasAnAbility(ability.getName())) {
                System.out.println(candidate.getName() + " possède déjà le pouvoir \"" + ability.getName() + "\", choisissez une autre carte.");
                continue;
            }

            return candidate;
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
        String labelAbility;
        if (!card.hasAbility()) {
            labelAbility = "Aucun";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Ability a : card.getAbilities()) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(a.getName());
            }
            labelAbility = sb.toString();
        }

        System.out.println("  [" + index + "] " + card.getName() + " (Atk: " + card.getAttackPoints() + ", HP: " + card.getHealthPoints() + ", Coût: " + labelCout + ", Pouvoir: " + labelAbility + ")");
    }
}