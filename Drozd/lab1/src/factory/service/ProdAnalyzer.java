package factory.service;

import factory.productionline.ProductionLine;
import factory.product.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

public class ProdAnalyzer {
    List<ProductionLine<? extends Product>> getAllLines() {
        return allLines;
    }

    private List<ProductionLine<? extends Product>> allLines;

    public ProdAnalyzer(List<ProductionLine<? extends Product>> allLines) {
        if (allLines == null) {
            throw new IllegalArgumentException("List of production lines cannot be null.");
        }
        this.allLines = allLines;
    }

    public void setAllLines(List<ProductionLine<? extends Product>> allLines) {
        this.allLines = allLines;
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return this.allLines.stream()
                .filter(x -> x.getEfficiency() >= threshold)
                .map(ProductionLine::getLineId)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countProductsByCategory() {
        return this.allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()));

    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return this.allLines.stream()
                .max(Comparator.comparingInt(ProductionLine::getLength));
    }

    public List<? extends Product> getAllProductsFromLines() {
        return this.allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .collect(Collectors.toList());
    }

    public int calculateTotalProductionTime() {
        return this.allLines.stream()
                .mapToInt(ProductionLine::totalTime)
                .sum();
    }

}
