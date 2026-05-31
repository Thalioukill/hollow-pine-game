import java.util.ArrayList;

public class Obstacle extends Card{

    public Obstacle(String name, Integer hp){
        super(name, hp);
    }

    // Constructeur de COPIE, pour l'instant pas très utile
    public Obstacle(Obstacle other){
        super(other);
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

    @Override
    public void takeDamage(int amount){
        this.m_healthPoints-= amount;
    }

    public Obstacle cloneObstacle() {

        return new Obstacle(this); // Utilise le constructeur de copie
    }


    @Override
    public ArrayList<String> getDisplayLines() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("*-----------*");
        lines.add(String.format("| %-9s |", truncate(m_name, 9)));
        lines.add("|-----------|");
        lines.add(String.format("| PV:  %-5d |", m_healthPoints));
        lines.add("|           |");
        lines.add("| Obstacle  |");
        lines.add("*-----------*");
        return lines;
    }

    private String truncate(String s, int maxLength) {
        if (s.length() <= maxLength) return s;
        return s.substring(0, maxLength);
    }
}
