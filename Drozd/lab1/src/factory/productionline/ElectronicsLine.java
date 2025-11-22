package factory.productionline;

import factory.product.Product;
import factory.product.ElectronicProduct;

import java.util.List;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String s, List<ElectronicProduct> list, double v) {
        super(s, list, v);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }

}