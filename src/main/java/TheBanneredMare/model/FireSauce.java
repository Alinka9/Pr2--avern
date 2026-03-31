package TheBanneredMare.model;

public class FireSauce extends OrderDecorator {

    private static final int ADDITIONAL_PRICE = 10;

    private static final String ADDITION_NAME = "ОГНЕННЫЙ СОУС";

    public FireSauce(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return wrappedOrder.getDescription();
    }

    @Override
    public int getPrice() {
        return wrappedOrder.getPrice() + ADDITIONAL_PRICE;
    }

    @Override
    public String getFullName() {

        String currentFullName = wrappedOrder.getFullName();

        if (currentFullName.contains(" + ")) {
            return currentFullName + " + " + ADDITION_NAME;
        } else {
            return currentFullName + " + " + ADDITION_NAME;
        }
    }
}