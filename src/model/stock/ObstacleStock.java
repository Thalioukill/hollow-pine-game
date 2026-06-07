package model.stock;

import model.cards.Obstacle;

import java.util.ArrayList;
import java.util.Optional;

public class ObstacleStock {
    private final ArrayList<Obstacle> m_templates = new ArrayList<>();

    public static final String ROCHER = "Rocher";
    public static final String SAPIN  = "Sapin";

    public ObstacleStock() {
        m_templates.add(new Obstacle(ROCHER, 5));
        m_templates.add(new Obstacle(SAPIN,  3));
    }

    public Optional<Obstacle> getObstacle(String name) {
        for (Obstacle o : m_templates) {
            if (o.getName().equals(name)) {
                return Optional.of(new Obstacle(o)); // Constructeur de copie pour la sécurité
            }
        }
        return Optional.empty();
    }
}