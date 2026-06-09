package model.cards;

public class Obstacle extends Card {
    private final ObstacleType m_type;

    public Obstacle(String name, int hp, ObstacleType type) {
        super(name, hp);
        this.m_type = type;
    }

    public ObstacleType getObstacleType() { return m_type; }

    // Constructeur de COPIE, pour l'instant pas très utile
    public Obstacle(Obstacle other){
        super(other);
        this.m_type = other.m_type;
    }

    public boolean isSacrificable() {
        return false;
    }

    @Override
    public void attack(Card target) {
    }

    public int getAttackPoints(){
        return 0;
    }
}
