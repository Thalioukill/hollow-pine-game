public interface GameStrategy {
    public Opponent initOpponent();

    public void opponentAction(Opponent opponent, int roundNumber);
}
