package pl.DeLogistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {

        while (true) {
            System.out.println("1. Zacznij nowe skanowanie");
            System.out.println("2. Kontynuuj skanowanie");
            System.out.println("Wpisz \"koniec\", aby zakończyć pracę");
            System.out.print("Wpisz nr: ");

            Scanner menuInput = new Scanner(System.in);
            String menuInputString = menuInput.nextLine();

            if (menuInputString.equals("koniec")){
                System.exit(0);
            }

            if (menuInputString.equals("1")) {
                startFromInputFile();
            } else if (menuInputString.equals("2")) {
                startFromExistingFile();
            } else {
                System.out.println("Polecenie nie zostało rozpoznane. Dostępne komendy to \"1\", \"2\" i \"koniec\"");
            }
        }
    }

    private static void startFromExistingFile() {
        System.out.print("Podaj nazwę pliku (bez rozszerzenia):");
        Scanner startInput = new Scanner(System.in);
        String startInputString = startInput.nextLine().trim();
        String fileName = startInputString + ".txt";
        String anomaliesFileName = startInputString + "-anomalie.txt";

        Map<String, Product> productMap = new TreeMap<>();
        Map<String, Integer> anomalies = new TreeMap<>();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(";");
                productMap.put(fields[0], new Product(fields[1], Integer.valueOf(fields[2]), Integer.valueOf(fields[3])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nie można odnaleźć pliku: " + e.getMessage());
        }

        try {
            File file = new File(anomaliesFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(";");
                anomalies.put(fields[0], Integer.valueOf(fields[1]));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nie można odnaleźć pliku: " + e.getMessage());
        }
        mainLoop(productMap, anomalies);
    }

    private static void startFromInputFile() {
        System.out.print("Podaj nazwę pliku (bez rozszerzenia):");
        Scanner startInput = new Scanner(System.in);
        String startInputString = startInput.nextLine();
        String fileName = startInputString + ".txt";

        Map<String, Product> productMap = new TreeMap<>();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(";");
                productMap.put(fields[0], new Product(fields[1], Integer.valueOf(fields[2]), 0));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nie można odnaleźć pliku: " + e.getMessage());
        }

        Map<String, Integer> anomalies = new TreeMap<>();
        mainLoop(productMap, anomalies);
    }

    private static void mainLoop(Map<String, Product> productMap, Map<String, Integer> anomalies) {
        Scanner scanner = new Scanner(System.in);
        int flagForUpdate = 1;
        System.out.print("Podaj nazwę pliku do zapisu: ");
        String scr = scanner.nextLine();
        String outputFileName = scr.trim();


        while (true) {
            flagForUpdate++;
            if (flagForUpdate == 200) {
                System.out.println("Automatyczny zapis do pliku:");
                writeDataToFiles(outputFileName, productMap, anomalies);
                flagForUpdate = 1;
            }

            System.out.print("-----------------------------------------------------------------\n");
            System.out.print("Podaj kod lub wpisz \"koniec\": ");
            String input = scanner.nextLine();
            input = input.trim();


            if (input.equals("koniec")) {
                writeDataToFiles(outputFileName, productMap, anomalies);
                System.exit(0);
            }

            if (!productMap.containsKey(input)) {
                addToAnomaliesMap(input, anomalies);
                System.out.println("NIE MA TAKIEGO KODU! DODANO DO LISTY Z ANOMALIAMI.");
            } else {
                String orderCurrent = productMap.get(input).getOrder();
                Integer declaredQuantityCurrent = productMap.get(input).getDeclaredQuantity();
                Integer quantityCurrent = productMap.get(input).getQuantity();

                if (Objects.equals(quantityCurrent, declaredQuantityCurrent)) {
                    System.out.println("ZEBRANO JUŻ WSZYSTKIE PRODUKTY Z TEGO EANU! DODANO DO LISTY Z ANOMALIAMI.");
                    addToAnomaliesMap(input, anomalies);
                } else {
                    System.out.println("Zeskanowano EAN: " + input + ". Ilość: " + (quantityCurrent + 1) + " z " + declaredQuantityCurrent);
                    System.out.println("Produkt idzie na zamówienie " + orderCurrent);
                    if (orderCurrent.equals("APRIL")) {
                        System.out.println("<---- <---- <---- <---- <---- <---- <---- <---- <----");
                        System.out.println("<---- <---- <---- <---- <---- <---- <---- <---- <----");
                        System.out.println("<---- <---- <---- <---- <---- <---- <---- <---- <----");
                    } else {
                        System.out.println("----> ----> ----> ----> ----> ----> ----> ----> ---->");
                        System.out.println("----> ----> ----> ----> ----> ----> ----> ----> ---->");
                        System.out.println("----> ----> ----> ----> ----> ----> ----> ----> ---->");
                    }
                    productMap.put(input, new Product(orderCurrent, declaredQuantityCurrent, quantityCurrent + 1));
                }
            }
        }
    }

    private static void writeDataToFiles(String outputFileName, Map<String, Product> productMap, Map<String, Integer> anomalies) {
        String productFile = outputFileName + ".txt";
        String anomaliesFile = outputFileName + "-anomalie.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(productFile));
            for (Map.Entry<String, Product> entry : productMap.entrySet()) {
                String row = entry.getKey() + ";" + entry.getValue().getOrder() + ";" + entry.getValue().getDeclaredQuantity()
                        + ";" + entry.getValue().getQuantity() + "\n";

                writer.write(row);
            }
            writer.close();
            System.out.println("Dane zostały pomyślnie zapisane do pliku.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(anomaliesFile));
            for (Map.Entry<String, Integer> entry : anomalies.entrySet()) {
                String row = entry.getKey() + ";" + entry.getValue() + "\n";
                writer.write(row);
            }
            writer.close();
            System.out.println("Anomalie zostały pomyślnie zapisane do pliku.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addToAnomaliesMap(String input, Map<String, Integer> anomalies) {
        if (anomalies.containsKey(input)) {
            anomalies.put(input, anomalies.get(input) + 1);
        } else {
            anomalies.put(input, 1);
        }
    }
}