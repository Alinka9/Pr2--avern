package TheBanneredMare.model;

public abstract class OrderDecorator implements Order {

    protected Order wrappedOrder;

    public OrderDecorator(Order order) {
        this.wrappedOrder = order;
    }

    @Override
    public String getDescription() {
        return wrappedOrder.getDescription();
    }

    @Override
    public int getPrice() {
        return wrappedOrder.getPrice();
    }

    @Override
    public String getFullName() {
        return wrappedOrder.getFullName();
    }

    public Order getWrappedOrder() {
        return wrappedOrder;
    }
}