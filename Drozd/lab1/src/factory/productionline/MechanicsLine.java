package factory.productionline;

import factory.product.Product;
import factory.product.MechanicalProduct;

import java.util.List;

public class MechanicsLine extends ProductionLine<MechanicalProduct> {
    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }

    public MechanicsLine(String lineId, List<MechanicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }
}