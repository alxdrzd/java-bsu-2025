package factory.product;

public class ElectronicProduct extends Product {
    @Override
    public String getCategory() {
        return "Electronic";
    }

    public ElectronicProduct(String id, String name,int productionTime) {
        super(id, name, productionTime);
    }
}
