package model.stock;

import model.cards.Obstacle;
import model.cards.ObstacleType;

import java.util.ArrayList;
import java.util.Optional;

public class ObstacleStock {
    private final ArrayList<Obstacle> m_templates = new ArrayList<>();

    public ObstacleStock() {
        m_templates.add(new Obstacle("Rocher", 5, ObstacleType.ROCHER));
        m_templates.add(new Obstacle("Sapin",  3, ObstacleType.SAPIN));
    }

    public Optional<Obstacle> getObstacle(ObstacleType type) {
        for (Obstacle o : m_templates) {
            if (o.getObstacleType() == type) {
                return Optional.of(new Obstacle(o));
            }
        }
        return Optional.empty();
    }
}