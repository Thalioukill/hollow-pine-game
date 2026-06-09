package model.cards;

public enum AnimalType {
    CHAT("Chat"),
    GRIZZLY("Grizzly"),
    COYOTE("Coyote"),
    MOINEAU("Moineau"),
    CORBEAU("Corbeau"),
    ECUREUIL("Ecureuil"),
    HERMINE("Hermine"),
    LOUVETEAU("Louveteau"),
    LOUP("Loup"),
    PUNAISE("Punaise"),
    ELAN("Élan"),
    VIPERE("Vipère"),
    PORC_EPIC("Porc-épic");

    private final String m_displayName;

    AnimalType(String displayName) {
        this.m_displayName = displayName;
    }

    public String getDisplayName() {
        return m_displayName;
    }
}