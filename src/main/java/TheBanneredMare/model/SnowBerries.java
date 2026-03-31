package TheBanneredMare.model;

public class SnowBerries extends OrderDecorator {

    private static final int ADDITIONAL_PRICE = 5;

    private static final String ADDITION_NAME = "СНЕЖНЫЕ ЯГОДЫ";

    public SnowBerries(Order order) {
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