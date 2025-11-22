package factory;

import factory.product.ChemicalProduct;
import factory.product.ElectronicProduct;
import factory.product.MechanicalProduct;
import factory.product.Product;
import factory.productionline.ChemicalsLine;
import factory.productionline.ElectronicsLine;
import factory.productionline.MechanicsLine;
import factory.productionline.ProductionLine;
import factory.service.ProdAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Запуск системы моделирования производственных линий ---");

        System.out.println("\n1. Создание тестовых продуктов...");
        ElectronicProduct phone = new ElectronicProduct("E001", "Смартфон X1", 120);
        ElectronicProduct laptop = new ElectronicProduct("E002", "Ноутбук Pro", 180);
        ElectronicProduct tablet = new ElectronicProduct("E003", "Планшет Lite", 150);

        MechanicalProduct engine = new MechanicalProduct("M001", "Двигатель V8", 500);
        MechanicalProduct gearbox = new MechanicalProduct("M002", "Коробка передач", 350);

        ChemicalProduct fertilizer = new ChemicalProduct("C001", "Удобрение 'Азот+'", 90);
        ChemicalProduct plastic = new ChemicalProduct("C002", "Пластиковые гранулы", 75);
        System.out.println("Продукты успешно созданы.");

        System.out.println("\n2. Создание и настройка производственных линий...");
        ElectronicsLine electronicsLine = new ElectronicsLine(
                "EL-01",
                new ArrayList<>(Arrays.asList(phone, laptop, tablet)),
                0.95
        );

        MechanicsLine mechanicsLine = new MechanicsLine(
                "MECH-01",
                new ArrayList<>(Arrays.asList(engine, gearbox)),
                0.80
        );

        ChemicalsLine chemicalsLine = new ChemicalsLine(
                "CHEM-01",
                new ArrayList<>(List.of(fertilizer, plastic)),
                0.70
        );
        System.out.println("Линии успешно созданы и загружены продуктами.");

        System.out.println("\n3. Инициализация анализатора...");
        List<ProductionLine<? extends Product>> allLines = Arrays.asList(electronicsLine, mechanicsLine, chemicalsLine);
        ProdAnalyzer analyzer = new ProdAnalyzer(allLines);
        System.out.println("Анализатор готов к работе.");

        System.out.println("\n--- НАЧАЛО АНАЛИЗА ---");

        double efficiencyThreshold = 0.85;
        System.out.println("\n4.1. Линии с эффективностью выше " + efficiencyThreshold + ":");
        List<String> highEfficiencyLines = analyzer.getHighEfficiencyLines(efficiencyThreshold);
        System.out.println("Найденные ID линий: " + highEfficiencyLines);

        System.out.println("\n4.2. Общее количество продуктов по категориям:");
        System.out.println(analyzer.countProductsByCategory());

        System.out.println("\n4.3. Поиск самой загруженной линии (по количеству продуктов):");
        Optional<ProductionLine<? extends Product>> mostLoadedLine = analyzer.findMostLoadedLine();
        if (mostLoadedLine.isPresent()) {
            ProductionLine<? extends Product> line = mostLoadedLine.get();
            System.out.println("Самая загруженная линия: " + line.getLineId() + " (" + line.getProducts().size() + " продуктов)");
        } else {
            System.out.println("Производственные линии не найдены.");
        }

        System.out.println("\n4.4. Полный список всех производимых продуктов:");
        List<? extends Product> allProducts = analyzer.getAllProductsFromLines();
        allProducts.forEach(product -> System.out.println(" - " + product.getName() + " (Категория: " + product.getCategory() + ")"));

        System.out.println("\n4.5. Расчет суммарного времени производства всех продуктов:");
        int totalTime = analyzer.calculateTotalProductionTime();
        System.out.println("Общее время: " + totalTime + " минут.");

        System.out.println("\n--- АНАЛИЗ ЗАВЕРШЕН ---");


        System.out.println("\n--- ТЕСТИРОВАНИЕ ВАЛИДАЦИИ ---");

        try {
            System.out.println("\n5.1. Попытка создать продукт с отрицательным временем производства...");
            new ElectronicProduct("E-FAIL", "Бракованный продукт", -50);
        } catch (IllegalArgumentException e) {
            System.out.println("Успешно перехвачена ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\n5.2. Попытка создать линию с эффективностью > 1.0...");
            new ElectronicsLine("EL-FAIL", new ArrayList<>(), 1.5);
        } catch (IllegalArgumentException e) {
            System.out.println("Успешно перехвачена ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\n5.3. Попытка создать продукт с пустым именем...");
            new MechanicalProduct("M-FAIL", " ", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("Успешно перехвачена ошибка: " + e.getMessage());
        }

        System.out.println("\n--- Тестирование завершено ---");
    }
}