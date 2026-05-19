import java.util.ArrayList;

public class CardStock {
    private final ArrayList<Card> m_templates = new ArrayList<>();

    //Bloc d'initialisation statique pour remplir le stock au chargement de la classe.

    public CardStock() {
        // Ajout des animaux terrestres (Nom, Atk, HP, Blood, Bone)
        m_templates.add(new Animal("Chat", 0, 1, 1, 0));
        m_templates.add(new Animal("Grizzly", 4, 6, 3, 0));
        m_templates.add(new Animal("Coyote", 2, 1, 0, 4));
        m_templates.add(new Animal("Ecureuil", 0, 1, 0, 0));
        m_templates.add(new Animal("Hermine", 1, 3, 1, 0));
        m_templates.add(new Animal("Louveteau", 1, 1, 1, 0));
        m_templates.add(new Animal("Loup", 3, 2, 2, 0));
        m_templates.add(new Animal("Punaise", 1, 2, 0, 2));

        // Ajout des animaux volants (Logique de FlyingAbility ou Ability)
        m_templates.add(new FlyingAbility("Moineau", 1, 2, 1, 0));
        m_templates.add(new FlyingAbility("Corbeau", 2, 3, 2, 0));

    }

    /*
     Récupère une NOUVELLE instance d'une carte par son nom.
     Il est crucial de renvoyer une copie pour éviter que les dégâts
     en jeu ne modifient le modèle dans le stock
     */
    public Optional<Card> getCard(String name) {
        for (Card template : m_templates) {
            if (template.getName().equals(name)) {
                // Utilisation du constructeur de copie pour la sécurité

            }
        }
        return Optional.empty(); // Utilisation d'Optional au lieu de null
    }

    @Override
    public String toString() {
        return "CardStock(available=" + m_templates.size() + ")";
    }
}
}
