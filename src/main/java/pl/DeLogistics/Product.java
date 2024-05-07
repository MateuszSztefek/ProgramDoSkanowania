package pl.DeLogistics;

public class Product {
    private final String order;
    private final Integer declaredQuantity;
    private final Integer Quantity;

    public Product(String order, Integer declaredQuantity, Integer quantity) {
        this.order = order;
        this.declaredQuantity = declaredQuantity;
        Quantity = quantity;
    }


    public String getOrder() {
        return order;
    }


    public Integer getDeclaredQuantity() {
        return declaredQuantity;
    }


    public Integer getQuantity() {
        return Quantity;
    }


}

