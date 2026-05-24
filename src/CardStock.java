import java.util.ArrayList;
import java.util.Optional;

public class CardStock {
    private final ArrayList<Card> m_templates = new ArrayList<>();
    // Constantes de noms (comme un genre d'enum...
    public static final String CHAT      = "Chat";
    public static final String GRIZZLY   = "Grizzly";
    public static final String COYOTE    = "Coyote";
    public static final String ECUREUIL  = "Ecureuil";
    public static final String HERMINE   = "Hermine";
    public static final String LOUVETEAU = "Louveteau";
    public static final String LOUP      = "Loup";
    public static final String PUNAISE   = "Punaise";
    public static final String MOINEAU   = "Moineau";
    public static final String CORBEAU   = "Corbeau";

    //Bloc d'initialisation statique pour remplir le stock au chargement de la classe.

    public CardStock() {
        // Ajout des animaux terrestres (Nom, Atk, HP, Blood, Bone)
        m_templates.add(new Animal(CHAT,      0, 1, 1, 0));
        m_templates.add(new Animal(GRIZZLY,   4, 6, 3, 0));
        m_templates.add(new Animal(COYOTE,    2, 1, 0, 4));
        m_templates.add(new Animal(ECUREUIL,  0, 1, 0, 0));
        m_templates.add(new Animal(HERMINE,   1, 3, 1, 0));
        m_templates.add(new Animal(LOUVETEAU, 1, 1, 1, 0));
        m_templates.add(new Animal(LOUP,      3, 2, 2, 0));
        m_templates.add(new Animal(PUNAISE,   1, 2, 0, 2));

        // Animaux volants (Animals avec une FlyingAbility)
        Animal moineau = new Animal(MOINEAU, 1, 2, 1, 0);
        moineau.addAbility(new FlyingAbility());
        m_templates.add(moineau);

        Animal corbeau = new Animal(CORBEAU, 2, 3, 2, 0);
        corbeau.addAbility(new FlyingAbility());
        m_templates.add(corbeau);
    }

    /*
     Récupère une NOUVELLE instance d'une carte par son nom.
     Il est crucial de renvoyer une copie pour éviter que les dégâts
     en jeu ne modifient le modèle dans le stock
     */
    public Optional<Card> getCard(String name) {
        for (Card template : m_templates) {
            if (template.getName().equals(name)) {
                // Utilisation du constructeur de copie pour la sécurité :on appelle cloneCard() qui renvoie une COPIE PROFNDE du type concret correspondant !!!!!
                return Optional.of(template.cloneCard());
            }
        }
        return Optional.empty();
    }


    @Override
    public String toString() {
        return "CardStock(available=" + m_templates.size() + ")";
    }
}

