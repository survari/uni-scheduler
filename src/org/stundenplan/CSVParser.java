package org.stundenplan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVParser {
    public static ArrayList<String[]> parseCSV(String path) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        ArrayList<String[]> items = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.trim().startsWith("//")) {
                continue;
            }

            items.add(line.split(","));
        }

        return items;
    }
}
