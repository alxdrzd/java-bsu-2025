package factory.product;

public class ChemicalProduct extends Product {
    @Override
    public String getCategory() {
        return "Chemical";
    }

    public ChemicalProduct(String id, String name,int productionTime) {
        super(id, name, productionTime);
    }

}
