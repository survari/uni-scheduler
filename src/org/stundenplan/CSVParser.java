package org.stundenplan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
    public static ArrayList<String[]> parseCSV(String path) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        ArrayList<String[]> items = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.trim().startsWith("//")) {
                continue;
            } else  if (line.contains("//")) {
                line = line.split("//")[0].trim();
            }

            items.add(Arrays.stream(line.split(","))
                    .map(v -> v.trim())
                    .toArray(String[]::new));
        }

        return items;
    }
}
