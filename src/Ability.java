import java.util.Optional;

public interface Ability {
    default void onAttack(Animal self, Optional<Card> target, Game game) {}
    default void onPlay(Animal self, Player owner, Game game) {}
    default void onDeath(Animal self, Player owner, Game game) {}
    default void onHit(Animal self, Animal attacker, Game game) {}
}