import java.util.ArrayList;
import java.util.Optional;

public class AnimalStock {
    private final ArrayList<Animal> m_templates = new ArrayList<>();
    // Constantes de noms (comme un genre d'enum...
    public static final String CHAT = "Chat";
    public static final String GRIZZLY = "Grizzly";
    public static final String COYOTE = "Coyote";
    public static final String MOINEAU = "Moineau";
    public static final String CORBEAU = "Corbeau";
    public static final String ECUREUIL = "Ecureuil";
    public static final String HERMINE = "Hermine";
    public static final String LOUVETEAU = "Louveteau";
    public static final String LOUP = "Loup";
    public static final String PUNAISE = "Punaise";
    public static final String ELAN = "Élan";
    public static final String VIPERE = "Vipère";
    public static final String PORC_EPIC = "Porc-épic";
    //Bloc d'initialisation statique pour remplir le stock au chargement de la classe.

    public AnimalStock() {
        // Ajout des animaux terrestres (Nom, Atk, HP, Blood, Bone)
        m_templates.add(new Animal(GRIZZLY,4, 6, 3, 0));
        m_templates.add(new Animal(COYOTE,2, 1, 0, 4));
        m_templates.add(new Animal(ECUREUIL,1, 0, 0, 0));
        m_templates.add(new Animal(HERMINE,1, 3, 1, 0));
        m_templates.add(new Animal(LOUP,3, 2, 2, 0));

        // Chat — Nombreuses Vies
        Animal chat = new Animal(CHAT, 0, 1, 1, 0);
        chat.addAbility(new MultipleLivesAbility());
        m_templates.add(chat);


        // Moineau — Volant
        Animal moineau = new Animal(MOINEAU, 1, 2, 1, 0);
        moineau.addAbility(new FlyingAbility());
        m_templates.add(moineau);

        // Corbeau — Volant
        Animal corbeau = new Animal(CORBEAU, 2, 3, 2, 0);
        corbeau.addAbility(new FlyingAbility());
        m_templates.add(corbeau);

        // Louveteau — Croissance (se transforme en Loup)
        Animal louveteau = new Animal(LOUVETEAU, 1, 1, 1, 0);
        louveteau.addAbility(new GrowthAbility(new Animal(LOUP, 3, 2, 2, 0)));
        m_templates.add(louveteau);

        // Punaise — Puant
        Animal punaise = new Animal(PUNAISE, 1, 2, 0, 2);
        punaise.addAbility(new StinkyAbility());
        m_templates.add(punaise);

        // Élan — Coureur
        Animal elan = new Animal(ELAN, 2, 4, 2, 0);
        elan.addAbility(new RunnerAbility());
        m_templates.add(elan);

        // Vipère — Contact Mortel
        Animal vipere = new Animal(VIPERE, 1, 1, 2, 0);
        vipere.addAbility(new DeathTouchAbility());
        m_templates.add(vipere);

        // Porc-épic — Piques Pointues
        Animal porcEpic = new Animal(PORC_EPIC, 1, 2, 1, 0);
        porcEpic.addAbility(new SharpQuillsAbility());
        m_templates.add(porcEpic);
    }

    /*
     Récupère une NOUVELLE instance d'une carte par son nom = copie profonde !
     */

    public Optional<Animal> getCard(String name) {
        for (Animal template : m_templates) {
            if (template.getName().equals(name)) {
                // Utilisation du constructeur de copie pour la sécurité :on appelle cloneCard() qui renvoie une COPIE PROFNDE du type concret correspondant !!!!!
                return Optional.of(template.cloneAnimal());
            }
        }
        return Optional.empty();
    }


    @Override
    public String toString() {
        return "CardStock(available=" + m_templates.size() + ")";
    }
}

