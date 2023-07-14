package org.example.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String FILE_PATH = "product.txt";

    public static List<String> readProducts() {
        List<String> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                products.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void writeProduct(String product) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(product);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
