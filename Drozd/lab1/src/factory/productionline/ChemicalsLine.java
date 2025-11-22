package factory.productionline;

import factory.product.Product;
import factory.product.ChemicalProduct;

import java.util.List;

public class ChemicalsLine extends ProductionLine<ChemicalProduct> {
    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }

    public ChemicalsLine(String lineId, List<ChemicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }
}
