package factory.product;

public class MechanicalProduct extends Product{
    @Override
    public String getCategory() {
        return "Mechanical";
    }

    public MechanicalProduct(String id, String name,int productionTime) {
        super(id, name, productionTime);
    }

}
