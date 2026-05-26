import java.util.Optional;

public interface Ability {
    void onAttack(Animal self, Optional<Card> target, Game game) {
    }

    void onPlay(Animal self, Player owner, Game game) {
    }

    void onDeath(Animal self, Player owner, Game game) {
    }

    void onHit(Animal self, Animal attacker, Game game) {
    }
}