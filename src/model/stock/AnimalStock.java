package model.stock;

import model.abilities.*;
import model.cards.Animal;
import model.cards.AnimalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalStock {
    private final ArrayList<Animal> m_templates = new ArrayList<>();

    public AnimalStock() {
        m_templates.add(new Animal(AnimalType.GRIZZLY.getDisplayName(), 4, 6, 3, 0, AnimalType.GRIZZLY));
        m_templates.add(new Animal(AnimalType.COYOTE.getDisplayName(), 2, 1, 0, 4, AnimalType.COYOTE));
        m_templates.add(new Animal(AnimalType.ECUREUIL.getDisplayName(), 0, 1, 0, 0, AnimalType.ECUREUIL));
        m_templates.add(new Animal(AnimalType.HERMINE.getDisplayName(), 1, 3, 1, 0, AnimalType.HERMINE));
        m_templates.add(new Animal(AnimalType.LOUP.getDisplayName(), 3, 2, 2, 0, AnimalType.LOUP));

        // Chat — Nombreuses Vies
        Animal chat = new Animal(AnimalType.CHAT.getDisplayName(), 0, 1, 1, 0, AnimalType.CHAT);
        chat.addAbility(new MultipleLivesAbility());
        m_templates.add(chat);

        // Moineau — Volant
        Animal moineau = new Animal(AnimalType.MOINEAU.getDisplayName(), 1, 2, 1, 0, AnimalType.MOINEAU);
        moineau.addAbility(new FlyingAbility());
        m_templates.add(moineau);

        // Corbeau — Volant
        Animal corbeau = new Animal(AnimalType.CORBEAU.getDisplayName(), 2, 3, 2, 0, AnimalType.CORBEAU);
        corbeau.addAbility(new FlyingAbility());
        m_templates.add(corbeau);

        // Louveteau — Croissance (se transforme en Loup)
        Animal louveteau = new Animal(AnimalType.LOUVETEAU.getDisplayName(), 1, 1, 1, 0, AnimalType.LOUVETEAU);
        louveteau.addAbility(new GrowthAbility(new Animal(AnimalType.LOUP.getDisplayName(), 3, 2, 2, 0, AnimalType.LOUP)));
        m_templates.add(louveteau);

        // Punaise — Puant
        Animal punaise = new Animal(AnimalType.PUNAISE.getDisplayName(), 1, 2, 0, 2, AnimalType.PUNAISE);
        punaise.addAbility(new StinkyAbility());
        m_templates.add(punaise);

        // Élan — Coureur
        Animal elan = new Animal(AnimalType.ELAN.getDisplayName(), 2, 4, 2, 0, AnimalType.ELAN);
        elan.addAbility(new RunnerAbility());
        m_templates.add(elan);

        // Vipère — Contact Mortel
        Animal vipere = new Animal(AnimalType.VIPERE.getDisplayName(), 1, 1, 2, 0, AnimalType.VIPERE);
        vipere.addAbility(new DeathTouchAbility());
        m_templates.add(vipere);

        // Porc-épic — Piques Pointues
        Animal porcEpic = new Animal(AnimalType.PORC_EPIC.getDisplayName(), 1, 2, 1, 0, AnimalType.PORC_EPIC);
        porcEpic.addAbility(new SharpQuillsAbility());
        m_templates.add(porcEpic);

    }

    public Optional<Animal> getCard(AnimalType type) {
        for (Animal template : m_templates) {
            if (template.getAnimalType() == type) {
                return Optional.of(template.cloneAnimal());
            }
        }
        return Optional.empty();
    }

    public ArrayList<AnimalType> getAllAnimalTypes() {
        return new ArrayList<>(List.of(
                AnimalType.GRIZZLY, AnimalType.COYOTE, AnimalType.VIPERE, AnimalType.PORC_EPIC, AnimalType.MOINEAU, AnimalType.CORBEAU, AnimalType.ECUREUIL, AnimalType.HERMINE, AnimalType.LOUVETEAU, AnimalType.LOUP, AnimalType.ELAN, AnimalType.PUNAISE, AnimalType.CHAT));
    }

    @Override
    public String toString() {
        return "AnimalStock(available=" + m_templates.size() + ")";
    }
}

