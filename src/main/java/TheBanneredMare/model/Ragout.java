package TheBanneredMare.model;

public class Ragout implements Order {

    private static final int BASE_PRICE = 50;

    private static final String NAME = "НОРДСКОЕ РАГУ";

    @Override
    public String getDescription() {
        return NAME;
    }

    @Override
    public int getPrice() {
        return BASE_PRICE;
    }

    @Override
    public String getFullName() {
        return NAME;
    }
}