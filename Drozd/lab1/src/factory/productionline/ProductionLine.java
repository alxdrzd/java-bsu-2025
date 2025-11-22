package factory.productionline;

import factory.product.Product;

import java.util.ArrayList;
import java.util.List;

abstract public class ProductionLine<T extends Product> {
    private String lineId;
    private List<T> products;
    private double efficiency;

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return products;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setLineId(String lineId) {
        if (lineId == null || lineId.trim().isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be null or empty.");
        }
        this.lineId = lineId;
    }

    public void setProducts(List<T> products) {
        this.products = products;
    }

    public void setEfficiency(double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Efficiency must be between 0.0 and 1.0. Got: " + efficiency);
        }
        this.efficiency = efficiency;
    }

    public int getLength() {
        return this.products.size();
    }

    public void addProduct(T product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                    "Line " + lineId + " cannot produce product of type " + product.getCategory()
            );
        }
        this.products.add(product);
    }

    public abstract boolean canProduce(Product product);

    public ProductionLine(String lineId, List<T> products, double efficiency) {
        this.setLineId(lineId);
        if (products == null) {
            this.products = new ArrayList<>();
        } else {
            this.products = products;
        }
        this.setEfficiency(efficiency);
    }

    public int totalTime() {
        return this.products.stream()
                .mapToInt(Product::getProductionTime)
                .sum();
    }
}
