package factory.product;

abstract public class Product {
    private String id;
    private String name;
    private int productionTime;

    public Product(String id, String name, int productionTime) {
        setId(id);
        setName(name);
        setProductionTime(productionTime);
    }

    public abstract String getCategory();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public final void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
        this.id = id;
    }

    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        this.name = name;
    }

    public final void setProductionTime(int productionTime) {
        if (productionTime < 0) {
            throw new IllegalArgumentException("Production time cannot be negative. Got: " + productionTime);
        }
        this.productionTime = productionTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", productionTime=" + productionTime +
                '}';
    }
}
