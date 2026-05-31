public class GrowthAbility extends Ability{
    private final Animal m_finalForm;

    public GrowthAbility(Animal loup) {
        this.m_finalForm = loup;
    }

    @Override
    public void onTurnUpdate(Animal self, int position, BoardLine line) {



       /* // L'animal s'est déjà "reposé" un tour, on procède à la transformation
        AnimalStock stock = new AnimalStock();
        Animal loup = stock.getCard(AnimalStock.LOUP).get();
        */
        line.replaceCard(position, m_finalForm.cloneAnimal());
    }

    public String getName(){
        return "Croissance";
    }
}
