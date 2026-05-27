
public class Main
{
    public static void main(String[] args)
    {
        Interactions dialogue = new Interactions();
        //dialogue.displayTitle();
        dialogue.displayMessage("Another challenger... It has been ages.");

        GameManager manager = new GameManager(dialogue);
        manager.startCampaign();

        ;
    }
}
