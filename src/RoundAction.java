import java.util.List;

public class RoundAction {
    private final List<Integer> m_positions;

    public RoundAction(List<Integer> positions) {
        this.m_positions = positions;
    }

    public List<Integer> getPositions() {
        return m_positions;
    }

    public static RoundAction empty() {
        return new RoundAction(List.of());
    }
}