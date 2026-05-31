public class GrowthAbility {
    @Override
    public void onTurnStarted(Animal self, int position, BoardLine line) {
        // L'animal s'est déjà "reposé" un tour, on procède à la transformation

        // Création de l'instance du Loup (Atk: 3, PV: 2)
        Animal loup = AnimalFactory.createLoup();

        // 2. Remplacement physique sur le plateau
        line.replaceCard(position, loup);

        System.out.println("Le Louveteau a grandi en Loup !");
    }
}
